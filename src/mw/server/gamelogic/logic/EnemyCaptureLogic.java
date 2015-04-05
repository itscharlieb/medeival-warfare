package mw.server.gamelogic.logic;

import java.util.ArrayList;
import java.util.Collection;

import mw.server.gamelogic.enums.StructureType;
import mw.server.gamelogic.state.Game;
import mw.server.gamelogic.state.Player;
import mw.server.gamelogic.state.Tile;
import mw.server.gamelogic.state.Village;

public class EnemyCaptureLogic {


	public static void CaptureTile(Village invadingVillage, Village invadedVillage,  Tile invadedTile, Game pGame, Player aCurrentPlayer)
	{
		//Case for invading a non-village capital
		if (invadedTile.getStructureType() != StructureType.VILLAGE_CAPITAL);
		{
			fuse(invadingVillage, invadedVillage, invadedTile, pGame, aCurrentPlayer);
		}
		if(invadedTile.getStructureType() == StructureType.VILLAGE_CAPITAL)
		{
			invadeCapital(invadingVillage, invadedVillage, invadedTile, pGame, aCurrentPlayer);
		}
	}


	private static void invadeCapital(Village invadingVillage, Village invadedVillage, Tile invadedTile, Game pGame, Player aCurrentPlayer) 
	{
		int lGold = invadedVillage.getGold();
		int lWood = invadedVillage.getWood();
		invadingVillage.addOrSubtractGold(lGold);
		invadingVillage.addOrSubtractWood(lWood);
		invadedVillage.addOrSubtractGold(-lGold);
		invadedVillage.addOrSubtractWood(-lWood);
		invadedVillage.setRandomCapital();
		fuse(invadingVillage, invadedVillage, invadedTile, pGame, aCurrentPlayer);
	}
	
	private static void fuse(Village invadingVillage, Village invadedVillage,  Tile invadedTile, Game pGame, Player aCurrentPlayer) {

		invadedVillage.removeTile(invadedTile);
		invadingVillage.addTile(invadedTile);
		Tile invadingCapital = invadingVillage.getCapital(); 
		Collection<Village> toFuse = new ArrayList<Village>(); 
		boolean needToFuse = false; 

		for(Tile lTile : pGame.getNeighbors(invadedTile))
		{
			if(lTile.getColor() == invadingVillage.getColor() ) //check the neighbors to see which same-color villages are fuse candidates
			{
				if (!pGame.getVillage(lTile).equals(invadingVillage)) // make sure we aren't just looking at the invading village over and over
				{ 
					toFuse.add(pGame.getVillage(lTile)); 
					aCurrentPlayer.removeVillage(pGame.getVillage(lTile));
					needToFuse = true; 
				}
			}
			if(needToFuse)
			{
				pGame.fuseVillages(toFuse, invadingCapital, aCurrentPlayer);
			}
		}

	}
}