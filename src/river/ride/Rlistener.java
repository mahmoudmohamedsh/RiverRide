/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package river.ride;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import texture.TextureReader;

/**
 *
 * @author Genius
 */
public class Rlistener implements GLEventListener, KeyListener {

    String textureNames[] = {"stars.png", "plane.png", "planeR.png", "planeL.png", "enemy.png", "stars2.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    static Sprite player;
    static ArrayList<Sprite> backGround = new ArrayList<>();
    static ArrayList<Sprite> enemies = new ArrayList<>();
    static int respawnEnemyTime = 20;
    static int timer = 0;
    static boolean pause = false;

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("Assets" + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        backGround.add(new Sprite(gl, 50, 50, textures[5], 1f, 1f, 0));
        backGround.add(new Sprite(gl, 50, 150, textures[5], 1f, 1f, 0));
        player = new Sprite(gl, 10, 10, textures[1], 0.2f, 0.18f, 0);

    }

    @Override
    public void display(GLAutoDrawable gld) {
        if (!pause) {
            GL gl = gld.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
            gl.glLoadIdentity();
            backGround = backGroundLoop(backGround);
            if (timer == respawnEnemyTime) {
                timer = 0;
                enemies.add(createEnemy(gl));
            }
            timer++;
            enemies = fall(enemies, false);
            player = handleKeyPress(player);
            respawnSprite(player);
        } else {
            for (int i = 0; i < backGround.size(); i++) {
                respawnSprite(backGround.get(i));
            }
            for (int i = 0; i < enemies.size(); i++) {
                respawnSprite(enemies.get(i));
            }
            respawnSprite(player);
        }

    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    public Sprite handleKeyPress(Sprite sprite) {

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (sprite.getX() > sprite.getxScale() * consts.maxWidth / 2) {
                sprite.setX(sprite.getX() - 2);
                player.setTexture(textures[3]);
            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (sprite.getX() < consts.maxWidth - sprite.getxScale() * consts.maxWidth / 2) {
                sprite.setX(sprite.getX() + 2);
                player.setTexture(textures[2]);
            }
        }
        /*if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (sprite.getX() < consts.maxWidth-sprite.) {
                x++;
            }
        }*/
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            pause = true;
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            backGroundLoop(backGround);
            fall(enemies, true);
        }
        return sprite;

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.clear(keyCode);
        player.setTexture(textures[1]);
        pause = false;
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    private void respawnSprite(Sprite Sprite) {
        Sprite.DrawSprite();
    }

    private Sprite createEnemy(GL gl) {
        int x = (int) (Math.random() * 91) + 6;
        int y = 105;
        Sprite enemy = new Sprite(gl, x, y, textures[4], 0.15f, 0.10f, (int) (Math.random() * 2));
        return enemy;
    }

    private ArrayList<Sprite> fall(ArrayList<Sprite> enemies, boolean up) {
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                //level 1
                Sprite enemy = enemies.get(i);
                enemy.setY(enemy.getY() - 1);
                respawnSprite(enemy);
                if (enemy.getY() < -30) {
                    enemies.remove(enemy);
                }
                /////////////////////////////////////////////level 1
                
                //check and change direction
                if (enemy.getX() > 94 && enemies.get(i).getMoveDirection() == 1) {
                    enemies.get(i).setMoveDirection(0);
                } else if (enemy.getX() < 6 && enemies.get(i).getMoveDirection() == 0) {
                    enemies.get(i).setMoveDirection(1);
                }
                
                //level 2
                if (enemy.getY() < 70) {
                    if (!up) {
                        //move left and right

                        if (enemies.get(i).getMoveDirection() == 0) {
                            enemies.get(i).setX(enemies.get(i).getX() - 2);
                        } else {
                            enemies.get(i).setX(enemies.get(i).getX() + 2);
                        }

                    } else {
                        if (enemies.get(i).getMoveDirection() == 0) {
                            enemies.get(i).setX(enemies.get(i).getX() - 1);
                        } else {
                            enemies.get(i).setX(enemies.get(i).getX() + 1);
                        }
                    }
                }
                /////////////////////////////////level 2
            }
        }
        return enemies;
    }

    private ArrayList<Sprite> backGroundLoop(ArrayList<Sprite> enemies) {
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                Sprite enemy = enemies.get(i);
                enemy.setY(enemy.getY() - 1);
                respawnSprite(enemy);
                if (enemy.getY() <= -50) {
                    enemies.get(i).setY(150);
                }
            }
        }
        return enemies;
    }

}
