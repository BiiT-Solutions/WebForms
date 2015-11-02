package com.biit.webforms.utils.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.biit.webforms.logger.WebformsLogger;

public class ImageTools {
	/**
	 * Creates a default image for forms without flows.
	 * 
	 * @return
	 */

	public static byte[] createDefaultImage(int imageWidth, int imageHeight, String text) {
		int MIN_SIZE = 200;
		ByteArrayOutputStream imagebuffer = null;
		// No data (because there is not any flow).
		// HEIGHT discount the top margin of panel.
		int width = imageWidth < MIN_SIZE ? MIN_SIZE : imageWidth;
		int height = imageHeight < MIN_SIZE ? MIN_SIZE : imageHeight;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics drawable = image.getGraphics();
		drawable.setColor(Color.WHITE);
		drawable.fillRect(0, 0, (int) width, height);
		drawable.setColor(Color.black);
		drawable.drawString("Image cannot be created.", width / 2 - 75, height / 2);

		try {
			// Write the image to a buffer.
			imagebuffer = new ByteArrayOutputStream();
			ImageIO.write(image, "png", imagebuffer);

			// Return a stream from the buffer.
			return imagebuffer.toByteArray();
		} catch (IOException e) {
			WebformsLogger.errorMessage(ImageTools.class.getName(), e);
			return null;
		}
	}
}
