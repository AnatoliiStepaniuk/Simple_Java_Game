package com.tutorial.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class BossEnemy extends GameObject{

	private int timer=0;
	private Random r = new Random();

	public BossEnemy(int x, int y, Handler myHandler) {
		super(x, y, ID.BossEnemy, myHandler);
		velX = 0.0;
		velY = 2.0;
		height = 96;
		width = 96;
	}

	public void tick() {
		
		x += velX;
		y += velY;
		
		if (timer++ >= 200){
			if(velX == 0.0) velX = 2.0;
			velX *= 1.001;
			velX = Game.clamp(velX, -10.0, 10.0);
			int spawn = r.nextInt((int)(100/Math.abs(velX))); // the faster he goes the more bullets he throws
			if (spawn == 0) Game.gameHandler.addObject(new BossEnemyBullet(x+width/2, y+(int)(0.9*height), r.nextInt(10)-5, 5, Game.gameHandler));
			}
		else if(timer++ >=150 ) velY = 0;
		//turn around:
		if(x <= 0 || x >= Game.WIDTH-width) velX *= -1.0;
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x,y,width,height);
	}
}
