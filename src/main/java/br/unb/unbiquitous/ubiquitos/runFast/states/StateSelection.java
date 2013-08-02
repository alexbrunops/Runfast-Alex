package br.unb.unbiquitous.ubiquitos.runFast.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;

import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesController;
import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesEvent;
import br.unb.unbiquitous.ubiquitos.runFast.game.CarTemplate;
import br.unb.unbiquitous.ubiquitos.runFast.game.Team;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputEvent;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputListener;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputManager;
import br.unb.unbiquitous.ubiquitos.runFast.ui.Window;

public class StateSelection extends State implements InputListener{

	/**
	 * private static final long serialVersionUID = -7349476558557554499L;
	 */
	private static final long serialVersionUID = -7349476558557554499L;
	
	/*Specifications to the menu items display:*/
	private static final String FONT = "Helvetica";
	private static final int FONT_STYLE = Font.BOLD;
	private static final int FONT_SIZE = 14;
	
	private static final String MENU_IMAGE_PATH = "../images/image_menu.jpg";
	
	private static final String MSG_SPEED        = "Velocidade";
	private static final String MSG_ACCELERATION = "Aceleração";
	private static final String MSG_TURN         = "Curva";
	private static final String MSG_POWER        = "Ataque";
	private static final String MSG_DEFENSE      = "Defesa";
	
	private static final int RANDOM_NUMBER = 9;
	
	private Image background;
	
	private List<UpDevice> pilots;
	private List<Integer> pilotsPosition;
	private List<Boolean> pilotsClosed;
	private Color[] pilotsColors;
	private CarTemplate[] cars;
	
	public StateSelection(){
		InputManager.GetInstance().addInputListener(this);
		setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        
        background = null;
        
        initPilots();
        initPilotsColors();
        initCarsTemplates();
	}
	
	private void initPilots() {
		pilots = new ArrayList<UpDevice>();
		pilotsPosition = new ArrayList<Integer>();
		pilotsClosed = new ArrayList<Boolean>();
	}
	
	private void initPilotsColors() {
		pilotsColors = new Color[4];
        pilotsColors[0] = Color.BLUE;
        pilotsColors[1] = Color.RED;
        pilotsColors[2] = Color.GREEN;
        pilotsColors[3] = Color.MAGENTA;
	}
	
	private void initCarsTemplates() {
		cars = new CarTemplate[9];
        cars[0] = new CarTemplate(CarTemplate.CAR_BLUE);
        cars[1] = new CarTemplate(CarTemplate.CAR_RED);
        cars[2] = new CarTemplate(CarTemplate.CAR_BLUERED);
        cars[3] = new CarTemplate(CarTemplate.CAR_BLUEWHITE);
        cars[4] = new CarTemplate(CarTemplate.CAR_GREENWHITE);
        cars[5] = new CarTemplate(CarTemplate.CAR_REDYELLOW);
        cars[6] = new CarTemplate(CarTemplate.CAR_WHITE);
        cars[7] = new CarTemplate(CarTemplate.CAR_F1BLUE);
        cars[8] = new CarTemplate(CarTemplate.CAR_F1RED);
        
	}
	
	@Override
	public void load(DevicesController devController) {
		super.load(devController);
		
		ImageIcon ii = new ImageIcon(getClass().getResource(MENU_IMAGE_PATH));
        this.background = ii.getImage();
        
        loadPilots();
	}
	
	private void loadPilots() {
		List<Team> teams = devicesController.getTeams();
		
		if(pilots.size() < teams.size()) {
			for(int i=pilots.size(); i<teams.size(); ++i) {
				pilots.add(teams.get(i).getPilot());
				pilotsPosition.add(0);
				pilotsClosed.add(false);
			}
		}
	}
	

	@Override
	public void load(DevicesController devController, Stack stack) {
		load(devController);
	}

	@Override
	public Stack unload() {
		super.unload();
		
		InputManager.GetInstance().removeInputListener(this);
		CarTemplate cars[] = new CarTemplate[pilots.size()];
		
		for(int i=0;i<cars.length;++i) {
			cars[i] = this.cars[pilotsPosition.get(i)];
		}
		
		return new Stack(pilots,cars,pilots.size());
	}

