package incoming;

import processing.core.PApplet;
import java.util.ArrayList;

public class GameEngine {
 
    public static final int ROTATIONINCREMENT = 10;
    public static final int PLANETRADIUS = 0;
    public static final int METEORITERADIUS = 0;
    public static final int BULLETRADIUS = 0;
    public static final int EXPLOSIONRADIUS = 0;

    private Planet player;
    private ArrayList<Meteorite> meteorites;
    private ArrayList<Explosion> explosions;
    
    private int meteoriteDelay;
    private int maxDelay;
    private int minDelay;
    private int meteoriteTimer;
    private int meteoriteSpeed;

    private int frames;

    public GameEngine() {
        this.player = Planet.player;
        this.meteorites = new ArrayList<Meteorite>();
        this.explosions = new ArrayList<Explosion>();
        this.maxDelay = 180;
        this.minDelay = 60;
        this.meteoriteDelay = (int) (Math.random() * this.maxDelay) + this.minDelay;
        this.meteoriteTimer = 0;
        this.meteoriteSpeed = 2;
        this.frames = 0;
    }

    public Planet getPlayer() {
        return this.player;
    }

    public ArrayList<Meteorite> getMeteorites() {
        return this.meteorites;
    }

    public ArrayList<Explosion> getExplosions() {
        return this.explosions;
    }

    public void draw(PApplet app) {
        // Draw player
        this.player.draw(app);

        // Check collision
        ArrayList<Meteorite> removeMeteorites = new ArrayList<Meteorite>();
        ArrayList<Bullet> bullets = this.player.getBullets();
        for (Meteorite meteorite : this.meteorites) {
            meteorite.draw(app);
            meteorite.tick();
            if (meteorite.getY() >= 0) {
                removeMeteorites.add(meteorite);
                player.takeDamage();
            }
            ArrayList<Bullet> removeBullets = new ArrayList<Bullet>();
            for (Bullet bullet : bullets) {
                if ((bullet.getDirection() <= meteorite.getDirection() + 10) && (bullet.getDirection() >= meteorite.getDirection() - 10)) {
                    if ((bullet.getY() <= meteorite.getY() + ROTATIONINCREMENT) && (bullet.getY() >= meteorite.getY() - ROTATIONINCREMENT)) {
                        removeMeteorites.add(meteorite);
                        removeBullets.add(bullet);
                    }
                }
            }
            bullets.removeAll(removeBullets);
        }
        this.meteorites.removeAll(removeMeteorites);

        // Calculate logic and draw explosions
        ArrayList<Explosion> removeExplosions = new ArrayList<Explosion>();
        for (Explosion explosion : this.explosions) {
            explosion.draw(app);
        }
        this.explosions.removeAll(removeExplosions);

        // Initialise new meteorites
        this.meteoriteTimer += 1;
        if (this.meteoriteTimer >= this.meteoriteDelay) {
            this.meteoriteTimer = 0;
            this.meteoriteDelay = (int) (Math.random() * this.maxDelay) + this.minDelay;
            int meteoriteX = 0;
            int meteoriteY = -425;
            int meteoriteDirection = ((int) (Math.random() * 36)) * ROTATIONINCREMENT;
            this.meteorites.add(new Meteorite(meteoriteX, meteoriteY, meteoriteDirection, this.meteoriteSpeed)); 
        }
        
        // Increase meteor speed as game progresses
        this.frames += 1;
        if (this.frames >= 3600) {
            this.frames = 0;
            if (this.maxDelay > 120 && this.minDelay > 0) {
                this.maxDelay -= 1;
                this.minDelay -= 1;
                this.meteoriteSpeed += 1;
            }
        }
    } 
}