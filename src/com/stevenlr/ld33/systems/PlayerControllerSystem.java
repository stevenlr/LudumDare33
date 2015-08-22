package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Config;
import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.waffle2.entitysystem.Entity;
import org.joml.Vector2f;

public class PlayerControllerSystem {

	private static final float SPEED = 80;

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(PlayerComponent.class, PhysicalComponent.class);

		if (entities.size() != 1) {
			throw new RuntimeException("Too many player! :o");
		}

		Entity e = entities.get(0);
		PhysicalComponent phys = e.getAs(PhysicalComponent.class);
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
	}
}
