package glengine;

public class LevelScene extends Scene {
	public LevelScene() {
		System.out.println("Inside level scene");
		Window.get().r = Window.get().g = Window.get().b = 1;
	}

	/**
	 * This function is called once per frame
	 *
	 * @param dt The time in seconds since the last update.
	 */
	@Override
	public void update(float dt) {

	}
}
