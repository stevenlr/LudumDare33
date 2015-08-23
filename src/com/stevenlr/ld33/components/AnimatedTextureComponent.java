package com.stevenlr.ld33.components;

import com.stevenlr.waffle2.entitysystem.Component;
import com.stevenlr.waffle2.graphics.Animation;

public class AnimatedTextureComponent extends Component {

	public Animation.Instance animation;

	public AnimatedTextureComponent(Animation.Instance animation) {
		this.animation = animation;
	}
}
