package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import textureUtils.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, float[] texCoords, float[] normals,int[] indices) {

        int vaoID = createVAO();
        bindIndicesVBO(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,texCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);

    }

    public void cleanUp() {
        for(int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : vbos) {
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

        int textureId =  Texture.loadTexture(filename);
        textures.add(textureId);
        return textureId;

    }

    private void storeDataInAttributeList(int atttributeNumber, int coordSize, float[] data) {

        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer=storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(atttributeNumber,coordSize, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);

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