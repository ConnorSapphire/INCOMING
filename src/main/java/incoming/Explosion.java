package incoming;

import processing.core.PApplet;
import processing.core.PImage;

public class Explosion {

    private int x;
    private int y;
    private int direction;
    private PImage sprite;

    public Explosion(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
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
}