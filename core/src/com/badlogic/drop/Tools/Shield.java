package com.badlogic.drop.Tools;

import com.badlogic.drop.CuocChienSinhTon;
import com.badlogic.drop.Screens.FirstMap;
import com.badlogic.drop.Screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Shield extends Items{
	public Shield(World worldd, PlayScreen screenn,float x, float y) {
		
		super(worldd, screenn, x, y);
		
	}

	@Override
	public void prepairTexture() {
		this.ItemScaleX = this.ItemScaleY = 0.5f;
		this.texture = new Texture("Items/Shield.png");
		setRegion(texture);
		regionH = getRegionHeight()/CuocChienSinhTon.PPM*ItemScaleX;
		regionW = getRegionHeight()/CuocChienSinhTon.PPM*ItemScaleY;
		
	}

	@Override
	public void effect() {
		
		screen.getPlayer().shieldBegin = System.currentTimeMillis();
		((FirstMap) screen).StageCreator.itemsRemove.add(this);
		
	}
}