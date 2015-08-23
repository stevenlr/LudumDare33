package com.stevenlr.ld33.systems;

import java.util.List;
import java.util.Random;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.BulletComponent;
import com.stevenlr.ld33.components.EnnemyComponent;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.ld33.entities.BulletEntity;
import com.stevenlr.ld33.entities.CoinEntity;
import com.stevenlr.ld33.entities.TemporaryLightEntity;
import com.stevenlr.waffle2.entitysystem.Entity;
import org.joml.Vector2f;

public class HunterLogicSystem {

	private static final float BULLET_SPEED = 20;

	private Random rand = new Random();

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(LivingComponent.class, EnnemyComponent.class, PhysicalComponent.class);
		List<Entity> players = Game.instance.entitySystem.getAllEntitiesPossessing(PlayerComponent.class, PhysicalComponent.class);
		Entity playerEntity = null;

		if (players.size() == 1) {
			playerEntity = players.get(0);
		}

		for (Entity e : entities) {
			PhysicalComponent ep = e.getAs(PhysicalComponent.class);

			if (e.getAs(LivingComponent.class).currentLife <= 0) {
				Game.instance.entitySystem.removeEntity(e);
				Entity c = new CoinEntity(ep.x, ep.y);
				c.getAs(PhysicalComponent.class).dx = ep.dx * 5;
				c.getAs(PhysicalComponent.class).dy = ep.dy * 5;
				playerEntity.getAs(PlayerComponent.class).score++;
				continue;
			}

			if (playerEntity != null) {
				EnnemyComponent en = e.getAs(EnnemyComponent.class);
				PhysicalComponent pp = playerEntity.getAs(PhysicalComponent.class);

				en.lastShot += dt;

				if (en.lastShot > 1.5 + rand.nextFloat() * 0.7f) {
					en.lastShot = 0;

					Vector2f dir = new Vector2f(ep.x - pp.x, ep.y - pp.y);
					dir.normalize();

					Entity bullet = new BulletEntity(35, ep.x, ep.y, -dir.x * BULLET_SPEED, -dir.y * BULLET_SPEED);
					bullet.getAs(BulletComponent.class).friendly = false;

					new TemporaryLightEntity(ep.x, ep.y, 10, 0.3f, 0.3f, 0.3f, 0.05f);
				}
			}
		}
	}
}
