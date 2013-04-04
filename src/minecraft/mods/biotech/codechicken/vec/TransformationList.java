package mods.biotech.codechicken.vec;

import java.util.ArrayList;


public class TransformationList implements ITransformation
{
    private ArrayList<ITransformation> transformations = new ArrayList<ITransformation>();
    private Matrix4 mat;
    
    public TransformationList add(ITransformation t)
    {
        if(mat != null)
            throw new RuntimeException("List already compiled");
        transformations.add(t);
        return this;
    }
    
    public Matrix4 compile()
    {
        if(mat == null)
        {
            mat = new Matrix4();
            for(ITransformation t : transformations)
                t.apply(mat);
        }
        return mat;
    }

    @Override
    public void transform(Vector3 vec)
    {
        if(mat != null)
            mat.transform(vec);
        else
            for(ITransformation t : transformations)
                t.transform(vec);
    }

    @Override
    public void apply(Matrix4 mat)
    {
        mat.multiply(compile());
    }
}
