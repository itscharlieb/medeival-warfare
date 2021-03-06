/**
 * Logic for moving units 
 * @author emilysager
 */
package mw.server.gamelogic.logic;

import java.util.Collection;

import mw.server.gamelogic.enums.ActionType;
import mw.server.gamelogic.enums.Color;
import mw.server.gamelogic.enums.StructureType;
import mw.server.gamelogic.enums.UnitType;
import mw.server.gamelogic.enums.VillageType;
import mw.server.gamelogic.graph.Graph;
import mw.server.gamelogic.state.Game;
import mw.server.gamelogic.state.Tile;
import mw.server.gamelogic.state.Unit;




public final class TileGraphLogic {

	public static boolean isReachableNode(Graph<Tile> pGraph, Tile crtTile, Tile startTile)
	{

		Collection<Tile> pCrtNeighbors = pGraph.getNeighbors(crtTile); 
		if (startTile.hasUnit())
		{
			Unit pUnit = startTile.getUnit();
			if (pUnit.getActionType() == ActionType.MOVED)
			{
				return false;
			}
			if (isSeaTile(crtTile))
			{
				return false; 
			}
			if (isWithinVillage(startTile, crtTile))
			{
				return moveUnitWithinVillage(pUnit, crtTile, pCrtNeighbors); 
			}
			if (isNeutralLand(crtTile))
			{
				return moveUnitToNeutralLand(pUnit, crtTile); 
			}
			if (isEnemyTerritory(startTile, crtTile))
			{
				return (moveUnitToEnemyTerritory(pUnit, crtTile, pCrtNeighbors)); 
			}
		}

		return crtTile.equals(startTile); 



	}

	public static boolean isPathOver(Graph<Tile> pGraph, Tile startTile, Tile destinationTile)
	{
		if (isNeutralLand(destinationTile)) 
		{
			return true; 
		}
		if (isEnemyTerritory(startTile, destinationTile))
		{
			return true; 
		}
		else 
		{ 
			if (destinationTile.hasUnit())
			{
				return true; 
			}
			if (isTreeOnTile(destinationTile))
			{
				return true; 
			}
			if (isTombstoneOnTile(destinationTile))
			{
				return true; 
			}
			if (isWatchtowerOnTile(destinationTile))
			{
				return true; 
			}
			if (isCapitalOnTile(destinationTile))
			{
				return true; 
			}

			return false; 

		}
	}

	private static boolean isSeaTile(Tile crtTile) {
		if (crtTile.getColor()== Color.SEATILE)
		{
			return true; 
		}
		return false;
	}
	private static boolean isNeutralLand(Tile crtTile) {
		if (crtTile.getColor() == Color.NEUTRAL)
		{
			return true; 
		}
		return false;
	}
	private static boolean isEnemyTerritory(Tile startTile, Tile crtTile) {
		if (startTile.getColor() != crtTile.getColor() && crtTile.getColor()!=Color.SEATILE && crtTile.getColor()!=Color.NEUTRAL)
		{
			return true; 
		}
		return false;
	}
	private static boolean isWithinVillage(Tile startTile, Tile crtTile) {
		if (startTile.getColor() == crtTile.getColor())
		{
			return true; 
		}
		return false;
	}

