package Billiard;

import java.awt.Rectangle;

public interface Masa {
	static final short LEFT = 74;
	static final short TOP = 134;
	static final short RIGHT = 565;
	static final short BOTTOM = 365;
	static final byte d = 2;
	static final byte DIM = 26;
	static final byte dim = DIM / d;
	static final Rectangle Table = new Rectangle(LEFT - Bila.dim, TOP
			- Bila.dim, RIGHT - LEFT + Bila.DIM, BOTTOM - TOP + Bila.DIM);
}