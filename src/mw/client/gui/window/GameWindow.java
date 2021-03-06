package mw.client.gui.window;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mw.client.controller.CurrentClientState;
import mw.client.controller.guimodel.ActionInterpreter;
import mw.client.controller.guimodel.ChoiceCenter;
import mw.client.controller.guimodel.ChoiceCenter.ChoiceType;
import mw.client.gui.api.basics.ObservableWindowComponent;
import mw.client.gui.api.basics.ObservableWindowComponent.ChangedState;
import mw.client.gui.api.basics.WindowComponent;
import mw.client.gui.api.components.AbstractButton;
import mw.client.gui.api.components.BlockComponent;
import mw.client.gui.api.components.ResizableWindow;
import mw.client.gui.api.components.TextDisplay;
import mw.client.gui.api.extminueto.ExtendedMinuetoColor;
import mw.client.gui.api.interactive.TextField;
import mw.client.gui.api.layouts.HorizontalLayout;
import mw.client.gui.api.layouts.VerticalLayout;
import mw.client.gui.menuing.InGameMenu;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoFocusHandler;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouse;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindowInvalidStateException;

import com.sun.xml.internal.ws.Closeable;

public class GameWindow implements Observer {
	
	public static final MinuetoColor BACKGROUND_COLOR = ExtendedMinuetoColor.mixColors(MinuetoColor.BLACK, MinuetoColor.WHITE, 0.05);
	public static final int DEFAULT_MAP_WIDTH = 1000;
	public static final int DEFAULT_MAP_HEIGHT = 700;
	public static final int CONTROL_LAYOUT_HEIGHT = 150;
	
	private final ResizableWindow window;
	private final MinuetoEventQueue queue;
	
	private MapDisplay md;
	private MapComponent mapComp;
	
	private VerticalLayout windowLayout;
	private HorizontalLayout controlBarLayout;
	private HorizontalLayout userLayout;
	
	private AbstractButton endTurn;
	private AbstractButton fire;
	private List<AbstractButton> choiceButtonsList;
	
	/* ========================
	 * 		Constructors
	 * ========================
	 */

	
	public GameWindow(MapDisplay mapDisp)
	{
		md = mapDisp;
		queue = new MinuetoEventQueue();
		mapComp = new MapComponent(0, 0, DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT, md);
		choiceButtonsList = new ArrayList<AbstractButton>();
		
		endTurn = new AbstractButton("End Turn")
		{
			public void buttonClick(int mouseButton)
			{
				if(mouseButton == MinuetoMouse.MOUSE_BUTTON_LEFT)
				{
					ActionInterpreter.singleton().handleEndTurn();
				}
			}
		};
		
		fire = new AbstractButton("FIRE CANNON") {
			
			@Override
			public void buttonClick(int mouseButton) {
				ActionInterpreter.singleton().startFiringCannon();
				
			}
		};
	
		windowLayout = new VerticalLayout(0, 0, 4);
		controlBarLayout = new HorizontalLayout(0, 0, CONTROL_LAYOUT_HEIGHT, 3);
		userLayout = new HorizontalLayout(0, 0, 2);
		
		windowLayout.addComponent(mapComp, 0);
		windowLayout.addComponent(userLayout, 1);
		windowLayout.addComponent(new BlockComponent(0, CONTROL_LAYOUT_HEIGHT, controlBarLayout), 2);
		window = new ResizableWindow(windowLayout.getWidth(), 941/*windowLayout.getHeight()*/, queue, "Medieval Warfare");
		
		mapComp.setWindow(this);
		windowLayout.setWindow(this);
		//controlBarLayout.setWindow(this);
		window.setVisible(true);
		GameWindow dumbRef = this;
		window.registerFocusHandler(new MinuetoFocusHandler() {
			
			@Override
			public void handleLostFocus()
			{
				
			}
			
			@Override
			public void handleGetFocus()
			{
				dumbRef.render();
			}
		}, queue);
		
		window.registerKeyboardHandler(new MinuetoKeyboardHandler() {
			
			@Override
			public void handleKeyType(char arg0)
			{
				
			}
			
			@Override
			public void handleKeyRelease(int arg0)
			{
				if (arg0 == MinuetoKeyboard.KEY_ESC) {
					InGameMenu escMenu = new InGameMenu();
				}
			}
			
			@Override
			public void handleKeyPress(int arg0)
			{
				
			}
		});
	}
	
	/* ==========================
	 * 		Public methods
	 * ==========================
	 */
	
	public void addUserDisplay(String user, MinuetoColor c)
	{
		TextDisplay userDisplay = new TextDisplay(user, c);
		this.userLayout.addComponent(userDisplay, 0);
	}
	
	public MinuetoEventQueue getEventQueue()
	{
		return this.queue;
	}
	
