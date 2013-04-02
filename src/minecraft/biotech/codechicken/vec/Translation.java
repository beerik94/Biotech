package biotech.codechicken.vec;


public class Translation implements ITransformation
{
    private Vector3 vec;
    
    public Translation(Vector3 vec)
    {
        this.vec = vec;
    }

    @Override
    public void transform(Vector3 vec)
    {
        vec.add(this.vec);
    }

    @Override
    public void apply(Matrix4 mat)
    {
        mat.translate(vec);
    }
}
