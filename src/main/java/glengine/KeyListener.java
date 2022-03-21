package glengine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
	private static KeyListener instance;
	private final boolean[] keyPressed = new boolean[350];

	private KeyListener() {

	}

	/**
	 * The get() method creates a new instance of the KeyListener class if one doesn't already
	 * exist, and returns a reference to it
	 *
	 * @return The instance of the KeyListener class.
	 */
	public static KeyListener get() {
		if (KeyListener.instance == null) {
			KeyListener.instance = new KeyListener();
		}

		return KeyListener.instance;
	}

	/**
	 * When a key is pressed, the keyPressed array is set to true for that key. When a key is
	 * released, the keyPressed array is set to false for that key
	 *
	 * @param window   The window that received the event.
	 * @param key      The key that was pressed or released.
	 * @param scancode The scancode of the key.
	 * @param action   The action the key triggered.
	 * @param mods     A bitfield describing which modifier keys were held down.
	 */
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			get().keyPressed[key] = true;
		} else if (action == GLFW_RELEASE) {
			get().keyPressed[key] = false;
		}
	}

	/**
	 * Returns true if the specified key is pressed
	 *
	 * @param keyCode The key code of the key you want to check.
	 * @return A boolean value.
	 */
	public static boolean isKeyPressed(int keyCode) {
		return get().keyPressed[keyCode];
	}
}
