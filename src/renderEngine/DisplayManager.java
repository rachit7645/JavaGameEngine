package renderEngine;

import entities.Camera;
import entities.Keys;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager implements Keys {

	private long window;
	public String gameVersion="Beta 0.2";

	public void run() {
		System.out.println("[INFO] :Starting, Using LWJGL " + Version.getVersion());
		System.out.println("[INFO] :Using Maven, JDK Version 1.8");

		System.out.println("[INFO] :Initialising...");
		init();

		System.out.println("[INFO] :Starting Loop");
		MainGameLoop.loop(window);

		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		System.out.println("[INFO] :Window Destroyed");
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
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);

		window = GLFW.glfwCreateWindow(800, 600, "A Game By RachitCodes: "+gameVersion, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				GLFW.glfwSetWindowShouldClose(window, true);
			}else if (key == GLFW.GLFW_KEY_W) {
				MainGameLoop.setKey(Keys.KEY_W);
				MainGameLoop.setToMovePlayer(true);
			}else if (key == GLFW.GLFW_KEY_A) {
				MainGameLoop.setKey(Keys.KEY_A);
				MainGameLoop.setToMovePlayer(true);
			}else if (key == GLFW.GLFW_KEY_S) {
				MainGameLoop.setKey(Keys.KEY_S);
				MainGameLoop.setToMovePlayer(true);
			}else if (key == GLFW.GLFW_KEY_D) {
				MainGameLoop.setKey(Keys.KEY_D);
				MainGameLoop.setToMovePlayer(true);
			}else if (key == GLFW.GLFW_KEY_SPACE) {
				MainGameLoop.setKey(Keys.KEY_SPACEBAR);
				MainGameLoop.setToMovePlayer(true);
			}else if (key == GLFW.GLFW_KEY_LEFT_SHIFT) {
			}
		});
		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> MasterRenderer.setToResize(true));

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(window);
	}
}