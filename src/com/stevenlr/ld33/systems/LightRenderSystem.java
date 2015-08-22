package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.LightComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Renderer;

public class LightRenderSystem {

	private int _texture;

	public LightRenderSystem() {
		_texture = Game.instance.textureRegistry.getTexture("light");
	}

	public void draw(Renderer r) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(LightComponent.class, PhysicalComponent.class);

		r.setBlending(Renderer.ADDITIVE);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);
			LightComponent light = e.getAs(LightComponent.class);

			r.drawTile(phys.x - light.size / 2 + light.offsetX, phys.y - light.size / 2 + light.offsetY, light.size, light.size, light.r, light.g, light.b, 1, _texture);
		}

		r.doRenderPass();
	}
}
