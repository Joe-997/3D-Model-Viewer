import javax.media.j3d.BranchGroup;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

public class ObjFileReader extends BranchGroup{

    private double creaseAngle = 60.0;

    public ObjFileReader(String filePath){
        BranchGroup branchGroup = new BranchGroup();
        int flags = ObjectFile.RESIZE;
        ObjectFile objFile = new ObjectFile(flags, (float)(creaseAngle*Math.PI)/180);
        Scene scenen = null;
        try {
            scenen = objFile.load(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load obj file!" + e.getMessage());
        }
        branchGroup.addChild(scenen.getSceneGroup());
        this.addChild(branchGroup);
    }

}