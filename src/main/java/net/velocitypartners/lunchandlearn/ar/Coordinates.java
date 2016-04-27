package net.velocitypartners.lunchandlearn.ar;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Capture;

import java.io.File;
import java.io.FilenameFilter;

public class Coordinates extends PApplet {
    // the full path to the camera_para.dat file
    String camPara = "extra/camera_para.dat";
    // the full path to the .patt pattern files
    String patternPath = "extra/patterns";
    // the dimensions at which the AR will take place. with the current library 1280x720 is about the highest possible resolution.
    int arWidth = 1280;
    int arHeight = 720;
    // the number of pattern markers (from the complete list of .patt files) that will be detected, here the first 10 from the list.
    int numMarkers = 100;

    Capture cam;
    MultiMarker nya;
    float displayScale;

    public void setup()
    {
        cam = new Capture(this, "name=HD Pro Webcam C920,size=1280x720,fps=30");
        size(arWidth, arHeight, OPENGL); // the sketch will resize correctly, so for example setting it to 1920 x 1080 will work as well
        cam.start();
        // create a text font for the coordinates and numbers on the boxes at a decent (80) resolution
        textFont(createFont("Arial", 80));
        // to correct for the scale difference between the AR detection coordinates and the size at which the result is displayed
        displayScale = (float) width / arWidth;
        // create a new MultiMarker at a specific resolution (arWidth x arHeight), with the default camera calibration and coordinate system
        nya = new MultiMarker(this, arWidth, arHeight, camPara, NyAR4PsgConfig.CONFIG_DEFAULT);
        // set the delay after which a lost marker is no longer displayed. by default set to something higher, but here manually set to immediate.
        nya.setLostDelay(1);
        String[] patterns = loadPatternFilenames(patternPath);
        // for the selected number of markers, add the marker for detection
        for (int i=0; i<numMarkers; i++) {
            nya.addARMarker(patternPath + "/" + patterns[i], 80);
        }
    }

    public void draw()
    {
        cam.read(); // read the cam image
        background(cam); // a background call is needed for correct display of the marker results
        image(cam, 0, 0, width, height); // display the image at the width and height of the sketch window
        PImage cSmall = cam.get();
        cSmall.resize(arWidth, arHeight);
        nya.detect(cSmall); // detect markers in the input image at the correct resolution (incorrect resolution will give assertion error)
        drawMarkers(); // draw the coordinates of the detected markers (2D)
    }

    // this function draws the marker coordinates, note that this is completely 2D and based on the AR dimensions (not the final display size)
    void drawMarkers() {
        // set the text alignment (to the left) and size (small)
        textAlign(LEFT, TOP);
        textSize(30);
        noStroke();
        // scale from AR detection size to sketch display size (changes the display of the coordinates, not the values)
        scale(displayScale);
        // for all the markers...
        for (int i=0; i<numMarkers; i++) {
            // if the marker does NOT exist (the ! exlamation mark negates it) continue to the next marker, aka do nothing
            if ((!nya.isExistMarker(i))) { continue; }
            // the following code is only reached and run if the marker DOES EXIST
            // get the four marker coordinates into an array of 2D PVectors
            PVector[] pos2d = nya.getMarkerVertex2D(i);
            // draw each vector both textually and with a red dot
            for (int j=0; j<pos2d.length; j++) {
                String s = "(" + (pos2d[j].x) + "," + (pos2d[j].y) + ")";
                fill(255);
                rect(pos2d[j].x, pos2d[j].y, textWidth(s) + 3, textAscent() + textDescent() + 3);
                fill(0);
                text(s, pos2d[j].x + 2, pos2d[j].y + 2);
                fill(255, 0, 0);
                ellipse(pos2d[j].x, pos2d[j].y, 10, 10);
            }
        }
    }

    private String[] loadPatternFilenames(String path) {
        File folder = new File(path);
        FilenameFilter pattFilter = (dir, name) -> name.toLowerCase().endsWith(".patt");
        return folder.list(pattFilter);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Coordinates"});
    }
}
