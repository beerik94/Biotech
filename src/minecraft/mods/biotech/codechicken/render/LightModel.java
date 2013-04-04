package mods.biotech.codechicken.render;

import mods.biotech.codechicken.vec.Vector3;

public class LightModel
{
    public static class Light
    {
        public Vector3 ambient = new Vector3();
        public Vector3 diffuse = new Vector3();
        public Vector3 position = new Vector3();
        
        public Light setPosition(Vector3 vec)
        {
            position.set(vec).normalize();
            return this;
        }
        
        public Light setDiffuse(Vector3 vec)
        {
            diffuse.set(vec);
            return this;
        }
        
        public Light setAmbient(Vector3 vec)
        {
            ambient.set(vec);
            return this;
        }
    }
    
    public static LightModel standardLightModel;
    static
    {
        standardLightModel = new LightModel()
                .setAmbient(new Vector3(0.4, 0.4, 0.4))
                .addLight(new Light()
                    .setPosition(new Vector3(0.2, 1, -0.7))
                    .setDiffuse(new Vector3(0.6, 0.6, 0.6)))
                .addLight(new Light()
                    .setPosition(new Vector3(-0.2, 1, 0.7))
                    .setDiffuse(new Vector3(0.6, 0.6, 0.6)));
    }
    
    private Vector3 ambient = new Vector3();
    private Light[] lights = new Light[8];
    private int lightCount;
    
    private LightModel addLight(Light light)
    {
        lights[lightCount++] = light;
        return this;
    }
    
    private LightModel setAmbient(Vector3 vec)
    {
        ambient.set(vec);
        return this;
    }

    public int apply(int colour, Vector3 normal)
    {
        Vector3 v_colour = new Vector3(((colour >> 16) & 0xFF)/255D, ((colour >> 8) & 0xFF)/255D, (colour & 0xFF)/255D);
        Vector3 n_colour = new Vector3(
                ambient.x*v_colour.x, 
                ambient.y*v_colour.y, 
                ambient.z*v_colour.z);
        
        for(int l = 0; l < lightCount; l++)
        {
            Light light = lights[l];
            double n_l = light.position.dotProduct(normal);
            double f = n_l > 0 ? 1 : 0;
            n_colour.x += v_colour.x*(light.ambient.x + f*light.diffuse.x*n_l);
            n_colour.y += v_colour.y*(light.ambient.y + f*light.diffuse.y*n_l);
            n_colour.z += v_colour.z*(light.ambient.z + f*light.diffuse.z*n_l);
        }

        if(n_colour.x > 1)
            n_colour.x = 1;
        if(n_colour.y > 1)
            n_colour.y = 1;
        if(n_colour.z > 1)
            n_colour.z = 1;
        
        return ((int)(n_colour.x*255)<<16) | ((int)(n_colour.y*255)<<8) | (int)(n_colour.z*255);
    }
}
