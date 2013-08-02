package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Car extends GameObject{

	public static final int ROTATE_SPECIFIC = 0x0002;
	public static final int ROTATE_NONE  = 0x0000;
	public static final int ROTATE_RIGHT = 0x0001;
	public static final int ROTATE_LEFT  = 0x0010;
	public static final int ROTATE_UP    = 0x0100;
	public static final int ROTATE_DOWN  = 0x1000;
	
	private static final int DECELERATION_RATE = 1;
	private static final int SPEED_MODIFIER = 22;
	
	private static final int BLOCKED_SPEED = 60;
	
	private static final int SHOT_BOMB_DELAY = 400;
	
	private static final String SIGHT_IMAGE = "../images/game/sight.png";
	
	//Images draw in the panel
	private Image carImage, sightImage;
	private Animation explosion;
	//Dead information
	private boolean isDead;
	private int deadTime;
	
	//Used to keep the original box informations
	private Rectangle originalBox;
	
	//List of shots
	private List<Bullet> shots;
	private int shotDelay, bombDelay;
	
	//Collision informations
	private boolean collided;
	private int topCollision, leftCollision, bottomCollision, rightCollision;
	
	//Attributes
	private int speed, speedMax, acceleration;
	private boolean accelerating, isSpeedBlocked;
	private double rotation, rotationSpecific, sightRotation, sightRotationSpecific, rotateRate;
	private int rotateSense, sightRotateSense;
	private int fuel;
	private int laps;
	private boolean inLap, inLapCorrect;
	private int attack, defense, life;
	private int money;
	//Equips
	private int equipPower,equipDefense,equipSpeed;
	//Items
	private Item item;
	
	//Used to make an item selection:
	//When the selection is enabled(enableOption) it can switch between the choices(option saves the choice number).
	private boolean enableOption;
	private int option;
	
	public Car(int x, int y, CarTemplate carType) {
		super(x, y, 0, 0);

		initImages(carType);
		initDeadInfos();
		
		box.width = carImage.getWidth(null);
		box.height = carImage.getHeight(null);
		
		originalBox = new Rectangle(box);
		
		initAttributes(carType);
		initColliders();
		shots = new ArrayList<Bullet>();
		enableOption = false;
		option = 0;
	}
	
	/**
	 * Initialize the cars images based in the carType.
	 * @param carType
	 */
	private void initImages(CarTemplate carType) {
		
		carImage = carType.getGameImage();
		
		ImageIcon ii = new ImageIcon(getClass().getResource(SIGHT_IMAGE));
		sightImage = ii.getImage();
	}
	
	/**
	 * Initiate the Dead informations
	 */
	private void initDeadInfos(){
		explosion = new Animation(0, 0);
		//Dead information
		resetDead();
	}
	
	private void resetDead(){
		explosion.resetAnimation();
		isDead = false;
		deadTime = 0;
	}
	
	/**
	 * Initialize the car attributes with the carTemplate.
	 */
	private void initAttributes(CarTemplate carType) {
		speed = 0;
		speedMax = carType.getSpeedMax();
		isSpeedBlocked = false;
		acceleration = carType.getAcceleration();
		accelerating = false;
		rotation = 0.0;
		rotateRate = carType.getRotateRate();
		rotateSense = ROTATE_NONE;
		sightRotation = 0.0;
		sightRotateSense = ROTATE_NONE;
		rotationSpecific = sightRotationSpecific = 0.0;//Initiate the specific rotation to 0
		fuel = carType.getFuel();
		laps = 0;
		inLap = inLapCorrect = false;
		attack = carType.getPower();
		defense = carType.getDefense();
		life = carType.getLife();
		money = 0;
		//Equips
		equipPower = equipDefense = equipSpeed = 0;
		//Items
		item = null;
	}
	
	/**
	 * Initialize the cars collisions to none.
	 */
	private void initColliders() {
		collided = false;
		topCollision    = 0;
		leftCollision   = 0;
		bottomCollision = 0;
		rightCollision  = 0;
	}

	/**
	 * Update the cars actions in the current frame.
	 */
	@Override
	public int update(int dt) {
		shotDelay += dt;
		bombDelay += dt;
		
		verifyIfIsDead();
		if(isDead){
			deadAction(dt);
			return 0;
		}
		
		if(accelerating)
			accelerate();
		else
			decelerate();
		
		//Rotates the car and its colisionShape(box)
		rotation = rotate(rotation,rotateSense,rotationSpecific);
		sightRotation = rotate(sightRotation,sightRotateSense,sightRotationSpecific);
		rotateBoxBounds();
		
		//Moves the car
		move();
		
		//Updates shots
		removeMarkedShots();
		for(int i =0;i<shots.size();++i)
			shots.get(i).update(dt);
		
		return 0;
	}
	
	//Dead actions:
	private void verifyIfIsDead(){
		if(life<1){
			isDead = true;
			life = 100;
			explosion.setX(getX() + originalBox.width/2 - (originalBox.width - box.width)/2);
			explosion.setY(getY() + originalBox.height/2 - (originalBox.height - box.height)/2);
			speed = 0;
			unEquipAll();
		}
	}
	
	private void deadAction(int dt){
		if(explosion.isDone()){
			deadTime += dt;
			if(deadTime>5000)
				resetDead();
		}else{
			explosion.update(dt);
		}
	}
	

	/**
	 * Renders the car, its sight and the shots.
	 */
	@Override
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		if(isDead){
			if(!explosion.isDone())
				explosion.render(g, cameraX, cameraY, panel);
			return;
		}
		
		Graphics2D gCar = (Graphics2D) g.create();
		gCar.translate(getX()+cameraX + originalBox.width/2 - (originalBox.width - box.width)/2,
				getY()+cameraY + originalBox.height/2 - (originalBox.height - box.height)/2);
		gCar.rotate(3.14);
		/* Creates a new Graphics for the sight */
		Graphics2D gSight = (Graphics2D) gCar.create();
		/* Draw carImage */
		gCar.rotate(rotation*Math.PI/180);
		gCar.drawImage(carImage, -originalBox.width/2, -originalBox.height/2, panel);
		/* Draw sightImage */
		gSight.rotate(sightRotation*Math.PI/180);
		gSight.drawImage(sightImage, -sightImage.getWidth(null)/2,
				-sightImage.getHeight(null)/2, panel);
		/* Disposes all the graphics */
		gSight.dispose();
		gCar.dispose();
		//g.draw(originalBox);
		//g.draw(box);
		
		//Updates shots
		for(int i =0;i<shots.size();++i)
			shots.get(i).render(g,cameraX,cameraY,panel);
	}

	/**
	 * Make collisions occurs, detecting where was the collision
	 * based in the intersection and attributing to the right side
	 * the effects of collision.
	 * @param intersection
	 * @return true if collided, false otherwise
	 */
	protected boolean makeCollision(Rectangle intersection){
		boolean collided=false;
		if(smoothCollision(intersection)){
			
			Rectangle i = intersection;
			Rectangle b = box;

			boolean isIxEqualsBx   = (i.x == b.x);//f==!a
			boolean isIyEqualsBy   = (i.y == b.y);//c==!d
			boolean isIxwEqualsBxw = (i.x+i.width  == b.x+b.width);//right bound equals b i==!b
			boolean isIyhEqualsByh = (i.y+i.height == b.y+b.height);//inferior bound equals b ==!e
			boolean isIwEqualsBw   = (i.width  == b.width);//g
			boolean isIhEqualsBh   = (i.height == b.height);//h
			
			boolean isIwBiggerIh = false;
			if(i.width!=0 && i.height !=0)
				isIwBiggerIh = (b.width/i.width < b.height/i.height);//bigger proportion
			
			//TOP
			if((isIwEqualsBw && isIyEqualsBy)||
					(!isIxEqualsBx && !isIxwEqualsBxw && isIyEqualsBy)||
					((isIwBiggerIh && isIyEqualsBy) && (isIxEqualsBx || isIxwEqualsBxw))) {
				++topCollision;
			}
			//LEFT
			else if((isIhEqualsBh && isIxEqualsBx)||
					(!isIyEqualsBy && !isIyhEqualsByh && isIxEqualsBx)||
					(!isIwBiggerIh && isIxEqualsBx)){
				++leftCollision;
			}
			//BOTTOM
			else if((isIwEqualsBw && !isIyEqualsBy)||
					(!isIxEqualsBx && !isIxwEqualsBxw && !isIyEqualsBy)||
					((isIwBiggerIh && !isIyEqualsBy) && (isIxEqualsBx || isIxwEqualsBxw))){
				++bottomCollision;
			}
			//RIGHT
			else {
				++rightCollision;
			}
			
			this.collided = collided = true;
			
		}
		return collided;
	}
	
	/**
	 * Makes collision based in the parameters received.
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 * @return true if collided, false otherwise.
	 */
	protected boolean makeCollision(boolean top, boolean bottom, boolean left, boolean right){
		boolean collided = false;
		
		//TOP
		if(top){
			++topCollision;
		}
		//LEFT
		if(left){
			++leftCollision;
		}
		//BOTTOM
		if(bottom){
			++bottomCollision;
		}
		//RIGHT
		if(right){
			++rightCollision;
		}
		if(top||bottom||left||right)
			this.collided = collided = true;
			
		return collided;
	}
	
	/**
	 * Smoothes the collision detection making the collision more real.
	 * @param intersection
	 * @return true if collision occurred false otherwise.
	 */
	private boolean smoothCollision(Rectangle intersection) {
		int width, height;
		Rectangle rect1,rect2;
		if(isTurnedUp(rotation)||isTurnedDown(rotation)) {
			width = box.width-originalBox.height;
			height = box.width/2;
		}else{
			width = box.width/2;
			height = box.height - originalBox.height;
		}
		if((rotation>0 && rotation<90)||(rotation>180 && rotation<270)) {
			rect1 = new Rectangle(box.x+box.width - width,box.y,width,height);
			rect2 = new Rectangle(box.x,box.y+box.height-height,width,height);
		}else{
			rect1 = new Rectangle(box.x,box.y,width,height);
			rect2 = new Rectangle(box.x+box.width - width,box.y+box.height-height,width,height);
		}
		
		if(rect1.contains(intersection)||rect2.contains(intersection))
			return false;
		else
			return true;
	}

	/**
	 * Executes the result movement a front collision inflicts in the car.
	 * @param n
	 */
	private void calculateFrontCollision(int n) {
		if(n>0){
			speed -= 10 + n*5;
			
			box.x -= (n*6*Math.cos(rotation * Math.PI/180));
			box.y -= (n*6*Math.sin(rotation * Math.PI/180));
		}
	}
	private void calculateBackCollision(int n) {
		if(n>0){
			speed += 10 + n*2;
			
			box.x += (n*2*Math.cos(rotation * Math.PI/180));
			box.y += (n*2*Math.sin(rotation * Math.PI/180));
		}
	}
	private void calculateLeftSideCollision(int n) {
		if(n>0){
			speed -= 3 + n*3;
			
			if(isTurnedUp(rotation))
				box.x += n*3;
			else if(isTurnedDown(rotation))
				box.x -= n*3;
			else if(isTurnedLeft(rotation))
				box.y -= n*3;
			else if(isTurnedRight(rotation))
				box.y += n*3;
		}
	}
	private void calculateRightSideCollision(int n) {
		if(n>0){
			speed -= 3 + n*3;
			
			if(isTurnedUp(rotation))
				box.x -= n*3;
			else if(isTurnedDown(rotation))
				box.x += n*3;
			else if(isTurnedLeft(rotation))
				box.y += n*3;
			else if(isTurnedRight(rotation))
				box.y -= n*3;
		}
	}
	
	/**
	 * Makes the car move action including the collision states.
	 */
	private void move() {
		if(collided) {
		//if(false){
			//TurnedUp
			if(isTurnedUp(rotation)){
				calculateFrontCollision(topCollision);
				calculateBackCollision(bottomCollision);
				calculateLeftSideCollision(leftCollision);
				calculateRightSideCollision(rightCollision);
			}//TurnedLeft
			else if(isTurnedLeft(rotation)){
				calculateFrontCollision(leftCollision);
				calculateBackCollision(rightCollision);
				calculateLeftSideCollision(bottomCollision);
				calculateRightSideCollision(topCollision);
			}//TurnedDown
			else if(isTurnedDown(rotation)){
				calculateFrontCollision(bottomCollision);
				calculateBackCollision(topCollision);
				calculateLeftSideCollision(rightCollision);
				calculateRightSideCollision(leftCollision);
			}//TurnedRight
			else{
				calculateFrontCollision(rightCollision);
				calculateBackCollision(leftCollision);
				calculateLeftSideCollision(topCollision);
				calculateRightSideCollision(bottomCollision);
			}
			initColliders();
		}
		box.x += (speed*Math.cos(rotation * Math.PI/180))/SPEED_MODIFIER;
		box.y += (speed*Math.sin(rotation * Math.PI/180))/SPEED_MODIFIER;
	}
	
	/**
	 * Makes the car acceleration. Increases car speed with the car acceleration.
	 */
	private void accelerate() {
		speed += acceleration;
		if(isSpeedBlocked){
			if(speed>BLOCKED_SPEED)
				speed = BLOCKED_SPEED;
		}else if(speed>speedMax)
			speed = speedMax;
	}
	
	/**
	 * Makes the car deceleration. Decreases speed with the DECELERATION_RATE.
	 */
	private void decelerate() {
		speed -= DECELERATION_RATE;
		if(speed<0)
			speed = 0;
	}
	
	/**
	 * Makes something rotation based in rotateSense provided.
	 * Returns the new rotation calculated based in the rotateSense.
	 * @param rotation
	 * @param rotateSense
	 * @param rotateSpecific
	 * @return newRotation
	 */
	private double rotate(double rotation, int rotateSense, double rotateSpecific) {
		rotation %= 360;
		if((rotateSense&ROTATE_SPECIFIC)!=0){
			rotation += rotateSpecific;
		}else if(rotateSense!=ROTATE_NONE) {
			double rotate = rotateRate;
			boolean canRotate = false;
			
			/*
			boolean turnedUp = isTurnedUp(rotation);//false;
			boolean turnedDown = isTurnedDown(rotation);//false;
			boolean turnedLeft = isTurnedLeft(rotation);//false;
			boolean turnedRight = isTurnedRight(rotation);//false;
			*/
			
			if((rotateSense&ROTATE_UP)!=0) {
				canRotate = true;
				if(rotation>270||rotation<46)
					rotate = -rotateRate;
				else if(rotation==270)
					canRotate = false;
			}
			if((rotateSense&ROTATE_DOWN)!=0) {
				canRotate = true;
				if(rotation>90&&rotation<226)
					rotate = -rotateRate;
				else if(rotation==90)
					canRotate = false;
			}
			if((rotateSense&ROTATE_LEFT)!=0) {
				canRotate = true;
				if(rotation>180)
					rotate = -rotateRate;
				else if(rotation==180)
					canRotate = false;
			}
			if((rotateSense&ROTATE_RIGHT)!=0) {
				canRotate = true;
				if(rotation>0&&rotation<135)
					rotate = -rotateRate;
				else if(rotation==0)
					canRotate = false;
			}
			
			if(canRotate)
				rotation += rotate;
			
			if(rotation>270-rotateRate && rotation<270+rotateRate)
				rotation = 270;
			else if(rotation>90-rotateRate && rotation<90+rotateRate)
				rotation = 90;
			else if(rotation>180-rotateRate && rotation<180+rotateRate)
				rotation = 180;
			else if(rotation>360-rotateRate || rotation<rotateRate && rotation>0)
				rotation = 0;
			
		}
		if(rotation<0)
			rotation += 360;
		
		return rotation;
	}
	
	/**
	 * Rotates the car box changing its size and x,y coordinates
	 * based in the current rotation.
	 */
	private void rotateBoxBounds() {
		int width, height;
		
		width = (int) (Math.abs((Math.cos(rotation* Math.PI/180)*originalBox.width)) +
				Math.abs((Math.sin(rotation* Math.PI/180)*originalBox.height)));
		height = (int) (Math.abs((Math.sin(rotation* Math.PI/180)*originalBox.width)) +
				Math.abs((Math.cos(rotation* Math.PI/180)*originalBox.height)));
		
		box.x = box.x + box.width/2 - width/2;
		box.y = box.y + box.height/2 - height/2;
		
		box.width = width;
		box.height = height;
	}
	
	
	/**
	 * Verify if the car is turned up.
	 * @return true if the car is turned up and false otherwise.
	 */
	public boolean isTurnedUp(double rotation) {
		if(rotation>225&&rotation<315)//270 UP
			return true;
		else
			return false;
	}
	
	/**
	 * Verify if the car is turned down.
	 * @return true if the car is turned down and false otherwise.
	 */
	public boolean isTurnedDown(double rotation) {
		if(rotation>45&&rotation<135)//90 DOWN
			return true;
		else 
			return false;
	}
	
	/**
	 * Verify if the car is turned left.
	 * @return true if the car is turned left and false otherwise.
	 */
	public boolean isTurnedLeft(double rotation) {
		if(rotation>134&&rotation<226)//180 LEFT
			return true;
		else 
			return false;
	}
	
	/**
	 * Verify if the car is turned right.
	 * @return true if the car is turned right and false otherwise.
	 */
	public boolean isTurnedRight(double rotation) {
		if(rotation>314||rotation<46)//0 RIGHT
			return true;
		else
			return false;
	}
	
	/**
	 * Starts and stops the car acceleration:
	 */
	public void startAcceleration() {
		accelerating = true;
	}
	
	public void stopAcceleration() {
		accelerating = false;
	}
	
	/**
	 * Starts and stops the car rotation:
	 * @param sense
	 * @param rotateSpecific
	 */
	public void startRotation(int sense,double rotateSpecific) {
		rotateSense |= sense;
		rotationSpecific = rotateSpecific;
	}
	
	public void startRotation(boolean turnLeft) {
		int sense = 0;
		
		if(turnLeft) {
			if (this.isTurnedUp(this.getRotation()))
				sense = ROTATE_LEFT;
			else if (this.isTurnedDown(this.getRotation()))
				sense = ROTATE_RIGHT;
			else if (this.isTurnedRight(this.getRotation()))
				sense = ROTATE_UP;
			else if (this.isTurnedLeft(this.getRotation()))
				sense = ROTATE_DOWN;
		} else {
			if (this.isTurnedUp(this.getRotation()))
				sense = ROTATE_RIGHT;
			else if (this.isTurnedDown(this.getRotation()))
				sense = ROTATE_LEFT;
			else if (this.isTurnedRight(this.getRotation()))
				sense = ROTATE_DOWN;
			else if (this.isTurnedLeft(this.getRotation()))
				sense = ROTATE_UP;
		}
		
		rotateSense = sense;
	}
	
	public void stopRotation(int sense) {
		rotateSense &= ~sense;
		rotationSpecific = 0.0;
	}
	
	public void stopRotation() {
		rotateSense = 0;
		rotationSpecific = 0.0;
	}
	
	/**
	 * Starts and stops the sight rotation:
	 * @param sense
	 * @param rotateSpecific
	 */
	public void startSightRotation(int sense,double rotateSpecific) {
		sightRotateSense |= sense;
		sightRotationSpecific = rotateSpecific;
	}
	
	public void stopSightRotation(int sense) {
		sightRotateSense &= ~sense;
		sightRotationSpecific = 0.0;
	}
	
	/**
	 * Make a shot action, creating a new bullet and firing it.
	 */
	public void shoot(){
		if((!isDead)&&(shotDelay>SHOT_BOMB_DELAY)){
			shots.add(new Bullet(getX()+ originalBox.width/2 - (originalBox.width - box.width)/2,
					getY()+ originalBox.height/2 - (originalBox.height - box.height)/2
					,sightRotation));
			shotDelay = 0;
		}
	}
	
	/**
	 * Remove one specific shot.
	 * @param shot
	 */
	private void removeMarkedShots() {
		for(int i = 0;i<shots.size();++i) {
			if(shots.get(i).isMarked()){
				shots.remove(i);
				--i;
			}
		}
	}

	public void activateItem() {
		if(!isEnableOption()) {
			if(item!=null){
				if(!item.activate(this)) {
					item = null;
					enableOption = false;
				}else
					enableOption = true;
			}
		}else{
			if(item!=null){
				item.reactivate(option);
				option = 0;
				item = null;
				enableOption=false;
			}
		}
	}
	
	public void leaveBomb() {
		if((!isDead)&&(bombDelay>SHOT_BOMB_DELAY)){
			Map.getInstance().addTrap(new Trap(getX(),getY(),Trap.TRAP_TYPE_BOMB));
			bombDelay = 0;
		}
	}
	
	public void changeOptionToRight() {
		if(enableOption)
			if(option<(Map.getInstance().getNumberOfTeams()-1))
				option++;
	}
	
	public void changeOptionToLeft() {
		if(enableOption)
			if(option>0)
				option--;
	}
	
	//Laps verifiers:
	/**
	 * Sets the car intoTheLap and if it entered through the right side.
	 * @param correct
	 */
	public void intoLap(boolean correct) {
		if(!inLap) {
			inLap = true;
			inLapCorrect = correct;
		}
	}
	/**
	 * Sets the car outOfLap and if it got out through the right side,
	 * if it had entered through the right side and got out through the right side
	 * increments laps, if it entered through the wrong side and left through the
	 * wrong side decrements laps.
	 * @param correct
	 */
	public void outOfLap(boolean correct) {
		if(inLap) {
			inLap = false;
			if(inLapCorrect == correct) {
				if(inLapCorrect)
					laps++;
				else
					laps--;
			}
		}
	}
	
	//Receivers:
	public void receiveTrap(Trap trap) {
		Random gerador = new Random();
		receiveHit(2);
		rotation += gerador.nextInt()%10;
	}
	
	/**
	 * @return the isDead
	 */
	public boolean isDead() {
		return isDead;
	}
	
	/**
	 * @return the deadTime
	 */
	public int getDeadTime() {
		return deadTime;
	}

	/**
	 * Receive the damage done by a shot.
	 */
	public void receiveShot(int attack){
		receiveHit(attack);
	}
	
	public void receiveHit(int damage) {
		if(damage>this.defense)
			this.life -= damage-this.defense;
		else
			--this.life;
	}

	public void receiveEquip(int equipType) {
		switch (equipType) {
			case Equip.EQUIP_TYPE_POWER:
				equipPower++;
				attack += Equip.EQUIP_BONUS_POWER;
				break;
			case Equip.EQUIP_TYPE_DEFENSE:
				equipDefense++;
				defense += Equip.EQUIP_BONUS_DEFENSE;
				break;
			case Equip.EQUIP_TYPE_SPEED:
				equipSpeed++;
				speedMax += Equip.EQUIP_BONUS_SPEED;
				break;
			case Equip.EQUIP_TYPE_MONEY:
				money += Equip.EQUIP_BONUS_MONEY;
				break;
			default:
				break;
		}
	}
	
	/**
	 * Gets the given item if there is empty space.
	 * @param item
	 * @return returns true if got the item and false otherwise.
	 */
	public boolean receiveItem(Item item) {
		if(this.item == null) {
			this.item = item;
			return true;
		}
		return false;
	}
	

	//Equips Actions
	public void unEquipAll() {
		defense -= equipDefense*Equip.EQUIP_BONUS_DEFENSE;
		attack -= equipPower*Equip.EQUIP_BONUS_POWER;
		speedMax -= equipSpeed*Equip.EQUIP_BONUS_SPEED;
		equipDefense = equipPower = equipSpeed = 0;
	}
	
	/**
	 * Gets the List of bullets sent.
	 * @return the list of shots made by this car.
	 */
	public List<Bullet> getShots() {
		return shots;
	}

	//General
	/**
	 * @return the carImage
	 */
	public Image getCarImage() {
		return carImage;
	}
	
	/**
	 * @return the originalBox
	 */
	public Rectangle getOriginalBox() {
		return originalBox;
	}
	
	/**
	 * @return the collided
	 */
	public boolean isCollided() {
		return collided;
	}

	//Attributes Getters
	public int getSpeed() {
		return speed;
	}

	public int getSpeedMax() {
		return speedMax;
	}
	
	public int getAcceleration() {
		return acceleration;
	}
	
	public boolean isAccelerating() {
		return accelerating;
	}

	public int getFuel() {
		return fuel;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getLife() {
		return life;
	}

	public int getLaps() {
		return laps;
	}

	public int getMoney() {
		return money;
	}
	
	public void increaseMoney(int bonus){
		money += bonus;
	}
	
	//Rotations
	/**
	 * @return if is rotating
	 */
	public boolean isRotating() {
		if(rotateSense!=ROTATE_NONE)
			return true;
		return false;
	}
	
	/**
	 * @return if is sightRotating
	 */
	public boolean isSightRotating() {
		if(sightRotateSense!=ROTATE_NONE)
			return true;
		return false;
	}
	
	/**
	 * @return the rotateRate
	 */
	public double getRotateRate() {
		return rotateRate;
	}
	
	/**
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @return the sightRotation
	 */
	public double getSightRotation() {
		return sightRotation;
	}
	
	/**
	 * @return the sightImage
	 */
	public Image getSightImage() {
		return sightImage;
	}


	//Equips
	/**
	 * @return the equipPower
	 */
	public int getEquipPower() {
		return equipPower;
	}

	/**
	 * @return the equipDefense
	 */
	public int getEquipDefense() {
		return equipDefense;
	}

	/**
	 * @return the equipSpeed
	 */
	public int getEquipSpeed() {
		return equipSpeed;
	}

	//Itens
	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * @return the enableOption
	 */
	public boolean isEnableOption() {
		return enableOption;
	}

	/**
	 * @return the option
	 */
	public int getOption() {
		return option;
	}

	/**
	 * @return the itemImage
	 */
	public Image getItemImage() {
		if(item!=null)
			return item.getItemImage();
		return null;
	}

	/**
	 * @return the isSpeedBloqued
	 */
	public boolean isSpeedBlocked() {
		return isSpeedBlocked;
	}
	
	public void unblockSpeed() {
		isSpeedBlocked = false;
	}
	
	public void blockSpeed() {
		isSpeedBlocked = true;
	}
}
