package com.eakjb.learning.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class World implements Grid {	
	private Point player;
	
	private Point goal;
	
	private final int width;
	private final int height;
	
	private final List<Point> obstacles = new ArrayList<Point>();
	
	/**
	 * The number of times the player has moved
	 */
	private int moves;
	
	public World(int width, int height, Point player, Point goal) {
		this.width=width;
		this.height=height;
		
		this.player=player;
		this.goal=goal;
	}
	
	public boolean isPositionBlocked(Point p) {
		for (Point obstacle : obstacles) {
			if (p.equals(obstacle)) return true;
		}
		return p.getX()>=width||p.getX()<0||p.getY()>=height||p.getY()<0;
	}
	
	public int getMaxFitness() {
		return (int) ORIGIN.distance(this.getWidth(), this.getHeight());
	}
	
	public int getMinFitness() {
		return 0;
	}	
	
	/**
	 * Returns the evolutionary fitness of the current player
	 * @return The current distance of the player to the goal subtracted from the maximum on screen distance (0,0) to (width,height)
	 */
	public double getFitness() {
		return (this.getMaxFitness() - player.distance(goal))/moves;
	}
	
	private List<WorldUpdateListener> worldUpdateListeners = new ArrayList<WorldUpdateListener>();
	public static interface WorldUpdateListener {
		public void worldUpdated();
	}
	public void addWorldUpdateListener(WorldUpdateListener listener) {
		this.worldUpdateListeners.add(listener);
	}
	protected void triggerWorldUpdate() {
		for (WorldUpdateListener listener : this.worldUpdateListeners)  {
			listener.worldUpdated();
		}
	}
	
	public boolean movePlayer(Point pos) {
		this.moves++;
		
		if (this.player.equals(pos)||
				this.isPositionBlocked(pos)) {
			triggerWorldUpdate();
			return false;
		}
		this.setPlayer(pos);
		return true;
	}

	//----Getters Setters----

	public Point getPlayer() {
		return player;
	}

	public void setPlayer(Point player) {
		this.player = player;
		triggerWorldUpdate();
	}

	public Point getGoal() {
		return goal;
	}

	public void setGoal(Point goal) {
		this.goal = goal;
		triggerWorldUpdate();
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Point> getObstacles() {
		return obstacles;
	}
	
}
