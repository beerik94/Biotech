package gigaherz.workercommand;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.common.FMLCommonHandler;

public class CmdWorker extends CommandBase {
	public CmdWorker() {}
	@Override
	public String getCommandName() {
		return "worker";
	}
	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var2[1] == "version") {
			var1.sendChatToPlayer("Biotech mod version: " + "0.1.1");
		}else{
			var1.sendChatToPlayer("Usage: " + getCommandName() + " version");
		}
	}
}
