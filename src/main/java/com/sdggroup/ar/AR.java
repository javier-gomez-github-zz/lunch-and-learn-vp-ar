package com.sdggroup.ar;

public class AR {

    public static void main(String[] args) {
        switch (args[0]) {
            case "CU":
                Cubes.main(new String[]{});
                break;
            case "CO":
                Coordinates.main(new String[]{});
                break;
            case "3D":
                Object3D.main(new String[]{});
                break;
            case "SP":
                Spheres.main(new String[]{});
                break;
            case "ST":
                SpheresAndTextures.main(new String[]{});
                break;
        }
    }
}
