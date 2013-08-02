package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Bullet extends GameObject{

	public static final double BULLET_SPEED = 0.25;
	
	private static Image bulletImage = null;
	
	private double rotation;
	
	private boolean marked;
	
	public Bullet(int x, int y, double rotation) {
		super(x, y, 0, 0);
		
		if(bulletImage==null){
			ImageIcon ii = new ImageIcon(getClass().getResource("../images/game/bullet.png"));
			bulletImage = ii.getImage();
		}
		
		box.width = bulletImage.getWidth(null);
		//box.height = bulletImage.getHeight(null);
		box.height = box.width;
		
		this.rotation = rotation*Math.PI/180;
		marked = false;
		
	}

	@Override
	public int update(int dt) {
		box.x += (dt*BULLET_SPEED*Math.cos(rotation));
		box.y += (dt*BULLET_SPEED*Math.sin(rotation));
		
		return 0;
	}

	@Override
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		Graphics2D gBullet = (Graphics2D) g.create();
		gBullet.translate(box.x+cameraX + box.width/2,
				box.y+cameraY + box.height/2);
		gBullet.rotate(rotation);
		gBullet.drawImage(bulletImage, -box.width/2, -box.height/2, panel);
		gBullet.dispose();
	}
	
	/**
	 * Mark a bullet to know that it has collided or is out of bounds.
	 */
	public void mark() {
		marked = true;
	}
	
	public boolean isMarked() {
		return marked;
	}

	/**
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	
}
