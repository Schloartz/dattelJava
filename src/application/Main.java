package application;

import edu.ufl.digitalworlds.gui.DWApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
    static Kinect kinect;
    static Controller controller;


    @Override
    public void start(Stage stage) throws Exception{
        //GUI
        Parent root = FXMLLoader.load(getClass().getResource("stage.fxml"));
        stage.setTitle("DattelVideo");
        Scene scene = new Scene(root, 1280+1920+1280 , 1200);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.setOnKeyPressed(e -> {
            if(e.getCode()== KeyCode.ESCAPE) {
                stage.close();
                kinect.stop();
                printDebug();
            }
        });
        stage.setOnCloseRequest(event ->{
            stage.close();
            kinect.stop();
            printDebug();
        });


        stage.show();
        //Kinect
        initKinect();

    }




    public static void main(String[] args) {
        launch(args);
    }

    private void printDebug() {
        double dur = (System.currentTimeMillis() - Main.kinect.duration)/1000;
        System.out.println("duration: "+dur);
        System.out.println("depth: "+Main.kinect.depth+" (fps: "+Main.kinect.depth/dur+")");
        System.out.println("color: "+Main.kinect.color+" (fps: "+Main.kinect.color/dur+")");
        System.out.println("skel: "+Main.kinect.skel+" (fps: "+Main.kinect.skel/dur+")");
        System.out.println("cccalls: "+Main.kinect.coordCalls+" (fps: "+Main.kinect.coordCalls/dur+")");
        System.out.println("img: "+Main.kinect.img+" (fps: "+Main.kinect.img/dur+")");


    }

    private static void initKinect(){
        //Kinect
        if(!System.getProperty("os.arch").toLowerCase().contains("64"))
        {
            System.out.println("WARNING: You are running a 32bit version of Java.");
            System.out.println("This may reduce significantly the performance of this application.");
            System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
        }

        kinect=new Kinect();
        if(!kinect.start(Kinect.DEPTH| Kinect.COLOR |Kinect.SKELETON |Kinect.XYZ|Kinect.PLAYER_INDEX))
        {
            DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");
            System.exit(0);
        }
    }
}
