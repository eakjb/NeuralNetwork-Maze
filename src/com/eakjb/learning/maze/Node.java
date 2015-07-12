package com.eakjb.learning.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node {
	private Point location;
	private boolean active = false;
	
	private final List<Node> positiveOutputs = new ArrayList<Node>();
	private final List<Node> negativeOutputs = new ArrayList<Node>();
	
	public Node(Point location) {
		this.location = location;
	}
	
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public boolean isActive() {
		return active;
	}
	protected void setActive(boolean active) {
		this.active = active;
		for (Node out : positiveOutputs) {
			out.setActive(active);
		}
		for (Node out : negativeOutputs) {
			out.setActive(!active);
		}
		
	}

	public List<Node> getPositiveOutputs() {
		return positiveOutputs;
	}
	
	public List<Node> getNegativeOutputs() {
		return negativeOutputs;
	}
}
