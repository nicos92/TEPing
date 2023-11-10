package com.example.tirameelping00.hilos;

import com.example.tirameelping00.detencion.Detener;
import com.example.tirameelping00.fechaYhora.FechaYhora;
import com.example.tirameelping00.fechaYhora.TiempoRestante;
import com.example.tirameelping00.log.Log;
import com.example.tirameelping00.notificacion.Notificacion;
import com.example.tirameelping00.ventana.DesactVentPing;
import javafx.application.Platform;
import javafx.scene.control.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.tirameelping00.TirameElPingController.misSonidos;

public class EjecutarPingHilo implements Runnable{

    private final TiempoRestante miTiempoRestante;

    private boolean timeDesconex;
    private final TextArea txtAreaSalida;
    private final Process process;
    private final String ip;
    private final TextField txtRutaArchivo;
    private final Slider volume;

    private final boolean bool;
    private final Detener detener;
    private final DesactVentPing desactVentPing;

    private boolean bolLog;




    public EjecutarPingHilo(Process p, String ip, boolean selected, TextArea txtAreaSalida, TextField txtRutaArchivo, DesactVentPing desactVentPing, Button btnIniciar, Button btnDetener, ProgressIndicator progress, Slider volume){
        this.process = p;
        this.ip = ip;
        this.bool = selected;
        this.txtAreaSalida = txtAreaSalida;
        this.txtRutaArchivo = txtRutaArchivo;
        this.volume = volume;
        this.miTiempoRestante = new TiempoRestante();
        this.timeDesconex = true;

        this.detener = new Detener(btnIniciar,btnDetener,progress);
        this.desactVentPing = desactVentPing;

    }

    @Override
    public void run() {
        try{



            int i = 0;
            txtAreaSalida.setText("");
            BufferedReader lector = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String inputLine;
            boolean notify = true;
            List<String> array = new ArrayList<>();
            File file = null;
            if (bool) {
                file = new File(getNameFile());
                txtRutaArchivo.setText(getNameFile());
            }

            while ((inputLine = lector.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                i++;

                String txt = FechaYhora.fechaYhoraNow() + "  " + inputLine + " \n ";

                if (bool) {
                    array.add(txt);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                    PrintWriter out = new PrintWriter(bw);
                    for (String text : array) {
                        out.write(text);
                    }
                    out.close();
                }

                if (i > 100){
                    i=0;
                    txtAreaSalida.setText("");
                }
                Platform.runLater(() -> txtAreaSalida.appendText( txt));

                try {
                    notify = sendNotificacion(notify, inputLine, ip);

                }catch (Exception e){
                    System.out.println("ERROR Desktop Notify: " + e.getMessage());
                    process.destroy();
                    Thread.currentThread().interrupt();

                }
            }

            detener.sendBtnDetener();
            desactVentPing.desactItemsPing(false);

        }catch (IOException e){
            System.out.println("Error Run: " + e.getMessage());
        }
    }

    private  boolean sendNotificacion(boolean notify, String inputLine, String ip){

        if (inputLine.contains("Error") || inputLine.contains("agotado")){
            //DesktopNotify.showDesktopMessage("Fallo en la Red", "revise la IP: " + ip, DesktopNotify.FAIL, 5000L);
            Platform.runLater(()-> Notificacion.enviarNoti("ERROR EN PING " , "revise la IP: " + ip, "ERROR", "SLIDE", 5));

            misSonidos[1].setGainControl(volume.getValue());
            misSonidos[1].playRun();
            //sonido.loop(new File("sonidos\\error.wav"));


            if (timeDesconex){
                Log.crearArchivoLog("Fallo    " + inputLine , "Ping: ", ip);
                miTiempoRestante.setFechaDesconex(FechaYhora.fechaInicioFin());
                timeDesconex = false;
            }

            bolLog = true;

            return true;
        }
        if ( inputLine.contains("tiempo") && notify){
            Platform.runLater(() -> Notificacion.enviarNoti("Conexion establecida", "Con la IP: " + ip, "SUCCESS", "POPUP", 4));

            misSonidos[2].setGainControl(volume.getValue());
            misSonidos[2].playRun();

            if (bolLog){
                bolLog = false;
                timeDesconex = true;
                miTiempoRestante.setFechaConex(FechaYhora.fechaInicioFin());
                Log.crearArchivoLog("Conexion " + inputLine, "Ping: ", ip);
                Log.crearArchivoLog(miTiempoRestante.getDiasResto(), "Ping: ", ip);
            }


            return false;
        }
        if( inputLine.contains("inaccesible")){
            //DesktopNotify.showDesktopMessage("Inaccesible", "No se Puede Acceder a la Direccion: " + ip, DesktopNotify.WARNING, 5000L);

            Platform.runLater(() -> Notificacion.enviarNoti("INACCESIBLE A PING ", "No se Puede Acceder a la Direccion: " + ip, "WARNING", "SLIDE", 5));

            misSonidos[1].setGainControl(volume.getValue());
            misSonidos[1].playRun();
            if (timeDesconex){
                Log.crearArchivoLog("Fallo    " + inputLine , "Ping: ", ip);
                miTiempoRestante.setFechaDesconex(FechaYhora.fechaInicioFin());
                timeDesconex = false;
            }

            bolLog = true;
        }
        if (inputLine.contains("Paquetes")) {
            //DesktopNotify.showDesktopMessage("Fin de Ping", "Con la IP: " + ip, DesktopNotify.INFORMATION, 4000L);
            misSonidos[0].setGainControl(volume.getValue());
            misSonidos[0].playRun();
            Platform.runLater(() -> Notificacion.enviarNoti("Fin de Ping", "Con la IP: " + ip, "INFORMATION", "FADE", 2));

        }
        return notify;
    }

    private  String getNameFile(){
        int cont = 0;
        File ruta = new File("K:\\");
        String[] nombres = ruta.list();
        assert nombres != null;
        String[] newName;
        for (String name : nombres) {
            if (name.contains("tirameElPing (")) {
                newName = name.split("[(]|[)]" );
                if (Integer.parseInt(newName[1]) > cont){
                    cont = Integer.parseInt(newName[1]);
                }
            }
        }
        cont++;
        return "K:\\tirameElPing (" + cont + ").txt";
    }


}
