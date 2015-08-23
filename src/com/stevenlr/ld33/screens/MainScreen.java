package com.stevenlr.ld33.screens;

import com.stevenlr.ld33.Game;
import com.stevenlr.waffle2.graphics.Camera;
import com.stevenlr.waffle2.graphics.Font;
import com.stevenlr.waffle2.graphics.Renderer;

public class MainScreen implements IScreen {

	@Override
	public void update(float dt) {
		if (Game.instance.mouse.wasPressed(0)) {
			Game.instance.setNextScreen(new GameScreen());
			Game.instance.audioRegistry.getSource("select").play();
		}
	}

	@Override
	public void draw(Renderer r) {
		Camera c = r.getCamera();

		c.setCenter(Game.WIDTH / 2, Game.HEIGHT / 2);
		c.setRadius(Game.WIDTH / 2);

		r.fill(0, 0, 0);

		r.drawText("font", Game.TITLE, Game.WIDTH / 2, Game.HEIGHT - 130, 80, 1, 1, 1, 1, Font.Alignment.MIDDLE);

		r.drawText("font", "Survive 5 waves as long as possible", Game.WIDTH / 2, 500, 30, 1, 1, 1, 0.6f, Font.Alignment.MIDDLE);
		r.drawText("font", "Collect gold", Game.WIDTH / 2, 450, 30, 1, 1, 1, 0.6f, Font.Alignment.MIDDLE);
		r.drawText("font", "Upgrade your arsenal", Game.WIDTH / 2, 400, 30, 1, 1, 1, 0.6f, Font.Alignment.MIDDLE);
		r.drawText("font", "Wreck faces", Game.WIDTH / 2, 350, 30, 1, 1, 1, 0.6f, Font.Alignment.MIDDLE);

		r.drawText("font", "Move", Game.WIDTH / 2 - 200, 300, 20, 1, 1, 1, 0.4f, Font.Alignment.LEFT);
		r.drawText("font", "W, A, S, D", Game.WIDTH / 2 + 200, 300, 20, 1, 1, 1, 0.4f, Font.Alignment.RIGHT);

		r.drawText("font", "Choose weapon", Game.WIDTH / 2 - 200, 260, 20, 1, 1, 1, 0.4f, Font.Alignment.LEFT);
		r.drawText("font", "1, 2", Game.WIDTH / 2 + 200, 260, 20, 1, 1, 1, 0.4f, Font.Alignment.RIGHT);

		r.drawText("font", "Fire", Game.WIDTH / 2 - 200, 220, 20, 1, 1, 1, 0.4f, Font.Alignment.LEFT);
		r.drawText("font", "Left click", Game.WIDTH / 2 + 200, 220, 20, 1, 1, 1, 0.4f, Font.Alignment.RIGHT);

		r.drawText("font", "Launch grenade", Game.WIDTH / 2 - 200, 180, 20, 1, 1, 1, 0.4f, Font.Alignment.LEFT);
		r.drawText("font", "Right click", Game.WIDTH / 2 + 200, 180, 20, 1, 1, 1, 0.4f, Font.Alignment.RIGHT);

		r.drawText("font", "Click to start", Game.WIDTH / 2, 90, 30, 1, 1, 1, 0.6f, Font.Alignment.MIDDLE);

		r.drawText("font", "By Steven Le Rouzic <stevenlr.com>", 20, 20, 16, 1, 1, 1, 0.4f, Font.Alignment.LEFT);
		r.drawText("font", "For Ludum Dare 33", Game.WIDTH - 20, 20, 16, 1, 1, 1, 0.4f, Font.Alignment.RIGHT);
	}
}
