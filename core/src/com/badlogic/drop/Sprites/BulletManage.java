package com.badlogic.drop.Sprites;

import com.badlogic.drop.Screens.PlayScreen;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectSet;

public class BulletManage {
	public static ObjectSet<Bullet> bullets =  new ObjectSet<Bullet>();
	public static ObjectSet<Bullet> removeBullet = new ObjectSet<Bullet>();
	static World world;
	static PlayScreen screen;
	
	public static void setup(World worldd, PlayScreen screenn) {
		world = worldd;
		screen = screenn;
	}
	
	public static void addBullet(String kind, float x, float y, int direction) {
		Bullet bl;
	
		if(kind == "FlyingEye") {
			bl = new EyeBullet(world,screen,x,y,direction);
			bullets.add(bl);
		}else if (kind.equals("EnergyBall")) {
			bl = new EnergyBall(world, screen, x, y, direction);
			bullets.add(bl);
		}
	}
	
	public static void update(float dt) {
		for(Bullet bl : bullets) {
			bl.update(dt);
			
		}
		System.out.println(bullets.size);
		
	}
	
	public static void markRemoved(Bullet bl) {
		
		System.out.println("wtfff");

		removeBullet.add(bl);
	}
	public static void remove() {
		for (Bullet bl : removeBullet) {
			world.destroyBody(bl.b2body);
			bl.b2body=null;
			bullets.remove(bl);
			removeBullet.remove(bl);
		}
	}
	
}
