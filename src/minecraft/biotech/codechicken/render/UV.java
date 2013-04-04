package biotech.codechicken.render;

public class UV
{
    public double u;
    public double v;
    
    public UV()
    {
    }
    
    public UV(double u, double v)
    {
        this.u = u;
        this.v = v;
    }
    
    public UV(UV uv)
    {
        this(uv.u, uv.v);
    }

    public UV set(double u, double v)
    {
        this.u = u;
        this.v = v;
        
        return this;
    }
    
    public UV copy()
    {
        return new UV(this);
    }
}