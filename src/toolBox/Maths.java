package toolBox;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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

}
