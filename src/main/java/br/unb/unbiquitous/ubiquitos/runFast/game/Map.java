package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesController;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputManager;
import br.unb.unbiquitous.ubiquitos.runFast.states.Stack;
import br.unb.unbiquitous.ubiquitos.runFast.states.StateManager;
import br.unb.unbiquitous.ubiquitos.runFast.ui.Window;

public class Map extends GameObject{

	private static final int MAP_DAMAGE = 5;

	private static final int MAX_ITEMS = 2;
	private static final int MAX_EQUIPS = 3;
	
	private static final String MAP_IMAGE_PATH = "../images/game/map.jpg";

	private static final int MAP_TIME = 120000;
	private static final int MAP_TEAM_INCREASE_TIME = 10000;
	
	private DevicesController devicesController;
	
	private Image background;
	
	private Random random = null;
	
	private int time;
	
	private List<Team> teams;
	private List<CarShower> teamsShowers;
	private List<Equip> equips;
	private List<Item> items;
	private List<Trap> traps;

	//Exterior Bounds:
	Line2D lineL = new Line2D.Float(173, 447, 173, 212);
	Line2D lineT = new Line2D.Float(298, 87, 1000, 87);
	Line2D lineR = new Line2D.Float(1125, 212, 1125, 447);
	Line2D lineB = new Line2D.Float(1000, 572, 298, 572);
	Line2D lineTLL = new Line2D.Float( 173, 212,  213, 127);
	Line2D lineTLT = new Line2D.Float( 213, 127,  298,  87);
	Line2D lineTRR = new Line2D.Float(1084, 127, 1125, 212);
	Line2D lineTRT = new Line2D.Float(1000,  87, 1084, 127);
	Line2D lineBLL = new Line2D.Float( 173, 446,  213, 531);
	Line2D lineBLB = new Line2D.Float( 213, 531,  298, 572);
	Line2D lineBRR = new Line2D.Float(1084, 531, 1125, 447);
	Line2D lineBRB = new Line2D.Float(1000, 572, 1084, 531);
	RoundRectangle2D exteriorBounds = new RoundRectangle2D.Float(166, 82, 962, 494, 300, 300);
	//Interior Bounds:
	RoundRectangle2D interiorBounds = new RoundRectangle2D.Float(290, 202, 720, 256, 90, 90);
	//Start line:
	Rectangle2D startBounds = new Rectangle2D.Float(450,456,20,120);
	
	private static Map instance = null;
	
	public static Map getInstance() {
		if(instance == null)
			instance = new Map();
		return instance;
	}
	
	private Map(){
		super(0,0,0,0);
	}
	
	public void load(DevicesController devController, int x, int y, Stack stack) {
		box.x = x;
		box.y = y;
		
		ImageIcon ii = new ImageIcon(getClass().getResource(MAP_IMAGE_PATH));
        this.background = ii.getImage();
        
        box.width = background.getWidth(null);
        box.height = background.getHeight(null);

        devicesController = devController;
        
        random = new Random();
        time = MAP_TIME;
        
        initFromStack(devController, stack);
	}
	
	private void initFromStack(DevicesController devController, Stack stack) {
		teams = devController.getTeams();
		teamsShowers = new ArrayList<CarShower>();
		equips = new ArrayList<Equip>();
		items = new ArrayList<Item>();
		traps = new ArrayList<Trap>();
		
		if(!stack.isEmpty()) {
			int i;
			for(i=0;i<stack.getLength();++i) {
				teams.get(i).initTeamCar(stack.getCars()[i]);
				InputManager.GetInstance().addInputListener(teams.get(i));
				teamsShowers.add(new CarShower(0+(i%2*1155),0+(i/2*300),teams.get(i)));
			}
			equips.add(new Equip(300, 150, Equip.EQUIP_TYPE_POWER));
	        equips.add(new Equip(900, 150, Equip.EQUIP_TYPE_DEFENSE));
	        equips.add(new Equip(400, 500, Equip.EQUIP_TYPE_SPEED));
	        equips.add(new Equip(800, 500, Equip.EQUIP_TYPE_MONEY));
	        
	        items.add(new Item(350,150));
	        items.add(new Item(350,180));
	        items.add(new Item(400,550));
	        items.add(new Item(800,4500));
		}
	}
	
	public Stack unload() {
		for(int i=0;i<teams.size();i++)
			InputManager.GetInstance().removeInputListener(teams.get(i));
		return new Stack();
	}

	@Override
	public int update(int dt) {
		time -= dt;
		
		generateEquips();
		generateItems();
		
		for(int i=0;i<teams.size();i++){
			teams.get(i).update(dt);
			if(teams.get(i).getCar()!=null)
				if(!teams.get(i).getCar().isDead())
					verifyCarsMapCollisions(teams.get(i));
		}
		verifyCarsCarsCollisions();
		verifyCarsShotsCollisions();
		verifyCarsEquipsCollisions();
		verifyCarsItemsCollisions();
		verifyCarsTrapsCollisions();
		verifyCarsLapsConclusion();
		
		for(int i=0;i<teamsShowers.size();i++)
			teamsShowers.get(i).update(dt);
		
		for(int i=0;i<equips.size();i++)
			equips.get(i).update(dt);
		
		for(int i=0;i<items.size();i++)
			items.get(i).update(dt);
		
		for(int i=0;i<traps.size();i++)
			traps.get(i).update(dt);
		
		return verifyGameTime();
	}

