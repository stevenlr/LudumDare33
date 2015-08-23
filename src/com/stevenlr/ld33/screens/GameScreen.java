package com.stevenlr.ld33.screens;

import com.stevenlr.ld33.level.Level;
import com.stevenlr.waffle2.graphics.Renderer;

public class GameScreen implements IScreen {

	private Level _level = new Level("/level.png");

	@Override
	public void update(float dt) {
		_level.update(dt);
	}

	@Override
	public void draw(Renderer r) {
		_level.render(r);
	}
}
