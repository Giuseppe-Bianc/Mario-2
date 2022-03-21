package components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {

	private final Texture texture;
	private final Vector2f[] texCoords;

	public Sprite(Texture texture) {
		this.texture = texture;
		Vector2f[] texCoords = {
				new Vector2f(1, 1),
				new Vector2f(1, 0),
				new Vector2f(0, 0),
				new Vector2f(0, 1)
		};
		this.texCoords = texCoords;
	}

	public Sprite(Texture texture, Vector2f[] texCoords) {
		this.texture = texture;
		this.texCoords = texCoords;
	}

	/**
	 * Returns the texture of the sprite
	 *
	 * @return The texture of the sprite.
	 */
	public Texture getTexture() {
		return this.texture;
	}

	/**
	 * Returns the texture coordinates for the vertices of this shape
	 *
	 * @return The texture coordinates for the vertices.
	 */
	public Vector2f[] getTexCoords() {
		return this.texCoords;
	}
}
