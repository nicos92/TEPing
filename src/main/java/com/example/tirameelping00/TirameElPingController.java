package com.example.tirameelping00;

import com.example.tirameelping00.baseDatos.Basesita;
import com.example.tirameelping00.baseDatos.NomNumIp;
import com.example.tirameelping00.detencion.Detener;
import com.example.tirameelping00.estilos.Style;
import com.example.tirameelping00.hilos.MiHilo;
import com.example.tirameelping00.sonido.Sonido;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TirameElPingController implements Initializable {


     public static final Sonido[] misSonidos = new Sonido[3];

    private  final String IPV4_PATTERN =
            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

    private  final Pattern pattern = Pattern.compile(IPV4_PATTERN);

    public  boolean isValidIp(final String dirIp) {
        Matcher matcher = pattern.matcher(dirIp);
        return matcher.matches();
    }

    public static List<MiHilo> misHilosEjec = new ArrayList<>();

     public static final Process[] processes = new Process[14];

     public static final Thread[] threads = new Thread[14];

     @FXML
     private HBox ventBtnsTodo;

     @FXML
     private ImageView imgVol, imgNotiError;

     @FXML
     private Slider volume;

    @FXML
    private Button btnMultiPing; //, btnRegPing, btnPing;

    @FXML
    private TextField nomIp1, nomIp2, nomIp3, nomIp4, nomIp5, nomIp6, nomIp7, nomIp8, nomIp9, nomIp10, nomIp11, nomIp12;

    @FXML
    private Button pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, posD10, posD11, posD12;

    @FXML
    private Button   btnIniciar1, btnIniciar2, btnIniciar3, btnIniciar4,btnIniciar5, btnIniciar6,
                    btnIniciar7, btnIniciar8, btnIniciar9, btnIniciarD10, btnIniciarD11, btnIniciarD12, btnIniciarTodo;//btnIniciar,



    @FXML
    private Button   btnDetener1, btnDetener2, btnDetener3, btnDetener4, btnDetener5, btnDetener6,
                    btnDetener7, btnDetener8, btnDetener9, btnDetenerD10, btnDetenerD11, btnDetenerD12, btnDetenerTodo;//btnDetener,

    @FXML
    private Button cont1, cont2, cont3, cont4, cont5, cont6, cont7, cont8, cont9, cont10, cont11, cont12;

    //@FXML private ProgressIndicator progress; //, progress1, progress2, progress3, progress4, progress5, progress6, progress7,
            //progress8, progress9;
    @FXML
    private TextField txtIP1, txtIP2, txtIP3, txtIP4, txtIP5, txtIP6, txtIP7, txtIP8, txtIP9, txtIP10, txtIP11, txtIP12;
    @FXML
    private RadioButton radBtn_t1, radBtn_t2, radBtn_t3, radBtn_t4,radBtn_t5, radBtn_t6, radBtn_t7, radBtn_t8,
            radBtn_t9, radBtn_t10, radBtn_t11, radBtn_t12;

    //@FXML private AnchorPane  ventanaTxtSalida; mainStage, ventanaPing,

    //@FXML private VBox ventMenu;

    @FXML
    private ScrollPane scrollMultiPing;

    @FXML
    private Text  txtError1, txtError2, txtError3, txtError4, txtError5, txtError6, txtError7,
            txtError8, txtError9, txtError10, txtError11, txtError12;


    public void onVentMultiPing(){
        //ventanaPing.setVisible(false);
        btnMultiPing.setStyle(Style.ventElegida());

        ventBtnsTodo.setVisible(true);

        scrollMultiPing.setVisible(true);


    }

    public void setImgMute(){
        //Sonido.setGainControl(volume.getValue());
        if (volume.getValue() == 0){
            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("imgs/mute.png")));
            imgVol.setImage(image1);
        }else {
            Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("imgs/vol.png")));
            imgVol.setImage(image2);

        }
    }

    public void creatSoundVol(){
        File ruta = new File("sonidos");

        String[] archivos = ruta.list();
        if (archivos != null) {
            for( int i=0 ; i < archivos.length ; i++){
                misSonidos[i] = new Sonido();
                misSonidos[i].selectSonido(new File("sonidos\\" + archivos[i]));
            }
        }


    }

    public void confirmVol(){
        misSonidos[0].setGainControl(volume.getValue());
        misSonidos[0].playRun();
    }


    public  void ejecutarMultiPing(int id, TextField _txtIP, Button _btnIniciar, Button _btnDetener,
                                    RadioButton _radBtn, TextField _nomIp, Text _txtError, Button _pos, Button _cont) {
        try {

            misHilosEjec.removeIf(mh -> mh.getId() == id);

            if (threads[id] != null) threads[id].interrupt();

            // prepara el comando CMD
            String metod = (_radBtn.isSelected() ? "-t" : "-a");
            String[] cmd = {"ping" , _txtIP.getText() , metod};

            processes[id] = Runtime.getRuntime().exec(cmd);

            Detener detener = new Detener( _btnIniciar, _btnDetener, _pos);
            // desactiva los elementos
            //DesactVentPing desactVentPing = new DesactVentPing(_txtIP, _radBtn, _nomIp);

            // ejecuta el hilo
            MiHilo miHilo = new MiHilo(id, processes[id], _txtIP, detener, _nomIp, _txtError, volume,
                     _cont, imgNotiError, _radBtn, _btnIniciar, _btnDetener, _pos);

            misHilosEjec.add(miHilo);

            threads[id]= new Thread(miHilo);
            threads[id].start();
            //desactFilaMultiPing(_nomIp, _txtIP,_radBtn, _btnIniciar, _btnDetener, _pos);

        } catch (Exception n){
            System.out.println("ERROR ejecutar Multi Ping: " + n.getMessage());
        }
    }

    public void iniciarTodoMultiPing(){
        Platform.runLater(() -> btnTodos(true));
        for( int i = 1; i < threads.length; i++){
            if (threads[i] == null || !threads[i].isAlive()){
                altaHilos(i);

            }
        }
        Platform.runLater(() -> btnTodos(false));
    }

    public void btnTodos(boolean b){
        btnIniciarTodo.setDisable(b);
        btnDetenerTodo.setDisable(b);
    }


    public void altaHilos(int id){
        switch (id){
            case 1 -> ejecutarMultiPing(id, txtIP1, btnIniciar1, btnDetener1,  radBtn_t1, nomIp1, txtError1, pos1, cont1);
            case 2 -> ejecutarMultiPing(id, txtIP2, btnIniciar2, btnDetener2,  radBtn_t2, nomIp2, txtError2, pos2, cont2);
            case 3 -> ejecutarMultiPing(id, txtIP3, btnIniciar3, btnDetener3,  radBtn_t3, nomIp3, txtError3, pos3, cont3);
            case 4 -> ejecutarMultiPing(id, txtIP4, btnIniciar4, btnDetener4,  radBtn_t4, nomIp4, txtError4, pos4, cont4);
            case 5 -> ejecutarMultiPing(id, txtIP5, btnIniciar5, btnDetener5,  radBtn_t5, nomIp5, txtError5, pos5, cont5);
            case 6 -> ejecutarMultiPing(id, txtIP6, btnIniciar6, btnDetener6,  radBtn_t6, nomIp6, txtError6, pos6, cont6);
            case 7 -> ejecutarMultiPing(id, txtIP7, btnIniciar7, btnDetener7,  radBtn_t7, nomIp7, txtError7, pos7, cont7);
            case 8 -> ejecutarMultiPing(id, txtIP8, btnIniciar8, btnDetener8,  radBtn_t8, nomIp8, txtError8, pos8, cont8);
            case 9 -> ejecutarMultiPing(id, txtIP9, btnIniciar9, btnDetener9,  radBtn_t9, nomIp9, txtError9, pos9, cont9);
            case 10 -> ejecutarMultiPing(id, txtIP10, btnIniciarD10, btnDetenerD10, radBtn_t10, nomIp10, txtError10, posD10, cont10);
            case 11 -> ejecutarMultiPing(id, txtIP11, btnIniciarD11, btnDetenerD11, radBtn_t11, nomIp11, txtError11, posD11, cont11);
            case 12 -> ejecutarMultiPing(id, txtIP12, btnIniciarD12, btnDetenerD12, radBtn_t12, nomIp12, txtError12, posD12, cont12);
        }
    }

     public void exitButton(){
         closeThreadProcess();
         Platform.exit();
         System.exit(0);
    }



     public void closeThreadProcess() {
         for( int i = 1; i < threads.length ; i++){
             if (threads[i] !=  null && threads[i].isAlive())threads[i].interrupt();
         }
         for( int i = 1; i < processes.length ; i++){
             if (processes[i] != null)processes[i].destroy();
         }



    }

    public void closeThreadProcessBtn() {
        Platform.runLater(() -> btnTodos(true));

        Text[] misTxtError = {txtError1, txtError2, txtError3, txtError4, txtError5, txtError6, txtError7,
            txtError8, txtError9, txtError10, txtError11, txtError12};

        for( int i = 1; i < threads.length -1 ; i++)if (threads[i] !=  null && threads[i].isAlive())threads[i].interrupt();

        for( int i = 1; i < processes.length - 1 ; i++)if (processes[i] != null)processes[i].destroy();

        for (Text t: misTxtError)t.setText("");

        misHilosEjec.removeAll(Collections.unmodifiableList(misHilosEjec));

        Platform.runLater(() -> btnTodos(false));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //ipConfigAll();
        cargarIPS();
        creatSoundVol();

        btnMultiPing.setOnAction(a -> onVentMultiPing());
        //btnRegPing.setOnAction(a -> onVentTxtSalida());


        Platform.setImplicitExit(false);




    }

    public void abrirLog(){
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(new File( "LOG\\" + LocalDate.now().getYear()  + " " + LocalDate.now().getMonth() + "\\TEP " + LocalDate.now().getYear() + " " +  LocalDate.now().getMonth() + " " +  LocalDate.now().getDayOfMonth() + ".log"));
        } catch (IOException | IllegalArgumentException e) {
            sendAlert("ERROR Archivo", "No se encuentra archivo: " + e.getMessage());
        }
    }

    public void abrirCarpetaLog(){
        File rutaCarpetaLog = new File("LOG");
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(rutaCarpetaLog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendAlert(String title, String cont){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(cont);
        alert.showAndWait();
    }

    public void cargarIPS(){

        Basesita basesita = new Basesita();
        List<NomNumIp> listaIPs = basesita.getNomNumIP();

        TextField[] txtIP = new TextField[12];
        txtIP[0] = txtIP1;
        txtIP[1] = txtIP2;
        txtIP[2] = txtIP3;
        txtIP[3] = txtIP4;
        txtIP[4] = txtIP5;
        txtIP[5] = txtIP6;
        txtIP[6] = txtIP7;
        txtIP[7] = txtIP8;
        txtIP[8] = txtIP9;
        txtIP[9] = txtIP10;
        txtIP[10] = txtIP11;
        txtIP[11] = txtIP12;
        TextField[] txtNom = new TextField[12];
        txtNom[0] = nomIp1;
        txtNom[1] = nomIp2;
        txtNom[2] = nomIp3;
        txtNom[3] = nomIp4;
        txtNom[4] = nomIp5;
        txtNom[5] = nomIp6;
        txtNom[6] = nomIp7;
        txtNom[7] = nomIp8;
        txtNom[8] = nomIp9;
        txtNom[9] = nomIp10;
        txtNom[10] = nomIp11;
        txtNom[11] = nomIp12;
        for (int i = 0; i < listaIPs.size(); i++) {
            txtNom[i].setText(listaIPs.get(i).getNombre());
            txtIP[i].setText(listaIPs.get(i).getIp());
        }

    }


    public void guardaIP(MouseEvent mouseEvent){
        Basesita basesita = new Basesita();
        String event = mouseEvent.getSource().toString();


        if (event.contains("pos1")){
            Platform.runLater(()->{
                if (isValidIp(txtIP1.getText())){
                    if (basesita.updateIps( nomIp1.getText(), txtIP1.getText(), 1)){
                        txtError1.setVisible(true);
                        txtError1.setText("guardado Correctamente");
                    }

                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");

                });

        }
        if (event.contains("pos2")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP2.getText())){
                    if (basesita.updateIps(nomIp2.getText(), txtIP2.getText(), 2)) {
                        txtError2.setVisible(true);
                        txtError2.setText("guardado Correctamente");

                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos3")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP3.getText())){
                    if (basesita.updateIps(nomIp3.getText(), txtIP3.getText(), 3)) {
                        txtError3.setVisible(true);
                        txtError3.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos4")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP4.getText())){
                    if (basesita.updateIps(nomIp4.getText(), txtIP4.getText(), 4)) {
                        txtError4.setVisible(true);
                        txtError4.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos5")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP5.getText())){
                    if (basesita.updateIps(nomIp5.getText(), txtIP5.getText(), 5)) {
                        txtError5.setVisible(true);

                        txtError5.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos6")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP6.getText())){
                    if (basesita.updateIps(nomIp6.getText(), txtIP6.getText(), 6)) {
                        txtError6.setVisible(true);

                        txtError6.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos7")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP7.getText())){
                    if (basesita.updateIps(nomIp7.getText(), txtIP7.getText(), 7)) {
                        txtError7.setVisible(true);

                        txtError7.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos8")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP8.getText())){
                    if (basesita.updateIps(nomIp8.getText(), txtIP8.getText(), 8)) {
                        txtError8.setVisible(true);

                        txtError8.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("pos9")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP9.getText())){
                    if (basesita.updateIps(nomIp9.getText(), txtIP9.getText(), 9)) {
                        txtError9.setVisible(true);

                        txtError9.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("posD10")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP10.getText())){
                    if (basesita.updateIps(nomIp10.getText(), txtIP10.getText(), 10)) {
                        txtError10.setVisible(true);

                        txtError10.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("posD11")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP11.getText())){
                    if (basesita.updateIps(nomIp11.getText(), txtIP11.getText(), 11)) {
                        txtError11.setVisible(true);

                        txtError11.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }
        if (event.contains("posD12")){
            Platform.runLater(()-> {
                if (isValidIp(txtIP12.getText())){
                    if (basesita.updateIps(nomIp12.getText(), txtIP12.getText(), 12)) {
                        txtError12.setVisible(true);

                        txtError12.setText("guardado Correctamente");
                    }
                }else sendAlert("ERROR Dir IPv4","Formato de Direccion IPV4 no valida");
            });
        }

    }

    public void btnIniciarMultiPing(MouseEvent mouseEvent) {

        String event = mouseEvent.getSource().toString();

        if (event.contains("btnIniciar1")){
            altaHilos(1);
        }
        if (event.contains("btnIniciar2")){
            altaHilos(2);
        }
        if (event.contains("btnIniciar3")){
            altaHilos(3);
        }
        if (event.contains("btnIniciar4")){
            altaHilos(4);
        }
        if (event.contains("btnIniciar5")){
            altaHilos(5);
        }
        if (event.contains("btnIniciar6")){
            altaHilos(6);
        }
        if (event.contains("btnIniciar7")){
            altaHilos(7);
        }
        if (event.contains("btnIniciar8")){
            altaHilos(8);
        }
        if (event.contains("btnIniciar9")){
            altaHilos(9);
        }
        if (event.contains("btnIniciarD10")){
            altaHilos(10);
        }
        if (event.contains("btnIniciarD11")){
            altaHilos(11);
        }
        if (event.contains("btnIniciarD12")){
            altaHilos(12);
        }

    }

    public void onBtnDetenerMulti(javafx.scene.input.MouseEvent mouseEvent){

        String event = mouseEvent.getSource().toString();



        if (event.contains("btnDetener1")){
            misHilosEjec.removeIf(mh -> mh.getId() == 1);

            btnIniciar1.setDisable(false);
            btnDetener1.setDisable(true);
            txtError1.setText("");

            threads[1].interrupt();
            processes[1].destroy();
        }
        if (event.contains("btnDetener2")){
            misHilosEjec.removeIf(mh -> mh.getId() == 2);

            btnIniciar2.setDisable(false);
            btnDetener2.setDisable(true);
            txtError2.setText("");

            threads[2].interrupt();
            processes[2].destroy();
        }
        if (event.contains("btnDetener3")){
            misHilosEjec.removeIf(mh -> mh.getId() == 3);

            btnIniciar3.setDisable(false);
            btnDetener3.setDisable(true);
            txtError3.setText("");

            threads[3].interrupt();
            processes[3].destroy();
        }
        if (event.contains("btnDetener4")){
            misHilosEjec.removeIf(mh -> mh.getId() == 4);

            btnIniciar4.setDisable(false);
            btnDetener4.setDisable(true);
            txtError4.setText("");

            threads[4].interrupt();
            processes[4].destroy();

        }
        if (event.contains("btnDetener5")){
            misHilosEjec.removeIf(mh -> mh.getId() == 5);

            btnIniciar5.setDisable(false);
            btnDetener5.setDisable(true);
            txtError5.setText("");

            threads[5].interrupt();
            processes[5].destroy();
        }
        if (event.contains("btnDetener6")){
            misHilosEjec.removeIf(mh -> mh.getId() == 6);

            btnIniciar6.setDisable(false);
            btnDetener6.setDisable(true);
            txtError6.setText("");

            threads[6].interrupt();
            processes[6].destroy();
        }
        if (event.contains("btnDetener7")){
            misHilosEjec.removeIf(mh -> mh.getId() == 7);

            btnIniciar7.setDisable(false);
            btnDetener7.setDisable(true);
            txtError7.setText("");

            threads[7].interrupt();
            processes[7].destroy();
        }
        if (event.contains("btnDetener8")){
            misHilosEjec.removeIf(mh -> mh.getId() == 8);

            btnIniciar8.setDisable(false);
            btnDetener8.setDisable(true);
            txtError8.setText("");

            threads[8].interrupt();
            processes[8].destroy();
        }
        if (event.contains("btnDetener9")){
            misHilosEjec.removeIf(mh -> mh.getId() == 9);

            btnIniciar9.setDisable(false);
            btnDetener9.setDisable(true);
            txtError9.setText("");

            threads[9].interrupt();
            processes[9].destroy();
        }
        if (event.contains("btnDetenerD10")){
            misHilosEjec.removeIf(mh -> mh.getId() == 10);

            btnIniciarD10.setDisable(false);
            btnDetenerD10.setDisable(true);
            txtError10.setText("");

            threads[10].interrupt();
            processes[10].destroy();
        }
        if (event.contains("btnDetenerD11")){
            misHilosEjec.removeIf(mh -> mh.getId() == 11);

            btnIniciarD11.setDisable(false);
            btnDetenerD11.setDisable(true);
            txtError11.setText("");

            threads[11].interrupt();
            processes[11].destroy();
        }
        if (event.contains("btnDetenerD12")){
            misHilosEjec.removeIf(mh -> mh.getId() == 12);

            btnIniciarD12.setDisable(false);
            btnDetenerD12.setDisable(true);
            txtError12.setText("");
            threads[12].interrupt();
            processes[12].destroy();
        }


    }
}