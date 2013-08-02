package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Equip extends GameObject{

	private static final String GENERAL_PATH = "../images/equips/";
	
	public static final int EQUIP_TYPE_POWER   = 0x0001;
	public static final int EQUIP_TYPE_DEFENSE = 0x0002;
	public static final int EQUIP_TYPE_SPEED   = 0x0003;
	public static final int EQUIP_TYPE_MONEY   = 0x0004;
	
	public static final int EQUIP_BONUS_POWER   = 2;
	public static final int EQUIP_BONUS_DEFENSE = 2;
	public static final int EQUIP_BONUS_SPEED   = 20;
	public static final int EQUIP_BONUS_MONEY   = 20;
	
	private Image equipImage;
	
	private int equipType;
	
	public Equip(int x, int y, int equipType) {
		super(x, y, 0, 0);
		
		this.equipType = equipType;
		
		initEquip();
		
		box.width = equipImage.getWidth(null);
        box.height = equipImage.getHeight(null);
	}
	
	private void initEquip() {
		ImageIcon ii = null;
		switch (equipType) {
			case EQUIP_TYPE_POWER:
				ii = new ImageIcon(getClass().getResource(GENERAL_PATH+"gun.png"));
				break;
			case EQUIP_TYPE_DEFENSE:
				ii = new ImageIcon(getClass().getResource(GENERAL_PATH+"tank.png"));
				break;
			case EQUIP_TYPE_SPEED:
				ii = new ImageIcon(getClass().getResource(GENERAL_PATH+"wheel.png"));
				break;
			case EQUIP_TYPE_MONEY:
				ii = new ImageIcon(getClass().getResource(GENERAL_PATH+"coin.png"));
				break;
			default:
				ii = new ImageIcon(getClass().getResource(GENERAL_PATH+"coin.png"));
				break;
		}
        this.equipImage = ii.getImage();
	}

	@Override
	public int update(int dt) {
		return 0;
	}

	@Override
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		Graphics2D gEquip = (Graphics2D) g.create();
		gEquip.drawImage(equipImage, getX()+cameraX, getY()+cameraY, panel);
		gEquip.dispose();
	}
	
	public int getEquipType() {
		return equipType;
	}

}
