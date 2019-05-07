package com.sdggroup.ar;

import com.sdggroup.ar.util.ARHelper;
import processing.core.PApplet;
import processing.core.PVector;

public class Coordinates extends ARHelper {

    public void draw()
    {
        // if there is a camera available
        if (camera.available()) {
            initCameraAndDetectMarkers();
            // draw the coordinates of the detected markers (2D)
            drawCoordinates();
        }
    }

    // this function draws the marker coordinates, note that this is completely 2D and based on the AR dimensions (not the final display size)
    private void drawCoordinates() {
        // set the text alignment
        textAlign(LEFT, TOP);
        // set the text size
        textSize(30);
        // removes borders
        noStroke();

        // scale from AR detection size to sketch display size (changes the display of the coordinates, not the values)
        scale(displayScale);

        // iterates over all the markers
        for (int i = 0; i < numMarkers; i++) {
            // if the marker does NOT exist continue to the next marker (do nothing)
            //if ((!nya.isExistMarker(i)) || ((i != 12) && (i != 23) && (i != 45) && (i != 88))) { continue; }
            if ((!nya.isExist(i))) { continue; }

            // get the four marker coordinates into an array of 2D PVectors
            PVector[] pos2d = nya.getMarkerVertex2D(i);

            // draw each vector both textually and with a red dot
            for (PVector pVector : pos2d) {
                String s = "(" + (pVector.x) + "," + (pVector.y) + ")";
                fill(255);
                rect(pVector.x, pVector.y, textWidth(s) + 3, textAscent() + textDescent() + 3);
                fill(0);
                text(s, pVector.x + 2, pVector.y + 2);
                fill(255, 0, 0);
                ellipse(pVector.x, pVector.y, 10, 10);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "com.sdggroup.ar.Coordinates"});
    }
}
