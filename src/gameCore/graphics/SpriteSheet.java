package gameCore.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	// The path to the sprite sheet file
	private String path;

	// The size in pixel of the sprite sheet
	public final int SIZE;

	private int sheetWidth, sheetHeight; // Width and height of a SpriteSheet

	// The width and height of one sprite on this sheet
	public final int SPRITE_WIDTH, SPRITE_HEIGHT;

	// The array of pixels of one sprite
	public int[] pixels;

	// The array of sprites after it has been split from the sheet
	private Sprite[] sprites;

	// Cards Sheets here:
	public static SpriteSheet cardSet = new SpriteSheet("/images/cards/cardSet.png", 1040, 530);

	// public static SpriteSheet cardBack_Horizontal = new
	// SpriteSheet("/images/cards/cardBack_Horizontal.png", 636, 80);
	// public static SpriteSheet cardHighlight = new
	// SpriteSheet("/images/cards/cardHighlight.png", 85, 110);

	/**
	 * For animated sprites.
	 * 
	 * @param sheet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param spriteSize
	 */
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		int w = width * spriteSize;
		int h = height * spriteSize;
		pixels = new int[w * h];
		if (width == height) {
			SIZE = width;
		} else {
			SIZE = -1;
		}
		SPRITE_WIDTH = w;
		SPRITE_HEIGHT = h;

		// Read all the pixels from the part of the sheet we want
		for (int y0 = 0; y0 < h; ++y0) {
			int yp = yy + y0;
			for (int x0 = 0; x0 < w; ++x0) {
				int xp = xx + x0;
				pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.SPRITE_WIDTH];
			}
		}

		int frame = 0;
		sprites = new Sprite[width * height];
		for (int ya = 0; ya < height; ++ya) {
			for (int xa = 0; xa < width; ++xa) {
				int[] spritePixels = new int[spriteSize * spriteSize];
				for (int y0 = 0; y0 < spriteSize; ++y0) {
					for (int x0 = 0; x0 < spriteSize; ++x0) {
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize) + (y0 + ya * spriteSize)
								* SPRITE_WIDTH];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;
			}
		}
	}

	/**
	 * For animated sprites when the sheet contains only the sprites for one
	 * animation.
	 * 
	 * @param path
	 * @param sheetWidth
	 * @param sheetHeight
	 * @param spriteWidth
	 * @param spriteHeight
	 */
	public SpriteSheet(String path, int sheetWidth, int sheetHeight, int spriteWidth, int spriteHeight) {
		this.path = path;
		this.sheetWidth = sheetWidth;
		this.sheetHeight = sheetHeight;
		this.SPRITE_WIDTH = spriteWidth;
		this.SPRITE_HEIGHT = spriteHeight;
		load();

		int animationWidth = sheetWidth / spriteWidth;
		int animationHeight = sheetHeight / spriteHeight;
		sprites = new Sprite[animationWidth * animationHeight];

		if (SPRITE_WIDTH == SPRITE_HEIGHT) {
			SIZE = SPRITE_WIDTH;
		} else {
			SIZE = -1;
		}

		int frame = 0;
		for (int ya = 0; ya < animationHeight; ++ya) {
			for (int xa = 0; xa < animationWidth; ++xa) {
				int[] spritePixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
				for (int y0 = 0; y0 < SPRITE_HEIGHT; ++y0) {
					for (int x0 = 0; x0 < SPRITE_WIDTH; ++x0) {
						spritePixels[x0 + y0 * SPRITE_WIDTH] = pixels[(x0 + xa * SPRITE_WIDTH)
								+ (y0 + ya * SPRITE_WIDTH)];
					}
				}
				Sprite sprite = new Sprite(spritePixels, SPRITE_WIDTH, SPRITE_HEIGHT);
				sprites[frame++] = sprite;
			}
		}
	}

	public SpriteSheet(String path, int size) {
		this.path = path;
		this.SIZE = size;
		SPRITE_WIDTH = size;
		SPRITE_HEIGHT = size;
		load();
	}

	public SpriteSheet(String path, int spriteWidth, int spriteHeight) {
		this.path = path;
		this.SIZE = -1;
		SPRITE_WIDTH = spriteWidth;
		SPRITE_HEIGHT = spriteHeight;
		load();
	}

	public Sprite[] split() {
		int amount = (getWidth() * getHeight()) / (SPRITE_WIDTH * SPRITE_HEIGHT);
		Sprite[] sprites = new Sprite[amount];
		int current = 0;
		int[] pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
		for (int yp = 0; yp < getHeight() / SPRITE_HEIGHT; ++yp) {
			for (int xp = 0; xp < getWidth() / SPRITE_WIDTH; ++xp) {
				for (int y = 0; y < SPRITE_HEIGHT; ++y) {
					for (int x = 0; x < SPRITE_WIDTH; ++x) {
						int xo = x + xp * SPRITE_WIDTH;
						int yo = y + yp * SPRITE_HEIGHT;
						pixels[x + y * SPRITE_WIDTH] = getPixels()[xo + yo * getWidth()];
					}
				}
				sprites[current++] = new Sprite(pixels, SPRITE_WIDTH, SPRITE_HEIGHT);
			}
		}
		return sprites;
	}

	public Sprite[] getSprites() {
		return sprites;
	}

	public int getWidth() {
		return sheetWidth;
	}

	public int getHeight() {
		return sheetHeight;
	}

	public int[] getPixels() {
		return pixels;
	}

	private void load() {
		try {
			System.out.print("Trying to load: " + path + " ...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			System.out.println(" succeeded!");
			sheetWidth = image.getWidth();
			sheetHeight = image.getHeight();
			pixels = new int[sheetWidth * sheetHeight];
			image.getRGB(0, 0, sheetWidth, sheetHeight, pixels, 0, sheetWidth);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(" failed!");
		}
	}
}
