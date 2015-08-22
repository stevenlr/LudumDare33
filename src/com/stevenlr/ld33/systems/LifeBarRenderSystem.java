package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Renderer;

public class LifeBarRenderSystem {

	public void draw(Renderer r) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(LivingComponent.class, PhysicalComponent.class);

		r.setDrawParameters(false, false, false);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);
			LivingComponent life = e.getAs(LivingComponent.class);

			if (!e.hasComponent(PlayerComponent.class)) {
				r.fillRect(phys.x - 0.5f, phys.y + phys.sy + 0.1f, 1, 0.2f, 0, 0, 0, 1);
				r.fillRect(phys.x - 0.5f, phys.y + phys.sy + 0.07f, life.currentLife / life.maxLife, 0.2f, 1, 0, 0, 1);
			}
		}
	}
}
