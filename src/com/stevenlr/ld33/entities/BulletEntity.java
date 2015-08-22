package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.BulletComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class BulletEntity extends Entity {

	public BulletEntity(float damage, float x, float y, float dx, float dy) {
		addComponent(BulletComponent.class, new BulletComponent(damage, true));
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, 0.1f, 0.1f));

		getAs(PhysicalComponent.class).onFloor = false;
		getAs(PhysicalComponent.class).dx = dx;
		getAs(PhysicalComponent.class).dy = dy;
		getAs(BulletComponent.class).initialSpeedX = dx;
		getAs(BulletComponent.class).initialSpeedY = dy;
	}
}
