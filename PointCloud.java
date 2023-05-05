import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.Point3f;


public class PointCloud extends Shape3D {
    int c = Viewer_main.coords.size();   //number of vertices
    ArrayList<float[]> coord = new ArrayList<float[]>();
    private float[][] vert = new float[c][6];
    private float[][] vert1 = new float[c][3];

    public PointCloud() {
        float[] coord = new float[3];
        float[] coord1 = new float[6];
        for (int i = 0; i < c; i++) {
            coord = Viewer_main.coords.get(i);
            float[] x = {coord[0] + (float) 0.01, coord[1] + (float) 0.01, coord[2] + (float) 0.01};
            for (int a = 0; a < 6; a++) {
                if (a <= 2) {
                    coord1[a] = coord[a];
                    vert1[i][a] = coord[a];
                } else {
                    coord1[a] = x[a - 3];
                }
                vert[i][a] = coord1[a];                   //all points
            }
        }

        //point cloud
        Point3f[] t = new Point3f[c];
        for (int i = 0; i < c; i++) {
            t[i] = new Point3f(vert1[i][0], vert1[i][1], vert1[i][2]);
        }
        PointArray geo = new PointArray(t.length, GeometryArray.COORDINATES);
        geo.setCoordinates(0, t);
        PointAttributes geoa = new PointAttributes();
        geoa.setPointSize(2.0f);
        geoa.setCapability(PointAttributes.ALLOW_SIZE_WRITE);

        Appearance app = new Appearance();
        app.setPointAttributes(geoa);
        this.setGeometry(geo);
        this.setAppearance(app);
    }
}