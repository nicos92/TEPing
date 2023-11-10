package com.example.tirameelping00.ventana;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class DesactVentPing {
    private final Label labelIp;
    private final TextField txtIP;
    private final RadioButton radBtn_Prueba;
    private final RadioButton radBtn_t;
    private final RadioButton radBtn_n;
    private final TextField txtCantPet;
    private final CheckBox host_a;
    private final CheckBox pingEnTxt;

//    private TextField nomIp;

    public DesactVentPing(Label labelIp, TextField txtIP, RadioButton radBtn_Prueba, RadioButton radBtn_t, RadioButton radBtn_n, TextField txtCantPet, CheckBox host_a, CheckBox pingEnTxt) {
        this.labelIp = labelIp;
        this.txtIP = txtIP;
        this.radBtn_Prueba = radBtn_Prueba;
        this.radBtn_t = radBtn_t;
        this.radBtn_n = radBtn_n;
        this.txtCantPet = txtCantPet;
        this.host_a = host_a;
        this.pingEnTxt = pingEnTxt;

    }


    public void desactItemsPing(boolean b) {
        labelIp.setDisable(b);
        txtIP.setDisable(b);
        radBtn_Prueba.setDisable(b);
        radBtn_t.setDisable(b);
        radBtn_n.setDisable(b);
        txtCantPet.setDisable(b);
        host_a.setDisable(b);
        pingEnTxt.setDisable(b);
    }



}
