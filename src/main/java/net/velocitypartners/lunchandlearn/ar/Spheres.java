package net.velocitypartners.lunchandlearn.ar;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;

import java.io.File;
import java.io.FilenameFilter;

public class Spheres extends PApplet {
    // the full path to the camera_para.dat file
    String camPara = "extra/camera_para.dat";
    // the full path to the .patt pattern files
    String patternPath = "extra/patterns";
    // the dimensions at which the AR will take place. with the current library 1280x720 is about the highest possible resolution.
    int arWidth = 1280;
    int arHeight = 720;
    // the number of pattern markers (from the complete list of .patt files) that will be detected, here the first 10 from the list.
    int numMarkers = 100;
    float displayScale;
    int[] colors = new int[numMarkers];

    Capture cam;
    MultiMarker nya;

    public void setup()
    {
        cam = new Capture(this, "name=HD Pro Webcam C920,size=1280x720,fps=30");
        size(arWidth, arHeight, OPENGL); // the sketch will resize correctly, so for example setting it to 1920 x 1080 will work as well
        cam.start(); // start capturing
        // to correct for the scale difference between the AR detection coordinates and the size at which the result is displayed
        displayScale = (float) width / arWidth;
        noStroke(); // turn off stroke for the rest of this sketch :-)
        // create a new MultiMarker at a specific resolution (arWidth x arHeight), with the default camera calibration and coordinate system
        nya = new MultiMarker(this, arWidth, arHeight, camPara, NyAR4PsgConfig.CONFIG_DEFAULT);
        // set the delay after which a lost marker is no longer displayed. by default set to something higher, but here manually set to immediate.
        nya.setLostDelay(1);
        String[] patterns = loadPatternFilenames(patternPath);
        // for the selected number of markers, add the marker for detection
        // create an individual scale, noiseScale and maximum mountainHeight for that marker (= mountain)
        for (int i=0; i<numMarkers; i++) {
            nya.addARMarker(patternPath + "/" + patterns[i], 80);
        }
    }

    public void draw()
    {
        // if there is a cam image coming in...
        if (cam.available()) {
            cam.read(); // read the cam image
            background(cam); // a background call is needed for correct display of the marker results
            image(cam, 0, 0, width, height); // display the image at the width and height of the sketch window
            // create a copy of the cam image at the resolution of the AR detection (otherwise nya.detect will throw an assertion error!)
            PImage cSmall = cam.get();
            cSmall.resize(arWidth, arHeight);
            nya.detect(cSmall); // detect markers in the image
            drawSpheres(); // draw boxes on the detected markers (3D)
        }
    }

    // this function draws correctly placed 3D spheres on top of detected markers
    void drawSpheres() {
        // set the AR perspective uniformly, this general point-of-view is the same for all markers
        nya.setARPerspective();
        // for all the markers...
        for (int i=0; i<numMarkers; i++) {
            // if the marker does NOT exist (the ! exlamation mark negates it) continue to the next marker, aka do nothing
            if ((!nya.isExistMarker(i))) { continue; }
            // the following code is only reached and run if the marker DOES EXIST
            // get the Matrix for this marker and use it (through setMatrix)
            setMatrix(nya.getMarkerMatrix(i));
            scale(1, -1); // turn things upside down to work intuitively for Processing users
            colors[i] = color(0, 155, 155, 100);
            translate(0, 0, 100);
//            rotateX(90);
            lights(); // turn on some lights
            stroke(0); // give the sphere a black stroke
            fill(colors[i]); // fill the box by it's individual color
            sphere(60); // the SPHERE
            noLights(); // turn off the lights
        }
        // reset to the default perspective
        perspective();
    }

    private String[] loadPatternFilenames(String path) {
        File folder = new File(path);
        FilenameFilter pattFilter = (dir, name) -> name.toLowerCase().endsWith(".patt");
        return folder.list(pattFilter);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Spheres"});
    }
}
