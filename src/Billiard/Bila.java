package Billiard;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Bila implements Masa, Runnable {

	static final byte RATE = 40;

	private static Game m;

	public double xv, yv, xp, yp, a, b, c;
	public Ellipse2D.Double bila;
	public Thread loop = null;
	public double putere = 0;
	

	public Bila(double x, double y, Game m) {
		xp = xv = x - dim;
		yp = yv = y - dim;
		bila = new Ellipse2D.Double(xp, yp, DIM, DIM);
		Bila.m = m;
	}

	public void buzunar() {
		if ((xp <= LEFT + d || xp >= RIGHT - d)
				&& (yp <= TOP + d || yp >= BOTTOM - d)
				|| xp >= (RIGHT + LEFT - dim) / d - d
				&& xp <= (RIGHT + LEFT + dim) / d + d
				&& (yp <= TOP || yp >= BOTTOM)) {
			stop();
			if (this == m.bile[0]) {
				bila = null;
				m.setOk(true);
			} else {
				if (m.bile[0].bila != null)
					m.setOk(false);
				this.xp = 295 - dim - m.getplayer()[m.getPlayer() - 1]
						* (DIM + dim) + 329 * (m.getPlayer() - 1);
				this.yp = 455.5;
				m.addplayer();
			}
			m.repaint();
		}
	}

	public synchronized void coliziune(Bila bi) {
		if (Point2D.Double.distance(xp, yp, bi.xp, bi.yp) <= DIM) {
			bi.xv = xp;
			bi.yv = yp;
			double x = xp, y = yp;
			xp = xv;
			yp = yv;
			bi.dreapta();
			double p = (1 - Math.abs(a * bi.xp + b * bi.yp + c)
					/ Math.sqrt(a * a + b * b) / DIM)
					* (RATE - putere)
					- (1 - Math.abs(bi.a * xp + bi.b * yp + bi.c)
							/ Math.sqrt(bi.a * bi.a + bi.b * bi.b) / DIM)
					* ((bi.putere == 0) ? 0 : RATE - bi.putere);
			if (putere >= bi.putere) {
				putere += Math.abs(p);
				bi.putere += RATE - putere + Math.abs(p);
			} else {
				putere += RATE - putere + Math.abs(p);
				bi.putere += Math.abs(p);
			}
			if (bi.loop == null)
				bi.start();
			xp = x;
			yp = y;
		}
	}

	public void dreapta() {
		a = yv - yp;
		b = xp - xv;
		c = yp * (xv - xp) - xp * (yv - yp);
	}

	public boolean peMasa() {
		return Table.contains(xp - dim, yp - dim, DIM, DIM);
	}

	public void run() {
		putere = RATE - putere;
		long time;
		Thread t = Thread.currentThread();
		dreapta();
		if (putere == RATE)
			stop();
		while (loop == t) {
			time = System.currentTimeMillis();
			if (!peMasa()) {
				if (xp > RIGHT) {
					xv = xp + xp - xv;
					xp = RIGHT;
				}
				if (xp < LEFT) {
					xv = xp - xv + xp;
					xp = LEFT;
				}
				if (yp > BOTTOM) {
					yv = yp + yp - yv;
					yp = BOTTOM;
				}
				if (yp < TOP) {
					yv = yp - yv + yp;
					yp = TOP;
				}
				dreapta();
			} else {
				double xaux, yaux;
				xaux = xp;
				yaux = yp;
				if (Math.abs(yv - yp) / (yv + yp) > Math.abs(xv - xp)
						/ (xv + xp)) {
					if (yv < yp)
						yp++;
					else
						yp--;
					if (xp != xv)
						xp = (-c - yp * b) / a;
				} else {
					if (xv < xp)
						xp++;
					else
						xp--;
					if (yp != yv)
						yp = (-c - xp * a) / b;
				}
				xv = xaux;
				yv = yaux;
			}
			m.repaint();
			buzunar();
			for (int i = 0; i < m.bile.length; i++)
				if (this != m.bile[i])
					coliziune(m.bile[i]);
			try {
				Thread.sleep(Math.max(((long) putere)
						- System.currentTimeMillis() + time, 0));
			} catch (Exception ex) {
				System.out.println("eroare la somn");
			}
			putere += putere / 300.;
			if (putere > RATE)
				stop();
		}
	}

	public void start() {
		loop = new Thread(this);
		loop.start();
	}

	public void stop() {
		loop = null;
		putere = 0;
		m.zeroForce();
		xv = xp;
		yv = yp;
		m.repaint();
	}
}