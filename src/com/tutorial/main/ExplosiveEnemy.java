package com.tutorial.main;

import java.awt.*;
import java.util.Random;

public class ExplosiveEnemy extends GameObject{

	private int timer;
	private Random r = new Random();
	public ExplosiveEnemy(int x, int y, Handler myHandler) {
		super(x, y, ID.ExplosiveEnemy, myHandler);
		velX = 5;
		velY = 5;
		width = 16;
		height = 16;
		timer = 0;
	}

	public void tick() {
		
		float diffX = x - Game.player.getX() - width/2;
		float diffY = y - Game.player.getY() - height/2;
		float distance = (float)Math.sqrt(diffX*diffX+diffY*diffY);
		velX = (int) (-4*diffX/distance);
		velY = (int) (-4*diffY/distance);
		
		x += velX;
		y += velY;

		height=16+16*timer/150;
		width=16+16*timer/150;

		if(y <= 0 || y >= Game.HEIGHT-height) velY *= -1;
		if(x <= 0 || x >= Game.WIDTH-width) velX *= -1;
		Game.gameHandler.addObject(new Trail(x, y, Color.yellow, width, height, 1.0f, 0.02f, Game.gameHandler));

		if(timer++ == 100){
			Game.gameHandler.removeObject(this);
			for(int i = 0; i < 15; i++){
				double dVelX = r.nextDouble() - 0.5;
				double dVelY = r.nextDouble() - 0.5;
				if(velX + dVelX != 0 && velY + dVelY != 0)
					Game.gameHandler.addObject(new BossEnemyBullet(x,y, 3*(velX + dVelX), 3*(velY+dVelY), Game.gameHandler));  // must do this only in state is New Game
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(x, y, width, height);
	}
}
