package models;

import textures.ModelTexture;

public class TexturedModel {

	public RawModel rawModel;
	public ModelTexture texture;

	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
