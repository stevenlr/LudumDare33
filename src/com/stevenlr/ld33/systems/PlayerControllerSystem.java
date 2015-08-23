package com.stevenlr.ld33.systems;

import java.util.List;
import java.util.Random;

import com.stevenlr.ld33.Config;
import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.CoinComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.ld33.entities.BulletEntity;
import com.stevenlr.ld33.entities.GrenadeEntity;
import com.stevenlr.ld33.entities.TemporaryLightEntity;
import com.stevenlr.ld33.level.Level;
import com.stevenlr.waffle2.entitysystem.Entity;
import org.joml.Vector2f;

public class PlayerControllerSystem {

	private static final float SPEED = 140;
	private static final float BULLET_SPEED = 30;
	private static final float GRENADE_SPEED = 75;

	private Random r = new Random();

	public void update(float dt, int level) {
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

		e.getAs(PlayerComponent.class).rotation = (float) Math.atan2((Game.HEIGHT + Level.INV_HEIGHT) / 2 - cursor.y, Game.WIDTH / 2 - cursor.x);

		player.lastShot += dt;

		if (Game.instance.keyboard.wasPressed(Config.KEY_RIFFLE)) {
			player.selectedWeapon = 0;
		}

		if (Game.instance.keyboard.wasPressed(Config.KEY_SHOTGUN)) {
			player.selectedWeapon = 1;
		}

		if (player.selectedWeapon == 0 && Game.instance.mouse.isDown(0) && player.lastShot > player.riffleCooldown) {
			player.lastShot = 0;
			float spread = (r.nextFloat() * 2 - 1) * 0.07f;
			new BulletEntity(player.riffleDamage, phys.x, phys.y,
					(float) -Math.cos(player.rotation + spread) * BULLET_SPEED, (float) -Math.sin(player.rotation + spread) * BULLET_SPEED);

			new TemporaryLightEntity(phys.x, phys.y, 12, 0.4f, 0.4f, 0.2f, 0.05f);
			Game.instance.audioRegistry.getSource("shoot").play(0.5f, r.nextFloat() * 0.3f + 1);
		}

		if (player.selectedWeapon == 1 && Game.instance.mouse.isDown(0) && player.lastShot > player.sgCooldown) {
			player.lastShot = 0;

			for (int i = 0; i < 9; ++i) {
				float spread = (r.nextFloat() * 2 - 1) * 0.2f;
				new BulletEntity(player.sgDamage, phys.x, phys.y,
						(float) -Math.cos(player.rotation + spread) * BULLET_SPEED, (float) -Math.sin(player.rotation + spread) * BULLET_SPEED);

				new TemporaryLightEntity(phys.x, phys.y, 12, 0.4f, 0.4f, 0.2f, 0.05f);
			}

			Game.instance.audioRegistry.getSource("shoot").play();
		}

		if (Game.instance.mouse.wasPressed(1) && player.nbGrenades > 0) {
			player.nbGrenades--;

			new GrenadeEntity(phys.x, phys.y,
					(float) -Math.cos(player.rotation) * GRENADE_SPEED, (float) -Math.sin(player.rotation) * GRENADE_SPEED, 1.5f);
		}

		List<Entity> coins = Game.instance.entitySystem.getAllEntitiesPossessing(CoinComponent.class, PhysicalComponent.class);

		for (Entity c : coins) {
			PhysicalComponent cphys  = c.getAs(PhysicalComponent.class);
			Vector2f diff = new Vector2f(cphys.x - phys.x, cphys.y - phys.y);
			float dist = diff.lengthSquared();

			if (dist < 4) {
				Game.instance.entitySystem.removeEntity(c);
				player.gold += r.nextInt(2 + level / 2) + 1;
				Game.instance.audioRegistry.getSource("coin").play();
			}
		}
	}
}
