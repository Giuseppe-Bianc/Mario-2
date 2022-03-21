package components;

import glengine.Component;
import glengine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

	private Vector4f color;
	private Sprite sprite;

	private Transform lastTransform;
	private boolean isDirty = false;

	public SpriteRenderer(Vector4f color) {
		this.color = color;
		this.sprite = new Sprite(null);
		this.isDirty = true;
	}

	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.color = new Vector4f(1, 1, 1, 1);
		this.isDirty = true;
	}

	/**
	 * The start() function is called when the SpriteRenderer is started.
	 */
	@Override
	public void start() {
		this.lastTransform = gameObject.transform.copy();
	}

	/**
	 * This function is called once per frame
	 *
	 * @param dt The time in seconds since the last update.
	 */
	@Override
	public void update(float dt) {
		if (!this.lastTransform.equals(this.gameObject.transform)) {
			this.gameObject.transform.copy(this.lastTransform);
			isDirty = true;
		}
	}

	/**
	 * Returns the color of the light
	 *
	 * @return The color of the light.
	 */
	public Vector4f getColor() {
		return this.color;
	}

	/**
	 * Returns the texture of the sprite
	 *
	 * @return The texture of the sprite.
	 */
	public Texture getTexture() {
		return sprite.getTexture();
	}

	/**
	 * Returns the texture coordinates of the sprite
	 *
	 * @return The texture coordinates of the sprite.
	 */
	public Vector2f[] getTexCoords() {
		return sprite.getTexCoords();
	}

	/**
	 * Set the sprite of the entity
	 *
	 * @param sprite The sprite to be drawn.
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.isDirty = true;
	}

	/**
	 * This function sets the color of the light
	 *
	 * @param color The color of the light.
	 */
	public void setColor(Vector4f color) {
		if (!this.color.equals(color)) {
			this.isDirty = true;
			this.color.set(color);
		}
	}

	/**
	 * Returns true if the object has been modified since it was last saved
	 *
	 * @return The boolean value of the isDirty field.
	 */
	public boolean isDirty() {
		return this.isDirty;
	}

	/**
	 * This function sets the isDirty field to false
	 */
	public void setClean() {
		this.isDirty = false;
	}
}
