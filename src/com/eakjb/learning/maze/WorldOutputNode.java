package com.eakjb.learning.maze;

import java.awt.Point;

public class WorldOutputNode extends Node {
	private final World world;
	private final Direction direction;

	public WorldOutputNode(Point location, World w, Direction d) {
		super(location);
		this.world=w;
		this.direction=d;
	}

	public Direction getDirection() {
		return direction;
	}

	public World getWorld() {
		return world;
	}
	
}
