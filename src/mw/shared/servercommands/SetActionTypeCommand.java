/**
 * @author Charlie Bloomfield
 * Mar 8, 2015
 */

package mw.shared.servercommands;

import java.util.UUID;

import mw.server.gamelogic.controllers.GameController;
import mw.server.network.mappers.AccountMapper;
import mw.server.network.mappers.GameMapper;
import mw.server.network.translators.NetworkToModelTranslator;
import mw.shared.Coordinates;
import mw.shared.SharedActionType;

/**
 * 
 */
public class SetActionTypeCommand extends AbstractServerCommand {
	private final String aType = "SetActionTypeCommand";
	private Coordinates aUnitCoordinates;
	private SharedActionType aActionType;
	
	/**
	 * Constructor
	 * @param pUnitCoordinates
	 * @param pActionType
	 */
	public SetActionTypeCommand(Coordinates pUnitCoordinates, SharedActionType pActionType) {
		aUnitCoordinates = pUnitCoordinates;
		aActionType = pActionType;
	}

	/**
	 * @see mw.shared.servercommands.AbstractServerCommand#execute(java.lang.Integer)
	 */
	@Override
	public void execute(Integer pClientID) throws Exception {
		UUID lAccountID = AccountMapper.getInstance().getAccountID(pClientID);
		GameController.setActionType(
				GameMapper.getInstance().getGame(lAccountID),
				aUnitCoordinates,
				NetworkToModelTranslator.translateActionType(aActionType)
				);

	}

}
