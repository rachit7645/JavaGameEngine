package entities;

import org.joml.Vector3f;

public class Light {

    private Vector3f position;
    private Vector3f color;

    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.color = colour;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
