package entities;

import org.joml.Vector3f;

public class Camera implements Keys{

    private Vector3f position = new Vector3f(100,15,50);
    private float pitch = 0;
    private float yaw = 0;
    private float roll = 0;

    public static void move(int KEY) {
        /*switch(KEY) {
            case KEY_W:
                position.z -= 0.5f;
                break;
            case KEY_A:
                position.x -= 0.5f;
                break;
            case KEY_S:
                position.z += 0.5f;
                break;
            case KEY_D:
                position.x += 0.5f;
                break;
            case KEY_SPACEBAR:
                position.y += 0.5f;
                break;
            case KEY_LSHIFT:
                position.y -= 0.5f;
                break;
        }*/
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
