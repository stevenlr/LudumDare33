package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.Particles;
import com.stevenlr.ld33.components.GrenadeComponent;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.entities.TemporaryLightEntity;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Renderer;
import org.joml.Vector2f;

public class GrenadeSystem {

	private int _texture;

	private static final float RADIUS = 6;
	private static final float RADIUS_SQ = RADIUS * RADIUS;
	private static final float DAMAGE = 200;

	public GrenadeSystem() {
		_texture = Game.instance.textureRegistry.getTexture("grenade");
	}

	public void update(float dt) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(GrenadeComponent.class, PhysicalComponent.class);
		List<Entity> damageables = Game.instance.entitySystem.getAllEntitiesPossessing(LivingComponent.class, PhysicalComponent.class);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);
			GrenadeComponent grenade = e.getAs(GrenadeComponent.class);

			grenade.timeLeft -= dt;

			if (grenade.timeLeft <= 0) {
				Game.instance.entitySystem.removeEntity(e);
				new TemporaryLightEntity(phys.x, phys.y, 20, 1f, 0.9f, 0.7f, 0.07f);
				Game.instance.audioRegistry.getSource("explosion").play();
				Particles.spawnExplosion(phys.x, phys.y);

				for (Entity d : damageables) {
					PhysicalComponent pos = d.getAs(PhysicalComponent.class);
					LivingComponent life = d.getAs(LivingComponent.class);
					Vector2f diff = new Vector2f(pos.x - phys.x, pos.y - phys.y);
					float dist = diff.lengthSquared();

					if (dist < RADIUS_SQ) {
						float damage = DAMAGE * (1 - dist / RADIUS_SQ);
						life.currentLife = Math.max(0, life.currentLife - damage);
					}
				}
			}
		}
	}

	public void draw(Renderer r) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(GrenadeComponent.class, PhysicalComponent.class);

		r.setDrawParameters(false, false, true);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);

			r.drawTile(phys.x, phys.y, phys.sx, phys.sy, _texture);
		}

		r.resetDrawParameters();
	}
}
