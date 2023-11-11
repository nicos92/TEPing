package com.example.tirameelping00.hilos;


import com.example.tirameelping00.detencion.Detener;
import com.example.tirameelping00.estilos.Style;
import com.example.tirameelping00.fechaYhora.FechaYhora;
import com.example.tirameelping00.fechaYhora.TiempoRestante;
import com.example.tirameelping00.log.Log;
import ds.desktop.notify.DesktopNotify;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.tirameelping00.TirameElPingController.misSonidos;


public class MiHilo implements Runnable {

    @Getter
    private final int id;

    private final TiempoRestante miTiempoRestante;

    private boolean timeDesconex;


    public void setProcess(Process process) {
        this.process = process;
    }

    private Process process;
    @Getter
    private final TextField ip;
    private final Detener detener;
    private final Slider volume;

    private boolean bolLog;

    private final TextField nomIp;

    private final Text txtError;
    private final ImageView imgNotiError;
    private final RadioButton radBtn;
    private final Button btnIniciar;
    private final Button btnDetener;
    private final Button pos;


    private final Lock lock = new ReentrantLock();
    private final Button btnCont;
    private int cont;

    public MiHilo(int id, Process process, TextField ip, Detener detener, TextField nomIp,
                  Text txtError, Slider volume, Button btnCont, ImageView imgNotiError, RadioButton _radBtn,
                  Button _btnIniciar, Button _btnDetener, Button _pos) {
        this.id = id;
        this.process = process;
        this.ip = ip;
        this.detener = detener;
        this.volume = volume;
        this.btnCont = btnCont;
        this.nomIp = nomIp;
        this.txtError = txtError;
        this.imgNotiError = imgNotiError;
        this.miTiempoRestante = new TiempoRestante();
        this.timeDesconex = true;
        radBtn = _radBtn;
        btnIniciar = _btnIniciar;
        btnDetener = _btnDetener;
        pos = _pos;
    }


    public String getRadBtn() {
        return (radBtn.isSelected() ? "-t" : "-a");
    }

    @Override
    public String toString() {
        return "MiHilo{" +
                "\n id=" + id +
                "\n , process=" + process +
                "\n , ip=" + ip +
                "\n , detener=" + detener +
                "\n , volume=" + volume +
                "\n , bolLog=" + bolLog +
                "\n , nomIp=" + nomIp +
                "\n , txtError=" + txtError +
                "\n , imgNotiError=" + imgNotiError +
                "\n , radBtn=" + radBtn +
                "\n , btnIniciar=" + btnIniciar +
                "\n , btnDetener=" + btnDetener +
                "\n , pos=" + pos +
                "\n , lock=" + lock +
                "\n , btnCont=" + btnCont +
                "ºn , cont=" + cont +
                '}';
    }

