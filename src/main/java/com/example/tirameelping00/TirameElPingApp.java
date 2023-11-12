package com.example.tirameelping00;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import com.example.tirameelping00.sonido.Sonido;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

import static com.example.tirameelping00.TirameElPingController.*;


public class TirameElPingApp extends Application {


    FXTrayIcon trayIcon;
    double yOffset;
    double xOffset;

    @Override
    public void start(Stage stage) throws IOException {

        //FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("design.fxml"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("desingTEP.fxml")));

        trayIcon = new FXTrayIcon(stage, Objects.requireNonNull(getClass().getResource("imgs/rj45.png")));
        trayIcon.show();

        trayIcon.setTrayIconTooltip("Tirame el Ping!");

        MenuItem detenerTodo = new MenuItem("Detener Todo");
        detenerTodo.setOnAction(e -> closeThreadProcess());

        MenuItem miOff = new MenuItem("Cerrar App");
        miOff.setOnAction(e -> cerrarApp());

        trayIcon.addMenuItems(detenerTodo, miOff);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        try {
            Scene scene = new Scene(root);
            stage.setTitle("Tirame El Ping");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("imgs/rj45.png"))));
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(true);

            stage.setMinWidth(755);
            stage.setMinHeight(350); //760

            stage.setMaxWidth(755);
            stage.setMaxHeight(768);
            stage.setMaximized(true);


            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toString());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR start: " + e.getMessage());
        }

    }

    private static void cerrarApp() {
        closeThreadProcess();
        Platform.exit();
        System.exit(0);
    }

    private static void closeThreadProcess() {
        for (Sonido s : misSonidos) if (s != null) s.closeSonido();

        for (Thread t : threads) {
            if (t != null && t.isAlive()) t.interrupt();
        }
        for (Process p : processes) {
            if (p != null) p.destroy();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}