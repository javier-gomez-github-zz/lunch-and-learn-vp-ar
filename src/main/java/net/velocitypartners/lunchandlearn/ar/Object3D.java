package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
import processing.core.PApplet;
import saito.objloader.OBJModel;

public class Object3D extends ARHelper {

    OBJModel teapotObject;
    OBJModel catObject;
    OBJModel iphoneObject;

    @Override
    public void setup() {
        super.setup();
        teapotObject = new OBJModel(this, "extra/objects/teapot/teapot.obj");
        teapotObject.scale(20);
        teapotObject.disableMaterial();

        catObject = new OBJModel(this, "extra/objects/cat/cat.obj");
        catObject.scale(250);

        iphoneObject = new OBJModel(this, "extra/objects/iphone/iPhone5.obj");
        iphoneObject.scale(0.15f);

    }

    public void draw()
    {
        // if there is a camera available
        if (camera.available()) {
            initCameraAndDetectMarkers();
            // draw the 3D Object in the detected markers
            drawObject3D();
        }
    }

    // this function draws correctly placed 3D objects on top of detected markers
    void drawObject3D() {
        // set the AR perspective uniformly, this general point-of-view is the same for all markers
        nya.setARPerspective();
        // iterates over all the markers
        for (int i = 0; i < numMarkers; i++) {
            // if the marker does NOT exist continue to the next marker (do nothing)
            if ((!nya.isExistMarker(i)) || ((i != 12) && (i != 23) && (i != 45))) { continue; }

            // get the Matrix for this marker and use it (through setMatrix)
            setMatrix(nya.getMarkerMatrix(i));

            translate(0, 0, 30);
            rotateX(4.7f);
            lights();
            if (i == 12) {
                // give the object a black stroke
                stroke(0, 0, 0);
                // fill the object by it's individual color
                fill(255, 0, 0);
                // draws the 3D Object
                teapotObject.draw();
            }
            else if (i == 23) {
                noStroke();
                // draws the 3D Object
                catObject.draw();
            } else if (i == 45) {
                noStroke();
                // draws the 3D Object
                iphoneObject.draw();
            }

        }
        // reset to the default perspective
        perspective();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Object3D"});
    }
}
