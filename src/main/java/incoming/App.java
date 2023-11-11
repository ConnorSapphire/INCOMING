package incoming;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.awt.Font;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;

public class App extends PApplet {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    public static final int FPS = 60;

    public GameEngine game;
	
	public ArrayList<PImage> planet;
    public ArrayList<PImage> meteorite;
    public ArrayList<PImage> explosion;
    public PImage bullet;
    public PImage life;
    public PImage lostLife;
    public PImage button;

    public File fontFile;
    public Font font;
    public PFont pFont;

    public Clip music;
    public Clip select;
    public Clip shoot;
    public Clip hurt;
    public Clip explode;

    public int planetAnimationTimer;
    public int meteoriteAnimationTimer;
    public int explosionAnimationTimer;
    public int selectedButton;

    private boolean howTo;
    private boolean credits;

    public App() {
        this.game = new GameEngine();
        this.planet = new ArrayList<PImage>();
        this.meteorite = new ArrayList<PImage>();
        this.explosion = new ArrayList<PImage>();
        this.planetAnimationTimer = 0;
        this.meteoriteAnimationTimer = 0;
        this.explosionAnimationTimer = 0;
        this.selectedButton = 0;
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
        this.life = loadImage(this.getClass().getResource("life/life0.png").getPath());
        this.lostLife = loadImage(this.getClass().getResource("life/life1.png").getPath());
        this.button = loadImage(this.getClass().getResource("button.png").getPath());
        imageMode(CENTER);
        
        // Load fonts
        this.fontFile = new File(this.getClass().getResource("Moonhouse.ttf").getPath());
        this.font = null;
        this.pFont = null;
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            this.pFont = new PFont(this.font, false);
        } catch(FontFormatException | IOException e) {
            e.printStackTrace();
        }
        
        // Load sound effects and begin music playback
        Mixer mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
        File musicFile = new File(this.getClass().getResource("sound/music.wav").getPath());
        File selectFile = new File(this.getClass().getResource("sound/select.wav").getPath());
        File shootFile = new File(this.getClass().getResource("sound/shoot.wav").getPath());
        File hurtFile = new File(this.getClass().getResource("sound/hurt.wav").getPath());
        File explodeFile = new File(this.getClass().getResource("sound/explode.wav").getPath());
        this.music = null;
        AudioInputStream musicStream = null;
        this.select = null;
        AudioInputStream selectStream = null;
        this.shoot = null;
        AudioInputStream shootStream = null;
        this.hurt = null;
        AudioInputStream hurtStream = null;
        this.explode = null;
        AudioInputStream explodeStream = null;
        try {
            musicStream = AudioSystem.getAudioInputStream(musicFile);
            DataLine.Info musicInfo = new DataLine.Info(Clip.class, musicStream.getFormat());
            this.music = (Clip) AudioSystem.getLine(musicInfo);
            this.music.open(musicStream);
            selectStream = AudioSystem.getAudioInputStream(selectFile);
            DataLine.Info selectInfo = new DataLine.Info(Clip.class, selectStream.getFormat());
            this.select = (Clip) AudioSystem.getLine(selectInfo);
            this.select.open(selectStream);
            shootStream = AudioSystem.getAudioInputStream(shootFile);
            DataLine.Info shootInfo = new DataLine.Info(Clip.class, shootStream.getFormat());
            this.shoot = (Clip) AudioSystem.getLine(shootInfo);
            this.shoot.open(shootStream);
            hurtStream = AudioSystem.getAudioInputStream(hurtFile);
            DataLine.Info hurtInfo = new DataLine.Info(Clip.class, hurtStream.getFormat());
            this.hurt = (Clip) AudioSystem.getLine(hurtInfo);
            this.hurt.open(hurtStream);
            explodeStream = AudioSystem.getAudioInputStream(explodeFile);
            DataLine.Info explodeInfo = new DataLine.Info(Clip.class, explodeStream.getFormat());
            this.explode = (Clip) AudioSystem.getLine(explodeInfo);
            this.explode.open(explodeStream);
        } catch(LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        this.music.loop((int) Double.POSITIVE_INFINITY);
        this.select.loop(0);
        this.shoot.loop(0);
        this.explode.loop(0);
        this.hurt.loop(0);
        game.setExplodeClip(this.explode);
        game.setHurtClip(this.hurt);

        // Assign sprites to existing objects
        game.getPlayer().setLifeSprite(this.life);
        game.getPlayer().setLostLifeSprite(this.lostLife);
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
        for (Meteorite m : meteorites) {
            if (this.meteoriteAnimationTimer < 5) {
                m.setSprite(this.meteorite.get(0));
            } else if (this.meteoriteAnimationTimer < 10) {
                m.setSprite(this.meteorite.get(1));
            } else if (this.meteoriteAnimationTimer < 15) {
                m.setSprite(this.meteorite.get(2));
            }
        }
    }

