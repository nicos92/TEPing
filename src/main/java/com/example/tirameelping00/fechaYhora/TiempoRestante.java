package com.example.tirameelping00.fechaYhora;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico
 * Fecha: 05/09/2023
 * Hora: 14:51
 * Project Name: tirameElPing00
 */


public class TiempoRestante {

    private String desconex;
    private String conex;

    public void setFechaConex(String dateTime){
        conex = dateTime;
    }

    public void setFechaDesconex(String dateTime){
        desconex = dateTime;
    }

    public String getDiasResto(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        try {

            Date fecha1 = sdf.parse(conex);
            Date fecha2 = sdf.parse(desconex);

            long diff = fecha1.getTime() - fecha2.getTime();

            long  MiliSeconds = TimeUnit.MILLISECONDS.toMillis(diff) % 1000;
            long  Seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;

            long  Minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
            long  Hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
            long  Days = TimeUnit.MILLISECONDS.toDays(diff) % 365;
            //long  Years = TimeUnit.MILLISECONDS.toDays(diff) / 365L;

            return "Tiempo desconectado: " +  Days + " dias, "
                    + String.format("%02d", Hours) + ":" +  String.format("%02d", Minutes) + ":" +  String.format("%02d", Seconds)
                    + "." +  MiliSeconds + " - De " + desconex + " a " + conex;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
