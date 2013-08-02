package br.unb.unbiquitous.ubiquitos.runFast.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;

import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputEvent;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputListener;
import br.unb.unbiquitous.ubiquitos.runFast.states.StateManager;

public class Team implements InputListener{

	public static String CHARACTER_PILOT = "pilot";
	public static String CHARACTER_COPILOT = "copilot";
	public static String CHARACTER_ASSISTANT = "assistant";
	
	private static int teamNumber = 0;

	private Car car;
	private CarTemplate carType;
	private int thisTeamNumber, blockedMembers;

	private UpDevice pilot;
	private UpDevice copilot;
	private List<UpDevice> assistants;
	
	public Team(UpDevice pilotDevice) {
		car = null;
		carType = null;
		thisTeamNumber = teamNumber;
		++teamNumber;
		blockedMembers =0;
		pilot = pilotDevice;
		copilot = null;
		assistants = new ArrayList<UpDevice>();
	}
	
	/*public Team(CarTemplate carType, UpDevice pilotDevice, UpDevice copilotDevice) {
		this.carType = carType;
		++teamNumber;
		initTeamCar(carType);
		pilot = pilotDevice;
		copilot = copilotDevice;
		setAssistants(new ArrayList<UpDevice>());
	}*/
	
	public void initTeamCar(CarTemplate carType){
		this.carType = carType;
		car = new Car(430,470+thisTeamNumber*25,carType);
	}
	
	public void render(Graphics2D g, int cameraX, int cameraY, JPanel panel) {
		if(car != null)
			car.render(g, cameraX, cameraY, panel);
	}
	
	public int update(int dt) {
		if(car != null)
			return car.update(dt);
		return StateManager.SAME_STATE;
	}
	
	/**
	 * Make a move based in the InputEvent
	 */
	public void inputPerformed(InputEvent e) {
		if(car!=null) {
			if(e.getDevice()==pilot)
				pilotInputPerformed(e);
			else if(e.getDevice()==copilot)
				copilotInputPerformed(e);
		}
	}
	
	public void inputReleased(InputEvent e) {
		if(car!=null) {
			if(e.getDevice()==pilot)
				pilotInputReleased(e);
			else if(e.getDevice()==copilot)
				copilotInputReleased(e);
		}
	}
	
	
	/**
	 * pilotInputs
	 * @param InputEvent e
	 */
	private void pilotInputPerformed(InputEvent e){
		switch(e.getInputCode()){
			case InputEvent.IC_UP:
				car.startRotation(Car.ROTATE_UP,0.0);
				break;
			case InputEvent.IC_DOWN:
				car.startRotation(Car.ROTATE_DOWN,0.0);
				break;
			case InputEvent.IC_LEFT:
				//car.startRotation(Car.ROTATE_LEFT,0.0);
				car.startRotation(true);
				break;
			case InputEvent.IC_RIGHT:
				//car.startRotation(Car.ROTATE_RIGHT,0.0);
				car.startRotation(false);
				break;
			case InputEvent.IC_ACTION:
				car.startAcceleration();
				break;
			case InputEvent.IC_PLUS_LEFT:
				car.changeOptionToLeft();
				break;
			case InputEvent.IC_PLUS_RIGHT:
				car.changeOptionToRight();
				break;
			case InputEvent.IC_PLUS_SELECT:
				car.activateItem();
				break;
				
			case InputEvent.IC_SPIN:
				car.startRotation(Car.ROTATE_SPECIFIC,e.getIntensity());
			default:
				break;
		}
	}