	@Override
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(background, getX()+cameraX, getY()+cameraY, panel);
		/*
		g2d.draw(exteriorBounds);
		g2d.draw(interiorBounds);
		g2d.draw(lineTLL);
		g2d.draw(lineTLT);
		g2d.draw(lineBLL);
		g2d.draw(lineBLB);
		g2d.draw(lineTRR);
		g2d.draw(lineTRT);
		g2d.draw(lineBRR);
		g2d.draw(lineBRB);
		g2d.draw(startBounds);
		*/

		for(int i=0;i<equips.size();i++)
			equips.get(i).render(g2d, 0, 0, panel);
		
		for(int i=0;i<items.size();i++)
			items.get(i).render(g2d, 0, 0, panel);
		
		for(int i=0;i<traps.size();i++)
			traps.get(i).render(g2d, 0, 0, panel);
		
		if(teams!=null){
			for(int i=0;i<teams.size();i++)
				teams.get(i).render(g2d, 0, 0, panel);
			for(int i=0;i<teamsShowers.size();i++)
				teamsShowers.get(i).render(g2d, 0, 0, panel);
		}
		
		//Write race time
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Helvetica", Font.BOLD, 24));
		g2d.drawString(""+(time/1000), Window.WINDOW_WIDTH/2 - 10, 50);
		
