package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class CarTemplate {

	//Possible cars
	public static final int CAR_BLUE       = 0x0001;
	public static final int CAR_RED        = 0x0002;
	public static final int CAR_BLUERED    = 0x0003;
	public static final int CAR_BLUEWHITE  = 0x0004;
	public static final int CAR_GREENWHITE = 0x0005;
	public static final int CAR_REDYELLOW  = 0x0006;
	public static final int CAR_WHITE      = 0x0007;
	public static final int CAR_F1BLUE     = 0x0008;
	public static final int CAR_F1RED      = 0x0009;
	
	//Max attributes values
	public static final int MAX_SPEEDMAX     = 200;
	public static final int MAX_ACCELERATION = 5;
	public static final int MAX_ROTATERATE   = 5;
	public static final int MAX_FUEL         = 50;
	public static final int MAX_POWER        = 10;
	public static final int MAX_DEFENSE      = 10;
	public static final int MAX_LIFE         = 100;
	
	private static final String GENERAL_PATH = "../images/cars/";
	private static final String GENERAL_FRONT_PATH = "fronts/";
	private static final String GENERAL_CARS_PATH = "cars/";
	private static final String GENERAL_SMALLS_PATH = "smalls/";
	private static final String GENERAL_FRONT_IMAGE_NAME = "_carfront";
	private static final String GENERAL_SELECT_IMAGE_NAME = "_car";
	private static final String GENERAL_GAME_IMAGE_NAME = "_car_small";
	private static final String GENERAL_FORMAT = ".png"; 
	
	//0-BLUE
	private static final String BLUE_NAME = "blue";
	private static final int BLUE_SPEEDMAX     = 120;
	private static final int BLUE_ACCELERATION = 3;
	private static final int BLUE_ROTATERATE   = 2;
	private static final int BLUE_FUEL         = 50;
	private static final int BLUE_POWER        = 6;
	private static final int BLUE_DEFENSE      = 6;
	private static final int BLUE_LIFE         = 100;
	
	//1-RED
	private static final String RED_NAME = "red";
	private static final int RED_SPEEDMAX     = 120;
	private static final int RED_ACCELERATION = 2;
	private static final int RED_ROTATERATE   = 3;
	private static final int RED_FUEL         = 50;
	private static final int RED_POWER        = 6;
	private static final int RED_DEFENSE      = 6;
	private static final int RED_LIFE         = 100;
	
	//2-BLUERED
	private static final String BLUERED_NAME = "bluered";
	private static final int BLUERED_SPEEDMAX     = 100;
	private static final int BLUERED_ACCELERATION = 2;
	private static final int BLUERED_ROTATERATE   = 4;
	private static final int BLUERED_FUEL         = 50;
	private static final int BLUERED_POWER        = 4;
	private static final int BLUERED_DEFENSE      = 5;
	private static final int BLUERED_LIFE         = 100;
	
	//3-BLUEWHITE
	private static final String BLUEWHITE_NAME = "bluewhite";
	private static final int BLUEWHITE_SPEEDMAX     = 110;
	private static final int BLUEWHITE_ACCELERATION = 1;
	private static final int BLUEWHITE_ROTATERATE   = 1;
	private static final int BLUEWHITE_FUEL         = 50;
	private static final int BLUEWHITE_POWER        = 8;
	private static final int BLUEWHITE_DEFENSE      = 10;
	private static final int BLUEWHITE_LIFE         = 100;
	
	//4-GREENWHITE
	private static final String GREENWHITE_NAME = "greenwhite";
	private static final int GREENWHITE_SPEEDMAX     = 130;
	private static final int GREENWHITE_ACCELERATION = 3;
	private static final int GREENWHITE_ROTATERATE   = 2;
	private static final int GREENWHITE_FUEL         = 50;
	private static final int GREENWHITE_POWER        = 5;
	private static final int GREENWHITE_DEFENSE      = 5;
	private static final int GREENWHITE_LIFE         = 100;
	
	//5-REDYELLOW
	private static final String REDYELLOW_NAME = "redyellow";
	private static final int REDYELLOW_SPEEDMAX     = 120;
	private static final int REDYELLOW_ACCELERATION = 2;
	private static final int REDYELLOW_ROTATERATE   = 3;
	private static final int REDYELLOW_FUEL         = 50;
	private static final int REDYELLOW_POWER        = 6;
	private static final int REDYELLOW_DEFENSE      = 3;
	private static final int REDYELLOW_LIFE         = 100;
	
	//6-WHITE
	private static final String WHITE_NAME = "white";
	private static final int WHITE_SPEEDMAX     = 130;
	private static final int WHITE_ACCELERATION = 3;
	private static final int WHITE_ROTATERATE   = 3;
	private static final int WHITE_FUEL         = 50;
	private static final int WHITE_POWER        = 4;
	private static final int WHITE_DEFENSE      = 4;
	private static final int WHITE_LIFE         = 100;
	
	//7-F1BLUE
	private static final String F1BLUE_NAME = "f1blue";
	private static final int F1BLUE_SPEEDMAX     = 150;
	private static final int F1BLUE_ACCELERATION = 5;
	private static final int F1BLUE_ROTATERATE   = 5;
	private static final int F1BLUE_FUEL         = 50;
	private static final int F1BLUE_POWER        = 2;
	private static final int F1BLUE_DEFENSE      = 3;
	private static final int F1BLUE_LIFE         = 100;
	
	//8-F1RED
	private static final String F1RED_NAME = "f1red";
	private static final int F1RED_SPEEDMAX     = 150;
	private static final int F1RED_ACCELERATION = 5;
	private static final int F1RED_ROTATERATE   = 5;
	private static final int F1RED_FUEL         = 50;
	private static final int F1RED_POWER        = 3;
	private static final int F1RED_DEFENSE      = 2;
	private static final int F1RED_LIFE         = 100;
	
	//Images
	private Image frontImage,selectImage,gameImage;
	//Attributes
	private int speedMax, acceleration;
	private double rotateRate;
	private int fuel;
	private int attack, defense, life;
	
	public CarTemplate(int carType) {
		switch(carType) {
			case CAR_BLUE:
				initBlueCar();
				break;
			case CAR_RED:
				initRedCar();
				break;
			case CAR_BLUERED:
				initBlueRedCar();
				break;
			case CAR_BLUEWHITE:
				initBlueWhiteCar();
				break;
			case CAR_GREENWHITE:
				initGreenWhiteCar();
				break;
			case CAR_REDYELLOW:
				initRedYellowCar();
				break;
			case CAR_WHITE:
				initWhiteCar();
				break;
			case CAR_F1BLUE:
				initF1BlueCar();
				break;
			case CAR_F1RED:
				initF1RedCar();
				break;
		}
	}
	
	private void initBlueCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+BLUE_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+BLUE_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+BLUE_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = BLUE_SPEEDMAX;
		acceleration = BLUE_ACCELERATION;
		rotateRate = BLUE_ROTATERATE;
		fuel = BLUE_FUEL;
		attack = BLUE_POWER;
		defense = BLUE_DEFENSE;
		life = BLUE_LIFE;
	}

	private void initRedCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+RED_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+RED_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+RED_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = RED_SPEEDMAX;
		acceleration = RED_ACCELERATION;
		rotateRate = RED_ROTATERATE;
		fuel = RED_FUEL;
		attack = RED_POWER;
		defense = RED_DEFENSE;
		life = RED_LIFE;
	}
	
	private void initBlueRedCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+BLUERED_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+BLUERED_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+BLUERED_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = BLUERED_SPEEDMAX;
		acceleration = BLUERED_ACCELERATION;
		rotateRate = BLUERED_ROTATERATE;
		fuel = BLUERED_FUEL;
		attack = BLUERED_POWER;
		defense = BLUERED_DEFENSE;
		life = BLUERED_LIFE;
	}
	
	private void initBlueWhiteCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+BLUEWHITE_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+BLUEWHITE_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+BLUEWHITE_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = BLUEWHITE_SPEEDMAX;
		acceleration = BLUEWHITE_ACCELERATION;
		rotateRate = BLUEWHITE_ROTATERATE;
		fuel = BLUEWHITE_FUEL;
		attack = BLUEWHITE_POWER;
		defense = BLUEWHITE_DEFENSE;
		life = BLUEWHITE_LIFE;
	}
	
	private void initGreenWhiteCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+GREENWHITE_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+GREENWHITE_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+GREENWHITE_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = GREENWHITE_SPEEDMAX;
		acceleration = GREENWHITE_ACCELERATION;
		rotateRate = GREENWHITE_ROTATERATE;
		fuel = GREENWHITE_FUEL;
		attack = GREENWHITE_POWER;
		defense = GREENWHITE_DEFENSE;
		life = GREENWHITE_LIFE;
	}
	
	private void initWhiteCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+WHITE_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+WHITE_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+WHITE_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = WHITE_SPEEDMAX;
		acceleration = WHITE_ACCELERATION;
		rotateRate = WHITE_ROTATERATE;
		fuel = WHITE_FUEL;
		attack = WHITE_POWER;
		defense = WHITE_DEFENSE;
		life = WHITE_LIFE;
	}
	
	private void initRedYellowCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+REDYELLOW_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+REDYELLOW_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+REDYELLOW_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = REDYELLOW_SPEEDMAX;
		acceleration = REDYELLOW_ACCELERATION;
		rotateRate = REDYELLOW_ROTATERATE;
		fuel = REDYELLOW_FUEL;
		attack = REDYELLOW_POWER;
		defense = REDYELLOW_DEFENSE;
		life = REDYELLOW_LIFE;
	}
	
	private void initF1BlueCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+F1BLUE_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+F1BLUE_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+F1BLUE_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = F1BLUE_SPEEDMAX;
		acceleration = F1BLUE_ACCELERATION;
		rotateRate = F1BLUE_ROTATERATE;
		fuel = F1BLUE_FUEL;
		attack = F1BLUE_POWER;
		defense = F1BLUE_DEFENSE;
		life = F1BLUE_LIFE;
	}
	
	private void initF1RedCar() {
		ImageIcon ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_FRONT_PATH+F1RED_NAME+GENERAL_FRONT_IMAGE_NAME+GENERAL_FORMAT));
		frontImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_CARS_PATH+F1RED_NAME+GENERAL_SELECT_IMAGE_NAME+GENERAL_FORMAT));
		selectImage = ii.getImage();
		ii = new ImageIcon(getClass().getResource(
				GENERAL_PATH+GENERAL_SMALLS_PATH+F1RED_NAME+GENERAL_GAME_IMAGE_NAME+GENERAL_FORMAT));
		gameImage = ii.getImage();
		
		speedMax = F1RED_SPEEDMAX;
		acceleration = F1RED_ACCELERATION;
		rotateRate = F1RED_ROTATERATE;
		fuel = F1RED_FUEL;
		attack = F1RED_POWER;
		defense = F1RED_DEFENSE;
		life = F1RED_LIFE;
	}
	
	
	//Attributes and Images Getters
	public Image getFrontImage() {
		return frontImage;
	}

	public Image getSelectImage() {
		return selectImage;
	}

	public Image getGameImage() {
		return gameImage;
	}

	public int getSpeedMax() {
		return speedMax;
	}

	public int getAcceleration() {
		return acceleration;
	}

	public double getRotateRate() {
		return rotateRate;
	}

	public int getFuel() {
		return fuel;
	}

	public int getPower() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getLife() {
		return life;
	}
}
