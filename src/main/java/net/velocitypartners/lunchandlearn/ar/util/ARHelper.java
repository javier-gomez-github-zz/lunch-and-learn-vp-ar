package net.velocitypartners.lunchandlearn.ar.util;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;

import java.io.File;
import java.io.FilenameFilter;

public class ARHelper extends PApplet {

    // the full path to the camera_para.dat file
    protected final String camPara = "extra/camera_para.dat";
    // the full path to the .patt pattern files
    protected final String patternPath = "extra/patterns";
    // the dimensions at which the AR will take place. with the current library 1280x720 is about the highest possible resolution.
    protected final int arWidth = 1280;
    protected final int arHeight = 720;
    // the number of pattern markers (from the complete list of .patt files) that will be detected, here the first 10 from the list.
    protected final int numMarkers = 100;
    protected float displayScale;
    protected final int[] colors = new int[numMarkers];

    protected Capture cam;
    protected MultiMarker nya;

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
        String[] patterns = loadPatternFileNames(patternPath);
        // for the selected number of markers, add the marker for detection
        // create an individual scale, noiseScale and maximum mountainHeight for that marker (= mountain)
        for (int i=0; i<numMarkers; i++) {
            nya.addARMarker(patternPath + "/" + patterns[i], 80);
        }
    }

    private String[] loadPatternFileNames(String path) {
        File folder = new File(path);
        FilenameFilter pattFilter = (dir, name) -> name.toLowerCase().endsWith(".patt");
        return folder.list(pattFilter);
    }

    protected void initCameraAndDetectMarkers() {
        cam.read(); // read the cam image
        background(cam); // a background call is needed for correct display of the marker results
        image(cam, 0, 0, width, height); // display the image at the width and height of the sketch window
        // create a copy of the cam image at the resolution of the AR detection (otherwise nya.detect will throw an assertion error!)
        PImage cSmall = cam.get();
        cSmall.resize(arWidth, arHeight);
        nya.detect(cSmall); // detect markers in the image
    }
}
