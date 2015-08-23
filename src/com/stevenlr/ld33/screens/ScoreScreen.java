package com.stevenlr.ld33.screens;

import com.stevenlr.ld33.Game;
import com.stevenlr.waffle2.graphics.Camera;
import com.stevenlr.waffle2.graphics.Font;
import com.stevenlr.waffle2.graphics.Renderer;

public class ScoreScreen implements IScreen {

	private int _score;
	private int _goldBonus;
	private int _nbDefeated;

	public ScoreScreen(int nbDefeated, int goldBonus) {
		_score = nbDefeated + goldBonus;
		_nbDefeated = nbDefeated;
		_goldBonus = goldBonus;
	}

	@Override
	public void update(float dt) {
		if (Game.instance.mouse.wasPressed(0)) {
			Game.instance.entitySystem.removeAllEntities();
			Game.instance.setNextScreen(new MainScreen());
			Game.instance.audioRegistry.getSource("select").play();
		}
	}

	@Override
	public void draw(Renderer r) {
		Camera c = r.getCamera();

		c.setCenter(Game.WIDTH / 2, Game.HEIGHT / 2);
		c.setRadius(Game.WIDTH / 2);

		r.fill(0, 0, 0);

		r.drawText("font", "Game over!", Game.WIDTH / 2, Game.HEIGHT - 100, 50, 1, 1, 1, 1, Font.Alignment.MIDDLE);

		r.drawText("font", "Hunters killed", Game.WIDTH / 2 - 300, Game.HEIGHT - 200, 30, 1, 1, 1, 0.6f, Font.Alignment.LEFT);
		r.drawText("font", "" + _nbDefeated, Game.WIDTH / 2 + 300, Game.HEIGHT - 200, 30, 1, 1, 1, 0.6f, Font.Alignment.RIGHT);

		r.drawText("font", "Gold bonus", Game.WIDTH / 2 - 300, Game.HEIGHT - 250, 30, 1, 1, 1, 0.6f, Font.Alignment.LEFT);
		r.drawText("font", "" + _goldBonus, Game.WIDTH / 2 + 300, Game.HEIGHT - 250, 30, 1, 1, 1, 0.6f, Font.Alignment.RIGHT);

		r.drawText("font", "Final score", Game.WIDTH / 2 - 300, Game.HEIGHT - 300, 30, 1, 1, 1, 1, Font.Alignment.LEFT);
		r.drawText("font", "" + _score, Game.WIDTH / 2 + 300, Game.HEIGHT - 300, 30, 1, 1, 1, 1, Font.Alignment.RIGHT);

		r.drawText("font", "Click to continue", Game.WIDTH / 2, Game.HEIGHT - 400, 30, 1, 1, 1, 0.6f, Font.Alignment.MIDDLE);
	}
}
