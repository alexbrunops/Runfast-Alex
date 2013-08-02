package br.unb.unbiquitous.ubiquitos.runFast.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Rectangle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CarTest {

	private static Car car;
	private static int x,y,w,h;
	private static CarTemplate carTemplate;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		x = 100;
		y = 200;
		carTemplate = new CarTemplate(CarTemplate.CAR_BLUEWHITE);
		
		car = new Car(x,y,carTemplate);
		w = car.getWidth();
		h = car.getHeight();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCar() {
		Car car = new Car(x,y,carTemplate);
	
		assertEquals(x, car.getX());
		assertEquals(y, car.getY());
		assertEquals(0, car.getSpeed());
		assertEquals(carTemplate.getSpeedMax(), car.getSpeedMax());
		assertEquals(carTemplate.getAcceleration(), car.getAcceleration());
		assertEquals(false, car.isAccelerating());
		assertEquals(carTemplate.getPower(), car.getAttack());
		assertEquals(carTemplate.getDefense(), car.getDefense());
		assertEquals(carTemplate.getLife(), car.getLife());
		assertEquals(carTemplate.getFuel(), car.getFuel());
		assertEquals(carTemplate.getRotateRate(), car.getRotateRate(),0.00000001);
		assertEquals(0, car.getRotation(),0.00000001);
		assertEquals(0, car.getSightRotation(),0.00000001);
		assertEquals(0, car.getLaps());
		assertEquals(0, car.getMoney());
		assertEquals(0, car.getEquipDefense());
		assertEquals(0, car.getEquipPower());
		assertEquals(0, car.getEquipSpeed());
		assertEquals(false, car.isCollided());
		assertEquals(null, car.getItem());
	}
	
	
	/*@Test
	public void testUpdate() {
		
		if(accelerating)
			accelerate();
		else
			decelerate();
		
		//Rotates the car and its colisionShape(box)
		rotation = rotate(rotation,rotateSense);
		sightRotation = rotate(sightRotation,sightRotateSense);
		rotateBoxBounds();
		
		//Moves the car
		move();
		
		//Updates shots
		removeMarkedShots();
		for(int i =0;i<shots.size();++i)
			shots.get(i).update(dt);
	}*/

	/*
	@Test
	public void testRender() {
		fail("Not yet implemented");
	}
	*/

	@Test
	public void testMakeCollisionRectangle() {
		//Do not test side collision
		car.update(33);
		assertEquals(false, car.isCollided());
		
		car.makeCollision(new Rectangle(x-w/2,y-h/2,w,h));
		assertEquals(true, car.isCollided());
		
		car.update(33);
		assertEquals(false, car.isCollided());
		
		car.makeCollision(new Rectangle(x+w/2,y-h/2,w,h));
		assertEquals(true, car.isCollided());
		
		car.update(33);
		assertEquals(false, car.isCollided());
		
		car.makeCollision(new Rectangle(x-w/2,y+h/2,w,h));
		assertEquals(true, car.isCollided());
		
		car.update(33);
		assertEquals(false, car.isCollided());
		
		car.makeCollision(new Rectangle(x+w/2,y+h/2,w,h));
		assertEquals(true, car.isCollided());
	}

	@Test
	public void testMakeCollisionBooleanBooleanBooleanBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTurnedUp() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTurnedDown() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTurnedLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTurnedRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartAcceleration() {
		car.startAcceleration();
		assertEquals(true, car.isAccelerating());
	}

	@Test
	public void testStopAcceleration() {
		car.stopAcceleration();
		assertEquals(false, car.isAccelerating());
	}

	@Test
	public void testStartRotation() {
		car.startRotation(Car.ROTATE_RIGHT,0.0);
		assertEquals(true, car.isRotating());
	}

	@Test
	public void testStopRotation() {
		car.stopRotation(Car.ROTATE_RIGHT);
		assertEquals(false, car.isRotating());
	}

	@Test
	public void testStartSightRotation() {
		car.startSightRotation(Car.ROTATE_RIGHT,0.0);
		assertEquals(true, car.isSightRotating());
	}

	@Test
	public void testStopSightRotation() {
		car.startSightRotation(Car.ROTATE_RIGHT,0.0);
		assertEquals(false, car.isSightRotating());
	}

	@Test
	public void testShoot() {
		int shots = car.getShots().size();
		car.shoot();
		assertEquals(shots+1, car.getShots().size());
	}

	@Test
	public void testActivateItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testLeaveBomb() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeOptionToRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeOptionToLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntoLap() {
		fail("Not yet implemented");
	}

	@Test
	public void testOutOfLap() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveTrap() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveShot() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveHit() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveEquip() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnEquipAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetShots() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSpeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSpeedMax() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFuel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAttack() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefense() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLife() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLaps() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMoney() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquipPower() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquipDefense() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquipSpeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEnableOption() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOption() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetItemImage() {
		Car car = new Car(x,y,carTemplate);
		
		assertEquals(null, car.getItemImage());
		
		car.receiveItem(new Item(0,0));
		
		assertNotNull(car.getItemImage());
	}

}