	@Override
	public int update(int dt) {
		boolean test = true;
		for(int i=0;i<pilotsClosed.size();++i) {
			if(!pilotsClosed.get(i))
				test = false;
		}
		if(!test)
			return StateManager.SAME_STATE;
		else
			return StateManager.STATE_GAME;
	}

	@Override
	public void render() {
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D gBackground = (Graphics2D)g.create();
        if(background!=null)
        	gBackground.drawImage(background, 0, 0, this);
        
        printCarsOptions((Graphics2D)g.create());
        
        Graphics2D gBigs = (Graphics2D)g.create();
        
        printBigsOptions(gBigs);
        
        Toolkit.getDefaultToolkit().sync();
        gBackground.dispose();
        gBigs.dispose();
        g.dispose();
	}
	
	private void printCarsOptions(Graphics2D g) {
		Graphics2D gCars = (Graphics2D)g.create();
		
		gCars.fillRect(465, 140, 350, 380);
		gCars.setColor(Color.WHITE);
		int i,dx,dy;
		for(i=0;i<cars.length;++i) {
			dx = (i%2)*175;
			dy= (i/2)*76;
			gCars.drawRect(465+dx, 140+dy, 175, 76);
			gCars.drawImage(cars[i].getSelectImage(), 485+dx, 147+dy, this);
		}
		gCars.drawRect(640, 444, 175, 76);
		
		for(i=0;i<pilotsPosition.size();++i) {
			gCars.setColor(pilotsColors[i]);
			gCars.drawRect(465 + ((pilotsPosition.get(i)%2)*175),
					140 + ((pilotsPosition.get(i)/2)*76), 175, 76);
		}
		
		gCars.dispose();
	}
	
	private void printBigsOptions(Graphics2D g) {
		Graphics2D gBigs = (Graphics2D)g.create();
		
		Font font = new Font(FONT, FONT_STYLE, FONT_SIZE);
		gBigs.setColor(Color.WHITE);
        int dx,dy;
        for(int i=0;i<pilotsPosition.size();++i) {
        	dx = (i%2)*814;
			dy= (i/2)*293;
			if(pilotsPosition.get(i)!=RANDOM_NUMBER){
				gBigs.drawImage(cars[pilotsPosition.get(i)].getFrontImage(), 41+dx, 75+dy, this);
				gBigs.drawRect(41+dx, 75+dy, 384, 218);
				printAttributes(56+dx, 90+dy, gBigs, font, pilotsPosition.get(i));
			}
		}
		
		gBigs.dispose();
	}

