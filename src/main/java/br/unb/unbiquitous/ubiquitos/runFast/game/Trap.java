package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Trap extends GameObject{

	private static final String GENERAL_PATH = "../images/items/";
	public static final String GENERAL_TRAPS_PATH = GENERAL_PATH+"traps/";
	
	public static final String BOMB_PATH = GENERAL_TRAPS_PATH+"bomb.png";
	public static final String OIL_PATH = GENERAL_TRAPS_PATH+"oil.png";
	
	public static final int TRAP_TYPE_BOMB = 0;
	public static final int TRAP_TYPE_OIL = 1;
	
	
	private Image trapImage;
	
	private int trapType;
	
	private int avaible = 30;
	
	public Trap(int x, int y,int trapType) {
		super(x, y, 0, 0);
		
		this.trapType = trapType;
		initTrap();
		box.width = trapImage.getWidth(null);
		box.height = trapImage.getHeight(null);
	}
	
	private void initTrap() {
		ImageIcon ii = new ImageIcon(getClass().getResource(OIL_PATH));
		switch (trapType) {
			case TRAP_TYPE_OIL:
				ii = new ImageIcon(getClass().getResource(OIL_PATH));
				break;
			case TRAP_TYPE_BOMB:
				ii = new ImageIcon(getClass().getResource(BOMB_PATH));
				break;

			default:
				//ii = new ImageIcon(getClass().getResource(BOMB_PATH));
				break;
		}
		trapImage = ii.getImage();
	}

	@Override
	public int update(int dt) {
		if(avaible>0)
			avaible--;
		return 0;
	}

	@Override
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		Graphics2D gTrap = (Graphics2D) g.create();
		gTrap.drawImage(trapImage, getX()+cameraX, getY()+cameraY, panel);
		gTrap.dispose();
	}
	
	public boolean isAvaible(){
		if(avaible==0)
			return true;
		return false;
	}

}
