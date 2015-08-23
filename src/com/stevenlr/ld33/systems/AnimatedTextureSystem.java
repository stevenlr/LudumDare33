package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.AnimatedTextureComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Renderer;

public class AnimatedTextureSystem {

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(AnimatedTextureComponent.class);

		for (Entity e : entities) {
			e.getAs(AnimatedTextureComponent.class).animation.update(dt);
		}
	}

	public void draw(Renderer r) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(AnimatedTextureComponent.class, PhysicalComponent.class);

		r.setDrawParameters(false, false, true);
		r.setBlending(Renderer.ALPHA);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);

			r.drawTile(phys.x, phys.y, phys.sx, phys.sy, e.getAs(AnimatedTextureComponent.class).animation.getTextureId());
		}

		r.resetDrawParameters();
	}
}
