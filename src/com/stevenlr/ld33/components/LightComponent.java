package com.stevenlr.ld33.components;

import com.stevenlr.waffle2.entitysystem.Component;

public class LightComponent extends Component {

	public float size;
	public float r;
	public float g;
	public float b;
	public float offsetX = 0;
	public float offsetY = 0;

	public LightComponent(float size, float r, float g, float b) {
		this.size = size;
		this.r = r;
		this.g = g;
		this.b = b;
	}
}
