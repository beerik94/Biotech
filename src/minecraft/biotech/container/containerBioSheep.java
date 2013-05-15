package biotech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import biotech.entity.bioSheep;

class containerBioSheep extends Container
{
    final bioSheep field_90034_a;

    containerBioSheep(bioSheep par1EntityBioSheep)
    {
        this.field_90034_a = par1EntityBioSheep;
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return false;
    }
}
