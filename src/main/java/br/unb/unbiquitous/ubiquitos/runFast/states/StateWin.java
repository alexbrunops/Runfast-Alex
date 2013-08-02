package br.unb.unbiquitous.ubiquitos.runFast.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.ImageIcon;

import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesController;
import br.unb.unbiquitous.ubiquitos.runFast.devicesControl.DevicesEvent;
import br.unb.unbiquitous.ubiquitos.runFast.game.Team;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputEvent;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputListener;
import br.unb.unbiquitous.ubiquitos.runFast.inputs.InputManager;
import br.unb.unbiquitous.ubiquitos.runFast.ui.Window;

public class StateWin extends State implements InputListener{

	/**
	 * private static final long serialVersionUID = 6666341412704905058L;
	 */
	private static final long serialVersionUID = 6666341412704905058L;

	private static final String WIN_IMAGE = "../images/image_win.jpg";
	private static final String MEDAL_IMAGE = "../images/medals/medal";
	
	private static final int OPT_RESTART = 0;
	private static final int OPT_MENU = 1;
	private static final int OPT_QUIT = 2;
	
	private static final String RESTART = "RESTART";
	private static final String MENU = "MENU";
	private static final String QUIT = "QUIT";
	
	private static final String FONT = "Helvetica";
	private static final int FONT_STYLE = Font.BOLD;
	private static final int FONT_SIZE = 50;
	private static final int FONT_SELECTED_SIZE = 80;
	
	private int B_WIDTH;
    private int B_HEIGHT;
	
	private Image background;
	private Image[] medals;
	
	private int[] teamsPositions;
	private int[] teamsPoints;
	
	private int selectedOption = OPT_RESTART;
	private int nextState = StateManager.SAME_STATE;

	public StateWin(){
		InputManager.GetInstance().addInputListener(this);
		setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();
        
        background = null;
	}
	
	/**
	 * ranks the teams based in the number of laps+money;
	 */
	private void rankCars(){
		List<Team> teams = devicesController.getTeams();
		teamsPositions = new int[teams.size()];
        teamsPoints = new int[teams.size()];
		
		for(int i=0; i<teams.size(); ++i){
			teamsPoints[i] = teams.get(i).getCar().getLaps()*100 + teams.get(i).getCar().getMoney();
			teamsPositions[i] = i;
		}
		
		boolean isOrdered = false;
		while(!isOrdered){
			isOrdered = true;
			for(int i=0; i<(teams.size()-1); ++i){
				if(teamsPoints[teamsPositions[i]] < teamsPoints[teamsPositions[i+1]]){
					int aux = teamsPositions[i];
					teamsPositions[i] = teamsPositions[i+1];
					teamsPositions[i+1] = aux;
					isOrdered = false;
				}
			}
		}
		
	}
	
	@Override
	public void load(DevicesController devController) {
		super.load(devController);
		
		ImageIcon ii = new ImageIcon(getClass().getResource(WIN_IMAGE));
        this.background = ii.getImage();
        
        medals = new Image[4];
        for(int i=1; i<5; ++i){
        	ii = new ImageIcon(getClass().getResource(MEDAL_IMAGE+i+".png"));
        	medals[i-1] = ii.getImage();
        }       
        
        rankCars();
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
        
        /*Print Cars*/
        printRankedCars(g2d);
        
        /*Menu Items*/
        printOptions(g);
        
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
        g.dispose();
	}
	
	private void printRankedCars(Graphics2D g){
		Graphics2D g2d = (Graphics2D)g.create();
		
		List<Team> teams = devicesController.getTeams();
		
		Font font = new Font(FONT, FONT_STYLE, FONT_SIZE/3);
		//First Place
		g2d.setFont(font);
		if(teams.size()>0){
			g2d.setColor(Color.WHITE);
			g2d.drawImage(teams.get(teamsPositions[0]).getCarType().getFrontImage(), B_WIDTH/2 - 192, 20, this);
			g2d.drawRect(B_WIDTH/2 - 192, 20, 384, 218);
			g2d.drawString("Time "+(teamsPositions[0]+1), B_WIDTH/2 - 172, 50);
			g2d.drawImage(medals[0], B_WIDTH/2 - 45, 0, this);
			g2d.drawString("Laps: ", B_WIDTH/2 - 60, 200 );
			g2d.drawString(teams.get(teamsPositions[0]).getCar().getLaps()+"*100 + ", B_WIDTH/2 +40, 200 );
			g2d.drawString("Money: ", B_WIDTH/2 - 60, 215 );
			g2d.drawString(""+teams.get(teamsPositions[0]).getCar().getMoney(), B_WIDTH/2 +40, 215 );
			g2d.drawString("Pontuação = ", B_WIDTH/2 - 60, 230 );
			g2d.drawString(""+teamsPoints[teamsPositions[0]], B_WIDTH/2 +40, 230 );
		}
		if(teams.size()>1){
			g2d.drawImage(teams.get(teamsPositions[1]).getCarType().getFrontImage(), B_WIDTH/2 - 576, 90, this);
			g2d.drawRect(B_WIDTH/2 - 576, 90, 384, 218);
			g2d.drawImage(medals[1], B_WIDTH/2 - 419, 80, this);
			g2d.drawString("Time "+(teamsPositions[1]+1), B_WIDTH/2 - 556, 120);
			g2d.drawString("Pontos: "+teamsPoints[teamsPositions[1]], B_WIDTH/2 - 434, 290 );
		}
		if(teams.size()>2){
			g2d.drawImage(teams.get(teamsPositions[2]).getCarType().getFrontImage(), B_WIDTH/2 + 192, 160, this);
			g2d.drawRect(B_WIDTH/2 + 192, 160, 384, 218);
			g2d.drawImage(medals[2], B_WIDTH/2 + 349, 150, this);
			g2d.drawString("Time "+(teamsPositions[2]+1), B_WIDTH/2 +212, 190);
			g2d.drawString("Pontos: "+teamsPoints[teamsPositions[2]], B_WIDTH/2 - 142, 360 );
		}
		if(teams.size()>3){
			g2d.drawImage(teams.get(teamsPositions[3]).getCarType().getFrontImage(), B_WIDTH/2 - 192, 250, this);
			g2d.drawRect(B_WIDTH/2 - 192, 260, 384, 218);
			g2d.drawImage(medals[3], B_WIDTH/2 - 27, 250, this);
			g2d.drawString("Time "+(teamsPositions[3]+1), B_WIDTH/2 -172, 280);
			g2d.drawString("Pontos: "+teamsPoints[teamsPositions[3]], B_WIDTH/2 - 50, 460 );
		}
		g2d.dispose();
	}
	
