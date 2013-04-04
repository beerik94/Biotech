package mods.biotech.codechicken.render;

import mods.biotech.codechicken.vec.Vector3;

public class Vertex5
{
    public Vector3 vec;
    public double u;
    public double v;
    
    public Vertex5(Vector3 vert, double u, double v)
    {
        this.vec = vert;
        this.u = u;
        this.v = v;
    }
    
    public Vertex5(double x, double y, double z, double u, double v)
    {
        this(new Vector3(x, y, z), u, v);
    }

    public Vertex5(Vertex5 vertex5)
    {
        this(vertex5.vec.copy(), vertex5.u, vertex5.v);
    }

    public void setUV(int u, int v)
    {
        this.u = u;
        this.v = v;
    }

    public Vertex5 copy()
    {
        return new Vertex5(this);
    }
}
