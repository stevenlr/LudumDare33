package com.stevenlr.ld33.level;

public abstract class Tile {

	public static Tile floor = new NormalTile(0, false);
	public static Tile wall = new NormalTile(1, true);
	public static Tile empty = new NormalTile(2, true);

	abstract public int getTexture();
	abstract public boolean isSolid();
}
