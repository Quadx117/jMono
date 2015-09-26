package gameCore.util;

public class Array1D {

	private Array1D() {
	}

	// Works on Sprites if width = height (exceeding width or height should be
	// transparent)
	public static int[] rotate(double angle, int[] pixels, int width, int height) {
		final double radians = Math.toRadians(angle);
		final double cos = Math.cos(radians);
		final double sin = Math.sin(radians);
		final int[] pixels2 = new int[pixels.length];
		final int centerx = width / 2;
		final int centery = height / 2;

		for (int y = 0; y < height; ++y) {
			final int n = y - centery;
			for (int x = 0; x < width; ++x) {
				final int m = x - centerx;
				final int dstIdx = getIndex(width, height, x, y);
				final int srcIdx = getIndex(width, height, ((int) (m * cos + n * sin)) + centerx, ((int) (n * cos - m
						* sin))
						+ centery);
				if (srcIdx == -1) {
					pixels2[dstIdx] = 0xFFFFFF;
				} else {
					pixels2[dstIdx] = pixels[srcIdx];
				}
			}
		}
		return pixels2;
	}

	private static int getIndex(int width, int height, int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return y * width + x;
		}
		return -1;
	}
}
