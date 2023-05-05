import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.javafx.geom.Vec3f;


public class Viewer_main extends JFrame{
    static String[] tokens;
    static String[] facetokens;
    static ArrayList <float[]> coords = new ArrayList < float[] > ();
    //ArrayList <Vec2f> texCoords = new ArrayList < Vec2f > ();
    static ArrayList<Vec3f> normals = new ArrayList<Vec3f>();
    static ArrayList<int[]> faces = new ArrayList<int[]>();
    static ArrayList<String> data = new ArrayList<>();


    public static void Mesh(String path) {
        try {
            File Obj = new File(path);
            Scanner Reader = new Scanner(Obj);
            while (Reader.hasNextLine()) {
                String temp = Reader.nextLine();
                data.add(temp);
            }
            Reader.close();

        } catch (FileNotFoundException e) {
            System.err.println(path + ": Not found");
            e.printStackTrace();
        }

        for (int i = 0, sz = data.size(); i < sz; ++i) {
            tokens = data.get(i).split("\\s+");             // Split line by spaces.
            // Skip empty lines.
            if (tokens.length > 0) {
                if (tokens[0].equals("o")) {
                } else if (tokens[0].equals("v")) {               // Coordinate.
                    float[] read = new float[3];
                    for(int j = 0; j < 3 ; j++) {
                        read[j] = Float.parseFloat(tokens[j+1]);
                    }
                    coords.add(read);


                } else if (tokens[0].equals("vn")) {              // Normal.
                    Vec3f read = new Vec3f(Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    normals.add(read);
                } else if (tokens[0].equals("f")) {               // Face.
                    int count = tokens.length;
                    int[] indices = new int[3];
                    for (int j = 1; j < count; ++j) {
                        facetokens = tokens[j].split("/");
                        indices[j - 1] = Integer.parseInt(tokens[j]);
                    }
                    faces.add(indices);

                }
            }
        }
    }
    int a = coords.size();   //number of vertices
    int b = faces.size();    //number of faces
    public BranchGroup createSceneGraph(int data){
        //scene branch
        BranchGroup group = new BranchGroup();
        Transform3D trans3d = new Transform3D();
        //trans3d.setTranslation(new Vector3f(-0.6f,0.0f,0.6f));
        TransformGroup transGroup = new TransformGroup(trans3d);
        transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);


        // scale
        trans3d.setScale(0.1);
        transGroup.setTransform(trans3d);
        group.addChild(transGroup);

        BoundingSphere bound= new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f bgColor = new Color3f(0.3f,0.3f,0.3f);    //background color
        Background bg = new Background(bgColor);
        bg.setApplicationBounds(bound);
        group.addChild(bg);


        //light for rendering
        Color3f lightColor = new Color3f(1.0f,1.0f,1.0f);
        Vector3f lightDirection = new Vector3f(50.0f,-50.0f,-50.0f);
        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        light.setInfluencingBounds(bound);
        group.addChild(light);
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Color3f color = new Color3f(0.0f, 1.0f, 0.0f);

        if (data == 1) {                                             //first mode is triangle mesh
            Shape3D text = new Text2D(b + " faces in total!", color, "SansSerif", 12, Font.BOLD);
            group.addChild(text);
            Shape3D shape2 = new Edges();
            objTrans.addChild(shape2);
        }else if(data == 2) {                                        //filled triangle mesh
            Shape3D text = new Text2D(a + " vertices in total, "+ b + " faces in total!", color, "SansSerif", 12, Font.BOLD);
            group.addChild(text);
            Shape3D shape = new Triangles();
            Shape3D shape2 = new Edges();
            objTrans.addChild(shape);
            objTrans.addChild(shape2);
        }else if(data == 3){                                         //point cloud
            Shape3D text = new Text2D(a + " vertices in total!", color, "SansSerif", 12, Font.BOLD);
            group.addChild(text);
            Shape3D shape3 = new PointCloud();
            objTrans.addChild(shape3);

        }else if(data == 4){                                         //rendering
            objTrans.addChild(new ObjFileReader(dir));
            //transGroup.addChild(objTrans);
        }
        transGroup.addChild(objTrans);

        MouseRotate behavior = new MouseRotate(objTrans);            //rotate
        objTrans.addChild(behavior);
        behavior.setSchedulingBounds(bound);

        MouseZoom behavior2 = new MouseZoom(objTrans);               //scale
        objTrans.addChild(behavior2);
        behavior2.setSchedulingBounds(bound);

        MouseTranslate behavior3 = new MouseTranslate(objTrans);     //transform
        objTrans.addChild(behavior3);
        behavior3.setSchedulingBounds(bound);
        group.compile();
        return group;
    }

    private Point3d picked = new Point3d();
    public Viewer_main(int data){
        int d = data;
        setLayout(new BorderLayout());
        GraphicsConfiguration  config=SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add("Center",canvas);
        BranchGroup scene = createSceneGraph(d);

        SimpleUniverse universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(scene);

    }


    public static String fileDir = System.getProperty("user.dir");
    public static String dir = fileDir + "/doraemon.obj";
    //scale（0.01）：turtle
    //scale（0.8）：Lion_vase, armadillo
    //scale（0.1）：fertility, doraemon,
    //point cloud only：lion2(0.1right),Lucy(0.001),gargoyle(0.1),bunny(1)
    public static void main(String[] args) {
        Mesh(dir);
        System.out.println("Please enter a number from below:");
        System.out.println("  1-Triangle\n  2-Filled Triangle\n  3-Point Cloud\n  4-Rendering");
        Scanner dtype = new Scanner(System.in);
        int data = dtype.nextInt();

        JFrame f = new Viewer_main(data);
        f.setSize(800, 800);
        f.setVisible(true);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}