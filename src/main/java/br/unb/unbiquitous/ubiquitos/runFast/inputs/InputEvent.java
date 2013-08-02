package br.unb.unbiquitous.ubiquitos.runFast.inputs;

import java.util.EventObject;

import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;

public class InputEvent extends EventObject {

	/**
	 * private static final long serialVersionUID = 3864120084930711205L;
	 */
	private static final long serialVersionUID = 3864120084930711205L;
	
	public static final int IC_ENTER       = 0x00;
	public static final int IC_UP          = 0x01;
	public static final int IC_DOWN        = 0x02;
	public static final int IC_LEFT        = 0x03;
	public static final int IC_RIGHT       = 0x04;
	public static final int IC_ACTION      = 0x05;
	public static final int IC_ACTION2     = 0x06;
	public static final int IC_PLUS_SELECT = 0x10;
	public static final int IC_PLUS_LEFT   = 0x11;
	public static final int IC_PLUS_RIGHT  = 0x12;
	
	public static final int IC_WITH_INTENSITY = 0x20;
	public static final int IC_SPIN           = 0x21;
	
	private int inputCode;
	private double intensity;
	private UpDevice device;
	
	//Constructors
	/**
	 * Creates one inputEvent with the given parameters.
	 * @param source
	 * @param inputCode
	 * @param device
	 */
    public InputEvent(Object source, int inputCode, UpDevice device) {
        super(source);
        this.setInputCode(inputCode);
        this.setDevice(device);
        this.intensity = 0.0;
    }
    /**
     * Creates one intensity inputEvent with the given parameters.
     * @param source
     * @param inputCode
     * @param device
     * @param intensity
     */
    public InputEvent(Object source, int inputCode, UpDevice device, double intensity) {
    	super(source);
    	this.setInputCode(inputCode);
    	this.setDevice(device);
    	this.intensity = intensity;
    }
    
    //Getters and setters
	/**
	 * @return the inputCode
	 */
	public int getInputCode() {
		return inputCode;
	}
	/**
	 * @param inputCode the inputCode to set
	 */
	public void setInputCode(int inputCode) {
		this.inputCode = inputCode;
	}
	/**
	 * @return the intensity
	 */
	public double getIntensity() {
		return intensity;
	}
	/**
	 * @param intensity the intensity to set
	 */
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	/**
	 * @return the device
	 */
	public UpDevice getDevice() {
		return device;
	}
	/**
	 * @param device the device to set
	 */
	public void setDevice(UpDevice device) {
		this.device = device;
	}

}
