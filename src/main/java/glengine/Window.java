package glengine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private int width, height;
	private final String title;
	private long glfwWindow;
	private ImGuiLayer imguiLayer;

	public float r, g, b, a;

	private static Window window = null;

	private static Scene currentScene;

	private Window() {
		this.width = 960;
		this.height = 540;
		this.title = "Mario";
		r = b = g = a = 1;
	}

	/**
	 * This function changes the current scene to the scene with the given ID
	 *
	 * @param newScene The scene to switch to.
	 */
	public static void changeScene(int newScene) {
		switch (newScene) {
			case 0:
				currentScene = new LevelEditorScene();
				currentScene.init();
				currentScene.start();
				break;
			case 1:
				currentScene = new LevelScene();
				currentScene.init();
				currentScene.start();
				break;
			default:
				assert false : "Unknown scene '" + newScene + "'";
				break;
		}
	}

	/**
	 * The get() function returns the Window object if it exists, otherwise it creates a new Window
	 * object and returns it
	 *
	 * @return The Window object.
	 */
	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;
	}

	/**
	 * Returns the current scene
	 *
	 * @return The current scene.
	 */
	public static Scene getScene() {
		return get().currentScene;
	}

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		init();
		loop();
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		glfwTerminate();
		requireNonNull(glfwSetErrorCallback(null)).free();
	}

	/**
	 * Set the window hint to be invisible, resizable, and maximized
	 */
	private void setGlfwWindowHint() {
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
	}

	/**
	 * Set the callbacks for the window
	 */
	private void SetCallbacks() {
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
			Window.setWidth(newWidth);
			Window.setHeight(newHeight);
		});
	}

	/**
	 * Initialize the window and OpenGL, set the window hints, create the window, set the callbacks,
	 * create the OpenGL context, enable blending, set the blending function, show the window, and
	 * initialize the ImGui layer
	 */
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		glfwDefaultWindowHints();
		setGlfwWindowHint();

		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window.");
		}

		SetCallbacks();
		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);
		glfwShowWindow(glfwWindow);
		GL.createCapabilities();
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		this.imguiLayer = new ImGuiLayer(glfwWindow);
		this.imguiLayer.initImGui();

		Window.changeScene(0);
	}

	/**
	 * The main loop of the game.
	 */
	public void loop() {
		float beginTime = (float) glfwGetTime();
		float endTime;
		float dt = -1.0f;

		currentScene.load();

		while (!glfwWindowShouldClose(glfwWindow)) {
			glfwPollEvents();
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);

			if (dt >= 0) {
				currentScene.update(dt);
			}

			this.imguiLayer.update(dt, currentScene);
			glfwSwapBuffers(glfwWindow);

			endTime = (float) glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
		currentScene.saveExit();
	}

	/**
	 * Returns the width of the image
	 *
	 * @return The width of the screen.
	 */
	public static int getWidth() {
		return get().width;
	}

	/**
	 * Returns the height of the window
	 *
	 * @return The height of the screen.
	 */
	public static int getHeight() {
		return get().height;
	}

	/**
	 * This function sets the width of the rectangle
	 *
	 * @param newWidth The new width of the window.
	 */
	public static void setWidth(int newWidth) {
		get().width = newWidth;
	}

	/**
	 * Set the height of the current window
	 *
	 * @param newHeight The new height of the window.
	 */
	public static void setHeight(int newHeight) {
		get().height = newHeight;
	}
}
