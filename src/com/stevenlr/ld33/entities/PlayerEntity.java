package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class PlayerEntity extends Entity {

	public static final float SIZE = 0.7f;

	public PlayerEntity(float x, float y) {
		addComponent(PlayerComponent.class, new PlayerComponent());
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, SIZE, SIZE));
	}
}
