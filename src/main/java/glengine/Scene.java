package glengine;

import renderer.Renderer;
import imgui.ImGui;
import com.google.gson.*;

import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<>();
	protected GameObject activeGameObject = null;
	protected boolean levelLoaded = false;

	public Scene() {

	}

	/**
	 * This function is called when the game is first started
	 */
	public void init() {

	}

	/**
	 * For each game object in the gameObjects array, call the start method on the game object
	 */
	public void start() {
		for (GameObject go : gameObjects) {
			go.start();
			this.renderer.add(go);
		}
		isRunning = true;
	}

	/**
	 * Adds a game object to the scene
	 *
	 * @param go The GameObject to add to the scene.
	 */
	public void addGameObjectToScene(GameObject go) {
		if (!isRunning) {
			gameObjects.add(go);
		} else {
			gameObjects.add(go);
			go.start();
			this.renderer.add(go);
		}
	}

	/**
	 * "Update the game state."
	 * <p>
	 * The update function is called once per frame, and is where the game state is updated
	 *
	 * @param dt The time in seconds since the last update.
	 */
	public abstract void update(float dt);

	/**
	 * Returns the camera that is attached to the scene
	 *
	 * @return The camera object.
	 */
	public Camera camera() {
		return this.camera;
	}

	public void sceneImgui() {
		if (activeGameObject != null) {
			ImGui.begin("inspector");
			activeGameObject.imgui();
			ImGui.end();
		}

		imgui();
	}

	public void imgui() {

	}

	public void saveExit() {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentDeserializer())
				.registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
				.create();
		try {
			FileWriter writer = new FileWriter("Level.txt");
			writer.write(gson.toJson(this.gameObjects));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentDeserializer())
				.registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
				.create();

		String inFile = "";
		try {
			inFile = new String(Files.readAllBytes(java.nio.file.Paths.get("level.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!inFile.equals("")) {
			GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
			for (GameObject obj : objs) {
				addGameObjectToScene(obj);
			}
			this.levelLoaded = true;
		}
	}
}
