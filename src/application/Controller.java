package application;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class Controller implements Initializable{
    @FXML
    private Label coordsLabel;
    @FXML
    private ImageView left, middle, right;


    private IntegerProperty img_left = new SimpleIntegerProperty(0);
    private IntegerProperty img_middle = new SimpleIntegerProperty(0);
    private IntegerProperty img_right = new SimpleIntegerProperty(0);
    //number of images
    private static final int n_mix = 1840;
    private static final int n_eat = 810;
    private static final int n_go = 1200;



    private static final DecimalFormat df = new DecimalFormat("####0.00");
    private static final double z_min = 0.8; // 0.4 possible in near mode
    private static final double z_max = 4.0; //according to https://msdn.microsoft.com/en-us/library/hh973078.aspx


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.controller = this;
        img_left.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
            try {
                left.setImage(ImageCache.get((int)newValue));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("left: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img++;
        });
        img_middle.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
            try {
                middle.setImage(ImageCache.get((int)newValue));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("middle: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img++;
        });
        img_right.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
            try {
                right.setImage(ImageCache.get((int)newValue));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("right: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img++;
        });
    }

    void setCoords(double[] c){
        Main.kinect.coordCalls++;
        double x,y,z;
        x = c[0];
        y = c[1];
        z = c[2];
        //Mix
        int i = n_mix - (int)((z-z_min)/(z_max-z_min)* n_mix) + 1;
        if(i<1){i=1;}
        if(i>n_mix){i=n_mix;}
        final int mix = i;
        //Eat
        i = n_eat - (int)((z-z_min)/(z_max-z_min)* n_eat) + 1;
        if(i<1){i=1;}
        if(i>n_eat){i=n_eat;}
        final int eat = i;
        //Go
        i = n_go - (int)((z-z_min)/(z_max-z_min)* n_go) + 1;
        if(i<1){i=1;}
        if(i>n_go){i=n_go;}
        final int go = i;


        //GUI Updates
        Platform.runLater(()->{
            img_left.setValue(mix);
            img_middle.setValue(eat);
            img_right.setValue(go);
            coordsLabel.setText("x: "+df.format(x)+"\ny: "+df.format(y)+"\nz: "+df.format(z)+"\nimg: "+mix+","+eat+","+go);
        });
    }
}
