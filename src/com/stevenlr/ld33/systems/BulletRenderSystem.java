package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.BulletComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Renderer;

public class BulletRenderSystem {

	public void draw(Renderer r) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(BulletComponent.class, PhysicalComponent.class);

		r.setDrawParameters(false, false, true);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);
			r.fillRect(phys.x, phys.y, phys.sx, phys.sy, 1, 1, 1, 1);
		}
	}
}
