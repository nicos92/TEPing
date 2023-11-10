package com.example.tirameelping00.hilos;

import com.example.tirameelping00.log.Log;
import ds.desktop.notify.DesktopNotify;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.tirameelping00.TirameElPingController.*;

/**
 * @author Nico
 * Fecha: 02/08/2023
 * Hora: 21:10
 * Project Name: tirameElPing00
 */
public class MiHiloIp implements Runnable{


    private final Process process;
    private final Slider volume;
    private final Menu miAutoPing;
    private final Text[] misTxtErrorHilo;
    private final String ip;

    public MiHiloIp(Process process, Slider volume, Menu miAutoPing, Text[] misTxtError, String ip) {

        this.process = process;
        this.volume = volume;
        this.miAutoPing = miAutoPing;
        this.misTxtErrorHilo = misTxtError;
        this.ip = ip;
    }

    @Override
    public void run() {

            try{
                BufferedReader lector = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String inputLine;
                boolean notify = true;

                while ((inputLine = lector.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                    try{
                        notify = sendNotificacion(notify, inputLine);
                    }catch (Exception e){
                        process.destroy();
                        Thread.currentThread().interrupt();
                    }
                }
                process.destroy();

            }catch (IOException e){
                System.out.println("Error Run MI Hilo IP: " + e.getMessage());
            }

    }

    private  boolean sendNotificacion( boolean notify, String inputLine){


        if (inputLine.contains("Error general") || inputLine.contains("agotado") || inputLine.contains("destino inaccesible")) {
            Platform.runLater(()->{
                closeThreadProcess();
                miAutoPing.setStyle("-fx-background-color: #ff6666");
                misSonidos[1].setGainControl(volume.getValue());
                misSonidos[1].playRun();
                Log.crearArchivoLog("Fallo Puerta de enlace      -    " + inputLine, " ", ip);

                DesktopNotify.showDesktopMessage("ERROR en la Red" , "revise su conexion" ,DesktopNotify.WARNING, 5000L);
            });
            return true;
        }

        if (inputLine.contains("tiempo") && notify) {

            Platform.runLater(()->{

                miAutoPing.setStyle("-fx-background-color: #4fe05b");
                misSonidos[2].setGainControl(volume.getValue());
                misSonidos[2].playRun();
                Log.crearArchivoLog("Conexion Puerta de enlace        -    " + inputLine, " ", ip);
                DesktopNotify.showDesktopMessage("CONEXION" , "Conexion establecida a su puerta de enlace " + ip  ,
                        DesktopNotify.SUCCESS,
                        5000L);
                iniciarNuevamente();
            });
            return false;
        }
        return notify;
    }
    private  void closeThreadProcess() {
        for( int i = 0;  i < (threads.length - 1); i++){
            if (threads[i] !=  null && threads[i].isAlive())threads[i].interrupt();
        }
        for( int i = 0; i < (processes.length -1); i++){
            if (processes[i] != null)processes[i].destroy();
        }

        for (Text t: misTxtErrorHilo){
            t.setText("");
        }


    }

    public void iniciarNuevamente(){
        Log.crearArchivoLog("iniciar todo", "inicio IPs activadas", "");
        for (MiHilo mh : misHilosEjec){

            String[] cmd = {"ping" , mh.getIp().getText() , mh.getRadBtn()};
            processes[mh.getId()].destroy();
            try {
                processes[mh.getId()] = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            mh.setProcess(processes[mh.getId()] );

            threads[mh.getId()] = new Thread(mh);
            threads[mh.getId()].start();

        }
    }
}