	/**
	 * Returns true if a unit can move to a given tile 
	 * @precondition destinationTile must be neutral land	
	 * @param pUnit
	 * @param destinationTile
	 * @return
	 */
	private static boolean moveUnitToNeutralLand(Unit pUnit, Tile destinationTile)
	{

		if (isTreeOnTile(destinationTile))
		{
			return unitCanChopTree(pUnit); 
		}
		if (isTombstoneOnTile(destinationTile))
		{
			return unitCanClearTombstone(pUnit);
		}
		return true; 

	}
	/**
	 * Returns true if a unit can move to a given tile
	 * @precondition destinationTile must be in the same village as pUnit
	 * @param pUnit
	 * @param destinationTile
	 * @param destinationNeighbors
	 * @return
	 */
	private static boolean moveUnitWithinVillage(Unit pUnit, Tile destinationTile, Collection<Tile> destinationNeighbors )
	{

		if (destinationTile.hasUnit())
		{
			return false; 
		}
		if (isTreeOnTile(destinationTile))
		{
			return unitCanChopTree(pUnit); 
		}
		if (isTombstoneOnTile(destinationTile))
		{
			return unitCanClearTombstone(pUnit); 
		}
		if (isWatchtowerOnTile(destinationTile))
		{
			return false; 
		}
		if (isCapitalOnTile(destinationTile))
		{
			return false; 
		}
		return true; 

	}
	/**
	 * Returns true if a unit can move to a given tile
	 * @precondition destinationTile must be enemyTerritory	
	 * @param pUnit
	 * @param destinationTile
	 * @param destinationNeighbors
	 * @return
	 */
	private static boolean moveUnitToEnemyTerritory(Unit pUnit, Tile destinationTile, Collection<Tile> destinationNeighbors)
	{
		System.out.println("[Game] MoveUnit to enemy territory");
		if((pUnit.getUnitType() == UnitType.PEASANT) || (pUnit.getUnitType() == UnitType.CANNON))
		{
			return false;
		}
		else
		{
			//Collection<Tile> destinationNeighbors = game.getNeighbors(destinationTile);
			for (Tile lTile: destinationNeighbors)
			{
				if(lTile.getColor().equals(destinationTile.getColor()))
				{
					if (lTile.hasUnit() 
							&& lTile.getStructureType()!= StructureType.WATCHTOWER)
					{
						System.out.println("Destination Tile  neighbors has unit");
						if(!unitCanTakeOver(pUnit, lTile.getUnit()) 
								&& lTile.getUnit().getUnitType() != UnitType.CANNON)
						{
							return false;
						}
					}
					if (lTile.getVillageType() == VillageType.FORT 
							&& pUnit.getUnitType() != UnitType.KNIGHT)
					{
						return false;
					}
					if (lTile.getVillageType() == VillageType.CASTLE)
					{
						return false;
					}
				}
			}
			if (isWatchtowerGuardingTile(destinationTile, destinationNeighbors))
			{
				System.out.println("[Game] Watchtower is guarding tile");
				if (pUnit.getUnitType() != UnitType.SOLDIER && pUnit.getUnitType()!= UnitType.KNIGHT )
				{
					return false;
				}
			}
			if (destinationTile.hasUnit())
			{
				System.out.println("Destination Tile has unit");
				return unitCanTakeOver(pUnit, destinationTile.getUnit()); 
			}
			if (isTreeOnTile(destinationTile))
			{
				System.out.println("Destination Tile has tree");
				return unitCanChopTree(pUnit); 
			}
			if (isTombstoneOnTile(destinationTile))
			{
				return unitCanClearTombstone(pUnit); 
			}
			if (isCapitalOnTile(destinationTile))
			{
				return canUnitInvadeCapital(pUnit, destinationTile); 
			}
		}
		return true;
	}

	private static boolean canUnitInvadeCapital(Unit pUnit, Tile destinationTile)
	{
		UnitType pUnitType = pUnit.getUnitType(); 
		VillageType dVillageType = destinationTile.getVillageType(); 
		if (pUnitType != UnitType.KNIGHT && pUnitType != UnitType.SOLDIER)
		{
			return false; 
		}
		if (dVillageType == VillageType.HOVEL)
		{
			return true; 
		}
		if (dVillageType == VillageType.TOWN)
		{
			if (pUnitType == UnitType.SOLDIER || pUnitType == UnitType.KNIGHT)
			{
				return true; 
			}
		}
		if (dVillageType == VillageType.FORT)
		{
			if (pUnitType == UnitType.KNIGHT)
			{
				return true; 
			}
		}
		//castles can't be taken over, so they get the default of false
		return false; 

	}