    @Override
    public void run() {

        try {
            Platform.runLater(() -> {
                desactFilaMultiPing(true);
                detener.sendBtnDetenerMulti(true);
                txtError.setVisible(false);

            });

            BufferedReader lector = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String inputLine;
            boolean notify = true;

            while ((inputLine = lector.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                try {
                    notify = sendNotificacion(notify, inputLine);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        txtError.setText("ERROR Desktop Notify: " + nomIp.getText() + " " + ip.getText() + ". Intente Nuevamente");
                        System.out.println("ERROR Desktop Notify: " + nomIp.getText() + " " + ip.getText() + " " + e.getMessage());
                        txtError.setVisible(true);
                    });
                    process.destroy();
                    Thread.currentThread().interrupt();
                }
            }

            Platform.runLater(() -> {

                detener.sendBtnDetenerMulti(false);
                //desactVentPing.desactItemsPingMulti(false);

                desactFilaMultiPing(false);
                styleNomIP(Style.normalItems());
                btnCont.setVisible(false);
                imgNotiError.setVisible(btnCont.isVisible());

            });

        } catch (IOException e) {
            System.out.println("Error Run: " + e.getMessage());
        }
    }

    private boolean sendNotificacion(boolean notify, String inputLine) {

        lock.lock();
        try {

            if (inputLine.contains("Error") || inputLine.contains("agotado")) {
                if (timeDesconex) {
                    miTiempoRestante.setFechaDesconex(FechaYhora.fechaInicioFin());
                    Log.crearArchivoLog("Fallo     - " + inputLine, nomIp.getText(), ip.getText());
                    timeDesconex = false;
                }
                Platform.runLater(() -> {
                    styleNomIP(Style.rojoItems());

                    cont++;
                    btnCont.setText(String.valueOf(cont));
                    btnCont.setVisible(true);
                    imgNotiError.setVisible(btnCont.isVisible());
                    txtError.setVisible(true);

                    txtError.setFill(Color.DARKRED);
                    txtError.setText(inputLine);

                    DesktopNotify.showDesktopMessage("Inaccesible a: " + nomIp.getText().toUpperCase(), "No se Puede Acceder a la Direccion: " + ip.getText(), DesktopNotify.ERROR, 5000L);
                });

                misSonidos[1].setGainControl(volume.getValue());
                misSonidos[1].playRun();

                bolLog = true;
                return true;
            }

            if (inputLine.contains("tiempo") && notify) {
                if (bolLog) {


                    miTiempoRestante.setFechaConex(FechaYhora.fechaInicioFin());
                    Log.crearArchivoLog("Conexion  - " + miTiempoRestante.getDiasResto(), nomIp.getText(), ip.getText());
                    //Log.crearArchivoLog(miTiempoRestante.getDiasResto(), nomIp.getText(), ip.getText());
                    styleNomIP(Style.normalItems());
                    bolLog = false;
                    timeDesconex = true;
                }
                Platform.runLater(() -> {

                    txtError.setVisible(true);
                    txtError.setFill(Color.DARKGREEN);
                    txtError.setText(inputLine);
                    DesktopNotify.showDesktopMessage("Conexion establecida a: " + nomIp.getText().toUpperCase(),
                            "Con la IP: " + ip.getText(), DesktopNotify.SUCCESS, 5000L);

                    misSonidos[2].setGainControl(volume.getValue());
                    misSonidos[2].playRun();
                });

                return false;
            }

            if (inputLine.contains("inaccesible")) {
                if (timeDesconex) {
                    miTiempoRestante.setFechaDesconex(FechaYhora.fechaInicioFin());
                    Log.crearArchivoLog("Inacces.  - " + inputLine, nomIp.getText(), ip.getText());
                    timeDesconex = false;
                }

                Platform.runLater(() -> {
                    styleNomIP(Style.rojoItems());
                    cont++;
                    btnCont.setText(String.valueOf(cont));
                    btnCont.setVisible(true);
                    imgNotiError.setVisible(btnCont.isVisible());

                    txtError.setVisible(true);
                    txtError.setFill(Color.DARKRED);
                    txtError.setText(inputLine);
                    DesktopNotify.showDesktopMessage("Inaccesible a: " + nomIp.getText().toUpperCase(), "No se Puede Acceder a la " +
                            "Direccion: " + ip.getText(), DesktopNotify.WARNING, 5000L);
                });

                misSonidos[1].setGainControl(volume.getValue());
                misSonidos[1].playRun();
                bolLog = true;
            }

            if (inputLine.contains("Paquetes")) {
                Platform.runLater(() -> {
                    misSonidos[0].setGainControl(volume.getValue());
                    misSonidos[0].playRun();
                    DesktopNotify.showDesktopMessage("Fin de Ping a: " + nomIp.getText().toUpperCase(), "Con la IP: " + ip.getText(),
                            DesktopNotify.INFORMATION, 4000L);
                    styleNomIP(Style.normalItems());
                });
            }

        } finally {
            lock.unlock();
        }

        return notify;
    }

    public void styleNomIP(String estilo) {
        nomIp.setStyle(estilo);
        ip.setStyle(estilo);
    }

    public void desactFilaMultiPing(boolean bol) {
        nomIp.setDisable(bol);
        ip.setDisable(bol);
        radBtn.setDisable(bol);
        btnIniciar.setDisable(bol);
        btnDetener.setDisable(!bol);
        pos.setDisable(bol);
    }
}
