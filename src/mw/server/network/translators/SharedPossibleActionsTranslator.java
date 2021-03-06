package mw.server.network.translators;

import java.util.ArrayList;
import java.util.Collection;

import mw.server.gamelogic.PossibleGameActions;
import mw.server.gamelogic.controllers.TileController;
import mw.server.gamelogic.enums.ActionType;
import mw.server.gamelogic.enums.UnitType;
import mw.server.gamelogic.state.Tile;
import mw.shared.Coordinates;
import mw.shared.SharedActionType;
import mw.shared.SharedPossibleGameActions;
import mw.shared.SharedTile;

public abstract class SharedPossibleActionsTranslator {



	/* ========================
	 * 		Static methods
	 * ========================
	 */
	public static SharedPossibleGameActions translatePossibleGameActions(PossibleGameActions serverPossibleActions)
	{
		Collection<Coordinates> sharedMoves = new ArrayList<Coordinates>();
		Collection<Tile> serverMoves = serverPossibleActions.getMovableTiles();
		for (Tile t : serverMoves) {
			sharedMoves.add(SharedTileTranslator.translateCoordinates(TileController.getCoordinates(t)));
		}
		
		Collection<Coordinates> sharedCombinable = new ArrayList<Coordinates>();
		Collection<Tile> serverCombinable = serverPossibleActions.getCombinableUnitTiles();
		for (Tile t : serverCombinable)
		{
			sharedCombinable.add(SharedTileTranslator.translateCoordinates(TileController.getCoordinates(t)));
		}
		
		Collection<Coordinates> sharedHirable = new ArrayList<Coordinates>();
		Collection<Tile> serverHirable = serverPossibleActions.getHirableUnitTiles();
		for (Tile t : serverHirable)
		{
			sharedHirable.add(SharedTileTranslator.translateCoordinates(TileController.getCoordinates(t)));
		}
		Collection<Coordinates> sharedFirable = new ArrayList<Coordinates>();
		Collection<Tile> serverFirable = serverPossibleActions.getFirableTiles();
		for (Tile t: serverFirable)
		{
			sharedFirable.add(SharedTileTranslator.translateCoordinates(TileController.getCoordinates(t)));
		}
		 		
		Collection<SharedTile.UnitType> sharedUT = new ArrayList<SharedTile.UnitType>();
		Collection<UnitType> serverUT = serverPossibleActions.getUnitUpgrade();
		if (serverUT != null)
		{
			for (UnitType ut : serverUT) {
				sharedUT.add(SharedTileTranslator.translateUnitType(ut,  null));
			}
		}
		if (serverPossibleActions.getCanBuildWatchtower()) {
			sharedUT.add(SharedTile.UnitType.WATCHTOWER);
		}
		
		Collection<SharedActionType> sharedAT = new ArrayList<SharedActionType>();
		Collection<ActionType> serverAT = serverPossibleActions.getActions();
		if (serverAT != null)
		{
			for (ActionType at : serverAT) {
				sharedAT.add(translateActionType(at));
			}
		}
		
		SharedTile.VillageType sharedVT = SharedTileTranslator.translateVillageType(serverPossibleActions.getVillageUpgrade());
		
		boolean sharedCanBuildWatchtower = serverPossibleActions.getCanBuildWatchtower();
		
		return new SharedPossibleGameActions(sharedMoves, sharedUT, sharedAT, sharedVT, sharedCombinable, sharedHirable, sharedFirable, sharedCanBuildWatchtower);

	}
	
	public static SharedActionType translateActionType(ActionType at) {
		switch(at)
		{
		case BUILDINGROAD:
			return SharedActionType.BUILD_ROAD;
			
		case CULTIVATING_BEGIN:
			return SharedActionType.CULTIVATE_MEADOW;
			
			default:
				throw new IllegalArgumentException(at+" is not a legal value to translate to a SharedActionType");
		}
	}

}