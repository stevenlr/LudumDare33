package com.stevenlr.ld33.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.stevenlr.ld33.Animations;
import com.stevenlr.ld33.Game;
import com.stevenlr.ld33.components.LivingComponent;
import com.stevenlr.ld33.components.PhysicalComponent;
import com.stevenlr.ld33.components.PlayerComponent;
import com.stevenlr.ld33.entities.HunterSpawnerEntity;
import com.stevenlr.ld33.entities.LightEntity;
import com.stevenlr.ld33.entities.PlayerEntity;
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
import com.stevenlr.waffle2.graphics.Animation;
import com.stevenlr.waffle2.graphics.Camera;
import com.stevenlr.waffle2.graphics.Canvas;
import com.stevenlr.waffle2.graphics.Renderer;

public class Level {

	private int _width;
	private int _height;
	private Tile[] _tiles;
	private float _spawnX;
	private float _spawnY;
	private PlayerEntity _player;
	private Animation.Instance _playerAnimation;

	public Canvas _canvasLevel;
	public Canvas _canvasLights;

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
		_canvasLevel = new Canvas(Game.WIDTH, Game.HEIGHT);
		_canvasLights = new Canvas(Game.WIDTH, Game.HEIGHT);
		_playerAnimation = Animations.player.makeInstance();
	}

	public void update(float dt) {
		_playerControllerSystem.update(dt);
		_bulletLogicSystem.update(dt);
		_grenadeSystem.update(dt);
		_hunterSpawnerSystem.update(dt);
		_hunterLogicSystem.update(dt);
		_physicalMovementSystem.update(dt, this);
		_temporaryEntitiesSystem.update(dt);

		if (Math.abs(_player.getAs(PhysicalComponent.class).dx) > 0.01 || Math.abs(_player.getAs(PhysicalComponent.class).dy) > 0.01) {
			_playerAnimation.update(dt);
		}
	}

	public void render(Renderer r) {
		Renderer rl = _canvasLevel.getRenderer();
		Renderer ri = _canvasLights.getRenderer();

		Camera camera = rl.getCamera();
		Camera camera2 = ri.getCamera();

		camera.setRadius(10);
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

		rl.doRenderPass();

		ri.fill(0.1f, 0.13f, 0.1f);
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

		camera.setCenter(Game.WIDTH / 2, Game.HEIGHT / 2);
		camera.setRadius(Game.WIDTH / 2);

		LivingComponent life = _player.getAs(LivingComponent.class);

		rl.resetDrawParameters();
		rl.fillRect(20, 20, Game.WIDTH - 40, 20, 0, 0, 0, 1);
		rl.fillRect(22, 22, (Game.WIDTH - 44) * life.currentLife / life.maxLife, 16, 0.8f, 0.1f, 0.1f, 1);

		r.setBlending(Renderer.ALPHA);
		r.blitCanvas(_canvasLevel, 0, 0);
	}

	public boolean isSolidAt(int x, int y) {
		if (x < 0 || x >= _width || y < 0 || y >= _height) {
			return true;
		}

		return _tiles[_width * y + x].isSolid();
	}
}
