package com.stevenlr.ld33.entities;

import com.stevenlr.ld33.Animations;
import com.stevenlr.ld33.components.AnimatedTextureComponent;
import com.stevenlr.ld33.components.CoinComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.waffle2.entitysystem.Entity;

public class CoinEntity extends Entity {

	public CoinEntity(float x, float y) {
		addComponent(PhysicalComponent.class, new PhysicalComponent(x, y, 0.5f, 0.5f));
		addComponent(CoinComponent.class, new CoinComponent());
		addComponent(AnimatedTextureComponent.class, new AnimatedTextureComponent(Animations.coin.makeInstance()));
	}
}
