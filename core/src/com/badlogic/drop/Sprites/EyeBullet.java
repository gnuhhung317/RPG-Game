package com.badlogic.drop.Sprites;

import com.badlogic.drop.Screens.FirstMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class EyeBullet extends Bullet {


	public EyeBullet(World world, FirstMap screen, int x, int y, int direction) {
		super(world, screen, x, y, direction);
		Collision.setCategoryFilter(monsterDef, Collision.EYEBULLET_BITS);
	}

	@Override
	public void prepareAnimation() {
		atlasBullet = new TextureAtlas("Monster/FlyingEye/EyeBullet.pack");
		bullet = new Animation<TextureRegion>(0.15f, atlasBullet.getRegions());
		setRegion(atlasBullet.getRegions().get(0));
	}
	
	
}