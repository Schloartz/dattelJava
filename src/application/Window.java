package application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


class Window extends Stage{ // creates a stage with an imageview (for the two secondary windows)
    private ImageView frame;

    Window(int id, int width, int height){
        StackPane root = new StackPane();
        frame = new ImageView();

        root.getChildren().add(frame);
        setScene(new Scene(root, width, height));
        setTitle("Dattelvideo Screen "+id);
        if(id==2){
            setX(712-width-100); //left
        }else if(id==3){
            setX(712+width+100);
        }
        setY(244);
    }

    void setImage(Image img){
        frame.setImage(img);
    }

}
