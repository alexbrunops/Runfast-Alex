package br.unb.unbiquitous.ubiquitos.runFast.game;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BulletTest {

	private static Bullet bullet;
	private static int x, y;
	private static double rotation;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		x = 100;
		y = 200;
		rotation = 90;
		bullet = new Bullet(x,y,rotation);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		bullet = null;
	}

	@Test
	public void testBullet() {
		Bullet bullet = new Bullet(x,y,rotation);
		
		assertEquals(x, bullet.getX());
		assertEquals(y, bullet.getY());
		assertEquals(rotation*Math.PI/180, bullet.getRotation(),0.0000000001);
		assertEquals(false, bullet.isMarked());
		//Width and height not tested.. unacessary
	}
	
	@Test
	public void testUpdate() {
		int x = bullet.getX();
		int y = bullet.getY();
		int dt = 33;
		
		bullet.update(dt);
		
		x += (dt*Bullet.BULLET_SPEED*Math.cos(rotation*Math.PI/180));
		y += (dt*Bullet.BULLET_SPEED*Math.sin(rotation*Math.PI/180));
		
		assertEquals(x, bullet.getX());
		assertEquals(y, bullet.getY());
	}

	/*
	@Test
	public void testRender() {
		fail("Not yet implemented");
	}
	*/

	@Test
	public void testMarkAndisMarked() {
		Bullet bullet = new Bullet(x,y,rotation);
		
		assertEquals(false, bullet.isMarked());
		
		bullet.mark();
		
		assertEquals(true, bullet.isMarked());
	}

}
