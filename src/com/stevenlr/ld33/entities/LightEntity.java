package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.LightComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class LightEntity extends Entity {

	public LightEntity(float x, float y, float size, float r, float g, float b) {
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, size, size));
		addComponent(LightComponent.class, new LightComponent(size, r, g, b));

		getAs(PhysicalComponent.class).moving = false;
	}
}
