package com.stevenlr.ld33.systems;

import java.util.List;

import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.level.Level;
import com.stevenlr.waffle2.entitysystem.Entity;

public class PhysicalMovementSystem {

	private final float FRICTION = 0.8f;

	public void update(float dt, Level level) {
		List<Entity> entities = Game.instance.entitySystem.getAllEntitiesPossessing(PhysicalComponent.class);

		for (Entity e : entities) {
			PhysicalComponent phys = e.getAs(PhysicalComponent.class);

			if (!phys.moving) {
				continue;
			}

			phys.dx += phys.ax * dt;
			phys.dy += phys.ay * dt;

			if (phys.onFloor) {
				phys.dx *= FRICTION;
				phys.dy *= FRICTION;
			}

			tryMove(phys, phys.dx * dt, 0, level);
			tryMove(phys, 0, phys.dy * dt, level);

			phys.x = Math.round(phys.x * 100) / 100.0f;
			phys.y = Math.round(phys.y * 100) / 100.0f;
		}
	}

	public void tryMove(PhysicalComponent phys, float dx, float dy, Level level) {
		float x = phys.x + dx;
		float y = phys.y + dy;
		final float epsilon = 0.001f;

		int x0 = (int) Math.floor(x + epsilon - phys.sx / 2);
		int y0 = (int) Math.floor(y + epsilon - phys.sy / 2);
		int x1 = (int) Math.floor(x + phys.sx / 2 - epsilon);
		int y1 = (int) Math.floor(y + phys.sy / 2 - epsilon);

		for (int ty = y0; ty <= y1; ++ty) {
			for (int tx = x0; tx <= x1; ++tx) {
				if (level.isSolidAt(tx, ty)) {
					float penetrationX = 0;
					float penetrationY = 0;

					if (dx > 0) {
						penetrationX = x + phys.sx / 2 - tx + epsilon;
					} else if (dx < 0) {
						penetrationX = x - tx - 1 + epsilon - phys.sx / 2;
					}

					if (dy > 0) {
						penetrationY = y + phys.sy / 2 - ty + epsilon;
					} else if (dy < 0) {
						penetrationY = y - ty - 1 + epsilon - phys.sy / 2;
					}

					phys.x = x - penetrationX;
					phys.y = y - penetrationY;

					if (penetrationX != 0) {
						phys.dx *= -phys.bounceFactor;
					}

					if (penetrationY != 0) {
						phys.dy *= -phys.bounceFactor;
					}

					return;
				}
			}
		}

		phys.x = x;
		phys.y = y;
	}
}
