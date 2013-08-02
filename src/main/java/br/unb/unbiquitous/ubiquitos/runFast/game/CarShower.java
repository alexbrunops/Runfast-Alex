package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CarShower extends GameObject{

	private static final String FONT       = "Helvetica";
	private static final int    FONT_STYLE = Font.BOLD;
	private static final int    FONT_SIZE  = 14;
	
	private static final String TXT_TEAM    = "Time ";
	private static final String TXT_SPEED   = "Velocidade: ";
	private static final String TXT_FUEL    = "Combustível: ";
	private static final String TXT_TRACKS  = "Voltas: ";
	private static final String TXT_POWER   = "Força: ";
	private static final String TXT_DEFENSE = "Defesa: ";
	private static final String TXT_LIFE    = "Dano: ";
	private static final String TXT_MONEY   = "Dinheiro: ";

	private String teamName;
	private Car car;
	private Team team;
	
	private Image equipPowerImage, equipDefenseImage, equipSpeedImage;
	
	public CarShower(int x, int y, Team team) {
		super(x, y, 0, 0);
		
		this.team = team;
		this.car = team.getCar();
		teamName = " "+(team.getThisTeamNumber()+1);
		
		ImageIcon ii = new ImageIcon(getClass().getResource("../images/equips/gun.png"));
		equipPowerImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource("../images/equips/tank.png"));
		equipDefenseImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource("../images/equips/wheel.png"));
		equipSpeedImage = ii.getImage();
	}

	@Override
	public int update(int dt) {
		return 0;
	}

	@Override
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		Graphics2D gShower = (Graphics2D) g.create();
		
		Font font = new Font(FONT, FONT_STYLE, FONT_SIZE);
        FontMetrics metr = panel.getFontMetrics(font);

        gShower.setColor(Color.WHITE);
        gShower.setFont(font);

        gShower.drawString(TXT_TEAM   +teamName         , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*2);
        gShower.drawString(TXT_SPEED  +car.getSpeed()   , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*3);
        //gShower.drawString(TXT_FUEL   +car.getFuel()    , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*3);
        gShower.drawString(TXT_TRACKS +car.getLaps()    , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*4);
        gShower.drawString(TXT_POWER  +car.getAttack()  , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*5);
        gShower.drawString(TXT_DEFENSE+car.getDefense() , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*6);
        gShower.drawString(TXT_LIFE   +car.getLife()    , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*7);
        gShower.drawString(TXT_MONEY  +car.getMoney()   , box.x+cameraX, box.y+cameraY +(metr.getHeight()+5)*8);

        gShower.drawImage(equipPowerImage, box.x+cameraX,box.y+cameraY +(metr.getHeight()+5)*9, panel);
        gShower.drawString("x"+car.getEquipPower(),
        		box.x+cameraX+equipPowerImage.getWidth(null),
        		box.y+cameraY +(metr.getHeight()+5)*9);
        gShower.drawImage(equipDefenseImage, box.x+cameraX+40,box.y+cameraY +(metr.getHeight()+5)*9, panel);
        gShower.drawString("x"+car.getEquipDefense(),
        		box.x+cameraX+equipDefenseImage.getWidth(null)+40,
        		box.y+cameraY +(metr.getHeight()+5)*9);
        gShower.drawImage(equipSpeedImage, box.x+cameraX+80,box.y+cameraY +(metr.getHeight()+5)*9, panel);
        gShower.drawString("x"+car.getEquipSpeed(),
        		box.x+cameraX+equipSpeedImage.getWidth(null)+80,
        		box.y+cameraY +(metr.getHeight()+5)*9);
        
        if(car.getItemImage()!=null) {
        	gShower.fillRect(box.x+cameraX+5,
        			box.y+cameraY +(metr.getHeight()+5)*9+equipDefenseImage.getHeight(null)+5, 35, 35);
        	gShower.drawImage(car.getItemImage(),box.x+cameraX+5,
        			box.y+cameraY +(metr.getHeight()+5)*9+equipDefenseImage.getHeight(null)+5,panel);
        }else
        	gShower.drawRect(box.x+cameraX+5,
        			box.y+cameraY +(metr.getHeight()+5)*9+equipDefenseImage.getHeight(null)+5, 35, 35);
        
        if(car.isEnableOption()) {
        	for(int i = 0; i<Map.getInstance().getNumberOfTeams(); ++i) {
        		gShower.drawString(""+(i+1),box.x+cameraX+5+i*15,
            			box.y+cameraY +(metr.getHeight()+5)*10+
            			equipDefenseImage.getHeight(null)+5+35);
        	}
        	int chosen = car.getOption();
        	gShower.setColor(Color.BLUE);
        	gShower.drawString(""+(chosen+1),box.x+cameraX+5+chosen*15,
        			box.y+cameraY +(metr.getHeight()+5)*10+
        			equipDefenseImage.getHeight(null)+5+35);
        	gShower.setColor(Color.WHITE);
        }
        
        //Draws the time counter to make the car reaper. 
        if(car.isDead()){
        	font = new Font(FONT, FONT_STYLE, FONT_SIZE+10);
        	gShower.setFont(font);
        	gShower.setColor(Color.YELLOW);
        	gShower.drawString(""+(5000-car.getDeadTime())/1000, box.x+cameraX+20,
        			box.y+cameraY+(metr.getHeight()+5)*11+
        			equipDefenseImage.getHeight(null)+5+35);
        }
        
        Toolkit.getDefaultToolkit().sync();
    	gShower.dispose();
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}
	
}
