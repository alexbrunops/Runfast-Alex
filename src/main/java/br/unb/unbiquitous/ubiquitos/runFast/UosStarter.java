package br.unb.unbiquitous.ubiquitos.runFast;

import org.unbiquitous.uos.core.ContextException;
import org.unbiquitous.uos.core.UOS;

/**
 * main
 *
 */
public class UosStarter {
	/*public static void main (String args[]){
		StateManager stateManager = new StateManager();
		stateManager.begin();
	}*/
	public static void main(String[] args) throws ContextException {
		UOS uosApplicationContext = new UOS();
		uosApplicationContext.init();
	}
}
