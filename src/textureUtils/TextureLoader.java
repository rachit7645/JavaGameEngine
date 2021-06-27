package textureUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.*;
import textures.TextureData;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TextureLoader {

    public static TextureData loadTexture(String filename) {

        PNGDecoder decoder = null;

        try {
            decoder = new PNGDecoder(TextureLoader.class.getResourceAsStream("textures/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not get resource " + filename);
            System.exit(-1);
        }

        return loadTextureData(decoder, PNGDecoder.Format.RGBA);


    }

    public static TextureData loadTextureData(PNGDecoder decoder, PNGDecoder.Format format) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try {
            decoder.decode(buffer, decoder.getWidth() * 4, format);
        }catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();

        return new TextureData(buffer, decoder.getWidth(), decoder.getHeight());

    }

}
