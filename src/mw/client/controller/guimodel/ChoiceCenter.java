package mw.client.controller.guimodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mw.shared.SharedActionType;
import mw.shared.SharedTile;
import mw.shared.SharedTile.VillageType;

public class ChoiceCenter {
	
	public enum ChoiceType { UNIT_HIRE, VILLAGE_UPGRADE, UNIT_ACTION, UNIT_UPGRADE } ;
	
	private static final String vupChoiceName = "Type of village to upgrade to:";
	private UserChoice<SharedTile.VillageType> villageUpgradeChoice;

	private static final String uactChoiceName = "Select an action:";
	private UserChoice<SharedActionType> unitActionChoice;

	private static final String hireChoiceName = "Type of unit to hire:";
	private UserChoice<SharedTile.UnitType> unitHireChoice;
	
	private static final String uupChoiceName = "Type of unit to upgrade to:";
	private UserChoice<SharedTile.UnitType> unitUpgradeChoice;
	
	
	/* ========================
	 * 		Constructors
	 * ========================
	 */

	public ChoiceCenter()
	{
		clear();
	}

	/* ==========================
	 * 		Public methods
	 * ==========================
	 */

	public void clear() 
	{
		villageUpgradeChoice = null;
		unitActionChoice = null;
		unitHireChoice = null;
		DisplayUpdater.clearInfos();
	}
	
	public void handleChoiceResult(ChoiceType chType, String choseItem, ActionInterpreter handler)
	{
		switch(chType)
		{
		case VILLAGE_UPGRADE:
			VillageType vt = getChoiceResult(villageUpgradeChoice, choseItem);
			handler.notifyVillageUpgradeChoiceResult(vt);
			break;
			
		case UNIT_HIRE:
			SharedTile.UnitType ut = getChoiceResult(unitHireChoice, choseItem);
			handler.notifyUnitHireChoiceResult(ut);
			break;
			
		case UNIT_ACTION:
			SharedActionType at = getChoiceResult(unitActionChoice, choseItem);
			handler.notifyUnitActionChoiceResult(at);
			break;
			

		case UNIT_UPGRADE:
			SharedTile.UnitType upunit= getChoiceResult(unitUpgradeChoice, choseItem);
			handler.notifyUnitUpgradeChoiceResult(upunit);
			break;
			
			default:
				throw new IllegalArgumentException("The pair (choiceType="+chType+", choseItem=\""+choseItem+") is invalid");
		}
	}
	
	
	//		Display


	public void displayVillageUpgradeChoice(VillageType vt)
	{
		List<VillageType> vtList = new ArrayList<VillageType>();
		vtList.add(vt);
		villageUpgradeChoice = new UserChoice<SharedTile.VillageType>(vtList);
		DisplayUpdater.displayChoice(ChoiceType.VILLAGE_UPGRADE, villageUpgradeChoice.itemsToString());
	}

	public void displayUnitHireChoice(Collection<SharedTile.UnitType> ut)
	{
		unitHireChoice = new UserChoice<SharedTile.UnitType>(ut);
		DisplayUpdater.displayChoice(ChoiceType.UNIT_HIRE, unitHireChoice.itemsToString());
	}

	public void displayUnitActionChoice(Collection<SharedActionType> unitActions)
	{
		unitActionChoice = new UserChoice<SharedActionType>(unitActions);
		DisplayUpdater.displayChoice(ChoiceType.UNIT_ACTION, unitActionChoice.itemsToString());
	}

	public void displayUnitUpgradeChoice(Collection<SharedTile.UnitType> ut)
	{
		unitUpgradeChoice = new UserChoice<SharedTile.UnitType>(ut);
		DisplayUpdater.displayChoice(ChoiceType.UNIT_UPGRADE, unitUpgradeChoice.itemsToString());
	}
	
	/* ==========================
	 * 		Private methods
	 * ==========================
	 */

	private <T> T getChoiceResult(UserChoice<T> choice, String strRes)
	{
		if (choice == null)
			throw new IllegalArgumentException("No choice available, impossible to select anything");
		return choice.getItem(strRes);
	}

	/* ==========================
	 * 		Inherited methods
	 * ==========================
	 */



	/* ========================
	 * 		Static methods
	 * ========================
	 */
	
	public static String getChoiceTitle(ChoiceType choice)
	{
		switch (choice)
		{
		case UNIT_ACTION:
			return uactChoiceName;
			
		case UNIT_HIRE:
			return hireChoiceName;
			
		case VILLAGE_UPGRADE:
			return vupChoiceName;

		case UNIT_UPGRADE:
			return uupChoiceName;
			
			default:
				throw new IllegalArgumentException("The value "+choice+" does not have a choice name associated with it");
		}
	}

}