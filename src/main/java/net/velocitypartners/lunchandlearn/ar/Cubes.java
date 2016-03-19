package net.velocitypartners.lunchandlearn.ar;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Capture;

import java.io.File;
import java.io.FilenameFilter;

public class Cubes extends PApplet {

    // a central location is used for the camera_para.dat and pattern files, so you don't have to copy them to each individual sketch
    // Make sure to change both the camPara and the patternPath String to where the files are on YOUR computer
    // the full path to the camera_para.dat file
    String camPara = "/Users/jgomez/Documents/Processing/libraries/nyar4psg/data/camera_para.dat";
    // the full path to the .patt pattern files
    String patternPath = "/Users/jgomez/Documents/Processing/libraries/nyar4psg/patternMaker/examples/ARToolKit_Patterns";
    // the dimensions at which the AR will take place. with the current library 1280x720 is about the highest possible resolution.
    int arWidth = 640;
    int arHeight = 360;
    // the number of pattern markers (from the complete list of .patt files) that will be detected, here the first 10 from the list.
    int numMarkers = 10;
    float displayScale;
    int[] colors = new int[numMarkers];

    // the resolution at which the mountains will be displayed
    int resX = 60;
    int resY = 60;
    // this is a 2 dimensional float array that all the displayed mountains use during their update-to-draw routine
    float[][] val = new float[resX][resY];

    Capture cam;
    MultiMarker nya;
    float[] scaler = new float[numMarkers];
    float[] noiseScale = new float[numMarkers];
    float[] mountainHeight = new float[numMarkers];
    float[] mountainGrowth = new float[numMarkers];

    public void setup()
    {
        size(1280, 800, OPENGL); // the sketch will resize correctly, so for example setting it to 1920 x 1080 will work as well
        cam = new Capture(this, 1280, 800); // initializing the webcam capture at a specific resolution (correct/possible settings depends on YOUR webcam)
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
            scaler[i] = random(0.8f, 1.9f); // scaled a little smaller or bigger
            noiseScale[i] = random(0.02f, 0.075f); // the perlin noise scale to make it look nicely mountainy
            mountainHeight[i] = random(75, 150); // the maximum height of a mountain
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
            drawMarkers(); // draw the coordinates of the detected markers (2D)
            drawBoxes(); // draw boxes on the detected markers (3D)
        }
    }

    // this function draws the marker coordinates, note that this is completely 2D and based on the AR dimensions (not the final display size)
    void drawMarkers() {
        // set the text alignment (to the left) and size (small)
        textAlign(LEFT, TOP);
        textSize(10);
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
                String s = "(" + (int)(pos2d[j].x) + "," + (int)(pos2d[j].y) + ")";
                fill(255);
                rect(pos2d[j].x, pos2d[j].y, textWidth(s) + 3, textAscent() + textDescent() + 3);
                fill(0);
                text(s, pos2d[j].x + 2, pos2d[j].y + 2);
                fill(255, 0, 0);
                ellipse(pos2d[j].x, pos2d[j].y, 5, 5);
            }
        }
    }

    // this function draws correctly placed 3D boxes on top of detected markers
    void drawBoxes() {
        // set the AR perspective uniformly, this general point-of-view is the same for all markers
        nya.setARPerspective();
        // set the text alignment (full centered) and size (big)
        textAlign(CENTER, CENTER);
        textSize(20);
        // for all the markers...
        for (int i=0; i<numMarkers; i++) {
            // if the marker does NOT exist (the ! exlamation mark negates it) continue to the next marker, aka do nothing
            if ((!nya.isExistMarker(i))) { continue; }
            // the following code is only reached and run if the marker DOES EXIST
            // get the Matrix for this marker and use it (through setMatrix)
            setMatrix(nya.getMarkerMatrix(i));
            scale(1, -1); // turn things upside down to work intuitively for Processing users
            scale(scaler[i]); // scale the box by it's individual scaler
            colors[i] = color(random(255), random(255), random(255), 190); // random color, always at a transparency of 160
            translate(0, 0, 20); // translate the box by half (20) of it's size (40)
            lights(); // turn on some lights
            stroke(0); // give the box a black stroke
            fill(colors[i]); // fill the box by it's individual color
            box(40); // the BOX! ;-)
            noLights(); // turn off the lights
            translate(0f, 0f, 20.1f); // translate to just slightly above the box (to prevent OPENGL uglyness)
            noStroke();
            fill(255, 50);
            rect(-20, -20, 40, 40); // display a transparent white rectangle right above the box
            translate(0f, 0f, 0.1f); // translate to just slightly above the rectangle (to prevent OPENGL uglyness)
            fill(0);
            text("" + i, -20, -20, 40, 40); // display the ID of the box in black text centered in the rectangle
        }
        // reset to the default perspective
        perspective();
    }

    private String[] loadPatternFilenames(String path) {
        File folder = new File(path);
        FilenameFilter pattFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".patt");
            }
        };
        return folder.list(pattFilter);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Cubes"});
    }
}
