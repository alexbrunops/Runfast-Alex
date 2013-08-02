package br.unb.unbiquitous.ubiquitos.runFast;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

import br.unb.unbiquitous.ubiquitos.runFast.states.StateManager;

public class GameConsole implements UosApplication{

	public void start(Gateway gateway, OntologyStart ontology) {
		StateManager stateManager = new StateManager(gateway);
		stateManager.begin();
	}

	public void stop() throws Exception {}

	//public void init(OntologyDeploy ontology) {}

	public void tearDown(OntologyUndeploy ontology) throws Exception {}

	public void init(OntologyDeploy ontology, String id) {
		// TODO Auto-generated method stub
		
	}

}