	private void printAttributes(int x, int y, Graphics2D g, Font font, int carPos) {
		if(carPos==9)
			return;
		
		Graphics2D gAttributes = (Graphics2D)g.create();
		Graphics2D gBar = (Graphics2D)g.create();
		Graphics2D gBorder = (Graphics2D)g.create();
		
		
		FontMetrics metr = this.getFontMetrics(font);
		int barX = x+90;
		int barY = y+metr.getHeight()/4+4;
		int barHeight = metr.getHeight()/2;
		int barMaxWidth = 100;
		
		gBar.setColor(Color.GREEN);
		gBorder.setColor(Color.WHITE);
		gAttributes.setColor(Color.WHITE);
		gAttributes.setFont(font);
		
		gAttributes.drawString(MSG_SPEED, x, y +metr.getHeight());
		gBar.fillRect(barX, barY,
				barMaxWidth*cars[carPos].getSpeedMax()/CarTemplate.MAX_SPEEDMAX, barHeight);
		gBorder.drawRect(barX, barY,
				barMaxWidth*cars[carPos].getSpeedMax()/CarTemplate.MAX_SPEEDMAX, barHeight);
		
		gAttributes.drawString(MSG_ACCELERATION, x, y +metr.getHeight()*2);
		gBar.fillRect(barX, barY+metr.getHeight(),
				barMaxWidth*cars[carPos].getAcceleration()/CarTemplate.MAX_ACCELERATION, barHeight);
		gBorder.drawRect(barX, barY+metr.getHeight(),
				barMaxWidth*cars[carPos].getAcceleration()/CarTemplate.MAX_ACCELERATION, barHeight);
		
		gAttributes.drawString(MSG_TURN, x, y +metr.getHeight()*3);
		gBar.fillRect(barX, barY+metr.getHeight()*2,
				(int) (barMaxWidth*cars[carPos].getRotateRate()/CarTemplate.MAX_ROTATERATE), barHeight);
		gBorder.drawRect(barX, barY+metr.getHeight()*2,
				(int) (barMaxWidth*cars[carPos].getRotateRate()/CarTemplate.MAX_ROTATERATE), barHeight);
		
		gAttributes.drawString(MSG_POWER, x, y +metr.getHeight()*4);
		gBar.fillRect(barX, barY+metr.getHeight()*3,
				barMaxWidth*cars[carPos].getPower()/CarTemplate.MAX_POWER, barHeight);
		gBorder.drawRect(barX, barY+metr.getHeight()*3,
				barMaxWidth*cars[carPos].getPower()/CarTemplate.MAX_POWER, barHeight);
		
		gAttributes.drawString(MSG_DEFENSE, x, y +metr.getHeight()*5);
		gBar.fillRect(barX, barY+metr.getHeight()*4,
				barMaxWidth*cars[carPos].getDefense()/CarTemplate.MAX_DEFENSE, barHeight);
		gBorder.drawRect(barX, barY+metr.getHeight()*4,
				barMaxWidth*cars[carPos].getDefense()/CarTemplate.MAX_DEFENSE, barHeight);
		
		gAttributes.dispose();
		gBar.dispose();
		gBorder.dispose();
	}
	
	/**
	 * Gets the inputEvents and make the necessary actions.
	 */
	public void inputPerformed(InputEvent e) {
		
		int pos=0;
		boolean test = false;
		for(int i=0;i<pilots.size();++i){
			if(e.getDevice().getName().equals(pilots.get(i).getName())) {
				pos = i;
				test=true;
			}
		}
		if(!test)
			return;
		
		if((pilotsClosed.get(pos))&&(e.getInputCode()!=InputEvent.IC_ACTION2))
			return;
		
		switch(e.getInputCode()){
			case InputEvent.IC_UP:
				if(pilotsPosition.get(pos)>1)
					pilotsPosition.set(pos, pilotsPosition.get(pos) - 2);
				break;
			case InputEvent.IC_DOWN:
				if(pilotsPosition.get(pos)<8)
					pilotsPosition.set(pos, pilotsPosition.get(pos) +2);
				break;
			case InputEvent.IC_LEFT:
				if(pilotsPosition.get(pos)%2 == 1)
					pilotsPosition.set(pos, pilotsPosition.get(pos) -1);
				break;
			case InputEvent.IC_RIGHT:
				if(pilotsPosition.get(pos)%2 == 0)
					pilotsPosition.set(pos, pilotsPosition.get(pos) +1);
				break;
			case InputEvent.IC_ACTION:
				Random generator = new Random();
				if(pilotsPosition.get(pos)==RANDOM_NUMBER)
					pilotsPosition.set(pos, Math.abs(generator.nextInt()%9));
				pilotsClosed.set(pos, true);
				break;
			case InputEvent.IC_ACTION2:
				pilotsClosed.set(pos, false);
				break;
			default:
				break;
		}
	}

	public void inputReleased(InputEvent e) {}

	
	public void deviceEntered(DevicesEvent e) {
		loadPilots();
	}

	public void deviceGotOut(DevicesEvent e) {
		List<Team> teams = devicesController.getTeams();
		
		if(pilots.size() > teams.size()) {
			for(int i=0; i<pilots.size(); ++i) {
				if(pilots.get(i)==e.getDevice()){
					pilots.remove(i);
					pilotsPosition.remove(i);
					pilotsClosed.remove(i);
				}
			}
		}
	}

}
