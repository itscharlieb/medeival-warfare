package mw.client.model;

import java.nio.channels.ScatteringByteChannel;
import java.util.Observable;

import mw.client.model.Village.VillageType;
import mw.shared.SharedColor;


/**
 * The Tile class represents the conceptual state of a game tile in a 
 * restricted manner (only the informations that needs to be displayed).
 * @author Hugo KAPP
 *
 */
public final class Tile extends Observable {
	
	public enum Terrain {GRASS, TREE, MEADOW, TOMBSTONE, SEA};
	public enum UnitType {NONE, PEASANT, INFANTRY, SOLDIER, KNIGHT, WATCHTOWER};
	public enum StructureType {NONE, HOVEL, TOWN, FORT}; 
	
	
	private final Coordinates coord;
	
	private boolean hasRoad = false;
	private Terrain terrain = Terrain.GRASS;
	private UnitType unit = UnitType.NONE;
	private Village village = null; //StructureType.NONE;
	private SharedColor color = null; //Player owner = null;
	
	
	/**
	 * Constructs a new default Tile with coordinates (x,y). Default 
	 * tiles are neutral, have grass, and don't have any unit or village on them.
	 */
	public Tile(int x, int y)
	{
		coord = new Coordinates(x, y);
	}
	
	
	//	SETTERS
	
	/**
	 * Set the color of that Tile to c.
	 * @param c - new Color of the Tile
	 */
	public void setColor(SharedColor c)
	{
		if (!c.equals(color)) {
			color = c;
			setChanged();
		}
	}
	
	public void setRoad(boolean hasRoadNow)
	{
		if (hasRoad!=hasRoadNow) {
			hasRoad = hasRoadNow;
			setChanged();
		}
	}
	
	public void setTerrain(Terrain newTerrain)
	{
		if (terrain != newTerrain) {
			terrain = newTerrain;
			setChanged();
		}
	}
	
	public void setUnitType(UnitType newUnitType)
	{
		if (unit != newUnitType) {
			unit = newUnitType;
			setChanged();
		}
	}
	
	public void setStructureType(StructureType newStructureType)
	{
		if (newStructureType==StructureType.NONE)	{
			if (village!=null) {
				village=null;
				setChanged();
			}
		}
		else {
			if (village==null) {
				village = new Village();
				setChanged();
			}
			VillageType vt = translateToVillageType(newStructureType);
			if (village.getVillageType() != vt) {
				village.setVillageType(vt);
				setChanged();
			}
		}
	}
	
	public void setVillageGold(int newGold)
	{
		if (village==null) {
			village = new Village();
			setChanged();
		}
		if (village.getGold()!=newGold) {
			village.setGold(newGold);
			setChanged();
		}
	}
	
	public void setVillageWood(int newWood)
	{
		if (village == null) {
			village = new Village();
			setChanged();
		}
		if (village.getWood() != newWood) {
			village.setWood(newWood);
			setChanged();
		}
	}
	
	
	//	Getters
	
	
	public Coordinates getCoordinates()
	{
		return coord;
	}
	
	
	public UnitType getUnitType() {
		return unit;
	}
	
	
	public StructureType getStructureType() {
		if (village==null)
			return StructureType.NONE;
		else {
			switch (village.getVillageType())
			{
			case HOVEL:
				return StructureType.HOVEL;
				
			case TOWN:
				return StructureType.TOWN;
				
			case FORT:
				return StructureType.FORT;
				
				default:
					throw new IllegalStateException("Tile has a village, but VillageType "+village.getVillageType()+" is not recognized");
			}
		}
			
	}
	

	public SharedColor getColor() {
		return color;
	}
	
	//	Static methods
	
	
	private static VillageType translateToVillageType(StructureType st)
	{
		switch (st)
		{
		case HOVEL:
			return VillageType.HOVEL;
			
		case TOWN:
			return VillageType.TOWN;
			
		case FORT:
			return VillageType.FORT;
			
		default:
			throw new IllegalArgumentException("Invalid cast from StructureType to VillageType");
		}
	}
	
	
	
	@Override
	public int hashCode()
	{
		return coord.hashCode();
	}


	
}