	/**
	 * @deprecated
	 * @param pUnit
	 * @param destinationTile
	 * @param destinationNeighbors
	 * @return
	 */
	private static boolean isProtected(Unit pUnit, Tile destinationTile, Collection<Tile> destinationNeighbors)
	{
		
		System.out.println("[Game] Checking whether the tile is at coordinates (" +  destinationTile.getCoordinates().X + ", " +  destinationTile.getCoordinates().Y + ") is protected");
		//Unit trying to invade
		UnitType pUnitType = pUnit.getUnitType(); 
		for (Tile lTile : destinationNeighbors)
		{
			if (lTile.getColor() == destinationTile.getColor())
			{
				//Unit being invaded
				Unit lUnit = lTile.getUnit();
				if (lUnit != null)
				{
					//if the invaded unit is of lower rank than than the unit being invaded the tile is not protected
					// e.g. peasant <= infantry implies the tile is not protected 
					if(lUnit.getUnitType().ordinal() <= pUnitType.ordinal()) 
					{
						System.out.println("[Game]Not Protected");
						return false;
					}
				}
			}
		}
		if (isWatchtowerGuardingTile(destinationTile, destinationNeighbors) || isWatchtowerOnTile(destinationTile))
		{
			switch (pUnitType)
			{
			case PEASANT: 
				System.out.println("[Game] Not Protected");
				return false; 
			case INFANTRY: 
				System.out.println("[Game] Not protected");
				return false;
			case CANNON: 
			default: 
				break; 
			} 
		}
		System.out.println("[Game] Protected");
		return true; 
	}
	
	private static boolean unitCanTakeOver(Unit crtUnit, Unit enemyUnit)
	{
		UnitType crtType = crtUnit.getUnitType();
		UnitType enemyType = enemyUnit.getUnitType();

		if (crtType.ordinal() > enemyType.ordinal() )
		{
			return true; 
		}
		return false; 
	}	
	private static boolean unitCanChopTree(Unit pUnit)
	{
		UnitType pUnitType = pUnit.getUnitType(); 
		if (pUnitType == UnitType.KNIGHT || pUnitType == UnitType.CANNON) 
		{
			return false; 
		}
		return true;
	}

	private static boolean unitCanClearTombstone(Unit pUnit)
	{
		UnitType pUnitType = pUnit.getUnitType(); 
		if (pUnitType == UnitType.KNIGHT || pUnitType == UnitType.CANNON) 
		{
			return false; 
		}
		return true; 
	}

	private static boolean isCapitalOnTile(Tile pTile)
	{
		if (pTile.getStructureType() == StructureType.VILLAGE_CAPITAL)
		{
			return true; 
		}
		return false; 
	}
	private static boolean isWatchtowerOnTile(Tile pTile)
	{
		if (pTile.getStructureType() == StructureType.WATCHTOWER)
		{
			return true; 
		}
		return false; 
	}
	
	private static boolean isWatchtowerGuardingTile(Tile pTile, Collection<Tile> pNeighbors)
	{
		if (pTile.getStructureType() == StructureType.WATCHTOWER)
		{
			return true; 
		}
		if (pTile.getStructureType() != StructureType.WATCHTOWER)
		{
			for (Tile lTile : pNeighbors)
			{
				if (lTile.getStructureType() == StructureType.WATCHTOWER)
				{
					return true; 
				}
			}
		}
		return false; 
	}
	
	private static boolean isTombstoneOnTile(Tile pTile)
	{
		if (pTile.getStructureType() == StructureType.TOMBSTONE)
		{
			return true; 
		}
		return false;
	}
	
	private static boolean isTreeOnTile(Tile pTile)
	{
		if (pTile.getStructureType() == StructureType.TREE)
		{
			return true; 
		}
		return false; 
	}
	public static boolean isVillageBoundary(Graph<Tile> pGraph, Tile pStartTile, Tile pCrtTile)
	{
		Collection<Tile> lNeighbors = pGraph.getNeighbors(pCrtTile);
		boolean areAllNeighborsSameColor = true; 
		for (Tile lTile : lNeighbors)
		{
			if (!(lTile.getColor() == pStartTile.getColor()))
			{
				areAllNeighborsSameColor = false; 
			}
		}
		return areAllNeighborsSameColor; 

	}
	public static boolean tilesAreSameColor(Tile t1, Tile t2)
	{
		return t1.getColor() == t2.getColor();
	}
}


