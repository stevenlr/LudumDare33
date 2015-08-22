package com.stevenlr.ld33.level;

import com.stevenlr.ld33.Game;

public class NormalTile extends Tile {

	private int _textureTileId;
	private boolean _isSolid;
	private int _textureId = -1;

	public NormalTile(int textureTileId, boolean isSolid) {
		_textureTileId = textureTileId;
		_isSolid = isSolid;
	}

	@Override
	public int getTexture() {
		if (_textureId == -1) {
			_textureId = Game.instance.textureRegistry.getTexture("level_tileset", _textureTileId);
		}

		return _textureId;
	}

	@Override
	public boolean isSolid() {
		return _isSolid;
	}
}
