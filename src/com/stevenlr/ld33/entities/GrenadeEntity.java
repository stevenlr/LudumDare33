package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.GrenadeComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class GrenadeEntity extends Entity {

	public static final float SIZE = 0.4f;

	public GrenadeEntity(float x, float y, float dx, float dy, float timeLeft) {
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, SIZE, SIZE));
		addComponent(GrenadeComponent.class, new GrenadeComponent(timeLeft));

		getAs(PhysicalComponent.class).dx = dx;
		getAs(PhysicalComponent.class).dy = dy;
	}
}
