package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.LightComponent;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class PlayerEntity extends Entity {

	public static final float SIZE = 1.5f;

	public PlayerEntity(float x, float y) {
		addComponent(PlayerComponent.class, new PlayerComponent());
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, SIZE, SIZE));
		addComponent(LightComponent.class, new LightComponent(10, 0.25f, 0.25f, 0.25f));
		addComponent(LivingComponent.class, new LivingComponent(1000));

		getAs(LightComponent.class).offsetX = SIZE / 2;
		getAs(LightComponent.class).offsetY = SIZE / 2;
	}
}
