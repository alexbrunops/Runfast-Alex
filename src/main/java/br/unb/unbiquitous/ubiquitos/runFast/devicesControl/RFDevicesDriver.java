package br.unb.unbiquitous.ubiquitos.runFast.devicesControl;

import java.util.ArrayList;
import java.util.List;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.UOSMessageContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpService.ParameterType;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceResponse;

import br.unb.unbiquitous.ubiquitos.runFast.game.Team;
import br.unb.unbiquitous.ubiquitos.runFast.states.StateManager;

public class RFDevicesDriver implements UosDriver{

	//private static Logger logger = Logger.getLogger(RFDevicesDriver.class);

	public static final String RFDEVICES_DRIVER = "br.unb.unbiquitous.ubiquitos.runFast.devicesControl.RFDevicesDriver";
    
	public static final String RFDEVICES_EVENT = "RFDevicesEvent";
	

	private DevicesController devicesController;
	private int state;
	
	private Gateway gateway;
	
	public UpDriver getDriver() {
		UpDriver driver = new UpDriver(RFDEVICES_DRIVER);
		
		driver.addService("setDeviceController")
			.addParameter("devicesController", ParameterType.MANDATORY);
		driver.addService("getTeamsInfos");
		driver.addService("updateStateInfo")
			.addParameter("state", ParameterType.MANDATORY);
		
		driver.addService("isInGame");
		driver.addService("bonusResult")
			.addParameter("deviceName", ParameterType.MANDATORY)
			.addParameter("points", ParameterType.MANDATORY);
		driver.addService("breakResult")
			.addParameter("deviceName", ParameterType.MANDATORY);
		
		driver.addService("requestPlayerJoin")
			.addParameter("deviceName", ParameterType.MANDATORY)
			.addParameter("teamNumber", ParameterType.MANDATORY)
			.addParameter("character", ParameterType.MANDATORY)
			.addParameter("carType", ParameterType.OPTIONAL);
		driver.addService("playerQuit")
			.addParameter("deviceName", ParameterType.MANDATORY);
				
		return driver;
	}

	public List<UpDriver> getParent() {
		return new ArrayList<UpDriver>();
	}

	public void init(Gateway gateway, String instanceId) {
		this.gateway = gateway;
		state = StateManager.STATE_MENU;
		devicesController = null;
	}

	public void destroy() {}
	
	
	//Devices Services
	/**
	 * Initiate the devicesController.
	 * Necessary initiation to make it works properly.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void setDeviceController(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {
		devicesController = (DevicesController)serviceCall.getParameter("devicesController");
	}
	
	/**
	 * Get teams informations to tell
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void getTeamsInfos(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {
		serviceResponse.addParameter("numberOfTeams", devicesController.getTeams().size());
		for(int i=0;i<devicesController.getTeams().size();++i){
			if(devicesController.getTeams().get(i).getCopilot()!=null){
				serviceResponse.addParameter("team"+i+"CoPilot", true);
				serviceResponse.addParameter("team"+i+"NumberOfPlayers",
						2+devicesController.getTeams().get(i).getAssistants().size());
			}else{
				serviceResponse.addParameter("team"+i+"CoPilot", false);
				serviceResponse.addParameter("team"+i+"NumberOfPlayers",
						1+devicesController.getTeams().get(i).getAssistants().size());
			}
		}
	}
	
	/**
	 * Updates the state. Letting the driver know in which state
	 * it is in the moment.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void updateStateInfo(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {
		state = (Integer)serviceCall.getParameter("state");
	}
	
	/**
	 * Answers if it is already in the game.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void isInGame(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {
		if(state==StateManager.STATE_GAME){
			serviceResponse.addParameter("isInGame", true);
		}else{
			serviceResponse.addParameter("isInGame", false);
		}
	}
	
	/**
	 * Increases the team money with the bonusResult of this device.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void bonusResult(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {

		UpDevice device = getDevice(serviceCall.getParameterString("deviceName"));
		int bonus = (Integer) serviceCall.getParameter("bonus");
		
		List<Team> teams = devicesController.getTeams();
		
		for(int i=0; i<teams.size(); ++i){
			if((teams.get(i).getPilot()==device)||
					(teams.get(i).getCopilot()==device)||
					(teams.get(i).getAssistants().contains(device))){
				teams.get(i).getCar().increaseMoney(bonus);
			}
		}
	}
	
	/**
	 * Increases the team money with the bonusResult of this device.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void breakResult(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {

		UpDevice device = getDevice(serviceCall.getParameterString("deviceName"));
		
		List<Team> teams = devicesController.getTeams();
		
		for(int i=0; i<teams.size(); ++i){
			if((teams.get(i).getPilot()==device)||
					(teams.get(i).getCopilot()==device)||
					(teams.get(i).getAssistants().contains(device))){
				teams.get(i).unblockTeam();
			}
		}
	}
	
	/**
	 * A new device requests a place in the game.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void requestPlayerJoin(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {
		
		UpDevice device = getDevice(serviceCall.getParameterString("deviceName"));
		int teamNumber = (Integer) serviceCall.getParameter("teamNumber");
		String character = serviceCall.getParameterString("character");

		boolean hadJoined = false;
		//Creates a new team during game
		if((state==StateManager.STATE_GAME)&&(character.equals("pilot"))){
			hadJoined = devicesController.addPlayer(device, character, (Integer)serviceCall.getParameter("carType"));
		//Team already exists
		}else if(teamNumber < devicesController.getTeams().size())
			hadJoined = devicesController.addPlayer(devicesController.getTeams().get(teamNumber), device, character);
		//Creates new team
		else if(teamNumber < 4)
			hadJoined = devicesController.addPlayer(null, device, character);
		
		serviceResponse.addParameter("hadJoined", hadJoined);
	}
	
	/**
	 * One device leaves the game.
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
	public void playerQuit(ServiceCall serviceCall, 
			ServiceResponse serviceResponse, UOSMessageContext messageContext) {
		devicesController.playerQuit(getDevice(serviceCall.getParameterString("deviceName")));
	}
	
	/**
	 * Finds the correspondent UpDevice with the given name and returns it
	 * otherwise if it does not find it returns null;
	 * @param deviceName
	 * @return 
	 */
	public UpDevice getDevice(String deviceName){
		boolean found = false;
		int i = 0;
		UpDevice device = null;
		while((!found)&&(i<gateway.listDevices().size())){
			if(gateway.listDevices().get(i).getName().equals(deviceName)){
				device = gateway.listDevices().get(i);
				found = true;
			}
			++i;
		}
		return device;
	}
	
}
