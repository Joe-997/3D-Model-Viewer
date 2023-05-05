import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


public class Edges extends Shape3D {

    //edges' coords
    int c = Viewer_main.coords.size();   //number of vertices
    private float[][] vert = new float[c][6];
    private float[][] vert1 = new float[c][3];

    public Edges( ) {
        float [] coord = new float[3];
        float [] coord1 = new float[6];
        for(int i=0;i<c;i++){
            coord = Viewer_main.coords.get(i);
            float[] x = {coord[0]+(float)0.01, coord[1]+(float)0.01, coord[2]+(float)0.01};
            for (int a = 0;a < 6;a++){
                if (a<=2) {
                    coord1[a] = coord[a];
                    vert1[i][a] = coord[a];
                }else {
                    coord1[a] = x[a-3];
                }
                vert[i][a] = coord1[a];            //vertices of edges
            }
        }

        //all points
        Point3f[] t = new Point3f[c];
        for (int i = 0; i < c; i++){
            t[i] = new Point3f(vert1[i][0], vert1[i][1], vert1[i][2]);
        }


        //edges
        int ind = Viewer_main.faces.size();  //number of triangles
        int [] index = new int[ind*3];                 //number of vertices
        float [] index3 = new float[ind*3*3];          //vertices' coords
        Point3f[] tt = new Point3f[ind*3];             //new order
        int [] temp = new int[3];
        int k = 0;

        int l = 0;
        int ind4 = (ind)*4;
        Point3f[] ttt = new Point3f[ind4];        //add one more vertices, make every edge is a pair of vertices
        for(int i = 0; i < ind; i++) {
            temp = Viewer_main.faces.get(i);
            for (int j = 0; j <= 3; j++) {
                if (j<3) {
                    ttt[l] = t[temp[j] - 1];           //start from 1
                    l++;
                }else{
                    ttt[l] = t[temp[0]-1];
                    l++;
                }
            }
        }
        LineArray geo3 = new LineArray(ttt.length, GeometryArray.COORDINATES);
        geo3.setCoordinates(0, ttt);

        LineAttributes geoa3 = new LineAttributes();
        geoa3.setLineWidth(1.5f);
        geoa3.setCapability(LineAttributes.ALLOW_ANTIALIASING_WRITE);
        geoa3.setLineAntialiasingEnable(true);

        Appearance app = new Appearance( );
        app.setLineAttributes(geoa3);

        Color3f gray = new Color3f(0.1f, 0.1f, 1.1f);
        ColoringAttributes gr = new ColoringAttributes(gray, 0);
        app.setColoringAttributes(gr);

        this.setGeometry(geo3);
        this.setAppearance(app);
    }

}