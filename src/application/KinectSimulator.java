package application;


import java.util.TimerTask;

class KinectSimulator extends TimerTask{
    private double[] coords;

    KinectSimulator(){
        coords = new double[3];
        coords[0] = -0.3; //x
        coords[1] = 1; //y
        coords[2] = 4; //z
    }

    @Override
    public void run() {
        coords[0] = coords[0] + 0.001;
        coords[2] = coords[2] - 0.01;
        Main.controller.setCoords(coords);
    }
}
