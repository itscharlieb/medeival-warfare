/**
 * @author Charlie Bloomfield
 * Mar 8, 2015
 */

package mw.shared.servercommands;

import java.util.UUID;

import mw.server.gamelogic.controllers.GameController;
import mw.server.gamelogic.exceptions.NotEnoughIncomeException;
import mw.server.network.exceptions.IllegalCommandException;
import mw.server.network.mappers.GameMapper;
import mw.server.network.translators.NetworkToModelTranslator;
import mw.shared.Coordinates;
import mw.shared.SharedTile;

/**
 * 
 */
public class HireUnitCommand extends AbstractAuthenticatedServerCommand {
	private final String aType = "HireUnitCommand";

	Coordinates aUnitCoordinates;
	SharedTile.UnitType aUnitType;

	/**
	 * 
	 */
	public HireUnitCommand(Coordinates pUnitCoordinates, SharedTile.UnitType pUnitType) {
		aUnitCoordinates = pUnitCoordinates;
		aUnitType = pUnitType;
	}

	@Override
	protected void doExecution(UUID pAccountID) throws NotEnoughIncomeException, IllegalCommandException {
		GameController.hireUnit(
				GameMapper.getInstance().getGame(pAccountID),
				aUnitCoordinates,
				NetworkToModelTranslator.translateUnitType(aUnitType)
				);
	}
}
