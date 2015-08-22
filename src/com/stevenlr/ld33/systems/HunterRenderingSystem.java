package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.EnnemyComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Renderer;

public class HunterRenderingSystem {

	private int _texture;
	private int _shadow;

	public HunterRenderingSystem() {
		_texture = Game.instance.textureRegistry.getTexture("hunter");
		_shadow = Game.instance.textureRegistry.getTexture("shadow");
	}

	public void draw(Renderer r) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(EnnemyComponent.class, PhysicalComponent.class);
		List<Entity> players = Game.instance.entitySystem.getAllEntitiesPossessing(PlayerComponent.class, PhysicalComponent.class);
		PhysicalComponent playerPhys = null;

		if (players.size() == 1) {
			playerPhys = players.get(0).getAs(PhysicalComponent.class);
		}

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);

			float rotation = 0;

			if (playerPhys != null) {
				rotation = (float) Math.atan2(-playerPhys.y + phys.y, -playerPhys.x + phys.x);
			}

			r.push();
			r.translate(phys.x, phys.y);
			r.rotate(rotation);
			r.drawTile(0, 0, phys.sx * 3, phys.sy * 3, 1, 1, 1, 0.3f, _shadow);
			r.drawTile(0, 0, phys.sx, phys.sy, _texture);
			r.pop();
		}
	}
}
