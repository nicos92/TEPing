package com.example.tirameelping00.notificacion;



import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


public class Notificacion {

    public static void enviarNoti(String title, String message, String notiType, String animation, double time){

        AnimationType type = AnimationType.valueOf(animation);

        TrayNotification tray = new TrayNotification();

        tray.setAnimationType(type);
        tray.setTray(title, message, NotificationType.valueOf(notiType));

        tray.showAndDismiss(Duration.seconds(time));
    }


}
