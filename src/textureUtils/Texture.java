package textureUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {

    public static int loadTexture(String filename) {

        PNGDecoder decoder = null;

        try {
            decoder = new PNGDecoder(Texture.class.getResourceAsStream("res/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try {
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buf.flip();

        // Create a new OpenGL texture
        int textureId = GL11.glGenTextures();
        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(),decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

        return textureId;
    }

}