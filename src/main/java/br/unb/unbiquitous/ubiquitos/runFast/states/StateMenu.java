package br.unb.unbiquitous.ubiquitos.runFast.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesController;
import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesEvent;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputEvent;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputListener;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputManager;
import br.unb.unbiquitous.ubiquitos.runFast.ui.Window;

public class StateMenu extends State implements InputListener{

	/**
	 * private static final long serialVersionUID = -2194386631573094489L;
	 */
	private static final long serialVersionUID = -2194386631573094489L;
	
	private static final String MENU_IMAGE = "../images/image_menu.jpg";
	
	private static final int OPT_PLAY = 0;
	private static final int OPT_CREDITS = 1;
	private static final int OPT_QUIT = 2;
	
	/*Specifications to the menu items display:*/
	private static final String FONT = "Helvetica";
	private static final int FONT_STYLE = Font.BOLD;
	private static final int FONT_SIZE = 50;
	private static final int FONT_SELECTED_SIZE = 80;
	
	private static final String PLAY = "PLAY";
	private static final String CREDITS = "CREDITS";
	private static final String QUIT = "QUIT";
	
	private Image background;
	private int selectedOption = OPT_PLAY;
	private int nextState = StateManager.SAME_STATE;
	
	private static int B_WIDTH;
    private static int B_HEIGHT;
	
	public StateMenu(){
		InputManager.GetInstance().addInputListener(this);
		setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();
        
        background = null;
	}
	
	@Override
	public void load(DevicesController devController) {
		super.load(devController);
		
		ImageIcon ii = new ImageIcon(getClass().getResource(MENU_IMAGE));
        this.background = ii.getImage();
	}

	@Override
	public void load(DevicesController devController, Stack stack) {
		load(devController);
	}

	@Override
	public Stack unload() {
		super.unload();
		InputManager.GetInstance().removeInputListener(this);
		return new Stack();
	}

	@Override
	public int update(int dt) {
		return nextState;
	}

	@Override
	public void render() {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D)g;
        if(background!=null)
        	g2d.drawImage(background, 0, 0, this);
        
        /*Menu Items*/

        Font font = new Font(FONT, FONT_STYLE, FONT_SIZE);
        FontMetrics metr = this.getFontMetrics(font);
        Font selectedFont = new Font(FONT, FONT_STYLE, FONT_SELECTED_SIZE);
        FontMetrics selectedMetr = this.getFontMetrics(selectedFont);
        
        if(selectedOption==OPT_PLAY){
        	printItemSelected(g, PLAY, B_HEIGHT/2 - metr.getHeight()*2);
        	printItem(g, CREDITS, B_HEIGHT/2 - metr.getHeight());
        	printItem(g, QUIT, B_HEIGHT/2);
        	
        }else if(selectedOption==OPT_CREDITS){
        	printItem(g, PLAY,
        			B_HEIGHT/2 - metr.getHeight() - selectedMetr.getHeight());
        	printItemSelected(g, CREDITS, B_HEIGHT/2 - metr.getHeight());
        	printItem(g, QUIT, B_HEIGHT/2);
        
        }else if(selectedOption==OPT_QUIT){
        	printItem(g, PLAY,
        			B_HEIGHT/2 - metr.getHeight() - selectedMetr.getHeight());
        	printItem(g, CREDITS, B_HEIGHT/2 - selectedMetr.getHeight());
        	printItemSelected(g, QUIT, B_HEIGHT/2);
        };
                
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
        g.dispose();
    }

	/**
	 * Prints the selected item in the menu with the specifications
	 */
	private void printItemSelected(Graphics g, String msg, int posY){
        Font font = new Font(FONT, FONT_STYLE, FONT_SELECTED_SIZE);
        FontMetrics metr = this.getFontMetrics(font);

        g.setColor(new Color(0xBB,0xBB,0xEE));
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, posY);
	}
	/**
	 * Prints one regular item in the menu with the specifications
	 */
	private void printItem(Graphics g, String msg, int posY){
        Font font = new Font(FONT, FONT_STYLE, FONT_SIZE);
        FontMetrics metr = this.getFontMetrics(font);

        g.setColor(new Color(0xDE,0xDE,0xDE));
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, posY);
	}
	
	
	/**
	 * Gets the InputEvents and makes the necessaries changes in the menu
	 */
	public void inputPerformed(InputEvent e) {
		switch(e.getInputCode()){
			case InputEvent.IC_ENTER:
				if(selectedOption == OPT_PLAY)
					nextState = StateManager.STATE_SELECTION;
				else if(selectedOption == OPT_QUIT)
					nextState = StateManager.STATE_QUIT;
				break;
			case InputEvent.IC_ACTION:
				if(selectedOption == OPT_PLAY)
					nextState = StateManager.STATE_SELECTION;
				else if(selectedOption == OPT_QUIT)
					nextState = StateManager.STATE_QUIT;
				break;
			case InputEvent.IC_UP:
				--selectedOption;
				if(selectedOption<0)
					selectedOption = 2;
				break;
			case InputEvent.IC_DOWN:
				++selectedOption;
				if(selectedOption>2)
					selectedOption = 0;
				break;
			default:
				System.out.println("StateMenu - inputPerformed: default "+e.getInputCode());
				break;
		}
	}

	public void inputReleased(InputEvent e) {}

	/**
	 * DevicesListener implementation
	 */
	public void deviceEntered(DevicesEvent e) {}
	public void deviceGotOut(DevicesEvent e) {}
	
}