	public void render()
	{
		try {
			window.clear(BACKGROUND_COLOR);
			windowLayout.drawOn(window);
			window.render();
		}
		catch (MinuetoWindowInvalidStateException e) {
			System.out.println("[GameWindow] Minueto is so sick, it's not letting me draw");
			System.out.println("--> error message : "+e.getMessage());
		}
	}

	public void registerMouseHandler(MinuetoMouseHandler h)
	{
		window.registerMouseHandler(h);
	}

	public void unregisterMouseHandler(MinuetoMouseHandler h)
	{
		window.unregisterMouseHandler(h);
	}

	public void registerKeyboardHandler(MinuetoKeyboardHandler h) 
	{
		window.registerKeyboardHandler(h);
	}

	public void unregisterKeyboardHandler(MinuetoKeyboardHandler h) 
	{
		window.unregisterKeyboardHandler(h);
	}
	
	public void displayVillageResources(int gold, int wood)
	{
		VerticalLayout resourceLayout = new VerticalLayout(2);
		TextDisplay goldText = new TextDisplay("Gold: " + gold);
		TextDisplay woodText = new TextDisplay("Wood: " + wood);
		resourceLayout.addComponent(woodText);
		resourceLayout.addComponent(goldText);
		controlBarLayout.addComponent(resourceLayout, 0);
		//resourceLayout.setWindow(this);
		this.render();
	}
	
	public void addChoiceLayout(ChoiceType choiceType, List<String> choices)
	{
		System.out.println("Creating a choice with type "+choiceType);
		VerticalLayout choiceLayout = new VerticalLayout(choices.size() + 1);
		TextDisplay choiceTitle = new TextDisplay(ChoiceCenter.getChoiceTitle(choiceType));
		choiceLayout.addComponent(choiceTitle);
		for(String str : choices)//int i = 0; i < choices.size(); i++)
		{
			AbstractButton choiceButton = new AbstractButton(str)
				{				
					public void buttonClick(int mouseButton)
					{
						if (mouseButton == MinuetoMouse.MOUSE_BUTTON_LEFT)
						{
							System.out.println("Notifying for "+choiceType+" item "+str);
							ActionInterpreter.singleton().notifyChoiceResult(choiceType, str);
						}
					}
				};
			this.registerMouseHandler(choiceButton);
			choiceLayout.addComponent(choiceButton);
			choiceButtonsList.add(choiceButton);
		}
		switch (choiceType)
		{
		case VILLAGE_UPGRADE:
		case UNIT_UPGRADE:
			controlBarLayout.addComponent(choiceLayout, 1);
			break;
		case UNIT_HIRE:
		case UNIT_ACTION:
			controlBarLayout.addComponent(choiceLayout, 2);
			break;
		}
		//choiceLayout.setWindow(this);
		//this.render();
	}
	
	public void addEndTurnButton()
	{
		this.registerMouseHandler(endTurn);
		HorizontalLayout hlayout = new HorizontalLayout(1);
		hlayout.addComponent(endTurn);
		userLayout.addComponent(hlayout, 1);
		//windowLayout.addComponent(endTurn, 1);
		//this.render();
	}
	
	public void removeEndTurnButton()
	{
		window.unregisterMouseHandler(endTurn);
		userLayout.removeComponent(1);
		//this.render();
	}
	
	public void addFireButton()
	{
		this.registerMouseHandler(fire);
		controlBarLayout.addComponent(fire, 2);
	}
	
	public void removeFireButton()
	{
		controlBarLayout.removeComponent(2);
		window.unregisterMouseHandler(fire);
	}
	
	public void removeAllChoices()
	{
		controlBarLayout.removeComponent(1);
		controlBarLayout.removeComponent(2);
		for (AbstractButton b : choiceButtonsList)
			window.unregisterMouseHandler(b);
		choiceButtonsList = new ArrayList<AbstractButton>();
		//render();
	}

	public void hideVillageResources()
	{
		controlBarLayout.removeComponent(0);
		//render();
	}
	
	public void testAddTextField()
	{
		hideVillageResources();
		TextField field = new TextField(200);
		controlBarLayout.addComponent(field, 0);
		field.setWindow(this);
	}
	
	public void showMessage(String message) 
	{
		windowLayout.addComponent(new TextDisplay(message), 3);
	}
	
	public void close()
	{
		this.window.close();
	}
	
	/* ==========================
	 * 		Private methods
	 * ==========================
	 */


	/* ==========================
	 * 		Inherited methods
	 * ==========================
	 */

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ChangedState) {
			ChangedState changedState = (ChangedState)arg;
			WindowComponent comp = (WindowComponent)o;
			int width = comp.getWidth();
			int height = comp.getHeight();
			if (changedState == ChangedState.SIZE 
					&& comp == windowLayout 
					&& (width != window.getWidth() || height > window.getHeight()))
			{
				System.out.println("[GameWindow] The layout now has size "+width+", "+height);
				window.resize(width, height);
				comp.drawOn(window);
			}
		}
		render();
	}

	


	/* ========================
	 * 		Static methods
	 * ========================
	 */
		
}
