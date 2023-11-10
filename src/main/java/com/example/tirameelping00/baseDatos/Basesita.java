package com.example.tirameelping00.baseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Basesita {


    private Connection con = null;

    {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String rutaFile = "BD\\TEP.accdb";
            String url= "jdbc:ucanaccess://" + rutaFile;
            con = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println("Conexion Erronea: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public List<NomNumIp> getNomNumIP(){
        List<NomNumIp> nomNumIps = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement("select nomIP, numIp from multiPing order by id ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                NomNumIp nomNumIp = new NomNumIp();
                nomNumIp.setNombre(rs.getString(1));
                nomNumIp.setIp(rs.getString(2));
                nomNumIps.add(nomNumIp);
            }
            ps.close();
            rs.close();
        }catch (SQLException e){
            System.out.println( "Conexion Erronea: " + e);
        }

        return nomNumIps;
    }

    public boolean updateIps(String nom, String ip, int id){

        try{
            PreparedStatement ps = con.prepareStatement("update multiPing set nomIP = ?, numIP = ? where Id = ? ");
            ps.setString(1, nom);
            ps.setString(2, ip);
            ps.setInt(3, id);
            int result = ps.executeUpdate();
            if (result > 0){
                ps.close();
                return true;
            }
            ps.close();
        }catch (SQLException e){
            System.out.println( "Conexion Erronea: " + e.getMessage());
        }
        return false;

    }




}
