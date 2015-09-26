package gameCore.graphics;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

	// The size of the sprite (if width == height)
	public final int SIZE;

	// The width and height of the sprite
	private int width, height;

	// The x and y starting location of the sprite on a sheet
	private int x, y;

	// The array of pixels of the sprite
	public int[] pixels;

	protected SpriteSheet sheet;

	// Card sprites here:
	public static Sprite cardBackRed = new Sprite(80, 106, 1, 0, SpriteSheet.cardSet);
	public static Sprite cardBackBlue = new Sprite(80, 106, 0, 0, SpriteSheet.cardSet);

	public static Sprite firstJoker = new Sprite(80, 106, 11, 0, SpriteSheet.cardSet);
	public static Sprite secondJoker = new Sprite(80, 106, 12, 0, SpriteSheet.cardSet);

	public static Sprite clubAce = new Sprite(80, 106, 0, 1, SpriteSheet.cardSet);
	public static Sprite club2 = new Sprite(80, 106, 1, 1, SpriteSheet.cardSet);
	public static Sprite club3 = new Sprite(80, 106, 2, 1, SpriteSheet.cardSet);
	public static Sprite club4 = new Sprite(80, 106, 3, 1, SpriteSheet.cardSet);
	public static Sprite club5 = new Sprite(80, 106, 4, 1, SpriteSheet.cardSet);
	public static Sprite club6 = new Sprite(80, 106, 5, 1, SpriteSheet.cardSet);
	public static Sprite club7 = new Sprite(80, 106, 6, 1, SpriteSheet.cardSet);
	public static Sprite club8 = new Sprite(80, 106, 7, 1, SpriteSheet.cardSet);
	public static Sprite club9 = new Sprite(80, 106, 8, 1, SpriteSheet.cardSet);
	public static Sprite club10 = new Sprite(80, 106, 9, 1, SpriteSheet.cardSet);
	public static Sprite clubJ = new Sprite(80, 106, 10, 1, SpriteSheet.cardSet);
	public static Sprite clubQ = new Sprite(80, 106, 11, 1, SpriteSheet.cardSet);
	public static Sprite clubK = new Sprite(80, 106, 12, 1, SpriteSheet.cardSet);

	public static Sprite diamondAce = new Sprite(80, 106, 0, 2, SpriteSheet.cardSet);
	public static Sprite diamond2 = new Sprite(80, 106, 1, 2, SpriteSheet.cardSet);
	public static Sprite diamond3 = new Sprite(80, 106, 2, 2, SpriteSheet.cardSet);
	public static Sprite diamond4 = new Sprite(80, 106, 3, 2, SpriteSheet.cardSet);
	public static Sprite diamond5 = new Sprite(80, 106, 4, 2, SpriteSheet.cardSet);
	public static Sprite diamond6 = new Sprite(80, 106, 5, 2, SpriteSheet.cardSet);
	public static Sprite diamond7 = new Sprite(80, 106, 6, 2, SpriteSheet.cardSet);
	public static Sprite diamond8 = new Sprite(80, 106, 7, 2, SpriteSheet.cardSet);
	public static Sprite diamond9 = new Sprite(80, 106, 8, 2, SpriteSheet.cardSet);
	public static Sprite diamond10 = new Sprite(80, 106, 9, 2, SpriteSheet.cardSet);
	public static Sprite diamondJ = new Sprite(80, 106, 10, 2, SpriteSheet.cardSet);
	public static Sprite diamondQ = new Sprite(80, 106, 11, 2, SpriteSheet.cardSet);
	public static Sprite diamondK = new Sprite(80, 106, 12, 2, SpriteSheet.cardSet);

	public static Sprite spadeAce = new Sprite(80, 106, 0, 3, SpriteSheet.cardSet);
	public static Sprite spade2 = new Sprite(80, 106, 1, 3, SpriteSheet.cardSet);
	public static Sprite spade3 = new Sprite(80, 106, 2, 3, SpriteSheet.cardSet);
	public static Sprite spade4 = new Sprite(80, 106, 3, 3, SpriteSheet.cardSet);
	public static Sprite spade5 = new Sprite(80, 106, 4, 3, SpriteSheet.cardSet);
	public static Sprite spade6 = new Sprite(80, 106, 5, 3, SpriteSheet.cardSet);
	public static Sprite spade7 = new Sprite(80, 106, 6, 3, SpriteSheet.cardSet);
	public static Sprite spade8 = new Sprite(80, 106, 7, 3, SpriteSheet.cardSet);
	public static Sprite spade9 = new Sprite(80, 106, 8, 3, SpriteSheet.cardSet);
	public static Sprite spade10 = new Sprite(80, 106, 9, 3, SpriteSheet.cardSet);
	public static Sprite spadeJ = new Sprite(80, 106, 10, 3, SpriteSheet.cardSet);
	public static Sprite spadeQ = new Sprite(80, 106, 11, 3, SpriteSheet.cardSet);
	public static Sprite spadeK = new Sprite(80, 106, 12, 3, SpriteSheet.cardSet);

	public static Sprite heartAce = new Sprite(80, 106, 0, 4, SpriteSheet.cardSet);
	public static Sprite heart2 = new Sprite(80, 106, 1, 4, SpriteSheet.cardSet);
	public static Sprite heart3 = new Sprite(80, 106, 2, 4, SpriteSheet.cardSet);
	public static Sprite heart4 = new Sprite(80, 106, 3, 4, SpriteSheet.cardSet);
	public static Sprite heart5 = new Sprite(80, 106, 4, 4, SpriteSheet.cardSet);
	public static Sprite heart6 = new Sprite(80, 106, 5, 4, SpriteSheet.cardSet);
	public static Sprite heart7 = new Sprite(80, 106, 6, 4, SpriteSheet.cardSet);
	public static Sprite heart8 = new Sprite(80, 106, 7, 4, SpriteSheet.cardSet);
	public static Sprite heart9 = new Sprite(80, 106, 8, 4, SpriteSheet.cardSet);
	public static Sprite heart10 = new Sprite(80, 106, 9, 4, SpriteSheet.cardSet);
	public static Sprite heartJ = new Sprite(80, 106, 10, 4, SpriteSheet.cardSet);
	public static Sprite heartQ = new Sprite(80, 106, 11, 4, SpriteSheet.cardSet);
	public static Sprite heartK = new Sprite(80, 106, 12, 4, SpriteSheet.cardSet);

	// Horizontal cards
	// public static Sprite cardBack_Blue_hor = new Sprite(106, 80, 0, 0,
	// SpriteSheet.cardBack_Horizontal);
	// public static Sprite cardBack_Red_hor = new Sprite(106, 80, 1, 0,
	// SpriteSheet.cardBack_Horizontal);

	// Card highlight for selected card
	// public static Sprite cardHighlight = new Sprite(85, 110, 0, 0,
	// SpriteSheet.cardHighlight);

	protected Sprite(SpriteSheet sheet, int width, int height) {
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}

	public Sprite(int width, int height, int x, int y, SpriteSheet sheet) {
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		this.x = x * width;
		this.y = y * height;
		this.sheet = sheet;
		load();
	}

	public Sprite(String path) {
		this.SIZE = -1;
		this.x = 0;
		this.y = 0;
		load(path);
	}

	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * SIZE;
		this.y = y * SIZE;
		this.sheet = sheet;
		load();
	}

	public Sprite(int width, int height, SpriteSheet sheet) {
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		this.x = x * SIZE;
		this.y = y * SIZE;
		this.sheet = sheet;
		load();
	}

	public Sprite(int width, int height, int color) {
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		setColor(color);
	}

	public Sprite(int width, int height, Color color) {
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		setColor(color.getRGB());
	}

	public Sprite(int size, int color) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		setColor(color);
	}

	public Sprite(int size, Color color) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		setColor(color.getRGB());
	}

	public Sprite(int[] pixels, int width, int height) {
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;

		// create a copy of the array of pixels because it is passed by
		// reference
		this.pixels = new int[pixels.length];
		for (int i = 0; i < pixels.length; ++i) {
			this.pixels[i] = pixels[i];
		}
	}

	private void setColor(int color) {
		for (int i = 0; i < width * height; ++i) {
			pixels[i] = color;
		}
	}

	private void load() {
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SPRITE_WIDTH];
			}
		}
	}

	private void load(String path) {
		try {
			System.out.print("Trying to load: " + path + " ...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			System.out.println(" succeeded!");
			this.width = image.getWidth();
			this.height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(" failed!");
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}
}
