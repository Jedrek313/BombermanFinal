package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Block.Block;
import com.mygdx.game.Block.Breakable;

public class WorldRenderer {

    private static final float CAMERA_WIDTH = 15f;
    private static final float CAMERA_HEIGHT =15f;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    /** Textures **/
    private Texture bobTexture;
    private Texture blockTexture;
    private Texture breakTexture;
    private Texture bombTexture;
    private Texture enemyTexture;
    private Texture flameTexture;

    private SpriteBatch spriteBatch;
    private boolean debug = true;
    private int width;
    private int height;
    public float ppuX; // pixels per unit on the X axis
    public float ppuY; // pixels per unit on the Y axis\
    boolean stop = false;

    public void setSize (int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }

    public float getPpuX(){
        return ppuX;
    }
    public float getPpuY(){
        return ppuY;
    }

    private void loadTextures() {
        bobTexture = new  Texture(Gdx.files.internal("bob_01.png"));
        blockTexture = new Texture(Gdx.files.internal("block.png"));
        breakTexture = new Texture(Gdx.files.internal("break.png"));
        bombTexture = new Texture(Gdx.files.internal("bomb.png"));
        enemyTexture = new Texture(Gdx.files.internal("enemy.png"));
        flameTexture = new Texture(Gdx.files.internal("flame.png"));
    }

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    public void render() {
        spriteBatch.begin();
        drawBlocks();
        drawBreaks();
        drawBombs();
        drawBob();
        drawEnemies();
        drawFlames();
        spriteBatch.end();
        if (debug)
            drawDebug();
        //Player bob = world.getBob();
        /*
        for(Block block : world.getBlocks()) {
            if(bob.bounds.overlaps(block.getBounds())){
                stop = true;
            }
        }
        */
    }

    private void drawBombs(){
        for(Bomb bomb : world.getBombs()){
            spriteBatch.draw(bombTexture, bomb.getPosition().x * ppuX, bomb.getPosition().y * ppuY, Bomb.SIZE * ppuX, Bomb.SIZE * ppuY);
        }
    }

    private void drawBlocks() {
        for (Block block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }

    private void drawEnemies() {
        for (Enemy enemy : world.getEnemies()) {
            spriteBatch.draw(enemyTexture, enemy.getPosition().x * ppuX, enemy.getPosition().y * ppuY, Enemy.SIZE * ppuX, Enemy.SIZE * ppuY);
        }
    }

    private void drawFlames() {
        for (Flame flame : world.getFlames()) {
            spriteBatch.draw(flameTexture, flame.getPosition().x * ppuX, flame.getPosition().y * ppuY, Flame.SIZE * ppuX, Flame.SIZE * ppuY);
        }
    }

    private void drawBreaks() {
        for (Breakable breakable : world.getBreaks()) {
            spriteBatch.draw(breakTexture, breakable.getPosition().x * ppuX, breakable.getPosition().y * ppuY, Breakable.SIZE * ppuX, Breakable.SIZE * ppuY);
        }
    }

    private void drawBob() {
        Player bob = world.getBob();
        spriteBatch.draw(bobTexture, bob.getPosition().x * ppuX, bob.getPosition().y * ppuY, Player.SIZE * ppuX, Player.SIZE * ppuY);
    }


    private void drawDebug() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);

        for (Block block : world.getBlocks()) {
            Rectangle rect = block.getBounds();
            float x1 = block.getPosition().x + rect.x;
            float y1 = block.getPosition().y + rect.y;
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(x1, y1, rect.width, rect.height);
        }

        for (Breakable breakable : world.getBreaks()) {
            Rectangle rect = breakable.getBounds();
            float x1 = breakable.getPosition().x + rect.x;
            float y1 = breakable.getPosition().y + rect.y;
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(x1, y1, rect.width, rect.height);
        }
        // render Bob
        Player bob = world.getBob();
        Rectangle rect = bob.getBounds();
        float x1 = bob.getPosition().x + rect.x;
        float y1 = bob.getPosition().y + rect.y;
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(x1, y1, rect.width, rect.height);
        debugRenderer.end();
    }

    public boolean getStop(){
        return stop;
    }

}