package toolBox;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {

        Matrix4f matrix = new Matrix4f();
        matrix.identity()
                .translate(translation)
                .rotateX((float)Math.toRadians(rx))
                .rotateY((float)Math.toRadians(ry))
                .rotateZ((float)Math.toRadians(rz))
                .scale(scale);
        return matrix;

    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {

        Matrix4f matrix = new Matrix4f();

        matrix.identity()
                .translate(new Vector3f(translation.x,translation.y, 0.0f))
                .scale(new Vector3f(scale.x, scale.y, 1.0f));

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {

        Matrix4f viewMatrix = new Matrix4f();
        Vector3f cameraPosition = camera.getPosition();
        Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        viewMatrix.identity()
                .rotateX((float)Math.toRadians(camera.getPitch()))
                .rotateY((float)Math.toRadians(camera.getYaw()))
                .translate(negativeCameraPosition);

        return viewMatrix;

    }

    public static float barryCentric(Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector2f position) {

        float det = (vec2.z - vec3.z) * (vec1.x - vec3.x) + (vec3.x - vec2.x) * (vec1.z - vec3.z);
        float l1 = ((vec2.z - vec3.z) * (position.x - vec3.x) + (vec3.x - vec2.x) * (position.y - vec3.z)) / det;
        float l2 = ((vec3.z - vec1.z) * (position.x - vec3.x) + (vec1.x - vec3.x) * (position.y - vec3.z)) / det;
        float l3 = 1.0f - l1 - l2;

        return l1 * vec1.y + l2 * vec2.y + l3 * vec3.y;

    }

}
