package com.stevenlr.ld33.components;

import com.stevenlr.waffle2.entitysystem.Component;

public class BulletComponent extends Component {

	public float damage;
	public boolean friendly;
	public float initialSpeedX = 0;
	public float initialSpeedY = 0;

	public BulletComponent(float damage, boolean friendly) {
		this.damage = damage;
		this.friendly = friendly;
	}
}