	private void pilotInputReleased(InputEvent e){
		switch(e.getInputCode()){
			case InputEvent.IC_UP:
				car.stopRotation(Car.ROTATE_UP);
				break;
			case InputEvent.IC_DOWN:
				car.stopRotation(Car.ROTATE_DOWN);
				break;
			case InputEvent.IC_LEFT:
				//car.stopRotation(Car.ROTATE_LEFT);
				car.stopRotation();
				break;
			case InputEvent.IC_RIGHT:
				//car.stopRotation(Car.ROTATE_RIGHT);
				car.stopRotation();
				break;
			case InputEvent.IC_ACTION:
				car.stopAcceleration();
				break;
				
			case InputEvent.IC_SPIN:
				car.stopRotation(Car.ROTATE_SPECIFIC);
			default:
				break;
		}
	}
	
	
	/**
	 * Copilot Inputs
	 * @param InputEvent e
	 */
	private void copilotInputPerformed(InputEvent e){
		switch(e.getInputCode()){
			case InputEvent.IC_UP:
				car.startSightRotation(Car.ROTATE_UP,0.0);
				break;
			case InputEvent.IC_DOWN:
				car.startSightRotation(Car.ROTATE_DOWN,0.0);
				break;
			case InputEvent.IC_LEFT:
				car.startSightRotation(Car.ROTATE_LEFT,0.0);
				break;
			case InputEvent.IC_RIGHT:
				car.startSightRotation(Car.ROTATE_RIGHT,0.0);
				break;
			case InputEvent.IC_ACTION:
				car.shoot();
				break;
			case InputEvent.IC_ACTION2:
				car.leaveBomb();
				break;
				
			case InputEvent.IC_SPIN:
				car.startSightRotation(Car.ROTATE_SPECIFIC,e.getIntensity());
			default:
				break;
		}
	}
	
	
	private void copilotInputReleased(InputEvent e){
		switch(e.getInputCode()){
			case InputEvent.IC_UP:
				car.stopSightRotation(Car.ROTATE_UP);
				break;
			case InputEvent.IC_DOWN:
				car.stopSightRotation(Car.ROTATE_DOWN);
				break;
			case InputEvent.IC_LEFT:
				car.stopSightRotation(Car.ROTATE_LEFT);
				break;
			case InputEvent.IC_RIGHT:
				car.stopSightRotation(Car.ROTATE_RIGHT);
				break;
			case InputEvent.IC_ACTION:
				break;
				
			case InputEvent.IC_SPIN:
				car.stopSightRotation(Car.ROTATE_SPECIFIC);
			default:
				break;
		}
	}
	
	
	/* Getters and Setters*/
	/**
	 * Gets the team Car.
	 * @return Car
	 */
	public Car getCar() {
		return car;
	}
	
	/**
	 * @return the carType
	 */
	public CarTemplate getCarType() {
		return carType;
	}

	/**
	 * @return the thisTeamNumber
	 */
	public int getThisTeamNumber() {
		return thisTeamNumber;
	}

	/**
	 * Pilot
	 * @return int
	 */
	public UpDevice getPilot() {
		return pilot;
	}

	public void setPilot(UpDevice pilot) {
		this.pilot = pilot;
	}

	/**
	 * Copilot
	 * @return int
	 */
	public UpDevice getCopilot() {
		return copilot;
	}

	public void setCopilot(UpDevice copilot) {
		this.copilot = copilot;
	}

	/**
	 * Assistants
	 * @return List<UpDevices>
	 */
	public List<UpDevice> getAssistants() {
		return assistants;
	}

	public void setAssistants(List<UpDevice> assistants) {
		this.assistants = assistants;
	}
	
	public void addAssistant(UpDevice assistant) {
		assistants.add(assistant);
	}


	/**
	 * Gets the total number of players in this team.
	 * Adding pilot, copilot and assistants.
	 * @return number total of players
	 */
	public int getNumberOfPlayers(){
		int number = 0;
		if(pilot!=null)
			number = 1;
		if(copilot!=null)
			++number;
		if(assistants.size()>0)
			number += assistants.size();
		
		return number;
	}
	
	/**
	 * If the device belongs to this team it is removed.
	 * @param device
	 */
	public void removeMember(UpDevice device){
		if(device.getName().equals(pilot.getName()))
			pilot = null;
		else if(device == copilot)
			copilot = null;
		else{
			for(int i=0; i<assistants.size(); ++i){
				if(assistants.get(i) == device){
					assistants.remove(i);
					--i;
				}
			}
		}
	}
	
	public void decreaseTeamNumber(){
		--teamNumber;
	}
	
	public void blockTeam(int numberOfMembers){
		if(numberOfMembers > 0){
			blockedMembers += numberOfMembers;
			car.blockSpeed();
		}
	}
	
	public void unblockTeam(){
		--blockedMembers;
		if(blockedMembers<1){
			blockedMembers = 0;
			car.unblockSpeed();
		}
	}
}
