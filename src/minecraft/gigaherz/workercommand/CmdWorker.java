package gigaherz.workercommand;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CmdWorker extends CommandBase {

	@Override
	public String getCommandName() {
		return "worker";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var2[1] == "version") {
			var1.sendChatToPlayer("Biotech mod version: " + "0.1.1");
		}
	}

}
