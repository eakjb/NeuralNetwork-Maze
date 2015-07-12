package com.eakjb.learning.maze;

import java.awt.Point;

public enum Direction {	
	UP(new Point(0,-1)),
	DOWN(new Point(0,1)),
	LEFT(new Point(-1,0)),
	RIGHT(new Point(1,0));

	private final Point adjacentPosition;
	
	private Direction(Point pos) {
		this.adjacentPosition=pos;
	}

	public Point getAdjacentPosition() {
		return adjacentPosition;
	}
	
}
