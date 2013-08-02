package br.unb.unbiquitous.ubiquitos.runFast.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesController;
import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesEvent;
import br.unb.unbiquitous.ubiquitos.runFast.game.Map;
import br.unb.unbiquitous.ubiquitos.runFast.ui.Window;

public class StateGame extends State{

	/**
	 * private static final long serialVersionUID = 2113102754016352707L;
	 */
	private static final long serialVersionUID = 2113102754016352707L;
	
	private Map map;
	private int returnState = StateManager.SAME_STATE;
	
	public StateGame() {
		//InputManager.GetInstance().addInputListener(this);
		
		setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        
        map = null;
	}
	
	@Override
	public void load(DevicesController devController) {
		super.load(devController);
		map= Map.getInstance();
		map.load(devController, 128,0,new Stack());
	}

	@Override
	public void load(DevicesController devController, Stack stack) {
		super.load(devController);
		map= Map.getInstance();
		map.load(devController, 128,0,stack);
	}

	@Override
	public Stack unload() {
		super.unload();
		//InputManager.GetInstance().removeInputListener(this);
		return map.unload();
		//return 0;
	}

	@Override
	public int update(int dt) {
		returnState = map.update(dt);
		return returnState;
	}

	@Override
	public void render() {
		repaint();
	}
	
	public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D)g;
        
        if(map!=null)
        	map.render(g2d, 0, 0, this);
        //g2d.drawImage(map, 128, 0, this);
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
	}

	
	public void deviceEntered(DevicesEvent e) {
		map.updateTeams();
	}

	public void deviceGotOut(DevicesEvent e) {
		map.updateTeams();
	}
	
}
