package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.components.EnnemyComponent;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class HunterEntity extends Entity {

	public HunterEntity(float x, float y) {
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, 1, 1));
		addComponent(EnnemyComponent.class, new EnnemyComponent());
		addComponent(LivingComponent.class, new LivingComponent(100));
	}
}
