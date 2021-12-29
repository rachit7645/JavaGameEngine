package entities;

import org.joml.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);

	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.color = colour;
	}

	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.color = colour;
		this.attenuation = attenuation;
	}

	public Vector3f getAttenuation() {
		return attenuation;
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
