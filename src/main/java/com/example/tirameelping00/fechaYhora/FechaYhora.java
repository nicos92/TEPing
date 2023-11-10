package com.example.tirameelping00.fechaYhora;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaYhora {

    public static String fechaYhoraNow(){
        LocalDateTime fechaYhoraLog = LocalDateTime.now();
        DateTimeFormatter fechaLog = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss.SSS");
        return fechaYhoraLog.format(fechaLog);
    }

    public static String fechaInicioFin(){
        LocalDateTime fechaYhoraLog = LocalDateTime.now();
        DateTimeFormatter fechaLog = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss.SSS");
        return fechaYhoraLog.format(fechaLog);
    }
}
