package mw.server.gamelogic;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;



/**
 * Player class definition.
 * Generated by the TouchCORE code generator.
 */
public class Player implements Serializable{

	private Color aColor; 
    private Collection<Village> aVillages;
	
	public Player ()
	{
		aVillages = new HashSet<Village>();
	}
	
    public void assignColor(Color pColor)
    {
      aColor = pColor; 
    }
    
    public Color getPlayerColor()
    {
    	return aColor; 
    }
    
    public void addVillage(Village pVillage){
    	aVillages.add(pVillage);
    }

    public void removeVillage(Village pVillage) 
    {
        aVillages.remove(pVillage); 
    }

    public Collection<Village> getVillages() 
    {
    	return aVillages; 
    }
}
