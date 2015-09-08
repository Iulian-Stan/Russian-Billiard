package Billiard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Retea.*;

@SuppressWarnings("serial")
public class Game extends JPanel implements Masa, MouseListener,
		MouseMotionListener, MouseWheelListener {

	private static final double u = DIM - d;
	private static final int y = 262;
	private static final int x = 469;

	private static boolean ok = false;
	private static byte Putere = 0;
	private static byte Player = 1;
	private static byte player[] = { 0, 0 };

	private static BufferedImage masa;
	private static Image rBall, bBall, cue;
	private static Point Tinta = null;

	private static Window w;

	public Bila[] bile = { new Bila(x, y, this), new Bila(x / d, y, this),
			new Bila(x / d - u, y - dim, this),
			new Bila(x / d - u, y + dim, this),
			new Bila(x / d - d * u, y, this),
			new Bila(x / d - d * u, y - DIM, this),
			new Bila(x / d - d * u, y + DIM, this),
			new Bila(x / d - 3 * u, y - dim - DIM, this),
			new Bila(x / d - 3 * u, y - dim, this),
			new Bila(x / d - 3 * u, y + dim, this),
			new Bila(x / d - 3 * u, y + dim + DIM, this) };

	public Game(Window w) {
		try {
			masa = ImageIO.read(new File("table.jpg"));
		} catch (IOException e) {
			System.out.println("Eroare imagine fundal");
		}
		try {
			rBall = ImageIO.read(new File("redBall.gif"));
		} catch (IOException e) {
			System.out.println("Eroare imagine bila");
		}
		try {
			bBall = ImageIO.read(new File("blueBall.gif"));
		} catch (IOException e) {
			System.out.println("Eroare imagine bila");
		}
		try {
			cue = ImageIO.read(new File("cue.gif"));
		} catch (IOException e) {
			System.out.println("Eroare imagine bila");
		}
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
		Game.w = w;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (w.n == Player) {
			sendInfo();
		}
		if (valid()) {
			if (ok) {
				changePlayer();
				ok = false;
				if (bile[0].bila == null)
					bile[0] = new Bila(x, y, this);
				if (w.n > 0)
					sendInfo();
			}
		}
		g.drawImage(masa, 0, 0, null);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0xC0C0C0));
		g2.fillRect(103, 15, 70, 23);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Algerian", Font.ITALIC, 20));
		g2.drawString("Player" + Player, 100, 32);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Algerian", Font.BOLD, 40));
		g2.drawString("Russian Billiard", 230, 50);
		g2.setColor(Color.GRAY);
		g2.fillRect(12, 443, 286, 25);
		g2.fillRect(341, 443, 286, 25);
		for (int i = 1; i < bile.length; i++)
			g2.drawImage(bBall, (int) bile[i].xp - dim, (int) bile[i].yp
					- dim, DIM, DIM, null);
		g2.setColor(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(6));
		g2.drawRoundRect(12, 443, 286, 25, 3, 3);
		g2.drawRoundRect(341, 443, 286, 25, 3, 3);
		g2.clip(new Ellipse2D.Double(17, 15, 77, 77));
		for (int i = 0; i < Putere; i++) {
			g2.setColor(new Color(6 * i, 200, 255 - 6 * i));
			g2.fill3DRect(17, 91 - i * d, 77, d, true);
		}
		g2.setClip(null);
		g2.setStroke(new BasicStroke());
		if (bile[0].bila != null)
			g2.drawImage(rBall, (int) bile[0].xp - dim, (int) bile[0].yp
					- dim, DIM, DIM, null);
		if (Tinta != null) {
			g2.rotate(Math.atan2(Tinta.y - bile[0].yp, Tinta.x - bile[0].xp),
					bile[0].xp, bile[0].yp);
			g2.drawImage(cue, (int) bile[0].xp + dim + Putere,
					(int) bile[0].yp - dim / 3, 200, 10, null);
		}
		if (player[0] == 6 || player[1] == 6
				|| (player[0] == 5 && player[1] == 5)) {
			for (int i = 0; i < bile.length; i++)
				bile[i].stop();
			reset();
		}
	}

	public boolean getOk() {
		return ok;
	}

	public void setOk(boolean b) {
		ok = b;
	}

	public void zeroForce() {
		Putere = 0;
	}

	public byte getPlayer() {
		return Player;
	}

	public void changePlayer() {
		Player = (byte) ((Player == 1) ? 2 : 1);
	}

	public byte[] getplayer() {
		return player;
	}

	public void addplayer() {
		player[Player - 1]++;
	}

	public boolean valid() {
		for (int j = 0; j < 11; j++) 
			if (bile[j].loop != null)
				return false;
		return true;
	}

	public void reset() {
		Player = 1;
		player[0] = player[1] = 0;
		bile[0] = new Bila(x, y, this);
		bile[1] = new Bila(x / d, y, this);
		bile[2] = new Bila(x / d - u, y - dim, this);
		bile[3] = new Bila(x / d - u, y + dim, this);
		bile[4] = new Bila(x / d - d * u, y, this);
		bile[5] = new Bila(x / d - d * u, y - DIM, this);
		bile[6] = new Bila(x / d - d * u, y + DIM, this);
		bile[7] = new Bila(x / d - 3 * u, y - dim - DIM, this);
		bile[8] = new Bila(x / d - 3 * u, y - dim, this);
		bile[9] = new Bila(x / d - 3 * u, y + dim, this);
		bile[10] = new Bila(x / d - 3 * u, y + dim + DIM, this);
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		if (valid() && (Player == w.n || w.n == 0)) {
			Tinta = e.getPoint();
			repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
		Tinta = null;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		if (valid() && (Player == w.n || w.n == 0) && Putere != 0) {
			bile[0].xv = e.getX();
			bile[0].yv = e.getY();
			bile[0].putere = Putere;
			ok = true;
			bile[0].start();
			Tinta = null;
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if (valid() && (Player == w.n || w.n == 0)) {
			Tinta = e.getPoint();
			repaint();
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int i = e.getWheelRotation();
		if (valid() && (Player == w.n || w.n == 0) && Putere - i <= 39
				&& Putere - i >= 0) {
			Putere -= i;
		}
		repaint();
	}

	public void sendInfo() {
		int j;
		String mes = "" ;
		for (j = 0; j < 11; j++)
		mes += bile[j].xp + " " + bile[j].yp + " " ;
		mes += player[0] + " " ;
		mes += player[1] + " " ;
		mes += Player;
		if (w.n == 1)
			((SimpleServer) w.r).out.println(mes);
		if (w.n == 2)
			((SimpleClient) w.r).out.println(mes);
	}

	public void readInfo() {
		int j;
		StringTokenizer i = null;
		switch (w.n) {
		case 1:
			i = new StringTokenizer(((SimpleServer) w.r).mes);
			break;
		case 2:
			i = new StringTokenizer(((SimpleClient) w.r).mes);
			break;
		}
		for (j = 0; j < 11; j++) {
			bile[j].xp = Double.parseDouble(i.nextToken(" "));
			bile[j].yp = Double.parseDouble(i.nextToken(" "));
		}
		player[0] = Byte.parseByte(i.nextToken(" "));
		player[1] = Byte.parseByte(i.nextToken(" "));
		Player = Byte.parseByte(i.nextToken());
		repaint();
	}
}