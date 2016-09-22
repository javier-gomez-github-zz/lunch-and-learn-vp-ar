package net.velocitypartners.lunchandlearn.ar;

import net.velocitypartners.lunchandlearn.ar.util.ARHelper;
import processing.core.PApplet;
import processing.core.PVector;

public class Coordinates extends ARHelper {

    public void draw()
    {
        // if there is a cam image coming in...
        if (cam.available()) {
            initCameraAndDetectMarkers();
            drawMarkers(); // draw the coordinates of the detected markers (2D)
        }
    }

    // this function draws the marker coordinates, note that this is completely 2D and based on the AR dimensions (not the final display size)
    void drawMarkers() {
        // set the text alignment (to the left) and size (small)
        textAlign(LEFT, TOP);
        textSize(30);
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
