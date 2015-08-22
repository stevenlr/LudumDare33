package com.stevenlr.ld33.systems;

import java.util.List;
import java.util.Random;

import com.stevenlr.ld33.Config;
import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.ld33.entities.BulletEntity;
import com.stevenlr.ld33.entities.TemporaryLightEntity;
import com.stevenlr.waffle2.entitysystem.Entity;
import org.joml.Vector2f;

public class PlayerControllerSystem {

	private static final float SPEED = 140;
	private static final float BULLET_SPEED = 30;

	private Random r = new Random();

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(PlayerComponent.class, PhysicalComponent.class);

		if (entities.size() != 1) {
			throw new RuntimeException("Too many players! :o");
		}

		Entity e = entities.get(0);
		PhysicalComponent phys = e.getAs(PhysicalComponent.class);
		PlayerComponent player = e.getAs(PlayerComponent.class);
		Vector2f acc = new Vector2f(0, 0);

		if (Game.instance.keyboard.isDown(Config.KEY_FORWARD)) {
			acc.y += 1;
		}

		if (Game.instance.keyboard.isDown(Config.KEY_BACKWARD)) {
			acc.y -= 1;
		}

		if (Game.instance.keyboard.isDown(Config.KEY_LEFT)) {
			acc.x -= 1;
		}

		if (Game.instance.keyboard.isDown(Config.KEY_RIGHT)) {
			acc.x += 1;
		}

		if (acc.lengthSquared() > 0) {
			acc.normalize().mul(SPEED);
		}

		phys.ax = acc.x;
		phys.ay = acc.y;

		Vector2f cursor = Game.instance.mouse.getMousePosition();

		e.getAs(PlayerComponent.class).rotation = (float) Math.atan2(Game.HEIGHT / 2 - cursor.y, Game.WIDTH / 2 - cursor.x);

		player.lastShot += dt;

		if (Game.instance.mouse.isDown(0) && player.lastShot > 0.1f) {
			player.lastShot = 0;
			float spread = (r.nextFloat() * 2 - 1) * 0.07f;
			new BulletEntity(35, phys.x, phys.y,
					(float) -Math.cos(player.rotation + spread) * BULLET_SPEED, (float) -Math.sin(player.rotation + spread) * BULLET_SPEED);

			new TemporaryLightEntity(phys.x, phys.y, 12, 0.4f, 0.4f, 0.2f, 0.05f);
		}
	}
}