    public void animateExplosion() {
        ArrayList<Explosion> explosions = this.game.getExplosions();
        if (this.explosionAnimationTimer >= 30) {
            this.explosionAnimationTimer = 0;
            game.resetExplosions();
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

    public void drawMenu() {
        textFont(this.pFont, 64);
        text("INCOMING", 0, -110);
        textFont(this.pFont, 32);
        if (this.selectedButton == 0) {
            this.button.filter(INVERT);
            image(this.button, 0, 80);
            this.button.filter(INVERT);
        } else {
            image(this.button, 0, 80);
        }
        text("START", 0, 77);
        if (this.selectedButton == 1) {
            this.button.filter(INVERT);
            image(this.button, 0, 140);
            this.button.filter(INVERT);
        } else {
            image(this.button, 0, 140);
        }
        text("HOW TO PLAY", 0, 137);
        if (this.selectedButton == 2) {
            this.button.filter(INVERT);
            image(this.button, 0, 200);
            this.button.filter(INVERT);
        } else {
            image(this.button, 0, 200);
        }
        text("CREDITS", 0, 197);
    }

    public void drawGameOver() {
        textFont(this.pFont, 64);
        text("GAME OVER", 0, -110);
        textFont(this.pFont, 32);
        text(String.format("Score: %d", game.getScore()), 0, 100);
    }

    public void drawPause() {
        textFont(this.pFont, 64);
        text("PAUSED", 0, -110);
        textFont(this.pFont, 32);
        if (this.selectedButton == 0) {
            this.button.filter(INVERT);
            image(this.button, 0, 80);
            this.button.filter(INVERT);
        } else {
            image(this.button, 0, 80);
        }
        text("RESUME", 0, 77);
        if (this.selectedButton == 1) {
            this.button.filter(INVERT);
            image(this.button, 0, 140);
            this.button.filter(INVERT);
        } else {
            image(this.button, 0, 140);
        }
        text("RESTART", 0, 137);
        if (this.selectedButton == 2) {
            this.button.filter(INVERT);
            image(this.button, 0, 200);
            this.button.filter(INVERT);
        } else {
            image(this.button, 0, 200);
        }
        text("MENU", 0, 197);
    }

    public void drawHowTo() {
        background(71, 60, 120);
        textFont(this.pFont, 32);
        text("HOW TO PLAY", 0, -200);
        textFont(this.pFont, 16);
        textLeading(40);
        text("Use the left and right arrow keys to rotate the planet.\nUse the up arrow key to fire a laser pulse.\nMeteorites will explode when shot, this will give you a point.\nThe planet takes damage each time a meteorite hits it.\nThe planet can only withstand three hits.\n\nPause the game by pressing the down arrow.", 0, 0);
        text("PRESS ANY KEY TO RETURN TO THE MENU",0,240);
    }

    public void drawCredits() {
        background(71, 60, 120);
        textFont(this.pFont, 32);
        text("CREDITS", 0, -240);
        textFont(this.pFont, 16);
        textLeading(40);
        text("Animation: Connor\nArt: Connor\nCode: Connor\nFont: NimaType\nhttps://www.fontspace.com/moonhouse-font-f18420\nGame Design: Connor\nMusic: Battle (Boss) by BoxCat Games\nhttps://freemusicarchive.org/music/BoxCat_Games\nSound Effects: Connor\nhttps://sfxr.me/", 0, 0);
        text("PRESS ANY KEY TO RETURN TO THE MENU",0,240);
    }

    /**
     * Draw all elements in the game by current frame. 
     */
    public void draw() {
        if (!game.isPaused()) {
            this.planetAnimationTimer += 1;
            this.animatePlanet();
            this.meteoriteAnimationTimer += 1;
            this.animateMeteorite();
        }
        this.explosionAnimationTimer += 1;
        this.animateExplosion();
        this.setBulletSprites();

        fill(255,255,255);
        textAlign(CENTER, CENTER);
        textFont(this.pFont, 32);
        background(71, 60, 120);
        translate(App.WIDTH / 2, App.HEIGHT / 2);
        this.game.draw(this);
        
        if (howTo) {
            drawHowTo();
        } else if (credits) {
            drawCredits();
        } else if (game.isMenu()) {
            drawMenu();
        } else if (game.isOver()) {
            drawGameOver();
        } else if (game.isPaused()) {
            drawPause();
        }
    }

    public void keyPressed() {
        if (howTo || credits) {
            howTo = false;
            credits = false;
            this.selectedButton = 0;
            this.select.flush();
            this.select.loop(1);
            return;
        }

        if (!game.isPaused()) {
            if (keyCode == LEFT) {
                game.getPlayer().rotateCounterClockwise();
            }
            if (keyCode == RIGHT) {
                game.getPlayer().rotateClockwise();
            }
            if (keyCode == UP) {
                game.getPlayer().shoot();
                this.shoot.flush();
                this.shoot.loop(1);
            }
        } else {
            if (keyCode == UP) {
                this.selectedButton -= 1;
            }
            if (keyCode == DOWN) { 
                this.selectedButton += 1;
            }
            if (this.selectedButton < 0) {
                this.selectedButton += 3;
            } else if (this.selectedButton > 2) {
                this.selectedButton -= 3;
            }

            if (keyCode == 10) {
                if (this.selectedButton == 0) {
                    game.resume();
                } else if (this.selectedButton == 1) {
                    if (game.isMenu()) {
                        howTo = true;
                    } else {
                        game = new GameEngine();
                        game.getPlayer().reset();
                        game.resume();
                        this.selectedButton = 0;
                    }
                } else if (this.selectedButton == 2) {
                    if (game.isMenu()) {
                        credits = true;
                    } else {
                        game = new GameEngine();
                        game.getPlayer().reset();
                        this.selectedButton = 0;
                    }
                }
                this.select.flush();
                this.select.loop(1);
            }
        }

        if (!game.isOver()) {
            if (keyCode == DOWN) {
                if (!game.isPaused()) {
                    this.selectedButton = 0;
                    game.pause();
                }
            }
        } else {
            game = new GameEngine();
            game.getPlayer().reset();
            this.selectedButton = 0;
            this.select.flush();
            this.select.loop(1);
        }
    }

    public void mousePressed() {
        if (game.isOver()) {
            game = new GameEngine();
            game.getPlayer().reset();
            this.selectedButton = 0;
        }
    }

    public static void main(String[] args) {
        PApplet.main("incoming.App");
    }
}
