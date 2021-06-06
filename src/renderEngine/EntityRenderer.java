package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.StaticShader;
import textures.ModelTexture;
import toolBox.Maths;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader) {
        this.shader = shader;
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {

        for(TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);

            for(Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }

    }

    private void prepareTexturedModel(TexturedModel model) {

        RawModel rawModel = model.getRawModel();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        if(texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }

        shader.loadFakeLighting(texture.isUseFakeLighting());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, model.getTexture().getTextureID());

    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);

    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()
        );
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
