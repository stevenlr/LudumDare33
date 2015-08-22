package com.stevenlr.ld33.components;

import com.stevenlr.waffle2.entitysystem.Component;

public class PhysicalComponent extends Component {

	public float x = 0;
	public float y = 0;
	public float dx = 0;
	public float dy = 0;
	public float ax = 0;
	public float ay = 0;
	public float sx = 0;
	public float sy = 0;
	public float bounceFactor = 0;
	public boolean onFloor = true;
	public boolean moving = true;

	public PhysicalComponent(float x, float y, float sx, float sy) {
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
	}
}
