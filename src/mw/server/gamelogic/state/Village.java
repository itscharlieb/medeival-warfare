package mw.server.gamelogic.state;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import mw.server.gamelogic.enums.Color;
import mw.server.gamelogic.enums.StructureType;
import mw.server.gamelogic.enums.VillageType;
import mw.server.gamelogic.exceptions.CantUpgradeException;
import mw.server.gamelogic.exceptions.NotEnoughIncomeException;
import mw.server.gamelogic.logic.VillageLogic;



/**
 * Village class definition.
 * @author emilysager, Abhishek Gupta
 */
public class Village extends Observable implements Serializable
{
	private int aGold; 
	private int aWood; 
	private Tile aCapital; 
	private VillageType aVillageType; 
	private Collection<Tile> aTiles;

	/**
	 * Village Constructor
	 * @param lVillageTiles
	 */
	public Village(Collection<Tile> lVillageTiles) 
	{
		aTiles= lVillageTiles; 
		aGold = 100; 
		aWood = 100; 
	}
	
	/**
	 * Sets the type of a village to hovel, town or fort. 
	 * @param pVillageType
	 */
	public void setVillageType(VillageType pVillageType)
	{
		aVillageType = pVillageType;
		aCapital.setVillageType(pVillageType);
	}
	
	public void setRandomCapital()
	{
		Random r = new Random(); 
		int i = r.nextInt(150); 
		for (Tile lTile : aTiles)
		{
			if (i%10 == 0)
			{
				setCapital(lTile);
				break; 
			}
			else 
			{
				i++; 
			}
		}
	}

	/**
	 * Sets the Village Capital to be a hovel 
	 * @param pCapital
	 */
	public void setCapital(Tile pCapital)
	{
		aVillageType = VillageType.HOVEL;
		aCapital = pCapital;
		aCapital.setStructureType(StructureType.VILLAGE_CAPITAL);
		aCapital.setVillageType(VillageType.HOVEL); 
	}

	/**
	 *Gets the tiles that make up a village
	 * @return
	 */
	public Collection<Tile> getTiles ()
	{
		return aTiles; 
	}

	public void upgrade(VillageType pVillageType) throws NotEnoughIncomeException, CantUpgradeException 
	{
				VillageLogic.upgradeVillage(this, aVillageType);
	}

	/**
	 * Updates the village state at the beginning of a turn
	 * 
	 */
	public void beginTurnUpdate()  
	{
		addOrSubtractGold(VillageLogic.generateGold(aTiles));
		VillageLogic.clearTombstone(aTiles);
		VillageLogic.generateMeadows(aTiles);
		VillageLogic.buildRoads(aTiles);
		VillageLogic.readyUnits(aTiles); 
		try
		{
			VillageLogic.payVillagers(aTiles, this);
		}
		catch  (NotEnoughIncomeException e) 
		{
			VillageLogic.starveVillage(aTiles); 
		}
	}

	/**
	 * Adds the specified amount of gold to the village, maybe used in the context of takeOver village
	 * @param addGold
	 */
	public void addOrSubtractGold(int addGold) 
	{
		aGold = aGold + addGold; 
	}

	/**
	 * Adds the specified amount of wood to the village, maybe used in the context of takeOver village
	 * @param addWood
	 */
	public void addOrSubtractWood(int addWood) 
	{
		aWood = aWood + addWood;
	}

	public Tile getCapital()
	{
		return aCapital;
	}
	/**
	 * Adds a tile to a village
	 * @param pTile
	 */
	public void addTile(Tile pTile) 
	{
		pTile.setColor(getColor());
		aTiles.add(pTile);  
		
	}
	
	/**
	 * Removes a tile from a village
	 * @param pTile
	 */
	public void removeTile(Tile pTile) 
	{
		pTile.setColor(Color.NEUTRAL);
		aTiles.remove(pTile);
	}

	/**
	 * Returns the village type:  hovel, fort or town 
	 * @return
	 */
	public VillageType getVillageType() 
	{
		return aVillageType; 
	}
	
	/**
	 * getter for Village Gold
	 * @return
	 */
	public int getGold() 
	{
		return aGold;
	}

	/**
	 * getter for Village Wood
	 * @return
	 */
	public int getWood() 
	{
		return aWood;
	}
	
	/**
	 * Returns the color of the village
	 * @return
	 */
	public Color getColor() 
	{
		return aCapital.getColor();
	}
	
	public boolean contains(Tile pTile)
	{
		return aTiles.contains(pTile);
	}
	/**
	 * Returns the Tiles of a village
	 * @return
	 * @Override
	 */
	public String toString() 
	{
		String myString = "The village tiles are: ";

		for (Tile lTile : aTiles)
		{
			myString += lTile.toString();
		}
		return myString; 
	}
	
}