package com.example.tirameelping00.sonido;

import lombok.Getter;

import javax.sound.sampled.*;
import java.io.File;


public class Sonido extends Thread{

    @Getter
    private  Clip sonido;
    private FloatControl gainControl;

    @Override
    public void run() {
        playRun();
    }

    public void selectSonido(File url){
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            sonido = AudioSystem.getClip();
            sonido.open(audio);
            gainControl= (FloatControl) sonido.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            System.out.println("Error selectSonido: " + e.getMessage());
        }
    }

    public void playRun(){
        sonido.setFramePosition(0);
        sonido.start();
    }

    public  void closeSonido(){
        sonido.close();
    }

    public  void setGainControl(double volume){
        gainControl.setValue(-80 + (float) volume);
    }
}
