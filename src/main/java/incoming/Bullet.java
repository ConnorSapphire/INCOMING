package incoming;

import processing.core.PApplet;
import processing.core.PImage;

public class Bullet {

    private int x;
    private int y;
    private int direction;
    private int speed;
    private PImage sprite;

    public Bullet(int x, int y, int direction, int speed) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDirection() {
        return this.direction;
    }

    public PImage getSprite() {
        return this.sprite;
    } 

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }
    
    public void draw(PApplet app) {
        app.pushMatrix();
        app.rotate(app.radians(this.direction));
        app.fill(0, 255, 0);
        app.image(this.sprite, this.x, this.y);
        app.popMatrix();
    }

    public void tick() {
        this.y -= this.speed;
    }
}