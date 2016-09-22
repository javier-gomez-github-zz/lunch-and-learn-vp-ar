package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
import processing.core.PApplet;

public class Cubes extends ARHelper {

    public void draw()
    {
        // if there is a cam image coming in...
        if (cam.available()) {
            initCameraAndDetectMarkers();
            drawBoxes(); // draw boxes on the detected markers (3D)
        }
    }

    // this function draws correctly placed 3D boxes on top of detected markers
    void drawBoxes() {
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
            colors[i] = color(255, 0, 0, 190);
            translate(0, 0, 30); // translate the box by half (20) of it's size (40)
            lights(); // turn on some lights
            stroke(0); // give the box a black stroke
            fill(colors[i]); // fill the box by it's individual color
            box(65); // the BOX
            noLights(); // turn off the lights
        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Cubes"});
    }
}
