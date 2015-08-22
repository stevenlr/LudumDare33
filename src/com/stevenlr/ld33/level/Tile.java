package com.stevenlr.ld33.level;

public abstract class Tile {

	public static Tile floor = new NormalTile(0, false);
	public static Tile wall = new NormalTile(1, true);
	public static Tile empty = new NormalTile(2, true);
	public static Tile torch = new NormalTile(3, true);
	public static Tile lava = new NormalTile(4, false);

	abstract public int getTexture();
	abstract public boolean isSolid();
}
