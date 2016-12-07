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
    private ImageView frame;


    private IntegerProperty current_image = new SimpleIntegerProperty(0);
    private static final int n_images = 216;


    private static final DecimalFormat df = new DecimalFormat("####0.00");
    private static final double z_min = 0.8; // 0.4 possible in near mode
    private static final double z_max = 4.0; //according to https://msdn.microsoft.com/en-us/library/hh973078.aspx


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.controller = this;
        current_image.addListener((observable, oldValue, newValue) -> {
            double mill = System.currentTimeMillis();
            try {
                frame.setImage(ImageCache.get((int)newValue));
//                Main.secondStage.setImage(ImageCache.get((int)newValue));
//                Main.thirdStage.setImage(ImageCache.get((int)newValue));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("img: "+newValue+";"+"t: "+(System.currentTimeMillis()-mill));
            Main.kinect.img++;

        });
    }

    void setCoords(double[] c){
        Main.kinect.coordCalls++;
        double x,y,z;
        x = c[0];
        y = c[1];
        z = c[2];
        int img = n_images - (int)((z-z_min)/(z_max-z_min)* n_images) + 1;
        if(img<1){img=1;}
        if(img>216){img=216;}
        final int i = img;
        //GUI Updates
        Platform.runLater(()->{
            current_image.setValue(i);
            coordsLabel.setText("x: "+df.format(x)+"\ny: "+df.format(y)+"\nz: "+df.format(z)+"\nimg: "+i);
        });
    }
}
