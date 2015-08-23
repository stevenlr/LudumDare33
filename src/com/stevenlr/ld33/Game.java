package com.stevenlr.ld33;

import com.stevenlr.ld33.level.Level;
import com.stevenlr.ld33.screens.GameScreen;
import com.stevenlr.ld33.screens.IScreen;
import com.stevenlr.ld33.screens.MainScreen;
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
	public static final int WIDTH = 1000 / RESOLUTION;
	public static final int HEIGHT = 700 / RESOLUTION;

	public static Game instance;

	private IScreen _currentScreen;
	private IScreen _nextScreen;

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
		textureRegistry.registerTexture("/grenade.png", "grenade");
		textureRegistry.registerTexture("/icons.png", "icons", 16, 16);
		textureRegistry.registerTexture("/coin.png", "coin", 8, 8);
		textureRegistry.registerTexture("/monster.png", "monster", 20, 20);

		Waffle2.getInstance().getFontRegistry().registerFont("font", "/pixelmix.ttf", 32);
	}

	@Override
	public void init() {
		Animations.player = new Animation("monster");
		Animations.player.addFrame(0, 0.2f);
		Animations.player.addFrame(1, 0.2f);
		Animations.player.addFrame(2, 0.2f);
		Animations.player.addFrame(3, 0.2f);

		Animations.coin = new Animation("coin");
		Animations.coin.addFrame(0, 0.25f);
		Animations.coin.addFrame(1, 0.25f);
		Animations.coin.addFrame(2, 0.25f);
		Animations.coin.addFrame(3, 0.25f);

		_currentScreen = new MainScreen();
	}

	@Override
	public void update(float dt) {
		if (_nextScreen != null) {
			_currentScreen = _nextScreen;
			_nextScreen = null;
		}

		_currentScreen.update(dt);
	}

	@Override
	public void draw(Renderer r) {
		_currentScreen.draw(r);
	}

	public void setNextScreen(IScreen screen) {
		_nextScreen = screen;
	}
}
