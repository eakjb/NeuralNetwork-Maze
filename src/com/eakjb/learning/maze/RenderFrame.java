package com.eakjb.learning.maze;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class RenderFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final WorldCanvas worldCanvas;
	private final NetworkCanvas networkCanvas;

	public RenderFrame(int wScale, int nScale) {
		this(null,wScale,null,nScale);
	}

	public RenderFrame(World world, int worldScale, Network network, int networkScale) {
		this.worldCanvas = new WorldCanvas(world,worldScale);
		this.networkCanvas = new NetworkCanvas(network,networkScale);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				this.networkCanvas,this.worldCanvas);

		this.add(splitPane);
		this.setBounds(50, 50, 850, 550);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void repaintCanvases() {
		this.worldCanvas.repaint();
		this.networkCanvas.repaint();
	}

	public static class NetworkCanvas extends Canvas {
		private Network network;
		private int scale;

		public Network getNetwork() {
			return network;
		}

		public void setNetwork(Network network) {
			this.network = network;
			updateDimension();
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
			updateDimension();
		}

		private void updateDimension() {
			if (network!=null) {
				this.setMinimumSize(new Dimension(network.getWidth()*scale,network.getHeight()*scale));
			}
		}

		public NetworkCanvas(Network network, int scale) {
			this.network = network;
			this.scale = scale;
			updateDimension();
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics gNormal) {
			Graphics2D g = (Graphics2D) gNormal;

			//Draw nodes
			for (Node node : network) {
				g.setColor(node.isActive()?Color.GREEN:Color.DARK_GRAY);
				g.fillRect((int) node.getLocation().getX()*scale, (int) node.getLocation().getY()*scale, scale, scale);

			}

			//Draw grid
			for (int x = 0; x < network.getWidth(); x++) for (int y = 0; y < network.getHeight(); y++) {
				g.setColor(Color.BLACK);
				g.drawRect(x*scale, y*scale, scale, scale);
			}

			//Draw connections
			g.setStroke(new BasicStroke(3));
			for (Node node : network) {
				g.setColor(Color.GREEN);
				for (Node other : node.getPositiveOutputs()) {
					g.drawLine((int) node.getLocation().getX()*scale+scale/2, (int) node.getLocation().getY()*scale+scale/2,
							(int) other.getLocation().getX()*scale+scale/2, (int) other.getLocation().getY()*scale+scale/2);
				}

				g.setColor(Color.RED);
				for (Node other : node.getNegativeOutputs()) {
					g.drawLine((int) node.getLocation().getX()*scale+scale/2, (int) node.getLocation().getY()*scale+scale/2,
							(int) other.getLocation().getX()*scale+scale/2, (int) other.getLocation().getY()*scale+scale/2);
				}
			}

			//Draw Labels for output nodes
			for (Node node : network) {
				if (node instanceof WorldOutputNode) {
					g.setColor(Color.WHITE);
					g.fillRect((int) (scale*(node.getLocation().getX()+1)),
							(int) (scale*(node.getLocation().getY())),
							3*scale,scale);
					g.setColor(Color.BLACK);
					g.drawString(((WorldOutputNode) node).getDirection().name(),
							(int) (scale*(node.getLocation().getX()+1)+3),
							(int) (scale*(node.getLocation().getY()+1)-3));
					g.setStroke(new BasicStroke(1));
					g.drawRect((int) (scale*(node.getLocation().getX()+1)),
							(int) (scale*(node.getLocation().getY())),
							3*scale,scale);
				}
			}
		}
	}


	public static class WorldCanvas extends Canvas {
		private int scale;
		private World world;

		public World getWorld() {
			return world;
		}

		public void setWorld(World world) {
			this.world = world;
			updateDimension();
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
			updateDimension();
		}

		private void updateDimension() {
			if (world!=null) {
				this.setMinimumSize(new Dimension(world.getWidth()*scale,world.getHeight()*scale));
			}
		}

		public WorldCanvas(World world, int scale) {
			this.world = world;
			this.scale = scale;
			updateDimension();
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			g.drawString("Fitness    :" + world.getFitness(), 32, scale*world.getHeight()+32);
			g.drawString("Max Fitness: " + world.getMaxFitness(), 32, scale*world.getHeight()+48);
			g.drawString("Min Fitness: " + world.getMinFitness(), 32, scale*world.getHeight()+64);
			g.drawString("Moves      : " + world.getMoves(), 32, scale*world.getHeight()+80);

			//Draw finish
			g.setColor(Color.YELLOW);
			g.fillRect((int) world.getGoal().getX()*scale, (int) world.getGoal().getY()*scale, scale, scale);

			//Draw player
			g.setColor(Color.GREEN);
			g.fillRect((int) world.getPlayer().getX()*scale, (int) world.getPlayer().getY()*scale, scale, scale);

			for (int x = 0; x < world.getWidth(); x++) for (int y = 0; y < world.getHeight(); y++) {
				//Draw blocked positions
				g.setColor(Color.BLACK);
				if (world.isPositionBlocked(new Point(x, y))) {
					g.fillRect(x*scale, y*scale, scale, scale);
				}

				//Draw grid
				g.drawRect(x*scale, y*scale, scale, scale);

			}
		}
	}

	public WorldCanvas getWorldCanvas() {
		return worldCanvas;
	}

	public NetworkCanvas getNetworkCanvas() {
		return networkCanvas;
	}
}
