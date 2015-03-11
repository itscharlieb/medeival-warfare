package mw.shared.clientcommands;

import mw.client.controller.GameCommandHandler;

public class NotifyEndTurnCommand extends AbstractClientCommand {
	private final String aType = "NotifyEndTurnCommand";
	
	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void execute() {
		GameCommandHandler.setNowPlaying(false);
	}
}
