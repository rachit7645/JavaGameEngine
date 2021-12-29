package renderEngine;

import entities.Inputs;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

	private long window;

	private boolean showCursor = false;
	private boolean wireframe = false;
	public String gameVersion = "Alpha 0.4";

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

		window = GLFW.glfwCreateWindow(1366, 768, "A Game By RachitCodes: " + gameVersion, GLFW.glfwGetPrimaryMonitor(), NULL);
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
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(window);

		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		GLFW.glfwSetWindowAspectRatio(window, 16, 9);
	}

	private void setupCallbacks() {

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				if (!showCursor) {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
					showCursor = true;
				} else {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
					showCursor = false;
				}
			}

			if (key == GLFW.GLFW_KEY_F2 && action == GLFW.GLFW_RELEASE) {
				if (!wireframe) {
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
					wireframe = true;
				} else {
					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
					wireframe = false;
				}
			}

			if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_PRESS)
				Inputs.setIsWPressed(true);
			else if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS)
				Inputs.setIsSPressed(true);

			if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS)
				Inputs.setIsAPressed(true);
			else if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS)
				Inputs.setIsDPressed(true);

			if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS)
				Inputs.setIsSpacebarPressed(true);

			if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_RELEASE)
				Inputs.setIsWPressed(false);
			else if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_RELEASE)
				Inputs.setIsSPressed(false);

			if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_RELEASE)
				Inputs.setIsAPressed(false);
			else if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_RELEASE)
				Inputs.setIsDPressed(false);

			if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_RELEASE)
				Inputs.setIsSpacebarPressed(false);
		});

		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> MasterRenderer.setToResize(true));

		GLFW.glfwSetCursorPosCallback(window, (window, xPos, yPos) -> {
			Inputs.setOldCursorXPos(Inputs.getCursorXPos());
			Inputs.setOldCursorYPos(Inputs.getCursorYPos());
			Inputs.setCursorXPos((float) xPos);
			Inputs.setCursorYPos((float) yPos);
			Inputs.setToMoveCamera(true);
		});

		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
		});

		GLFW.glfwSetScrollCallback(window, (window, xOffset, yOffset) -> {
			Inputs.setMouseScrollY((float) yOffset);
			Inputs.setToMoveCamera(true);
		});

	}

}
