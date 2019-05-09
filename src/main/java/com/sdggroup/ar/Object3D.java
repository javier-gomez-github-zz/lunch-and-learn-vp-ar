package com.sdggroup.ar;

import com.sdggroup.ar.util.ARHelper;
import processing.core.PApplet;
import saito.objloader.OBJModel;

public class Object3D extends ARHelper {

    private OBJModel teapot;
    private OBJModel cat;
    private OBJModel iPhone;
    private OBJModel crema;
    private OBJModel pill;

    @Override
    public void setup() {
        super.setup();
        teapot = new OBJModel(this, "extra/objects/teapot/teapot.obj");
        teapot.scale(20);
        teapot.disableMaterial();

        cat = new OBJModel(this, "extra/objects/cat/cat.obj");
        cat.scale(250);

        iPhone = new OBJModel(this, "extra/objects/iphone/iPhone5.obj");
        iPhone.scale(0.15f);

        crema = new OBJModel(this, "extra/objects/crema/crema.obj");
        crema.scale(50f);

        pill= new OBJModel(this, "extra/objects/pill/pill.obj");
        pill.scale(150f);
    }

    public void draw() {
        // if there is a camera available
        if (camera.available()) {
            initCameraAndDetectMarkers();
            // draw the 3D Object in the detected markers
            drawObject3D();
        }
    }

    // this function draws correctly placed 3D objects on top of detected markers
    private void drawObject3D() {
        // set the AR perspective uniformly, this general point-of-view is the same for all markers
        nya.setARPerspective();
        // iterates over all the markers
        for (int i = 0; i < numMarkers; i++) {
            // if the marker does NOT exist continue to the next marker (do nothing)
            if ((!nya.isExist(i))) {
                continue;
            }

            // get the Matrix for this marker and use it (through setMatrix)
            setMatrix(nya.getMatrix(i));

            translate(0, 0, 30);
            rotateX(4.7f);
            lights();

            // Markers: 1 = 91; 2 = 10; 3 = 16; 4 = 31; 5 = 51; 6 = 58; 7 = 94
            if (i == 91) {
                // give the object a black stroke
                stroke(0, 0, 0);
                // fill the object by it's individual color
                fill(255, 0, 0);
                // draws the 3D Object
                teapot.draw();
            } else if (i == 10) {
                noStroke();
                // draws the 3D Object
                cat.draw();
            } else if (i == 16) {
                noStroke();
                // draws the 3D Object
                iPhone.draw();
            } else if (i == 31) {
                noStroke();
                // draws the 3D Object
                crema.draw();
            } else if (i == 51) {
                noStroke();
                // draws the 3D Object
                pill.draw();
            }

        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "com.sdggroup.ar.Object3D"});
    }
}
