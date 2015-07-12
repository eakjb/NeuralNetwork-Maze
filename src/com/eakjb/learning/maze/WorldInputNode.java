package com.eakjb.learning.maze;

import java.awt.Point;

public class WorldInputNode extends Node implements World.WorldUpdateListener {
	private final World world;
	private final Point relativeLocation;

	public WorldInputNode(Point location, World world, Point relative) {
		super(location);
		this.world=world;
		this.relativeLocation=relative;
		
		this.world.addWorldUpdateListener(this);
	}

	public Point getWorldLocation() {
		return relativeLocation;
	}

	@Override
	public void worldUpdated() {
		this.setActive(this.world.isPositionBlocked(new Point(
				(int) (world.getPlayer().getX()+relativeLocation.getX()),
				(int) (world.getPlayer().getY()+relativeLocation.getY()))));
	}
}
