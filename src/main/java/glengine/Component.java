package glengine;

public abstract class Component {

	public transient GameObject gameObject = null;

	/**
	 * This function is called when the game is started
	 */
	public void start() {

	}

	/**
	 * The update function is called once per frame, and is where the game state is updated
	 *
	 * @param dt The time in seconds since the last update.
	 */
	public void update(float dt) {

	}

	public void imgui() {

	}
}
