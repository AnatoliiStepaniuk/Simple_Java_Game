package com.tutorial.main;

import java.util.Random;

public class Spawn {

	private Random r = new Random();
	private int prevLevel = -1;
	GameObject tempCoin = null;

	public void tick(){
		if(Game.hud.getLevel() != prevLevel){ // do only when level changes
			prevLevel = Game.hud.getLevel();
		if(Game.hud.getLevel() == 2)
//			Game.gameHandler.addObject(new ExplosiveEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
			Game.gameHandler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
				else if(Game.hud.getLevel() == 3){
			Game.gameHandler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
			Game.gameHandler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
		}
		else if(Game.hud.getLevel() == 4){
			Game.gameHandler.addObject(new FastEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
		}
		else if(Game.hud.getLevel() == 5){
			Game.gameHandler.addObject(new SmartEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
		}
		else if(Game.hud.getLevel() == 6){
			Game.gameHandler.addObject(new ExplosiveEnemy(r.nextInt(Game.WIDTH-32),r.nextInt(Game.HEIGHT-32), Game.gameHandler));
		}
		else if(Game.gameState == Game.STATE.Boss){
					Game.gameHandler.clearEnemies();
					Game.gameHandler.addObject(new BossEnemy(Game.WIDTH/2-48,-100, Game.gameHandler));
				}

		} // only if level changes

		// throw coin until in is not intersecting other coins or HUD
		if(r.nextInt(500) == 0){
			do {
				if(r.nextInt(2) == 0)
					tempCoin = new HealthCoin(r.nextInt(Game.WIDTH - 32), r.nextInt(Game.HEIGHT - 64), (r.nextInt(4) + 2) * 5, Game.gameHandler);
				else
					tempCoin = new ScoreCoin(r.nextInt(Game.WIDTH - 32), r.nextInt(Game.HEIGHT - 64), (r.nextInt(5) + 1) * 100, Game.gameHandler);

				for (GameObject gameObject : Game.gameHandler.objectList)
					if (gameObject.getId() == ID.HealthCoin || gameObject.getId() == ID.ScoreCoin) {
							if(gameObject.getBounds().intersects(tempCoin.getBounds())){
								tempCoin = null;
								break;
							}
					}
				if(tempCoin != null && Game.hud.getBounds().intersects(tempCoin.getBounds())){
					tempCoin = null;
				}

			} while(tempCoin == null);
			Game.gameHandler.addObject(tempCoin);
			tempCoin = null;
		}
	}
}
