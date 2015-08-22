package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.HunterSpawnerComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class HunterSpawnerEntity extends Entity {

	public HunterSpawnerEntity(float x, float y) {
		addComponent(HunterSpawnerComponent.class, new HunterSpawnerComponent());
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, 1, 1));

		getAs(PhysicalComponent.class).moving = false;
	}
}
