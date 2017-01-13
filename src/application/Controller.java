package application;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

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
    private static final double z_min = 1; // 0.8 minimum
    private static final double z_max = 4.0; //according to https://msdn.microsoft.com/en-us/library/hh973078.aspx

    private static final double go_pos_x = 0.5; //position of the monitor (1m left of the kinect)
    private static final double go_pos_z = 1; //position of the monitor (0.3m away from the kinect)
    private static final double go_max_x = -0.2;
    private static final double go_max_z = 4;

    private static final double mix_pos_x = -0.5; //position of the monitor (1m left of the kinect)
    private static final double mix_pos_z = 1; //position of the monitor (0.3m away from the kinect)
    private static final double mix_max_x = 0.2;
    private static final double mix_max_z = 4;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.controller = this;
        img_left.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
//            try {
//                left.setImage(ImageCache.get((int) newValue));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
            left.setImage(new Image(getClass().getResourceAsStream("/img/mix/mix-" + newValue + ".jpg")));
            //System.out.println("left: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img1++;
        });
        img_middle.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
//            try {
//                middle.setImage(ImageCache.get((int) newValue+10000));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
            middle.setImage(new Image(getClass().getResourceAsStream("/img/eat/eat-" + newValue + ".jpg")));
            //System.out.println("middle: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img2++;
        });
        img_right.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
//            try {
//                right.setImage(ImageCache.get((int) newValue+20000));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
            right.setImage(new Image(getClass().getResourceAsStream("/img/go/go-" + newValue + ".jpg")));
            //System.out.println("right: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img3++;
        });
    }

    void setCoords(double[] c){ //method gets called from kinect -> computes index of images to be displayed
        Main.kinect.coordCalls++;
        double x,y,z;
        x = c[0]; //Weite (negativ/positiv)
        y = c[1]; //Höhe (negativ/positiv)
        z = c[2]; //Tiefe (positiv - je weiter weg, desto größer)
        //Mix
        //int i = n_mix - (int)((z-z_min)/(z_max-z_min)* n_mix) + 1;
        int i = n_mix - (int) ((Math.sqrt(Math.pow((x - mix_pos_x),2)+Math.pow(z - mix_pos_z, 2)))/(Math.sqrt(Math.pow((mix_max_x - mix_pos_x),2)+Math.pow(mix_max_z - mix_pos_z, 2))) * n_mix) + 1;
        if(i<1){i=1;}
        if(i>n_mix){i=n_mix;}
        final int mix = i;
        //Eat
        i = n_eat - (int)((z-z_min)/(z_max-z_min)* n_eat) + 1;
        if(i<1){i=1;}
        if(i>n_eat){i=n_eat;}
        final int eat = i;
        //Go
        //i = n_go - (int)((z-z_min)/(z_max-z_min)* n_go) + 1;
        i = n_go - (int) ((Math.sqrt(Math.pow((x - go_pos_x),2)+Math.pow(z - go_pos_z, 2)))/(Math.sqrt(Math.pow((go_max_x - go_pos_x),2)+Math.pow(go_max_z - go_pos_z, 2))) * n_go) + 1;
        if(i<1){i=1;}
        if(i>n_go){i=n_go;}
        final int go = i;


        //GUI Updates
        Platform.runLater(()->{
            img_left.setValue(mix);
            img_middle.setValue(eat);
            img_right.setValue(go);
            //coordsLabel.setText("x: "+df.format(x)+"\ny: "+df.format(y)+"\nz: "+df.format(z)+"\nimg: "+mix+","+eat+","+go);
            coordsLabel.setText("x: "+df.format(x)+"\ny: "+df.format(y)+"\nz: "+df.format(z)+"\nimg: "+Math.round((double) mix/(double) n_mix*100)+"% ,"+Math.round((double) eat/(double) n_eat*100)+"% ,"+Math.round((double) go/(double) n_go*100)+"%");

        });
    }
}
