package com.stevenlr.ld33;

import com.stevenlr.ld33.level.Level;
import com.stevenlr.ld33.screens.GameScreen;
import com.stevenlr.ld33.screens.IScreen;
import com.stevenlr.ld33.screens.MainScreen;
import com.stevenlr.waffle2.IWaffle2Game;
import com.stevenlr.waffle2.Waffle2;
import com.stevenlr.waffle2.Waffle2Game;
import com.stevenlr.waffle2.audio.AudioRegistry;
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
		title = Game.TITLE,
		showFps = true
)
public class Game implements IWaffle2Game {

	public static final int RESOLUTION = 1;
	public static final int WIDTH = 1000 / RESOLUTION;
	public static final int HEIGHT = 700 / RESOLUTION;
	public static final String TITLE = "The Ugly Hunted";

	public static Game instance;

	private IScreen _currentScreen;
	private IScreen _nextScreen;

	public TextureRegistry textureRegistry;
	public AudioRegistry audioRegistry;
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
		audioRegistry = Waffle2.getInstance().getAudioRegistry();

		textureRegistry.registerTexture("/level_tileset.png", "level_tileset", 16, 16);
		textureRegistry.registerTexture("/light.png", "light");
		textureRegistry.registerTexture("/hunter.png", "hunter");
		textureRegistry.registerTexture("/shadow.png", "shadow");
		textureRegistry.registerTexture("/grenade.png", "grenade");
		textureRegistry.registerTexture("/icons.png", "icons", 16, 16);
		textureRegistry.registerTexture("/coin.png", "coin", 8, 8);
		textureRegistry.registerTexture("/particle.png", "particle", 8, 8);
		textureRegistry.registerTexture("/monster.png", "monster", 20, 20);

		Waffle2.getInstance().getFontRegistry().registerFont("font", "/pixelmix.ttf", 32);

		float globalGain = 1;

		audioRegistry.registerSound("coin", "/sounds/coin.ogg");
		audioRegistry.registerSound("death", "/sounds/death.ogg");
		audioRegistry.registerSound("explosion", "/sounds/explosion.ogg");
		audioRegistry.registerSound("hurt", "/sounds/hurt.ogg");
		audioRegistry.registerSound("lost", "/sounds/lost.ogg");
		audioRegistry.registerSound("select", "/sounds/select.ogg");
		audioRegistry.registerSound("shoot", "/sounds/shoot.ogg");

		audioRegistry.registerSource("coin", "coin", false, 1 * globalGain, 5);
		audioRegistry.registerSource("death", "death", false, 1 * globalGain, 5);
		audioRegistry.registerSource("explosion", "explosion", false, 1 * globalGain, 10);
		audioRegistry.registerSource("hurt", "hurt", false, 1 * globalGain, 5);
		audioRegistry.registerSource("lost", "lost", false, 1 * globalGain, 1);
		audioRegistry.registerSource("select", "select", false, 1 * globalGain, 2);
		audioRegistry.registerSource("shoot", "shoot", false, 0.8f * globalGain, 10);
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

		Animations.particles = new Animation("particle");
		Animations.particles.addFrame(0, 0.2f);
		Animations.particles.addFrame(1, 0.2f);
		Animations.particles.addFrame(2, 0.2f);
		Animations.particles.addFrame(3, 0.2f);
		Animations.particles.addFrame(4, 0.2f);
		Animations.particles.addFrame(5, 0.2f);

		Particles.init();

		_currentScreen = new MainScreen();
	}

	@Override
	public void update(float dt) {
		if (_nextScreen != null) {
			_currentScreen = _nextScreen;
			_nextScreen = null;
		}

		Particles.update(dt);
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
