package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import textures.TextureData;
import toolBox.TextureLoader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] positions, float[] texCoords, float[] normals, int[] indices) {

		int vaoID = createVAO();
		bindIndicesVBO(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, texCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);

	}

	public RawModel loadToVAO(float[] positions, int dimensions) {

		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);

	}

	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}

	private int createVAO() {

		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;

	}

	public int loadTexture(String filename) {

		TextureData textureData = TextureLoader.loadTexture(filename);

		int textureId = GL11.glGenTextures();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(),
				textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				textureData.getImageData());

		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		textures.add(textureId);
		return textureId;

	}

	public int loadCubeMap(String[] textureFiles) {

		int textureID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		for (int i = 0; i < textureFiles.length; i++) {

			TextureData data = TextureLoader.loadTexture(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
					GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, data.getImageData());

		}

		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		textures.add(textureID);
		return textureID;

	}

	private void storeDataInAttributeList(int attributeNumber, int coordSize, float[] data) {

		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesVBO(int[] indices) {

		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

	}

	public static IntBuffer storeDataInIntBuffer(int[] data) {

		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;

	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {

		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;

	}

}
