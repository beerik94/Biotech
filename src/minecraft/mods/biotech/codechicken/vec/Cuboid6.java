package mods.biotech.codechicken.vec;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class Cuboid6
{
    public Vector3 min;
    public Vector3 max;
    
    public Cuboid6(Vector3 min, Vector3 max)
    {
        this.min = min;
        this.max = max;
    }

    public Cuboid6(AxisAlignedBB aabb)
    {
        min = new Vector3(aabb.minX, aabb.minY, aabb.minZ);
        max = new Vector3(aabb.maxX, aabb.maxY, aabb.maxZ);        
    }
    
    public Cuboid6(Cuboid6 cuboid)
    {
        min = cuboid.min.copy();
        max = cuboid.max.copy();
    }

    public Cuboid6(double minx, double miny, double minz, double maxx, double maxy, double maxz)
    {
        min = new Vector3(minx, miny, minz);
        max = new Vector3(maxx, maxy, maxz);
    }

    public AxisAlignedBB toAABB()
    {
        return AxisAlignedBB.getBoundingBox(min.x, min.y, min.z, max.x, max.y, max.z);
    }
    
    public Cuboid6 copy()
    {
        return new Cuboid6(this);
    }
    
    public Cuboid6 add(Vector3 vec)
    {
        min.add(vec);
        max.add(vec);
        return this;
    }

    public void setBlockBounds(Block block)
    {
        block.setBlockBounds((float)min.x, (float)min.y, (float)min.z, (float)max.x, (float)max.y, (float)max.z);
    }
}
