package com.badlogic.drop.Screens;

import java.util.LinkedList;

import com.badlogic.drop.CuocChienSinhTon;
import com.badlogic.drop.CuocChienSinhTon.MAP;
import com.badlogic.drop.Sprites.AnKhangHero;
import com.badlogic.drop.Sprites.EyeBullet;
import com.badlogic.drop.Sprites.FlyingEye;
import com.badlogic.drop.Sprites.Monster;
import com.badlogic.drop.Sprites.Hero.State;
import com.badlogic.drop.Tools.B2WorldCreator;
import com.badlogic.drop.Tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class FlappyMap extends PlayScreen{
	private final int SPEED = 5;
	private final int GRAVITY = -50;
	private final int DISTANCE = 10;
	private float timeCount;
	private TextureAtlas flyEngineAtlas;
	private LinkedList<Monster> monsters;
	private Animation<TextureRegion> flyEngineAnimation;
	public FlappyMap(CuocChienSinhTon game) {
		game.setMap(MAP.MAP2);
		atlas = new TextureAtlas("Hero.pack");
		this.game = game;

		// create camera
		camera = new OrthographicCamera();
		
		// load background
		backgroundTexture = new Texture("map1.png");
		
		// viewport => responsive 
		gamePort = new FitViewport(CuocChienSinhTon.V_WIDTH/CuocChienSinhTon.PPM, CuocChienSinhTon.V_HEIGHT/CuocChienSinhTon.PPM,camera);		
		
		// load tilemap into world
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("map1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/CuocChienSinhTon.PPM);
		
		// setup box2d
		world = new World(new Vector2(0,GRAVITY),true);
		b2dr = new Box2DDebugRenderer();
		new B2WorldCreator(world, map, this);
		// create monsters
		prepareMonster();
		// create hero
		region = atlas.findRegion("HeroIdle");
		prepareFlyEngineAnimation();
		player = new AnKhangHero(world,this);
		player.body.setLinearVelocity(SPEED,0);
		timeCount = 0;
	}
	public TextureAtlas getAtlas() {
		return atlas;
	}
	private void prepareMonster() {
		int intitDistance = 0;
		int monsterQuantity = 20;
		monsters = new LinkedList<Monster>();
		
		for (int i =0 ; i<monsterQuantity;i++) {
			FlyingEye flyingEye = new FlyingEye(world, this, 3+intitDistance, (int) (Math.random()*20));
			
			monsters.add(flyingEye);
			intitDistance+=DISTANCE;
		}
		
	}
	private void prepareFlyEngineAnimation() {
		flyEngineAtlas = new TextureAtlas("asset/map2/packs/can-dau-van.atlas");
		Array<AtlasRegion> flipXArray = new Array<AtlasRegion>();
		Array<AtlasRegion> originArray = flyEngineAtlas.getRegions();
		for (AtlasRegion region : originArray) {
			region.flip(true, false);
			flipXArray.add(region);
		}
		flyEngineAnimation = new Animation<TextureRegion>(0.1f,flipXArray);
		flyEngineAnimation.setPlayMode(PlayMode.LOOP);
		
	}
	// method that be called every 1/60s
	public void update(float dt) {
		//update monster
		for (Monster monster :monsters) {
			monster.update(dt);
		}
		//time count for speed up
		timeCount+=dt;
		
		player.currentState = State.STANDING;


		handleInput(dt);
		player.update(dt);
		world.step(1/60f, 6, 2);
		
		
		camera.position.x = player.getX()+10;
		renderer.setView(camera);
		camera.update();

	}
	public Body getBody() {
		return player.body;
	}
	private void heroJump() {
		getBody().setLinearVelocity(SPEED*(1+ timeCount/10), 17);

	}
	protected void handleInput(float dt) {
		if (Gdx.input.isTouched()||Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			heroJump();
		}
		
	}
	
	@Override
	public void render(float delta) {
		
		
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		
        game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundTexture, camera.position.x - gamePort.getWorldWidth() / 2,
				camera.position.y - gamePort.getWorldHeight() / 2, gamePort.getWorldWidth(),
				gamePort.getWorldHeight());
		game.batch.end();
		
//        //render tilemap
//		float scale = 5.0f; // Adjust this value as needed
//	    renderer.getBatch().setProjectionMatrix(camera.combined.cpy().scl(scale));
//
//	    // Render the map
//	    renderer.setView(camera);
//	    renderer.render();

		b2dr.render(world, camera.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.draw(flyEngineAnimation.getKeyFrame(player.getStateTime()), getBody().getPosition().x-player.getRegionWidth()/1.2f/CuocChienSinhTon.PPM, getBody().getPosition().y-player.getRegionHeight()/CuocChienSinhTon.PPM,flyEngineAnimation.getKeyFrame(0).getRegionWidth()*0.75f/CuocChienSinhTon.PPM,flyEngineAnimation.getKeyFrame(delta).getRegionHeight()*0.8f/CuocChienSinhTon.PPM);
		game.batch.end();
		
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);

		update(delta);

		}
	
	void updatePlayer(float dt) {
		
		
	}

		

		

	}


