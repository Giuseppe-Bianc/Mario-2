package renderer;

import components.SpriteRenderer;
import glengine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
	private final int MAX_BATCH_SIZE = 1000;
	private final List<RenderBatch> batches;

	public Renderer() {
		this.batches = new ArrayList<>();
	}

	/**
	 * Adds a sprite to the list of sprites to be rendered
	 *
	 * @param go The GameObject that you want to add to the list.
	 */
	public void add(GameObject go) {
		SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
		if (spr != null) {
			add(spr);
		}
	}

	/**
	 * If the sprite's zIndex is different from the zIndex of the last batch, create a new batch. If
	 * the sprite's texture is already in the batch, add it to the batch. If the batch is full,
	 * create a new batch
	 *
	 * @param sprite The sprite to add to the batch.
	 */
	private void add(SpriteRenderer sprite) {
		boolean added = false;
		int spriteZIndex = sprite.gameObject.zIndex();
		for (RenderBatch batch : batches) {
			if (batch.hasRoom() && batch.zIndex() == spriteZIndex) {
				Texture tex = sprite.getTexture();
				if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}

		if (!added) {
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spriteZIndex);
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
			Collections.sort(batches);
		}
	}

	/**
	 * For each batch, render the batch
	 */
	public void render() {
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}
}
