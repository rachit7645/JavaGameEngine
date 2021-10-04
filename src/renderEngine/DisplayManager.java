package renderEngine;

import entities.Camera;
import entities.Inputs;
import entities.Keys;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager implements Keys {

	private long window;

	private boolean iterator = false;
	private boolean iterator2 = false;
	public String gameVersion="Alpha 0.4";

	public void run() {
		System.out.println("[INFO] :Starting, Using LWJGL " + Version.getVersion());
		System.out.println("[INFO] :Using Maven, JDK Version 1.8");

		System.out.println("[INFO] :Initialising...");
		init();

		System.out.println("[INFO] :Starting Loop");
		MainGameLoop mainGameLoop = new MainGameLoop(window);
		mainGameLoop.loop();

		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		System.out.println("\n[INFO] :Window Destroyed");
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
		System.out.println("[INFO] :GLFW Terminated");
	}

	private void init() {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);

		window = GLFW.glfwCreateWindow(1366, 768, "A Game By RachitCodes: "+gameVersion, GLFW.glfwGetPrimaryMonitor(), NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		setupCallbacks();

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window,
					(vidMode.width() - pWidth.get(0)) / 2,
					(vidMode.height() - pHeight.get(0)) / 2);
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(window);

		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		GLFW.glfwSetWindowAspectRatio(window, 16, 9);
	}

	private void setupCallbacks() {

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				if (!iterator) {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
					iterator = true;
				} else {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
					iterator = false;
				}
			} else if(key == GLFW.GLFW_KEY_F2 && action == GLFW.GLFW_RELEASE) {
				if(!iterator2) {
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
					iterator2 = true;
				} else {
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
					iterator2 = false;
				}
			} else if (key == GLFW.GLFW_KEY_W) {
				Inputs.setKey(Keys.KEY_W);
				Inputs.setToMovePlayer(true);
			} else if (key == GLFW.GLFW_KEY_A) {
				Inputs.setKey(Keys.KEY_A);
				Inputs.setToMovePlayer(true);
			} else if (key == GLFW.GLFW_KEY_S) {
				Inputs.setKey(Keys.KEY_S);
				Inputs.setToMovePlayer(true);
			} else if (key == GLFW.GLFW_KEY_D) {
				Inputs.setKey(Keys.KEY_D);
				Inputs.setToMovePlayer(true);
			} else if (key == GLFW.GLFW_KEY_SPACE) {
				Inputs.setKey(Keys.KEY_SPACEBAR);
				Inputs.setToMovePlayer(true);
			}
		});

		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> MasterRenderer.setToResize(true));

		GLFW.glfwSetCursorPosCallback(window, (window, xPos, yPos) -> {
			Camera.setOldCursorXPos(Camera.getCursorXPos());
			Camera.setOldCursorYPos(Camera.getCursorYPos());
			Camera.setCursorXPos((float) xPos);
			Camera.setCursorYPos((float) yPos);
			Inputs.setToMoveCamera(true);
		});

		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action==GLFW.GLFW_PRESS)
				Camera.setIsMouseButton1Down(true);
			else if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action==GLFW.GLFW_RELEASE)
				Camera.setIsMouseButton1Down(false);
		});

		GLFW.glfwSetScrollCallback(window, (window, xOffset, yOffset) -> {
			Camera.setMouseScrollY((float) yOffset);
			Inputs.setToMoveCamera(true);
		});

	}

}
