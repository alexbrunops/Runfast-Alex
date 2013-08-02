package br.unb.unbiquitous.ubiquitos.runFast.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.unb.unbiquitous.ubiquitos.runFast.states.State;

public class Window extends JFrame{

	/**
	 * serialVersionUID = 96314687473652840L;
	 */
	private static final long serialVersionUID = 96314687473652840L;
	
	public static final int WINDOW_WIDTH = 1280;//1152;//1024;
	public static final int WINDOW_HEIGHT = 768;
	
	private static Window window = null;
	
	public static Window GetInstance() {
		if (window == null) {
			window = new Window();
			window.setTitle("Run Fast!");
		}
		return window;
	}
	
	private Window() {
		//super.setIconImage(new ImageIcon(getClass().getResource("boneco.png")).getImage());
		
		//add(new Board());
        setTitle("Run Fast!");
        
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	JPanel panel = (JPanel) getContentPane();
            	if(panel instanceof State)
            		((State)panel).endGame();
                System.exit(0);
            }
        });
        
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
		//this.setExtendedState(MAXIMIZED_BOTH);
	}
	
	public void change(JPanel painel) {
		window.remove(window.getContentPane());
		window.setContentPane(painel);
		window.setVisible(true);
	}
	
}
