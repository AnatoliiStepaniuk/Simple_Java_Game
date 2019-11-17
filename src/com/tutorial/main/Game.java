package com.tutorial.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	
	public static STATE gameState = STATE.Menu;
	private STATE oldState;
	private Random r = new Random();
	public static Handler gameHandler;
	public static Handler menuHandler;
	private static final long serialVersionUID = 1550691097823471818L;
	public static final int WIDTH = 640, HEIGHT = 480;
	private Thread thread;
	private boolean running = false;
	private Spawn spawn;
	public static Menu menu;
	public static Help help;
	public static HUD hud;
	public static EndScreen end;
	public static Player player;
	public static Sound sound;
	public static ScoreManager scoreManager;
	public Game() {
		gameHandler = new Handler();
		menuHandler = new Handler();
		menu = new Menu();
		help = new Help();
		hud = new HUD();
		end = new EndScreen();
		player = new Player(WIDTH/2-32,HEIGHT/2-32, gameHandler);
		this.addKeyListener(new KeyInput());
		this.addMouseListener(new MouseInput());
		this.addMouseMotionListener(new MouseMove());
		spawn = new Spawn();
		sound = new Sound();
		scoreManager = new ScoreManager();

		//sets a JFrame and starts the game:
		new Window(WIDTH, HEIGHT, "Let's Build a Game!", this);

	}

	public enum STATE {
		Menu,
		Help,
		Game,
		NewGame,
		Boss,
		End
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
//		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			if(running)
				render();
//			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
//				System.out.println("FPS: " + frames);
//				frames = 0;
			}
			
		}
		stop();
	}

		private void tick(){

			if(gameState != oldState){ // goes only once when the gameState changes
				oldState = gameState;
				if (gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Help){
					menuHandler.objectList.clear(); // in case if I'm changing Menu and Help back and forth
					for(int j = 0; j < 14; j++){
						menuHandler.addObject(new MenuParticle(r.nextInt(WIDTH-32),r.nextInt(HEIGHT-32), menuHandler));
					}		
				sound.stopGameMusic();
				sound.stopBossMusic();

					if(gameState == STATE.Menu){
						if(Game.gameHandler.objectList.size() != 0)
							menu.setMenuString("Paused");
						else
							menu.setMenuString("Menu");
					}

					if(gameState == STATE.End){
						if(scoreManager.updateScore(new Score(hud.getScore()))) // updated score in the file score.dat
							end.setHighScoreString("New Record!");
						else
							end.setHighScoreString("HighScore:");
						end.setHighScore(scoreManager.getHighScore());
					}
				}
				else if(gameState == STATE.NewGame){
					gameHandler.objectList.clear(); // clear handler only if new game 
					menuHandler.objectList.clear(); // clear handler only if new game 
					gameHandler.addObject(player); // must do this only in state is New Game
					gameHandler.addObject(new BasicEnemy(r.nextInt(WIDTH-32),r.nextInt(HEIGHT-32), gameHandler));  // must do this only in state is New Game
						hud.setLevel(1);
						hud.setScore(0);
						hud.HEALTH=100;

						sound.playGameMusic();
				}
				else if(gameState == STATE.Game){
					menuHandler.objectList.clear(); // clear handler only if new game 
					sound.playGameMusic(); // if continue playing, music starts again
				}
				else if(gameState == STATE.Boss){
					menuHandler.objectList.clear(); // clear handler only if new game 
					sound.stopGameMusic();
					sound.playBossMusic();
				}
			}

			if(gameState == STATE.Game || gameState == STATE.NewGame || gameState == STATE.Boss){
				gameHandler.tick(); // change particles position only if it is Game
				hud.tick();
				spawn.tick();				
			}else if(gameState == STATE.Menu){
				menuHandler.tick();
				menu.tick();
			}else if(gameState == STATE.Help){
				menuHandler.tick();
				help.tick();
			}else if(gameState == STATE.End) {
				menuHandler.tick();
				end.tick();
			}
		}
		
		private void render() {
			BufferStrategy bs = this.getBufferStrategy();
			
			if(bs == null){
				this.createBufferStrategy(3);
				return;
			}
			
			Graphics g = bs.getDrawGraphics();
			
			g.setColor(Color.blue);
			g.fillRect(0,0,WIDTH,HEIGHT);
			
			if(gameState == STATE.Game || gameState == STATE.NewGame || gameState == STATE.Boss){
				hud.render(g);			
				gameHandler.render(g); // draw particles only if it is Game
			}
			else if(gameState == STATE.Menu){				
				menuHandler.render(g); // draw menu particles
				menu.render(g);
			}else if(gameState == STATE.Help){				
				menuHandler.render(g); // draw menu particles
				help.render(g);
			}
			else if(gameState == STATE.End){				
				menuHandler.render(g); // draw menu particles
				end.render(g);
			}

			g.dispose();
			bs.show();
		} // end render
		
	public static int clamp(int var, int min, int max){
		if (var >= max)
			return max;
		else if (var <= min)
			return min;
		else
			return var;
	}

	//for Boss speed (speed is double)
	public static double clamp(double var, double min, double max){
		if (var >= max)
			return max;
		else if (var <= min)
			return min;
		else
			return var;
	}

	public static void main(String[] args){
		new Game();
	}
}
