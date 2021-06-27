package textures;

import java.nio.ByteBuffer;

public class TextureData {

	ByteBuffer imageData;
	int width;
	int height;

	public TextureData(ByteBuffer imageData, int width, int height) {

		this.imageData = imageData;
		this.width = width;
		this.height = height;

	}

	public ByteBuffer getImageData() {
		return imageData;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