	/**
	 * Print menu options(Restart, Menu e Quit).
	 * @param g
	 */
	private void printOptions(Graphics g){
		Font font = new Font(FONT, FONT_STYLE, FONT_SIZE);
        FontMetrics metr = this.getFontMetrics(font);
        Font selectedFont = new Font(FONT, FONT_STYLE, FONT_SELECTED_SIZE);
        FontMetrics selectedMetr = this.getFontMetrics(selectedFont);
        
        if(selectedOption==OPT_RESTART){
        	printItemSelected(g, RESTART, 5*B_HEIGHT/6 - metr.getHeight()*2);
        	printItem(g, MENU, 5*B_HEIGHT/6 - metr.getHeight());
        	printItem(g, QUIT, 5*B_HEIGHT/6);
        	
        }else if(selectedOption==OPT_MENU){
        	printItem(g, RESTART,
        			5*B_HEIGHT/6 - metr.getHeight() - selectedMetr.getHeight());
        	printItemSelected(g, MENU, 5*B_HEIGHT/6 - metr.getHeight());
        	printItem(g, QUIT, 5*B_HEIGHT/6);
        
        }else if(selectedOption==OPT_QUIT){
        	printItem(g, RESTART,
        			5*B_HEIGHT/6 - metr.getHeight() - selectedMetr.getHeight());
        	printItem(g, MENU, 5*B_HEIGHT/6 - selectedMetr.getHeight());
        	printItemSelected(g, QUIT, 5*B_HEIGHT/6);
        };
	}
	
	/**
	 * Prints the selected item in the menu with the specifications
	 * @param g
	 * @param msg
	 * @param posY
	 */
	private void printItemSelected(Graphics g, String msg, int posY){
        Font font = new Font(FONT, FONT_STYLE, FONT_SELECTED_SIZE);
        Font inside = new Font(FONT, FONT_STYLE, FONT_SELECTED_SIZE-3);
        FontMetrics metr = this.getFontMetrics(font);

        g.setColor(new Color(0x0A,0x0A,0x0A));
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, posY);
        
        g.setColor(new Color(0xBB,0xBB,0xEE));
        g.setFont(inside);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2 +4, posY+1);
	}
	/**
	 * Prints one regular item in the menu with the specifications
	 * @param g
	 * @param msg
	 * @param posY
	 */
	private void printItem(Graphics g, String msg, int posY){
        Font font = new Font(FONT, FONT_STYLE, FONT_SIZE);
        Font inside = new Font(FONT, FONT_STYLE, FONT_SIZE-3);
        FontMetrics metr = this.getFontMetrics(font);

        g.setColor(new Color(0x0A,0x0A,0x0A));
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, posY);
        
        g.setColor(new Color(0xEE,0xEE,0xEE));
        g.setFont(inside);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2 + 4, posY+1);
	}
	
	
	//InputListener:
	/**
	 * listener the InputPerformed event and changes the chosen menu option.
	 * @param e
	 */
	public void inputPerformed(InputEvent e) {
		switch(e.getInputCode()){
		case InputEvent.IC_ENTER:
			if(selectedOption == OPT_RESTART)
				nextState = StateManager.STATE_SELECTION;
			else if(selectedOption == OPT_MENU)
				nextState = StateManager.STATE_MENU;
			else if(selectedOption == OPT_QUIT)
				nextState = StateManager.STATE_QUIT;
			break;
		case InputEvent.IC_ACTION:
			if(selectedOption == OPT_RESTART)
				nextState = StateManager.STATE_SELECTION;
			else if(selectedOption == OPT_MENU)
				nextState = StateManager.STATE_MENU;
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

	//DevicesListener:
	/**
	 * DevicesListener implementation
	 */
	public void deviceEntered(DevicesEvent e) {}
	public void deviceGotOut(DevicesEvent e) {}
}