		g2d.dispose();
	}
	
	//Generators:
	private void generateEquips(){
		int chance = random.nextInt(200);
		if((equips.size()<(MAX_EQUIPS*teams.size()))&&(chance == 1)){
			switch(random.nextInt(4)){
			case 0://Left
				equips.add(new Equip(128+60+random.nextInt(80), 180+random.nextInt(280), random.nextInt(4)+1));
				break;
			case 1://Top
				equips.add(new Equip(128+150+random.nextInt(750), 100+random.nextInt(80), random.nextInt(4)+1));
				break;
			case 2://Right
				equips.add(new Equip(128+900+random.nextInt(80), 180+random.nextInt(280), random.nextInt(4)+1));
				break;
			case 3://Bottom
				equips.add(new Equip(128+150+random.nextInt(750), 470+random.nextInt(80), random.nextInt(4)+1));
				break;
			}
		}
	}
	
	private void generateItems(){
		int chance = random.nextInt(300);
		if((items.size()<(MAX_ITEMS*teams.size()))&&(chance == 1)){
			switch(random.nextInt(4)){
			case 0://Left
				items.add(new Item(128+60+random.nextInt(80), 180+random.nextInt(280)));
				break;
			case 1://Top
				items.add(new Item(128+150+random.nextInt(750), 100+random.nextInt(80)));
				break;
			case 2://Right
				items.add(new Item(128+900+random.nextInt(80), 180+random.nextInt(280)));
				break;
			case 3://Bottom
				items.add(new Item(128+150+random.nextInt(750), 470+random.nextInt(80)));
				break;
			}
		}
	}
	
	private int verifyGameTime(){
		if(time < -500){
			devicesController.endRace();
			return StateManager.STATE_WIN;
		}
		return StateManager.SAME_STATE;
	}
	
	/**
	 * Verify collision between the car and the Map bounds.
	 * @param team
	 */
	private void verifyCarsMapCollisions(Team team) {
		Rectangle teamBound = team.getCar().getBounds();
		if(interiorBounds.intersects(teamBound)) {
			if(team.getCar().makeCollision(teamBound.intersection(interiorBounds.getBounds())))
				team.getCar().receiveHit(MAP_DAMAGE);
		}else if(!exteriorBounds.contains(teamBound)) {
			//team.getCar().makeOutsideCollision(teamBound.intersection(exteriorBounds.getBounds()));
			boolean top = false, left = false, bottom = false, right = false;
			if(lineT.intersects(teamBound)||lineTLT.intersects(teamBound)||
					lineTRT.intersects(teamBound))
				top = true;
			if(lineL.intersects(teamBound)||lineTLL.intersects(teamBound)||
					lineBLL.intersects(teamBound))
				left = true;
			if(lineB.intersects(teamBound)||lineBLB.intersects(teamBound)||
					lineBRB.intersects(teamBound))
				bottom = true;
			if(lineR.intersects(teamBound)||lineTRR.intersects(teamBound)||
					lineBRR.intersects(teamBound))
				right = true;
			if(team.getCar().makeCollision(top, bottom, left, right))
				team.getCar().receiveHit(MAP_DAMAGE);
		}
	}
	
	/**
	 * Verify the collisions between the teams cars.
	 */
	private void verifyCarsCarsCollisions() {
		Car current, other;
		for(int i=0;i<teams.size();i++) {
			current = teams.get(i).getCar();
			if(current!=null){
				if(!current.isDead()){
					for(int j=i;j<teams.size();j++) {
						other = teams.get(j).getCar();
						if(other!=null){
							if((!other.isDead())&&(current != other)) {
								if(current.collidesWith(other)) {
									if(current.makeCollision(current.getBounds().intersection(other.getBounds())))
										current.receiveHit(other.getAttack());
									if(other.makeCollision(current.getBounds().intersection(current.getBounds())))
										other.receiveHit(current.getAttack());
								}
							}
						}
						
					}
				}
				
			}
		}
	}
	
	/**
	 * Verifies collision between shots and the cars.
	 */
	private void verifyCarsShotsCollisions() {
		Car current, other;
		List<Bullet> shots;
		//Take all cars shots
		for(int j=0;j<teams.size();j++) {
			other = teams.get(j).getCar();
			if(other!=null){
				shots = other.getShots();
				//pass through all the shots
				for(int k=0;k<shots.size();++k) {
					//pass through all the cars and verifies if it collides with the shot
					for(int i=0;i<teams.size();i++) {
						current = teams.get(i).getCar();
						if(current!=null){
							if((!current.isDead())&&(current!=other)) {
								if(current.collidesWith(shots.get(k))) {
									shots.get(k).mark();
									current.receiveShot(other.getAttack());
								}
							}
						}
					}
					//Verify if the shot is out of the map bound
					if(!exteriorBounds.contains(shots.get(k).getBounds()))
						shots.get(k).mark();
				}
			}//loop end
			
		}
	}
	
	private void verifyCarsEquipsCollisions() {
		Car current;
		for(int i=0;i<teams.size();i++) {
			current = teams.get(i).getCar();
			if(current!=null){
				if(!current.isDead()){
					for(int j=0;j<equips.size();j++) {
						if(equips.get(j).collidesWith(current)){
							current.receiveEquip(equips.get(j).getEquipType());
							equips.remove(j);
							--j;
						}
					}
				}
			}
			
		}
	}
	
	private void verifyCarsItemsCollisions() {
		Car current;
		for(int i=0;i<teams.size();i++) {
			current = teams.get(i).getCar();
			if(current!=null){
				if(!current.isDead()){
					for(int j=0;j<items.size();j++) {
						if(items.get(j).collidesWith(current)) {
							if(current.receiveItem(items.get(j))) {
								items.remove(j);
								--j;
							}
						}
					}
				}
			}
			
		}
	}
	
	private void verifyCarsTrapsCollisions() {
		Car current;
		for(int i=0;i<teams.size();i++) {
			current = teams.get(i).getCar();
			if(current!=null){
				if(!current.isDead()){
					for(int j=0;j<traps.size();j++) {
						if(traps.get(j).isAvaible())
							if(traps.get(j).collidesWith(current)) {
								current.receiveTrap(traps.get(j));
								traps.remove(j);
								--j;
							}
					}
				}
			}
			
		}
	}
	
	/**
	 * Verify if the cars are concluding a lap.
	 */
	private void verifyCarsLapsConclusion() {
		Car current;
		for(int i=0;i<teams.size();i++) {
			current = teams.get(i).getCar();
			if(current!=null){
				if(startBounds.intersects(current.getBounds())) {
					if(current.getBounds().getX()<startBounds.getX())
						current.intoLap(true);
					else
						current.intoLap(false);
				}else{
					if(current.getBounds().getX()<startBounds.getX())
						current.outOfLap(false);
					else
						current.outOfLap(true);
				}
			}
		}
	}
	
	/**
	 * Updates the teams list.
	 * Based in possible change of the size of teams.
	 */
	public void updateTeams(){
		//If there is a new team
		if(teamsShowers.size() < teams.size()){
			boolean found = false;
			for(int i=0; i<teams.size(); ++i){
				found = false;
				for(int j=0; j<teamsShowers.size(); ++j){
					if(teams.get(i)==teamsShowers.get(j).getTeam())
						found = true;
				}
				if(!found){
					//teams.get(i).initTeamCar(new CarTemplate(CarTemplate.CAR_BLUE));
					InputManager.GetInstance().addInputListener(teams.get(i));
					teamsShowers.add(new CarShower(0+(i%2*1155),0+(i/2*300),teams.get(i)));
					
					//increases game time
					time += MAP_TEAM_INCREASE_TIME;
				}
			}
		//If there is less teams 
		}else if(teamsShowers.size() > teams.size()){
			boolean found = false;
			for(int i=0; i<teamsShowers.size(); ++i){
				found = false;
				for(int j=0; j<teams.size(); ++j){
					if(teamsShowers.get(i).getTeam()==teams.get(j))
						found = true;
				}
				if(!found){
					InputManager.GetInstance().removeInputListener(teamsShowers.get(i).getTeam());
					teamsShowers.remove(i);
				}
			}
		}
	}
	
	public void addTrap(Trap trap) {
		traps.add(trap);
	}
	
	public int getNumberOfTeams() {
		return teams.size();
	}
	
	public Team getTeam(int number) {
		if(number > (teams.size()-1) || (number < 0))
			return null;
		return teams.get(number);
	}

	/**
	 * @return the devicesController
	 */
	public DevicesController getDevicesController() {
		return devicesController;
	}

}
