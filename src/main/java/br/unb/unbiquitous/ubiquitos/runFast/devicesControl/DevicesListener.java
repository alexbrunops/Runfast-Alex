package br.unb.unbiquitous.ubiquitos.runFast.devicesControl;

public interface DevicesListener {
	
	public void deviceEntered(DevicesEvent e);
	public void deviceGotOut(DevicesEvent e);
	
}
