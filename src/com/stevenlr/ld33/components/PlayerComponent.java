package com.stevenlr.ld33.components;

import com.stevenlr.waffle2.entitysystem.Component;

public class PlayerComponent extends Component {

	public float rotation;
	public float lastShot = 0;
	public int selectedWeapon = 0;
	public int nbGrenades = 3;
	public float riffleDamage = 33;
	public float sgDamage = 19;
	public float riffleCooldown = 0.12f;
	public float sgCooldown = 0.6f;
	public int gold = 0;
	public int nextUpgradeCost = 20;
	public int score = 0;
}
