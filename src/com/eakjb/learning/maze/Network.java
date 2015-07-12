package com.eakjb.learning.maze;

import java.util.ArrayList;


public class Network extends ArrayList<Node> implements Grid {
	private static final long serialVersionUID = 1L;
	
	private final int width;
	private final int height;
	
	public Network(int width, int height) {
		this.width=width;
		this.height=height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
