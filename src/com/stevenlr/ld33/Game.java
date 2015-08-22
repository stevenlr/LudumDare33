package com.stevenlr.ld33;

import com.stevenlr.ld33.level.Level;
import com.stevenlr.waffle2.IWaffle2Game;
import com.stevenlr.waffle2.Waffle2;
import com.stevenlr.waffle2.Waffle2Game;
import com.stevenlr.waffle2.entitysystem.EntitySystem;
import com.stevenlr.waffle2.graphics.Animation;
import com.stevenlr.waffle2.graphics.Renderer;
import com.stevenlr.waffle2.graphics.TextureRegistry;
import com.stevenlr.waffle2.input.KeyboardHandler;
import com.stevenlr.waffle2.input.MouseHandler;

@Waffle2Game(
		viewportWidth = Game.WIDTH,
		viewportHeight = Game.HEIGHT,
		resolution = Game.RESOLUTION,
		title = "I have no idea what the fuck I'm doing, LD33 edition",
		showFps = true
)
public class Game implements IWaffle2Game {

	public static final int RESOLUTION = 1;
	public static final int WIDTH = 800 / RESOLUTION;
	public static final int HEIGHT = 600 / RESOLUTION;

	public static Game instance;

	private Level _level;
	public TextureRegistry textureRegistry;
	public EntitySystem entitySystem;
	public KeyboardHandler keyboard;
	public MouseHandler mouse;

	public static void main(String args[]) {
		instance = new Game();
		Waffle2.getInstance().launchGame(instance);
	}

	@Override
	public void preInit() {
		textureRegistry = Waffle2.getInstance().getTextureRegistry();
		entitySystem = Waffle2.getInstance().getEntitySystem();
		keyboard = Waffle2.getInstance().getKeyboardHandler();
		mouse = Waffle2.getInstance().getMouseHandler();

		textureRegistry.registerTexture("/level_tileset.png", "level_tileset", 16, 16);
		textureRegistry.registerTexture("/light.png", "light");
		textureRegistry.registerTexture("/hunter.png", "hunter");
		textureRegistry.registerTexture("/shadow.png", "shadow");
		textureRegistry.registerTexture("/monster.png", "monster", 20, 20);
	}

	@Override
	public void init() {
		Animations.player = new Animation("monster");
		Animations.player.addFrame(0, 0.2f);
		Animations.player.addFrame(1, 0.2f);
		Animations.player.addFrame(2, 0.2f);
		Animations.player.addFrame(3, 0.2f);

		_level = new Level("/level.png");
	}

	@Override
	public void update(float dt) {
		_level.update(dt);
	}

	@Override
	public void draw(Renderer r) {
		_level.render(r);
	}
}
