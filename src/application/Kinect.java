package application;


import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

/*This class is an implementation of the abstract class J4KSDK.
  It is a simple example of source code that shows how to read
  depth, video, and skeleton frames from a Kinect sensor.*/
class Kinect extends J4KSDK{
    int depth = 0;
    int color = 0;
    int skel = 0;
    int coordCalls = 0;
    int img1 = 0;
    int img2 = 0;
    int img3 = 0;
    double duration = 0;

    private static final int joint_tracked = Skeleton.HEAD; //possible 0-19(https://msdn.microsoft.com/en-us/library/microsoft.kinect.jointtype.aspx); 20-24 no info
    /*The constructor of the class initializes the native Kinect
      SKD libraries and creates a new VideoFrame object.*/
    Kinect() {
        super();
        //setNearMode(true); //not working correctly
        start(J4KSDK.SKELETON);
    }

    /*The following method will run every time a new depth frame is
      received from the Kinect sensor. The data of the depth frame is
      converted into a DepthMap object, with U,V texture mapping if
      available.*/
    @Override
    public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
        //do nothing
        if(depth==0){
            duration = System.currentTimeMillis();
        }
        depth++;
    }

    @Override
    public void onColorFrameEvent(byte[] bytes) {
        //do nothing
        color++;
    }

    /*The following method will run every time a new skeleton frame
      is received from the Kinect sensor. The skeletons are converted
      into Skeleton objects.*/
    @Override
    public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] joint_position, float[] joint_orientation, byte[] joint_status) {
        skel++;
        Skeleton skeletons[]=new Skeleton[getMaxNumberOfSkeletons()];
        for(int i=0;i<getMaxNumberOfSkeletons();i++){
            skeletons[i]=Skeleton.getSkeleton(i, skeleton_tracked, joint_position, joint_orientation, joint_status, this);
            if(skeletons[i].isTracked()) {
//                float[] jpo = skeletons[i].getJointPositions();
//                for (int f=0;f<75;f=f+3) {
//                    System.out.println(i+"|"+ (f/3) + ": " + jpo[f] + " " +jpo[f+1] + " " + jpo[f+2]);
//                }
                double[] coords = skeletons[i].get3DJoint(joint_tracked);
                if(coords[0]!=0){ //TODO possibly useless
                    Main.controller.setCoords(coords);
                }
            }


//            }
        }
    }


}
