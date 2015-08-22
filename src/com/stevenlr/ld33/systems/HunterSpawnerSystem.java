package com.stevenlr.ld33.systems;

import java.util.List;
import java.util.Random;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.HunterSpawnerComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.entities.HunterEntity;
import com.stevenlr.waffle2.entitysystem.Entity;

public class HunterSpawnerSystem {

	private Random r = new Random();

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(HunterSpawnerComponent.class, PhysicalComponent.class);

		for(Entity e : entities) {
			HunterSpawnerComponent spawner = e.getAs(HunterSpawnerComponent.class);
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);

			spawner.lastSpawn += dt;

			if (spawner.lastSpawn > 4.25 + (r.nextFloat() * 2 - 1) * 0.7f) {
				Entity hunter = new HunterEntity(phys.x, phys.y);
				PhysicalComponent hunterPhys = hunter.getAs(PhysicalComponent.class);

				hunterPhys.dx = r.nextFloat() * 7;
				hunterPhys.dy = r.nextFloat() * 7;
				hunterPhys.bounceFactor = 0.95f;
				hunterPhys.onFloor = false;

				spawner.lastSpawn = 0;
			}
		}
	}
}
