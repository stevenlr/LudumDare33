package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.TemporaryComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class TemporaryEntitiesSystem {

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(TemporaryComponent.class);

		for (Entity e : entities) {
			TemporaryComponent time = e.getAs(TemporaryComponent.class);

			time.time -= dt;

			if (time.time <= 0) {
				Game.instance.entitySystem.removeEntity(e);
			}
		}
	}
}
