package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
import processing.core.PApplet;

public class Spheres extends ARHelper {

    public void draw()
    {
        // if there is a camera available
        if (camera.available()) {
            initCameraAndDetectMarkers();
            // draw spheres on the detected markers (3D)
            drawSpheres();
        }
    }

    // this function draws correctly placed 3D spheres on top of detected markers
    void drawSpheres() {
        // set the AR perspective uniformly, this general point-of-view is the same for all markers
        nya.setARPerspective();
        // iterates over all the markers
        for (int i=0; i<numMarkers; i++) {
            // if the marker does NOT exist continue to the next marker (do nothing)
            if ((!nya.isExistMarker(i))) { continue; }

            // get the Matrix for this marker and use it (through setMatrix)
            setMatrix(nya.getMarkerMatrix(i));

            // set color (RGB) and transparency
            colors[i] = color(155, 0, 155, 140);

            // translate the sphere on axis Z (for perspective)
            translate(0, 0, 100);

            // turn on some lights
            lights();
            // give the sphere a black stroke
            stroke(0);

            // fill the sphere by it's individual color
            fill(colors[i]);

            // draws the SPHERE (size 50)
            sphere(50);
        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Spheres"});
    }
}
