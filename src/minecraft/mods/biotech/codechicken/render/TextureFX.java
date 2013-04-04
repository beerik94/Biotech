package mods.biotech.codechicken.render;

import mods.biotech.codechicken.render.SpriteSheetManager.SpriteSheet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class TextureFX
{
    public byte[] imageData;
    public int tileSizeBase = 16;
    public int tileSizeSquare = 256;
    public int tileSizeMask = 15;
    public int tileSizeSquareMask = 255;
    
    public boolean anaglyphEnabled;
    public TextureSpecial texture;

    public TextureFX(int spriteIndex, SpriteSheet sheet)
    {
        texture = sheet.bindTextureFX(spriteIndex, this);
    }
    
    public TextureFX(int size, String name)
    {
        texture = new TextureSpecial(name).blank(size).addTextureFX(this);
    }

    public void setup()
    {
        imageData = new byte[tileSizeSquare << 2];
    }
    
    public void onTextureDimensionsUpdate(int width, int height)
    {
        if(width != height)
            throw new IllegalArgumentException("Non-Square textureFX not supported ("+width+":"+height+")");
        
        tileSizeBase = width;
        tileSizeSquare = tileSizeBase * tileSizeBase;
        tileSizeMask = tileSizeBase - 1;
        tileSizeSquareMask = tileSizeSquare - 1;
        setup();
    }
    
    public void update()
    {
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        onTick();
    }

    public void onTick()
    {
    }
    
    public boolean changed()
    {
        return true;
    }
}
