package glengine;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;
import imgui.ImGui;

public class LevelEditorScene extends Scene {

	private static final String PR = "assets/", OBJP = "Object ", PPNG = ".png";
	private static final String SHPT = PR + "shaders/default.glsl";
	private static final String PRT = PR + "images/", BLN = PRT + "blendImage1" + PPNG;
	private static final String BLN2 = PRT + "blendImage2" + PPNG;
	private static final int UNN = 100, UND = UNN * 2, UNF = UND + 56, UNQ = UNN * 4, SD = 16;

	private GameObject obj1;
	private Spritesheet sprites;
	private SpriteRenderer obj1Sprite;

	public LevelEditorScene() {

	}

	/**
	 * Loads the resources and creates the game objects
	 */
	@Override
	public void init() {
		loadResources();
		this.camera = new Camera(new Vector2f(-250, 0));
		if (levelLoaded) {
			return;
		}
		sprites = AssetPool.getSpritesheet(PRT + "spritesheet.png");

		obj1 = new GameObject(OBJP + "1", new Transform(new Vector2f(UND, UNN),
				new Vector2f(UNF, UNF)), 2);
		obj1Sprite = new SpriteRenderer();
		obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
		obj1.addComponent(obj1Sprite);
		this.addGameObjectToScene(obj1);
		this.activeGameObject = obj1;

		SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
		Sprite obj2Sprite = new Sprite();
		obj2Sprite.setTexture(AssetPool.getTexture(BLN2));
		obj2SpriteRenderer.setSprite(obj2Sprite);
		GameObject obj2 = new GameObject(OBJP + "2", new Transform(new Vector2f(UNQ, UNN),
				new Vector2f(UNF, UNF)), 3);
		obj2.addComponent(obj2SpriteRenderer);
		this.addGameObjectToScene(obj2);
	}

	/**
	 * Loads all the resources needed for the game
	 */
	private void loadResources() {
		AssetPool.getShader(SHPT);

		AssetPool.addSpritesheet(PRT + "spritesheet.png",
				new Spritesheet(AssetPool.getTexture(PRT + "spritesheet.png"),
						SD, SD, SD + 10, 0));
	}

	/**
	 * This function loops through all the game objects and calls their update function
	 *
	 * @param dt The time in seconds since the last update.
	 */
	@Override
	public void update(float dt) {

		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

		this.renderer.render();
	}

	@Override
	public void imgui() {
		ImGui.begin("test window");
		ImGui.text("Some random text");
		ImGui.end();
	}
}
