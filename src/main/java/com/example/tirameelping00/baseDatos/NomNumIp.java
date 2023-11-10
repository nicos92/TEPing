package com.example.tirameelping00.baseDatos;

import lombok.Getter;

@Getter
public class NomNumIp {
    private String nombre;
    private String ip;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "NomNumIp{" +
                "nombre='" + nombre + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
