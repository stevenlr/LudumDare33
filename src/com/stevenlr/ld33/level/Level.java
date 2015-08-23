package com.stevenlr.ld33.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.stevenlr.ld33.Animations;
import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.Particles;
import com.stevenlr.ld33.components.BulletComponent;
import com.stevenlr.ld33.components.CoinComponent;
import com.stevenlr.ld33.components.EnnemyComponent;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.ld33.entities.HunterSpawnerEntity;
import com.stevenlr.ld33.entities.LightEntity;
import com.stevenlr.ld33.entities.PlayerEntity;
import com.stevenlr.ld33.screens.ScoreScreen;
import com.stevenlr.ld33.systems.AnimatedTextureSystem;
import com.stevenlr.ld33.systems.BulletLogicSystem;
import com.stevenlr.ld33.systems.BulletRenderSystem;
import com.stevenlr.ld33.systems.GrenadeSystem;
import com.stevenlr.ld33.systems.HunterLogicSystem;
import com.stevenlr.ld33.systems.HunterRenderingSystem;
import com.stevenlr.ld33.systems.HunterSpawnerSystem;
import com.stevenlr.ld33.systems.LifeBarRenderSystem;
import com.stevenlr.ld33.systems.LightRenderSystem;
import com.stevenlr.ld33.systems.PhysicalMovementSystem;
import com.stevenlr.ld33.systems.PlayerControllerSystem;
import com.stevenlr.ld33.systems.TemporaryEntitiesSystem;
import com.stevenlr.waffle2.entitysystem.Entity;
import com.stevenlr.waffle2.graphics.Animation;
import com.stevenlr.waffle2.graphics.Camera;
import com.stevenlr.waffle2.graphics.Canvas;
import com.stevenlr.waffle2.graphics.Font;
import com.stevenlr.waffle2.graphics.Renderer;
import org.joml.Vector2f;

public class Level {

	private enum State {
		PLAYING,
		BUYING
	}

	public static final int INV_HEIGHT = 80;
	public static final int NADE_COST = 3;

	private State _currentState = State.PLAYING;
	private int _width;
	private int _height;
	private Tile[] _tiles;
	private float _spawnX;
	private float _spawnY;
	private PlayerEntity _player;
	private Animation.Instance _playerAnimation;
	private int _level = 1;
	private float _deathTime = 0;

	public Canvas _canvasLevel;
	public Canvas _canvasLights;
	public Canvas _canvasInv;

	private PlayerControllerSystem _playerControllerSystem = new PlayerControllerSystem();
	private PhysicalMovementSystem _physicalMovementSystem = new PhysicalMovementSystem();
	private LightRenderSystem _lightRenderSystem = new LightRenderSystem();
	private HunterSpawnerSystem _hunterSpawnerSystem = new HunterSpawnerSystem();
	private HunterRenderingSystem _hunterRenderingSystem = new HunterRenderingSystem();
	private LifeBarRenderSystem _lifeBarRenderSystem = new LifeBarRenderSystem();
	private BulletRenderSystem _bulletRenderSystem = new BulletRenderSystem();
	private BulletLogicSystem _bulletLogicSystem = new BulletLogicSystem();
	private HunterLogicSystem _hunterLogicSystem = new HunterLogicSystem();
	private TemporaryEntitiesSystem _temporaryEntitiesSystem = new TemporaryEntitiesSystem();
	private GrenadeSystem _grenadeSystem = new GrenadeSystem();
	private AnimatedTextureSystem _animatedTextureSystem = new AnimatedTextureSystem();

