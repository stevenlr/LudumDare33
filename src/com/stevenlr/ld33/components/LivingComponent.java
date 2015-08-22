package com.stevenlr.ld33.components;

import com.stevenlr.waffle2.entitysystem.Component;

public class LivingComponent extends Component {

	public float maxLife;
	public float currentLife;

	public LivingComponent(float maxLife) {
		this.maxLife = maxLife;
		this.currentLife = maxLife;
	}
}
