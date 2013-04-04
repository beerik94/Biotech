package biotech.codechicken.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class CCRenderState
{    
    private static boolean useNormals;
    private static boolean hasBrightness;
    private static int brightness;
    private static boolean useModelColours;
    private static int colour;
    private static boolean hasColour;
    
    public static void useNormals(boolean b)
    {
        useNormals = b;
    }
    
    public static boolean useNormals()
    {
        return useNormals;
    }
    
    public static void useModelColours(boolean b)
    {
        useModelColours = b;
    }

    public static boolean useModelColours()
    {
        return useModelColours;
    }
    
    public static void setBrightness(IBlockAccess world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        setBrightness(block == null ? 0xF000F0 : block.getMixedBrightnessForBlock(world, x, y, z));
        setColour(0xFFFFFFFF);
    }

    public static void setBrightness(int b)
    {
        hasBrightness = true;
        Tessellator.instance.setBrightness(brightness = b);
    }
    
    public static void setColourOpaque(int c)
    {
        setColour(c << 8 | 0xFF);
    }
    
    //RGBA
    public static void setColour(int c)
    {
        hasColour = true;
        colour = c;
        Tessellator.instance.setColorRGBA(colour >> 24 & 0xFF, colour >> 16 & 0xFF, colour >> 8 & 0xFF, colour & 0xFF);
    }
    
    public static void changeTexture(String texture)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        if(Tessellator.instance.isDrawing)
            apply();
    }

    public static void apply()
    {
        if(hasBrightness)
            Tessellator.instance.setBrightness(brightness);
        if(hasColour)
            Tessellator.instance.setColorRGBA(colour >> 24 & 0xFF, colour >> 16 & 0xFF, colour >> 8 & 0xFF, colour & 0xFF);
    }

    public static void reset()
    {
        hasBrightness = false;
        useModelColours = false;
        hasColour = false;
    }

    public static void startDrawing(int i)
    {
        Tessellator.instance.startDrawing(i);
        apply();
    }

    public static void draw()
    {
        Tessellator.instance.draw();
    }

    public static void pullLightmap()
    {
        setBrightness((int)OpenGlHelper.lastBrightnessY << 16 | (int)OpenGlHelper.lastBrightnessX);
    }
}
