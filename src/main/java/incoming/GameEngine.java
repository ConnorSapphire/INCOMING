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

    private boolean paused;
    private boolean menu;
    private boolean gameOver;

    public GameEngine() {
        this.player = Planet.player;
        this.meteorites = new ArrayList<Meteorite>();
        this.explosions = new ArrayList<Explosion>();
        this.maxDelay = 180;
        this.minDelay = 60;
        this.meteoriteDelay = (int) Double.POSITIVE_INFINITY/*(Math.random() * this.maxDelay) + this.minDelay */;
        this.meteoriteTimer = 0;
        this.meteoriteSpeed = 2;
        this.frames = 0;
        this.paused = true;
        this.menu = true;
        this.gameOver = false;
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

    public void pause() {
        for (Meteorite meteorite : this.meteorites) {
            meteorite.setSpeed(0);
        }
        for (Bullet bullet : this.player.getBullets()) {
            bullet.setSpeed(0);
        }
        this.meteoriteDelay = (int) Double.POSITIVE_INFINITY;
        this.paused = true;
    }

    public void resume() {
        for (Meteorite meteorite : this.meteorites) {
            meteorite.setSpeed(this.meteoriteSpeed);
        }
        for (Bullet bullet : this.player.getBullets()) {
            bullet.setSpeed(this.player.getBulletSpeed());
        }
        this.meteoriteDelay = (int) (Math.random() * this.maxDelay) + this.minDelay;
        this.paused = false;
        this.menu = false;
    }

    public void menu() {
        this.pause();
        this.menu = true;
        this.gameOver = false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isMenu() {
        return this.menu;
    }

    public boolean isOver() {
        return this.gameOver;
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
            if (meteorite.getY() >= -50) {
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

        // Draw player lives
        if (!menu) {
            if (player.getLives() == 3) {
                app.image(player.getLifeSprite(), -280, -280);
                app.image(player.getLifeSprite(), -260, -280);
                app.image(player.getLifeSprite(), -240, -280);       
            } else if (player.getLives() == 2) {
                app.image(player.getLifeSprite(), -280, -280);
                app.image(player.getLifeSprite(), -260, -280);
                app.image(player.getLostLifeSprite(), -240, -280); 
            } else if (player.getLives() == 1) {
                app.image(player.getLifeSprite(), -280, -280);
                app.image(player.getLostLifeSprite(), -260, -280);
                app.image(player.getLostLifeSprite(), -240, -280); 
            } else if (player.getLives() <= 0) {
                app.image(player.getLostLifeSprite(), -280, -280);
                app.image(player.getLostLifeSprite(), -260, -280);
                app.image(player.getLostLifeSprite(), -240, -280); 
            }
        }

        // End game once out of lives
        if (player.getLives() <= 0) {
            this.pause();
            this.gameOver = true;
        }
    } 
}