package br.unb.unbiquitous.ubiquitos.runFast.states;

import javax.swing.JPanel;

import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesController;
import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesListener;

public abstract class State extends JPanel implements DevicesListener{

	/**
	 * serialVersionUID = -257465401526364067L;
	 */
	private static final long serialVersionUID = -257465401526364067L;
	
	protected DevicesController devicesController;
	
	public void load(DevicesController devController) {
		devicesController = devController;
		devController.addDevicesListener(this);
	}
	public void load(DevicesController devController, Stack stack) {
		load(devController);
	}
	public Stack unload() {
		devicesController.removeDevicesListener(this);
		return null;
	}
	public abstract int update(int dt);
	public abstract void render();
	
	public void endGame(){
		devicesController.endGame();
	}
}
