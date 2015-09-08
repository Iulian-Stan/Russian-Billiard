package Billiard;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Retea.*;

@SuppressWarnings("serial")
public class Window extends JFrame {

	public Game panel;
	public Thread r;
	int port;	
	int n = 0;

	public Window() {
		super("Billiard");

		n = JOptionPane.showOptionDialog(null, "Tipul conexiunii", "Messenger",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new Object[] { "Alone", "Server", "Client" }, null);
		if (n == 1) {
			// port =
			// Integer.parseInt(JOptionPane.showInputDialog("Dati portul"));
			port = 9999;
			r = new SimpleServer(port, this);
		}
		if (n == 2) {
			// port =
			// Integer.parseInt(JOptionPane.showInputDialog("Dati portul"));
			// String adress = JOptionPane.showInputDialog("Dati adresa");
			port = 9999;
			String adress = "localhost";
			r = new SimpleClient(port, adress, this);
		}

		Toolkit kit = Toolkit.getDefaultToolkit();
		Image ico = kit.getImage("ico.gif");
		setIconImage(ico);
		JPanel pane = (JPanel) getContentPane();
		pane.setBorder(BorderFactory.createEmptyBorder());
		JMenuBar mb = new JMenuBar();
		JMenu m = new JMenu("Tips");
		JMenuItem mi1 = new JMenuItem("Rules");
		mi1.addActionListener(new ActionListener() {
			String s = "              Rules : \n"
					+ "Each player must score the blue ball\n"
					+ "using the red one , if no blue balls\n"
					+ "are scored or the red one drops - \n"
					+ "players change turns. The game is over\n"
					+ "when one of the players scores 6 balls\n"
					+ "or each has already 5 balls";

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(null, s, "Help",
						JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JMenuItem mi2 = new JMenuItem("Control");
		mi2.addActionListener(new ActionListener() {
			String s = "              Control : \n"
					+ "Move the mouse to chose the direction\n"
					+ "of the hit. Using the scroll one set\n"
					+ "the power (you'll see the cue moving\n"
					+ "After just click thr mouse. Good Luck!";

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(null, s, "Control",
						JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		m.add(mi1);
		m.add(mi2);
		mb.add(m);
		pane.add(mb, BorderLayout.NORTH);
		panel = new Game(this);
		pane.add(panel, BorderLayout.CENTER);
		setVisible(true);
		Insets insets = getInsets();
		setSize(637 + insets.left, 500 + insets.top);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

	public static void main(String[] args) {
		Window Billiard = new Window();
		Billiard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (Billiard.n > 0)
			Billiard.r.start();
	}
}