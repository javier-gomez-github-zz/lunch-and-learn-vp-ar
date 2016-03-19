package net.velocitypartners.lunchandlearn.ar;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Capture;

import java.io.File;
import java.io.FilenameFilter;

public class Example extends PApplet {

    String camPara = "/Users/jgomez/Documents/Processing/libraries/nyar4psg/data/camera_para.dat";
    String patternPath = "/Users/jgomez/Documents/Processing/libraries/nyar4psg/patternMaker/examples/ARToolKit_Patterns";

    int arWidth = 640;
    int arHeight = 360;

    int numMarkers = 10;
    float displayScale;
    int[] colors = new int[numMarkers];

    int resX = 60;
    int resY = 60;

    float[][] val = new float[resX][resY];

    Capture cam;
    MultiMarker nya;
    float[] scaler = new float[numMarkers];
    float[] noiseScale = new float[numMarkers];
    float[] mountainHeight = new float[numMarkers];
    float[] mountainGrowth = new float[numMarkers];

    @Override
    public void setup() {
        size(1280, 720, OPENGL);
        cam = new Capture(this, 1280, 720);
        cam.start();
        displayScale = (float) width / arWidth;
        noStroke();
        nya = new MultiMarker(this, arWidth, arHeight, camPara, NyAR4PsgConfig.CONFIG_DEFAULT);
        nya.setLostDelay(1);

        String[] patterns = loadPatternFilenames(patternPath);
        for (int i = 0; i < numMarkers; i++) {
            nya.addARMarker(patternPath + "/" + patterns[i], 80);
            scaler[i] = random(0.8f, 1.9f);
            noiseScale[i] = random(0.02f, 0.075f);
            mountainHeight[i] = random(75, 150);
        }
    }

    @Override
    public void draw() {
        if (cam.available()) {
            cam.read();
            background(cam);
            image(cam, 0, 0, width, height);
            PImage cSmall = cam.get();
            cSmall.resize(arWidth, arHeight);
            nya.detect(cSmall);
            drawMarkers();
            drawBoxes();
        }
    }

    private void drawMarkers() {
        textAlign(LEFT, TOP);
        textSize(10);
        noStroke();
        scale(displayScale);
        for (int i = 0; i < numMarkers; i++) {
            if ((!nya.isExistMarker(i))) {
                continue;
            }
            PVector[] pos2d = nya.getMarkerVertex2D(i);
            for (int j = 0; j < pos2d.length; j++) {
                String s = "(" + (int) (pos2d[j].x) + "," + (int) (pos2d[j].y) + ")";
                fill(255);
                rect(pos2d[j].x, pos2d[j].y, textWidth(s) + 3, textAscent() + textDescent() + 3);
                fill(0);
                text(s, pos2d[j].x + 2, pos2d[j].y + 2);
                fill(255, 0, 0);
                ellipse(pos2d[j].x, pos2d[j].y, 5, 5);
            }
        }
    }

    private void drawBoxes() {
        nya.setARPerspective();
        textAlign(CENTER, CENTER);
        textSize(20);
        for (int i = 0; i < numMarkers; i++) {
            if ((!nya.isExistMarker(i))) {
                continue;
            }
            setMatrix(nya.getMarkerMatrix(i));
            scale(1, -1);
            scale(scaler[i]);
            colors[i] = color(random(255), random(255), random(255), 190);
            translate(0, 0, 20);
            lights();
            stroke(0);
            fill(colors[i]);
            box(40);
            noLights();
            translate(0f, 0f, 20.1f);
            noStroke();
            fill(255, 50);
            rect(-20, -20, 40, 40);
            translate(0f, 0f, 0.1f);
            fill(0);
            text("" + i, -20, -20, 40, 40);
        }
        perspective();
    }

    private String[] loadPatternFilenames(String path) {
        File folder = new File(path);
        FilenameFilter pattFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".patt");
            }
        };
        return folder.list(pattFilter);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "net.velocitypartners.lunchandlearn.ar.Example"});
    }
}
