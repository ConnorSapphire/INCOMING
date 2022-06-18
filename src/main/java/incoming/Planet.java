package incoming;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
// import incoming.App;
// import incoming.Bullet;

public enum Planet {
    player();

    private final int x = 0;
    private final int y = 0;
    private final int bulletSpeed = 10;

    private int direction;
    private ArrayList<Bullet> bullets;
    private PImage sprite;
    private int lives;

    Planet() {
        this.direction = 0;
        this.bullets = new ArrayList<Bullet>();
        this.lives = 3;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getBulletSpeed() {
        return this.bulletSpeed;
    }

    public int getDirection() {
        return this.direction;
    }

    public void rotateClockwise() {
        this.direction += GameEngine.ROTATIONINCREMENT;
        this.direction %= 360;
    }

    public void rotateCounterClockwise() {
        this.direction -= GameEngine.ROTATIONINCREMENT;
        this.direction %= 360;
        if (this.direction < 0) {
            this.direction += 360;
        }
    }

    public ArrayList<Bullet> getBullets() {
        return this.bullets;
    } 

    public Bullet shoot() {
        Bullet bullet = new Bullet(0, 0, this.direction, this.bulletSpeed);
        bullets.add(bullet);
        return bullet;
    }

    public void takeDamage() {
        this.lives -= 1;
    }

    public int getLives() {
        return this.lives;
    }

    public PImage getSprite() {
        return this.sprite;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void draw(PApplet app) {
        app.pushMatrix();
        ArrayList<Bullet> removeBullets = new ArrayList<Bullet>();
        for (Bullet bullet : this.bullets) {
            bullet.draw(app);
            bullet.tick();
            if (bullet.getY() <= -425) {
                removeBullets.add(bullet);
            }
        }
        this.bullets.removeAll(removeBullets);
        app.rotate(app.radians(this.direction));
        app.image(this.sprite, this.x, this.y);
        app.popMatrix();
    }
}