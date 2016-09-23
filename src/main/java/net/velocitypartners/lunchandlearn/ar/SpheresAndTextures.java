package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class SpheresAndTextures extends ARHelper {

    protected PImage earth;
    protected PShape globe;

    @Override
    public void setup() {
        super.setup();
        earth = loadImage("extra/textures/world32k.jpg");
        globe = createShape(SPHERE, 100);
        globe.setTexture(earth);
        noStroke();
    }

    public void draw()
    {
        // if there is a cam image coming in...
        if (cam.available()) {
            initCameraAndDetectMarkers();
            loadTexturesAndDrawSphere();
        }
    }

    // this function draws correctly placed 3D spheres on top of detected markers with textures
    void loadTexturesAndDrawSphere() {
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
            translate(0, 0, 200);
            rotateX(300.0f);
            rotateY(50.0f);
            lights(); // turn on some lights
            stroke(0); // give the sphere a black stroke
            shape(globe);
            noLights(); // turn off the lights
        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.SpheresAndTextures"});
    }
}
