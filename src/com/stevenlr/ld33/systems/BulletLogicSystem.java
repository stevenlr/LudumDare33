package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.BulletComponent;
import com.stevenlr.ld33.components.EnnemyComponent;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class BulletLogicSystem {

	public void update(float dt) {
		List<Entity> bullets = Game.instance.entitySystem.getAllEntitiesPossessing(BulletComponent.class, PhysicalComponent.class);
		List<Entity> ennemies = Game.instance.entitySystem.getAllEntitiesPossessing(LivingComponent.class, EnnemyComponent.class, PhysicalComponent.class);
		List<Entity> friendlies = Game.instance.entitySystem.getAllEntitiesPossessing(LivingComponent.class, PlayerComponent.class, PhysicalComponent.class);

		for (Entity b : bullets) {
			BulletComponent bullet = b.getAs(BulletComponent.class);
			PhysicalComponent phys = b.getAs(PhysicalComponent.class);

			if (bullet.initialSpeedX != phys.dx || bullet.initialSpeedY != phys.dy) {
				Game.instance.entitySystem.removeEntity(b);
				continue;
			}

			if (bullet.friendly) {
				for (Entity e : ennemies) {
					PhysicalComponent ephys = e.getAs(PhysicalComponent.class);
					LivingComponent life = e.getAs(LivingComponent.class);

					if (overlap(ephys, phys)) {
						Game.instance.entitySystem.removeEntity(b);
						life.currentLife = Math.max(0, life.currentLife - bullet.damage);
						break;
					}
				}
			} else {
				for (Entity e : friendlies) {
					PhysicalComponent ephys = e.getAs(PhysicalComponent.class);
					LivingComponent life = e.getAs(LivingComponent.class);

					if (overlap(ephys, phys)) {
						Game.instance.entitySystem.removeEntity(b);
						life.currentLife = Math.max(0, life.currentLife - bullet.damage);
						break;
					}
				}
			}
		}
	}

	private static boolean overlap(PhysicalComponent p1, PhysicalComponent p2) {
		if (p1.x + p1.sx / 2 < p2.x - p2.sx / 2 || p1.x - p1.sx / 2 > p2.x + p2.sx) {
			return false;
		}

		if (p1.y + p1.sy < p2.y - p2.sy / 2 || p1.y - p1.sy / 2 > p2.y + p2.sy) {
			return false;
		}

		return true;
	}
}
