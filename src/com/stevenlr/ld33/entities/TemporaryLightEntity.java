package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.LightComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.TemporaryComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class TemporaryLightEntity extends Entity {

	public TemporaryLightEntity(float x, float y, float size, float r, float g, float b, float time) {
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, size, size));
		addComponent(LightComponent.class, new LightComponent(size, r, g, b));
		addComponent(TemporaryComponent.class, new TemporaryComponent(time));

		getAs(PhysicalComponent.class).moving = false;
	}
}
