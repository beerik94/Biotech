package biotech.codechicken.vec;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Rotation implements ITransformation
{
    private double angle;
    private Vector3 axis;
    private Vector3 point;
    
    private Vector3 negate;
    private Quat quat;
    
    public Rotation(double angle, Vector3 axis, Vector3 point)
    {
        this.angle = angle;
        this.axis = axis;
        this.point = point;
    }
    
    public Rotation(double angle, Vector3 axis)
    {
        this(angle, axis, null);
    }

    @Override
    public void transform(Vector3 vec)
    {
        if(quat == null)
            quat = Quat.aroundAxis(vec, angle);
        
        if(point == null)
            vec.rotate(quat);
        else
            vec.subtract(point).rotate(quat).add(point);
    }

    @Override
    public void apply(Matrix4 mat)
    {
        if(point == null)
            mat.rotate(angle, axis);
        else
        {
            if(negate == null)
                negate = point.copy().negate();
            
            mat.translate(point);
            mat.rotate(angle, axis);
            mat.translate(negate);
        }
    }

    @SideOnly(Side.CLIENT)
    public void glRotate()
    {
        if(point == null)
            GL11.glRotatef((float)angle*57.2958F, (float)axis.x, (float)axis.y, (float)axis.z);
        else
        {
            GL11.glTranslated(point.x, point.y, point.z);
            GL11.glRotatef((float)angle*57.2958F, (float)axis.x, (float)axis.y, (float)axis.z);
            GL11.glTranslated(-point.x, -point.y, -point.z);
        }
    }
}
