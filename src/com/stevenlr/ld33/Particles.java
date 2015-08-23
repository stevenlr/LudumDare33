package com.stevenlr.ld33;

import com.stevenlr.waffle2.graphics.Renderer;
import com.stevenlr.waffle2.particles.AnimatedTextureParticleSystem;
import com.stevenlr.waffle2.particles.ParticleSystem;
import com.stevenlr.waffle2.particles.spawner.OnceParticleSpawner;
import com.stevenlr.waffle2.particles.spawner.ParticleSpawner;
import com.stevenlr.waffle2.particles.spawner.PointParticleSpawner;
import com.stevenlr.waffle2.particles.spawner.RandomVelocityParticleSpawner;

public class Particles {

	public static ParticleSystem blood;
	public static ParticleSystem explosion;

	public static void init() {
		blood = new AnimatedTextureParticleSystem(Animations.particles, 100);
		blood.setColor(0.7f, 0, 0, 1);
		blood.setSize(0.5f, 0.5f);
		blood.setDuration(1.15f);
		blood.setLinearDamping(0.1f);

		explosion = new AnimatedTextureParticleSystem(Animations.particles, 300);
		explosion.setColor(1, 0.9f, 0.7f, 1);
		explosion.setSize(0.7f, 0.7f);
		explosion.setDuration(1.15f);
		explosion.setLinearDamping(0.05f);
		explosion.setRenderAdditive(true);
	}

	public static void update(float dt) {
		blood.update(dt);
		explosion.update(dt);
	}

	public static void drawAdditive(Renderer r) {
		explosion.draw(r);
	}

	public static void drawAlpha(Renderer r) {
		blood.draw(r);
	}

	public static void spawnBlood(float x, float y) {
		ParticleSpawner spawner = new PointParticleSpawner(x, y, new RandomVelocityParticleSpawner(4, 10, new OnceParticleSpawner(30, 0)));

		blood.addSpawner(spawner);
	}

	public static void spawnExplosion(float x, float y) {
		ParticleSpawner spawner = new PointParticleSpawner(x, y, new RandomVelocityParticleSpawner(4, 13, new OnceParticleSpawner(70, 0)));

		explosion.addSpawner(spawner);
	}
}
