package com.sdggroup.ar;

import com.sdggroup.ar.util.ARHelper;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class SpheresAndTextures extends ARHelper {

    protected PImage earth;
    protected PShape globe;

    @Override
    public void setup() {
        super.setup();

        // loads texture from external file
        earth = loadImage("extra/textures/world32k.jpg");
        // creates an Sphere object of size 60
        globe = createShape(SPHERE, 60);
        // sets the previous defined texture into the sphere object
        globe.setTexture(earth);
        // removes borders
        noStroke();
    }

    public void draw()
    {
        // if there is a camera available
        if (camera.available()) {
            initCameraAndDetectMarkers();
            // draws spheres with textures on the detected markers (3D)
            loadTexturesAndDrawSphere();
        }
    }

    // this function draws correctly placed 3D spheres on top of detected markers with textures
    void loadTexturesAndDrawSphere() {
        // set the AR perspective uniformly, this general point-of-view is the same for all markers
        nya.setARPerspective();
        // iterates over all the markers
        for (int i = 0; i < numMarkers; i++) {
            // if the marker does NOT exist continue to the next marker (do nothing)
            if ((!nya.isExistMarker(i)) || ((i != 12) && (i != 23) && (i != 45) && (i != 88))) { continue; }

            // get the Matrix for this marker and use it (through setMatrix)
            setMatrix(nya.getMarkerMatrix(i));
            scale(1, -1); // turn things upside down to work intuitively for Processing users

            // translate the sphere on axis Z (for perspective)
            translate(0, 0, 100);

            // rotates the sphere in axis X, Y and Z
            rotateX(160.0f);
            rotateY(45.0f);
            rotateZ(90);

            // turn on some lights
            lights();

            // give the sphere a black stroke
            stroke(0);

            // draws the shape
            shape(globe);
        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "com.sdggroup.ar.SpheresAndTextures"});
    }
}
