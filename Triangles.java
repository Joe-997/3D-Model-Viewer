import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


public class Triangles extends Shape3D {

    // coordinates
    int c = Viewer_main.coords.size();   //number of vertices
    private float[][] vert = new float[c][6];
    private float[][] vert1 = new float[c][3];

    public Triangles( ) {
        float [] coord = new float[3];
        float [] coord1 = new float[6];
        for(int i=0;i<c;i++){    //three vertices one time
            coord = Viewer_main.coords.get(i);
            float[] x = {coord[0]+(float)0.01, coord[1]+(float)0.01, coord[2]+(float)0.01};
            for (int a = 0;a < 6;a++){
                if (a<=2) {
                    coord1[a] = coord[a];
                    vert1[i][a] = coord[a];
                }else {
                    coord1[a] = x[a-3];
                }
                vert[i][a] = coord1[a];
            }
        }

        //get points
        Point3f[] t = new Point3f[c];
        for (int i = 0; i < c; i++){
            t[i] = new Point3f(vert1[i][0], vert1[i][1], vert1[i][2]);
        }

        //get triangles
        int ind = Viewer_main.faces.size();  //number of triangles
        int [] index = new int[ind*3];                 //vertices
        float [] index3 = new float[ind*3*3];          //vertices' coords
        Point3f[] tt = new Point3f[ind*3];             //new order of vertices
        int [] temp = new int[3];
        int k = 0;
        for(int i = 0; i < ind; i++){                  //go through all triangles
            temp = Viewer_main.faces.get(i);   //get a triangle's coords
            for (int j = 0; j < 3; j++){
                tt[k] = t[temp[j]-1];                  //start from 1, not 0;
                k++;
            }
        }
        TriangleArray geo2 = new TriangleArray(tt.length, GeometryArray.COORDINATES);
        geo2.setCoordinates(0,tt);


        Appearance app = new Appearance( );

        Color3f gray = new Color3f(0.8f, 0.8f, 0.8f);            //light gray
        ColoringAttributes gr = new ColoringAttributes(gray, 0);
        app.setColoringAttributes(gr);

        this.setGeometry(geo2);
        this.setAppearance(app);
    }

}