package glengine;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

	private static final String PR = "assets/", OBJP = "Object ", PPNG = ".png";
	private static final String PRT = PR + "images/";
	private static final String SHPT = PR + "shaders/default.glsl";
	private static final int UNN = 100, UND = UNN * 2, UNQ = UNN * 4, SPD = 16;

	private GameObject obj1;
	private Spritesheet sprites;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		loadResources();

		this.camera = new Camera(new Vector2f(-250, 0));

		sprites = AssetPool.getSpritesheet(PRT + "spritesheet.png");

		obj1 = new GameObject(OBJP + "1", new Transform(new Vector2f(200, UNN),
				new Vector2f(256, 256)), 2);
		obj1.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture(PRT + "blendImage1.png")
		)));
		this.addGameObjectToScene(obj1);

		GameObject obj2 = new GameObject(OBJP + "2", new Transform(new Vector2f(400, UNN),
				new Vector2f(256, 256)), 3);
		obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture(PRT + "blendImage2" +
				".png"))));
		this.addGameObjectToScene(obj2);
	}

	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");

		AssetPool.addSpritesheet(PRT + "spritesheet.png",
				new Spritesheet(AssetPool.getTexture(PRT + "spritesheet.png"),
						16, 16, 26, 0));
	}

	@Override
	public void update(float dt) {

		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

		this.renderer.render();
	}
}
