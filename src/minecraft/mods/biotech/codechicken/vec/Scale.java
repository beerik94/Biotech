package mods.biotech.codechicken.vec;


public class Scale implements ITransformation
{
    private Vector3 factor;
    private Vector3 point;
    
    private Vector3 negate;
    
    public Scale(Vector3 factor, Vector3 point)
    {
        this.factor = factor;
        this.point = point;
    }
    
    public Scale(Vector3 factor)
    {
        this(factor, null);
    }
    
    @Override
    public void transform(Vector3 vec)
    {
        if(point == null)
            vec.multiply(factor);
        else
            vec.subtract(point).multiply(factor).add(point);
    }
    
    @Override
    public void apply(Matrix4 mat)
    {
        if(point == null)
            mat.scale(factor);
        else
        {
            if(negate == null)
                negate = point.copy().negate();
            
            mat.translate(point);
            mat.scale(factor);
            mat.translate(negate);
        }
    }
}
