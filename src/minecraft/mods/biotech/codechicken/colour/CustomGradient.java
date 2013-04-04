package mods.biotech.codechicken.colour;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import mods.biotech.codechicken.alg.MathHelper;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.FMLCommonHandler;

public class CustomGradient
{
    public int[] gradient;
    
    public CustomGradient(String textureFile)
    {
        try
        {
            BufferedImage img = ImageIO.read(Minecraft.getMinecraft().texturePackList.getSelectedTexturePack().getResourceAsStream(textureFile));
            int[] data = new int[img.getWidth()];
            img.getRGB(0, 0, img.getWidth(), 1, data, 0, img.getWidth());
            gradient = new int[img.getWidth()];
            for(int i = 0; i < data.length; i++)
                gradient[i] = (data[i]<<8)|(((data[i])>>24)&0xFF);
        }
        catch (IOException var5)
        {
            FMLCommonHandler.instance().raiseException(var5, "Error while reading gradient: "+textureFile, true);
        }
    }
    
    public ColourRGBA getColour(double position)
    {
        return new ColourRGBA(getColourI(position));
    }
    
    public int getColourI(double position)
    {
        int off = (int)MathHelper.clip(gradient.length*position, 0, gradient.length-1);
        return gradient[off];
    }
}
