package incoming;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
//import processing.data.JSONObject;
//import processing.data.JSONArray;
//import processing.core.PFont;

public class App extends PApplet {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    public static final int FPS = 60;

    public GameEngine game;
	
	public ArrayList<PImage> planet;
    public ArrayList<PImage> meteorite;
    public ArrayList<PImage> explosion;
    public PImage bullet;
    public ArrayList<PImage> space;

    public int planetAnimationTimer;
    public int meteoriteAnimationTimer;
    public int explosionAnimationTimer;

    public App() {
        this.game = new GameEngine();
        this.planet = new ArrayList<PImage>();
        this.meteorite = new ArrayList<PImage>();
        this.explosion = new ArrayList<PImage>();
        this.space = new ArrayList<PImage>();
        this.planetAnimationTimer = 0;
        this.meteoriteAnimationTimer = 0;
        this.explosionAnimationTimer = 0;
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
		this.planet.add(loadImage(this.getClass().getResource("planet/planet0.png").getPath()));
		this.planet.add(loadImage(this.getClass().getResource("planet/planet1.png").getPath()));
        this.meteorite.add(loadImage(this.getClass().getResource("meteorite/meteorite0.png").getPath()));
        this.meteorite.add(loadImage(this.getClass().getResource("meteorite/meteorite1.png").getPath()));
        this.meteorite.add(loadImage(this.getClass().getResource("meteorite/meteorite2.png").getPath()));
        this.explosion.add(loadImage(this.getClass().getResource("explosion/explosion0.png").getPath()));
        this.explosion.add(loadImage(this.getClass().getResource("explosion/explosion1.png").getPath()));
        this.explosion.add(loadImage(this.getClass().getResource("explosion/explosion2.png").getPath()));
        this.explosion.add(loadImage(this.getClass().getResource("explosion/explosion3.png").getPath()));
        this.explosion.add(loadImage(this.getClass().getResource("explosion/explosion4.png").getPath()));
        this.explosion.add(loadImage(this.getClass().getResource("explosion/explosion5.png").getPath()));
        this.bullet = loadImage(this.getClass().getResource("bullet/bullet.png").getPath());
        imageMode(CENTER);
        
        this.animatePlanet();
        this.animateMeteorite();
        this.animateExplosion();
        this.setBulletSprites();
    }
	
    public void animatePlanet() {
        if (this.planetAnimationTimer >= 40) {
            this.planetAnimationTimer = 0;
        }
        if (this.planetAnimationTimer < 20) {
            this.game.getPlayer().setSprite(this.planet.get(0));
        } else if (this.planetAnimationTimer < 40) {
            this.game.getPlayer().setSprite(this.planet.get(1));
        }
    }

    public void animateMeteorite() {
        ArrayList<Meteorite> meteorites = this.game.getMeteorites();
        if (this.meteoriteAnimationTimer >= 15) {
            this.meteoriteAnimationTimer = 0;
        }
        for (Meteorite meteorite : meteorites) {
            if (this.meteoriteAnimationTimer < 5) {
                meteorite.setSprite(this.meteorite.get(0));
            } else if (this.meteoriteAnimationTimer < 10) {
                meteorite.setSprite(this.meteorite.get(1));
            } else if (this.meteoriteAnimationTimer < 15) {
                meteorite.setSprite(this.meteorite.get(2));
            }
        }
    }

    public void animateExplosion() {
        ArrayList<Explosion> explosions = this.game.getExplosions();
        if (this.explosionAnimationTimer >= 30) {
            this.meteoriteAnimationTimer = 0;
        }
        for (Explosion explosion : explosions) {
            if (this.explosionAnimationTimer < 5) {
                explosion.setSprite(this.explosion.get(0));
            } else if (this.explosionAnimationTimer < 10) {
                explosion.setSprite(this.explosion.get(1));
            } else if (this.explosionAnimationTimer < 15) {
                explosion.setSprite(this.explosion.get(2));
            } else if (this.explosionAnimationTimer < 20) {
                explosion.setSprite(this.explosion.get(3));
            } else if (this.explosionAnimationTimer < 25) {
                explosion.setSprite(this.explosion.get(4));
            } else if (this.explosionAnimationTimer < 30) {
                explosion.setSprite(this.explosion.get(5));
            }
        }
    }

    public void setBulletSprites() {
        ArrayList<Bullet> bullets = this.game.getPlayer().getBullets();
        for (Bullet bullet : bullets) {
            bullet.setSprite(this.bullet);
        }
    }

    /**
     * Draw all elements in the game by current frame. 
     */
    public void draw() {
        this.planetAnimationTimer += 1;
        this.animatePlanet();
        this.meteoriteAnimationTimer += 1;
        this.animateMeteorite();
        this.explosionAnimationTimer += 1;
        this.animateExplosion();
        this.setBulletSprites();

        background(71, 60, 120);
        translate(App.WIDTH / 2, App.HEIGHT / 2);
        this.game.draw(this);
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == LEFT) {
                game.getPlayer().rotateCounterClockwise();
            }
            if (keyCode == RIGHT) {
                game.getPlayer().rotateClockwise();
            }
            if (keyCode == UP) {
                game.getPlayer().shoot();
            }
            if (keyCode == DOWN) {
                if (game.isPaused()) {
                    game.resume();
                } else {
                    game.pause();
                }
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("incoming.App");
    }
}
