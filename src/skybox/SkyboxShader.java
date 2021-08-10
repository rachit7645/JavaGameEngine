package skybox;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import renderEngine.MainGameLoop;
import shaders.ShaderProgram;
import toolBox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "shaderFiles/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "shaderFiles/skyboxFragmentShader.glsl";

	private static final float ROTATE_SPEED = 0.01f;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;

	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadFogColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r, g, b));
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30(0);
		matrix.m31(0);
		matrix.m32(0);
		rotation += ROTATE_SPEED * MainGameLoop.getDelta();
		matrix.rotateY((float) Math.toRadians(rotation));
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
