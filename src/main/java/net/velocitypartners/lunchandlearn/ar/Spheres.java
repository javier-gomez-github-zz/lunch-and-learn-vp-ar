package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
import processing.core.PApplet;

public class Spheres extends ARHelper {

    public void draw()
    {
        // if there is a cam image coming in...
        if (cam.available()) {
            initCameraAndDetectMarkers();
            drawSpheres(); // draw spheres on the detected markers (3D)
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
            sphere(50); // the SPHERE
            noLights(); // turn off the lights
        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Spheres"});
    }
}
