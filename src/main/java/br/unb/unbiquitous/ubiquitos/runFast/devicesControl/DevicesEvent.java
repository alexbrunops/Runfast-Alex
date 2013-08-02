package br.unb.unbiquitous.ubiquitos.runFast.devicesControl;

import java.util.EventObject;

import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;

public class DevicesEvent extends EventObject {

	/**
	 * private static final long serialVersionUID = 3452532560005337556L;
	 */
	private static final long serialVersionUID = 3452532560005337556L;
	
	private UpDevice device;
	
	public DevicesEvent(Object source, UpDevice device) {
		super(source);
		this.setDevice(device);
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