	public Level(String filename) {
		BufferedImage img = null;

		try {
			img= ImageIO.read(Game.class.getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}

		_width = img.getWidth();
		_height = img.getHeight();
		_tiles = new Tile[_width * _height];

		for (int y = 0; y < _height; ++y) {
			for (int x = 0; x < _width; ++x) {
				int color = img.getRGB(x, _height - y - 1);
				int tileIndex = _width * y + x;

				switch (color) {
				case 0xffffffff:
					_tiles[tileIndex] = Tile.floor;
					break;
				case 0xff000000:
					_tiles[tileIndex] = Tile.wall;
					break;
				case 0xff00ff00:
					_tiles[tileIndex] = Tile.floor;
					_spawnX = x;
					_spawnY = y;
					break;
				case 0xffff8000:
					_tiles[tileIndex] = Tile.torch;
					new LightEntity(x, y, 6, 0.6f, 0.55f, 0.4f);
					break;
				case 0xffff0000:
					_tiles[tileIndex] = Tile.floor;
					new HunterSpawnerEntity(x, y);
					break;
				case 0xffffff00:
					_tiles[tileIndex] = Tile.lava;
					new LightEntity(x, y, 9, 0.65f, 0.6f, 0.45f);
					break;
				default:
					_tiles[tileIndex] = Tile.wall;
				}
			}
		}

		_player = new PlayerEntity(_spawnX, _spawnY);
		_canvasLevel = new Canvas(Game.WIDTH, Game.HEIGHT - INV_HEIGHT);
		_canvasLights = new Canvas(Game.WIDTH, Game.HEIGHT - INV_HEIGHT);
		_canvasInv = new Canvas(Game.WIDTH, INV_HEIGHT);
		_playerAnimation = Animations.player.makeInstance();
	}

	public void update(float dt) {
		switch (_currentState) {
		case PLAYING:
			updateLevel(dt);
			break;
		case BUYING:
			updateShop(dt);
			break;
		}
	}

	public void updateLevel(float dt) {
		if (_player.getAs(LivingComponent.class).currentLife > 0) {
			_playerControllerSystem.update(dt, _level);
			_bulletLogicSystem.update(dt);
			_grenadeSystem.update(dt);
			_hunterSpawnerSystem.update(dt, _level);
			_hunterLogicSystem.update(dt);
			_physicalMovementSystem.update(dt, this);
			_temporaryEntitiesSystem.update(dt);
			_animatedTextureSystem.update(dt);

			if (Math.abs(_player.getAs(PhysicalComponent.class).dx) > 0.01 || Math.abs(_player.getAs(PhysicalComponent.class).dy) > 0.01) {
				_playerAnimation.update(dt);
			}
		} else {
			if (_deathTime == 0) {
				Game.instance.audioRegistry.getSource("lost").play();
			}

			_deathTime += dt;

			if (_deathTime >= 3) {
				_currentState = State.BUYING;
				_level++;

				if (_level > 5) {
					PlayerComponent player = _player.getAs(PlayerComponent.class);
					Game.instance.setNextScreen(new ScoreScreen(player.score, player.gold / 2));
				}
			}
		}
	}

	public void updateShop(float dt) {
		PlayerComponent player = _player.getAs(PlayerComponent.class);

		if (Game.instance.mouse.wasPressed(0)) {
			Vector2f pos = Game.instance.mouse.getMousePosition();

			if (player.gold >= NADE_COST && _buyGrenadeButton.isIn(pos.x, pos.y)) {
				player.gold -= NADE_COST;
				player.nbGrenades++;
				Game.instance.audioRegistry.getSource("select").play();
			}

			if (player.gold >= player.nextUpgradeCost) {
				boolean upgraded = false;

				if (_speedRiffleButton.isIn(pos.x, pos.y)) {
					player.riffleCooldown *= 0.9;
					upgraded = true;
					Game.instance.audioRegistry.getSource("select").play();
				} else if (_damageRiffleButton.isIn(pos.x, pos.y)) {
					player.riffleDamage *= 1.2;
					upgraded = true;
					Game.instance.audioRegistry.getSource("select").play();
				} else if (_speedShotgunButton.isIn(pos.x, pos.y)) {
					player.sgCooldown *= 0.9;
					upgraded = true;
					Game.instance.audioRegistry.getSource("select").play();
				} else if (_damageShotgunButton.isIn(pos.x, pos.y)) {
					player.sgDamage *= 1.2;
					upgraded = true;
					Game.instance.audioRegistry.getSource("select").play();
				}

				if (upgraded) {
					player.gold -= player.nextUpgradeCost;
					player.nextUpgradeCost *= 1.2f;
				}
			}

			if (_continueButton.isIn(pos.x, pos.y)) {
				_currentState = State.PLAYING;
				clearLevel();
				Game.instance.audioRegistry.getSource("select").play();

				LivingComponent life = _player.getAs(LivingComponent.class);

				life.currentLife = life.maxLife;
				_deathTime = 0;
			}
		}
	}

	public void render(Renderer r) {
		switch (_currentState) {
		case PLAYING:
			renderLevel(r);
			renderGui(r);
			break;
		case BUYING:
			renderShop(r);
			break;
		}
	}

	private class Button {
		public float x;
		public float y;
		public float sx;
		public float sy;
		public String text;

		private Button(float x, float y, float sx, float sy, String text) {
			this.x = x;
			this.y = y;
			this.sx = sx;
			this.sy = sy;
			this.text = text;
		}

		private void draw(Renderer r) {
			r.setDrawParameters(false, false, true);
			r.fillRect(x, y, sx, sy, 1, 1, 1, 0.3f);
			r.setDrawParameters(false, false, false);
			r.drawText("font", text, x, y - 0.3f * sy, sy * 0.6f, 1, 1, 1, 1, Font.Alignment.MIDDLE);
			r.resetDrawParameters();
		}

		private boolean isIn(float mx, float my) {
			if (mx < x - sx / 2 || mx > x + sx / 2) {
				return false;
			}

			if (my < y - sy / 2 || my > y + sy / 2) {
				return false;
			}

			return true;
		}
	}

	private Button _continueButton = new Button(Game.WIDTH / 2, 50, 300, 50, "Continue");
	private Button _buyGrenadeButton = new Button(350, 125, 250, 50, "Buy (" + NADE_COST + " g)");
	private Button _damageRiffleButton = new Button(250, 325, 250, 50, "+ Damage");
	private Button _speedRiffleButton = new Button(530, 325, 250, 50, "+ Speed");
	private Button _damageShotgunButton = new Button(250, 225, 250, 50, "+ Damage");
	private Button _speedShotgunButton = new Button(530, 225, 250, 50, "+ Speed");

	private void renderShop(Renderer r) {
		Camera c = r.getCamera();
		PlayerComponent player = _player.getAs(PlayerComponent.class);

		c.setCenter(Game.WIDTH / 2, Game.HEIGHT / 2);
		c.setRadius(Game.WIDTH / 2);

		r.fill(0, 0, 0);
		r.setBlending(Renderer.ALPHA);

		r.drawText("font", "THE SHOP", Game.WIDTH / 2, Game.HEIGHT - 60, 50, 1, 1, 1, 0.5f, Font.Alignment.MIDDLE);

		r.drawTile(10, 400, 60, 60, 1, 1, 1, 1, Game.instance.textureRegistry.getTexture("icons", 3));
		r.drawText("font", "" + player.gold, 90, 410, 40, 1, 1, 1, 1, Font.Alignment.LEFT);
		r.drawText("font", "Next upgrade cost : " + player.nextUpgradeCost, 280, 420, 25, 1, 1, 1, 1, Font.Alignment.LEFT);
		r.drawTile(10, 300, 60, 60, 1, 1, 1, 1, Game.instance.textureRegistry.getTexture("icons", 0));
		r.drawTile(10, 200, 60, 60, 1, 1, 1, 1, Game.instance.textureRegistry.getTexture("icons", 1));
		r.drawTile(10, 100, 60, 60, 1, 1, 1, 1, Game.instance.textureRegistry.getTexture("icons", 2));
		r.drawText("font", "" + player.nbGrenades, 90, 110, 40, 1, 1, 1, 1, Font.Alignment.LEFT);

		_continueButton.draw(r);

		if (player.gold >= NADE_COST) {
			_buyGrenadeButton.draw(r);
		}

		if (player.gold >= player.nextUpgradeCost) {
			_damageRiffleButton.draw(r);
			_speedRiffleButton.draw(r);
			_damageShotgunButton.draw(r);
			_speedShotgunButton.draw(r);
		}
	}

	private void renderGui(Renderer r) {
		Renderer ri = _canvasInv.getRenderer();
		Camera c = ri.getCamera();
		PlayerComponent player = _player.getAs(PlayerComponent.class);

		c.setRadius(Game.WIDTH / 2);
		c.setCenter(Game.WIDTH / 2, INV_HEIGHT / 2);

		ri.setBlending(Renderer.ALPHA);
		ri.fill(0, 0, 0);
		ri.drawTile(10, 10, 60, 60, 1, 1, 1, (player.selectedWeapon == 0) ? 1 : 0.3f, Game.instance.textureRegistry.getTexture("icons", 0));
		ri.drawTile(80 + 20, 10, 60, 60, 1, 1, 1, (player.selectedWeapon == 1) ? 1 : 0.3f, Game.instance.textureRegistry.getTexture("icons", 1));
		ri.drawTile(2 * 80 + 20, 10, 60, 60, 1, 1, 1, 1, Game.instance.textureRegistry.getTexture("icons", 2));
		ri.drawText("font", "" + player.nbGrenades, 3 * 80 + 20, 20, 50, 1, 1, 1, 1, Font.Alignment.LEFT);
		ri.drawTile(5 * 80 + 20, 10, 60, 60, 1, 1, 1, 1f, Game.instance.textureRegistry.getTexture("icons", 3));
		ri.drawText("font", "" + player.gold, 6 * 80 + 20, 20, 50, 1, 1, 1, 1, Font.Alignment.LEFT);

		ri.doRenderPass();

		r.blitCanvas(_canvasInv, 0, 0);
	}

	public void renderLevel(Renderer r) {
		Renderer rl = _canvasLevel.getRenderer();
		Renderer ri = _canvasLights.getRenderer();

		Camera camera = rl.getCamera();
		Camera camera2 = ri.getCamera();

		camera.setRadius(12);
		camera.setCenter(_player.getAs(PhysicalComponent.class).x, _player.getAs(PhysicalComponent.class).y);
		camera2.setRadius(camera.getRadius());
		camera2.setCenter(camera.getCenter());

		rl.fill(0, 0, 0);

		int x0 = (int) Math.floor(camera.getCenter().x - camera.getRadius());
		int x1 = (int) Math.ceil(camera.getCenter().x + camera.getRadius());
		int y0 = (int) Math.floor(camera.getCenter().y - camera.getRadius());
		int y1 = (int) Math.ceil(camera.getCenter().y + camera.getRadius());

		x0 = Math.min(Math.max(x0, 0), _width - 1);
		x1 = Math.min(Math.max(x1, 0), _width - 1) + 1;
		y0 = Math.min(Math.max(y0, 0), _height - 1);
		y1 = Math.min(Math.max(y1, 0), _height - 1) + 1;

		for (int y = y0; y < y1; ++y) {
			for (int x = x0; x < x1; ++x) {
				Tile tile = _tiles[_width * y + x];

				rl.drawTile(x, y, 1, 1, tile.getTexture());
			}
		}


		rl.setDrawParameters(false, false, true);
		_hunterRenderingSystem.draw(rl);

		rl.push();
		rl.translate(_player.getAs(PhysicalComponent.class).x, _player.getAs(PhysicalComponent.class).y);
		rl.rotate(_player.getAs(PlayerComponent.class).rotation);
		rl.drawTile(0, 0, PlayerEntity.SIZE * 2, PlayerEntity.SIZE * 2, 1, 1, 1, 0.3f, Game.instance.textureRegistry.getTexture("shadow"));
		rl.drawTile(0, 0,
				PlayerEntity.SIZE, PlayerEntity.SIZE,
				_playerAnimation.getTextureId());
		rl.resetDrawParameters();
		rl.pop();

		_grenadeSystem.draw(rl);
		_animatedTextureSystem.draw(rl);

		Particles.drawAlpha(rl);

		rl.doRenderPass();

		ri.fill(0.05f, 0.08f, 0.05f);
		_lightRenderSystem.draw(ri);
		ri.doRenderPass();

		rl.setBlending(Renderer.MULTIPLICATIVE);
		rl.blitCanvas(_canvasLights, 0, 0);

		rl.push();
		rl.setDrawParameters(false, false, true);
		rl.setBlending(Renderer.ALPHA);
		_bulletRenderSystem.draw(rl);
		_lifeBarRenderSystem.draw(rl);

		rl.setDrawParameters(false, false, true);
		rl.setBlending(Renderer.ADDITIVE);
		rl.translate(_player.getAs(PhysicalComponent.class).x, _player.getAs(PhysicalComponent.class).y);
		rl.rotate(_player.getAs(PlayerComponent.class).rotation);
		rl.drawTile(0, 0,
				PlayerEntity.SIZE, PlayerEntity.SIZE,
				Game.instance.textureRegistry.getTexture("monster", 4));
		rl.resetDrawParameters();
		rl.setBlending(Renderer.ALPHA);
		rl.pop();

		Particles.drawAdditive(rl);
		rl.setBlending(Renderer.ALPHA);

		camera.setCenter(Game.WIDTH / 2, (Game.HEIGHT - INV_HEIGHT) / 2);
		camera.setRadius(Game.WIDTH / 2);

		LivingComponent life = _player.getAs(LivingComponent.class);

		rl.resetDrawParameters();
		rl.fillRect(20, 20, Game.WIDTH - 40, 20, 0, 0, 0, 1);
		rl.fillRect(22, 22, (Game.WIDTH - 44) * life.currentLife / life.maxLife, 16, 0.8f, 0.1f, 0.1f, 1);

		rl.drawText("font", "Level " + _level + "/5", 20, Game.HEIGHT - INV_HEIGHT - 50, 30, 1, 1, 1, 1, Font.Alignment.LEFT);

		r.setBlending(Renderer.ALPHA);
		r.blitCanvas(_canvasLevel, 0, INV_HEIGHT);

		if (_deathTime > 0) {
			float ratio = _deathTime / 2;

			Camera c = r.getCamera();

			c.setCenter(0, 0);
			c.setRadius(1);
			r.setBlending(Renderer.ALPHA);
			r.fillRect(-1, -1, 2, 2, 1, 0, 0, ratio * 0.5f);
			r.drawText("font", "You died", 0, 0, 0.1f, 1, 1, 1, ratio, Font.Alignment.MIDDLE);

			r.doRenderPass();
		}
	}

	public boolean isSolidAt(int x, int y) {
		if (x < 0 || x >= _width || y < 0 || y >= _height) {
			return true;
		}

		return _tiles[_width * y + x].isSolid();
	}

	public void clearLevel() {
		List<Entity> bullets = Game.instance.entitySystem.getAllEntitiesPossessing(BulletComponent.class);
		List<Entity> hunters = Game.instance.entitySystem.getAllEntitiesPossessing(EnnemyComponent.class);
		List<Entity> coins = Game.instance.entitySystem.getAllEntitiesPossessing(CoinComponent.class);

		for (Entity e : bullets) {
			Game.instance.entitySystem.removeEntity(e);
		}

		for (Entity e : hunters) {
			Game.instance.entitySystem.removeEntity(e);
		}

		for (Entity e : coins) {
			Game.instance.entitySystem.removeEntity(e);
		}
	}
}
