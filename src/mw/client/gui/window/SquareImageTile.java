package mw.client.gui.window;

import java.util.Observable;

import mw.client.gui.api.extminueto.ExtendedMinuetoColor;
import mw.client.gui.api.extminueto.ExtendedMinuetoImage;
import mw.client.gui.window.ImageFileManager.TileType;
import mw.shared.SharedTile.*;

import org.minueto.MinuetoColor;
import org.minueto.image.*;

/**
 * This class defines the ImageTile object 
 * @author Arthur Denefle
 *
 */
@Deprecated
public class SquareImageTile extends Observable {
	private MinuetoImage image;
	public static final int DEFAULT_TILE_WIDTH = 80;
	public static final int DEFAULT_TILE_HEIGHT = 80;
	
	/**
	 * Default ImageTile constructor, creates an ImageTile with a blank MinuetoImage.
	 */
	public SquareImageTile()
	{
		//image = ImageFileManager.getTileImage(TileType.DEFAULT);
		setImage(new MinuetoImage(DEFAULT_TILE_WIDTH, DEFAULT_TILE_HEIGHT));
	}
	
	public SquareImageTile(MinuetoColor c, VillageType v, UnitType u, Terrain t)
	{
		setImage(ExtendedMinuetoImage.coloredSquare(DEFAULT_TILE_WIDTH, DEFAULT_TILE_HEIGHT, c));
		image.draw(ImageFileManager.getTerrainImage(t), 0, 0);
		image.draw(ImageFileManager.getUnitImage(u), 0, 0);
		image.draw(ImageFileManager.getVillageImage(v), 0, 0);
	}
	/**
	 * Getter for an ImageTile's MinuetoImage.
	 * @return MinuetoImage
	 */
	public MinuetoImage getTileImage()
	{
		return image;
	}
	/**
	 * 
	 */
	public void updateImage(MinuetoColor c, Terrain t, VillageType v, UnitType u)
	{
		setImage(ExtendedMinuetoImage.coloredSquare(DEFAULT_TILE_WIDTH, DEFAULT_TILE_HEIGHT, c));
		image.draw(ImageFileManager.getTerrainImage(t), 0, 0);
		image.draw(ImageFileManager.getUnitImage(u), 0, 0);
		image.draw(ImageFileManager.getVillageImage(v), 0, 0);
	}
	
	{
		setImage(ImageFileManager.getTileImage(TileType.GRASS));
		setChanged();
		notifyObservers();
	}
	
	public void updateColor(MinuetoColor c)
	{
		setImage(ExtendedMinuetoImage.coloredSquare(DEFAULT_TILE_WIDTH, DEFAULT_TILE_HEIGHT, c));
		setChanged();
		notifyObservers();
	}
	
	public void setImage(MinuetoImage newImage)
	{
		image = ExtendedMinuetoImage.drawBorder(newImage, ExtendedMinuetoColor.GREY);
	}
	
	/*public void setBorderSelected(MinuetoImage selectedImage, MinuetoColor c)
	{
		image = ExtendedMinuetoImage.drawBorder(selectedImage, c);
	}*/
	
	public void drawBorder(MinuetoColor c)
	{
		image = ExtendedMinuetoImage.drawBorder(image, c);
	}
}
