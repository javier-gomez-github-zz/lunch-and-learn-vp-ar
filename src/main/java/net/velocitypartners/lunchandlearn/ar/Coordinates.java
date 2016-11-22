package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
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
    void drawCoordinates() {
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
            if ((!nya.isExistMarker(i)) || ((i != 12) && (i != 23) && (i != 45) && (i != 88))) { continue; }

            // get the four marker coordinates into an array of 2D PVectors
            PVector[] pos2d = nya.getMarkerVertex2D(i);

            // draw each vector both textually and with a red dot
            for (int j = 0; j < pos2d.length; j++) {
                String s = "(" + (pos2d[j].x) + "," + (pos2d[j].y) + ")";
                fill(255);
                rect(pos2d[j].x, pos2d[j].y, textWidth(s) + 3, textAscent() + textDescent() + 3);
                fill(0);
                text(s, pos2d[j].x + 2, pos2d[j].y + 2);
                fill(255, 0, 0);
                ellipse(pos2d[j].x, pos2d[j].y, 10, 10);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Coordinates"});
    }
}
