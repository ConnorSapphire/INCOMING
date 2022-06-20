package incoming;

import processing.core.PApplet;
import processing.core.PImage;

public class Meteorite {

    private int x;
    private int y;
    private int direction;
    private int speed;
    private PImage sprite;

    public Meteorite(int x, int y, int direction, int speed) {
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

    public void setSpeed(int speed) {
        this.speed = speed;
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
        app.image(this.sprite, this.x, this.y);
        app.popMatrix();
    }

    public void tick() {
        this.y += this.speed;
    }
}