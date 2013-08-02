package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public abstract class GameObject {
	
	protected Rectangle box;
	
	public GameObject(int x, int y, int w, int h) {
		box = new Rectangle(x,y,w,h);
	}
    
	//Abstract methods
	public abstract int update(int dt);
    public abstract void render(Graphics2D g,int cameraX,int cameraY,JPanel panel);
    
    /**
     * Verifies if collides with other GameObject
     * @param other
     * @return true if collides with the other GameObject
     * and false otherwise.
     */
    public boolean collidesWith(GameObject other) {
    	return box.intersects(other.getBounds());
    }
    
    public int getX() {
    	return box.x;
    }
    public int getY() {
    	return box.y;
    }
    public int getWidth() {
    	return box.width;
    }
    public int getHeight() {
    	return box.height;
    }
    public Rectangle getBounds() {
        return box;
    }
	
}
