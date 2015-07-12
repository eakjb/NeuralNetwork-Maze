package com.eakjb.learning.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Maze {
	public static final int PLAYER_STUCK_MOVES_BEFORE_RESET = 3;
	public static final int FPS = 60;
	public static final int MILLISECOND_FRAME_DELAY = 1000 / FPS;

	public static final int STEPS_PER_SECOND = 5;
	public static final int MILLISECOND_STEP_DELAY = 1000 / STEPS_PER_SECOND;

	private static void setupSimulation(RenderFrame frame) {
		World w = new World(10,10,new Point(0,0), new Point(9,9));
		
		w.getObstacles().add(new Point(1,1));
		
		Network n = new Network(30,20);

		frame.getWorldCanvas().setWorld(w);
		frame.getNetworkCanvas().setNetwork(n);

		List<WorldOutputNode> worldOutputNodes = new ArrayList<WorldOutputNode>();

		worldOutputNodes.add(new WorldOutputNode(new Point(25,1),w,Direction.UP));
		worldOutputNodes.add(new WorldOutputNode(new Point(25,2),w,Direction.DOWN));
		worldOutputNodes.add(new WorldOutputNode(new Point(25,3),w,Direction.LEFT));
		worldOutputNodes.add(new WorldOutputNode(new Point(25,4),w,Direction.RIGHT));

		for (Node node : worldOutputNodes) {
			n.add(node);
		}

		Node test = new Node(new Point(18,2));
		test.getPositiveOutputs().add(worldOutputNodes.get(1));
		n.add(test);

		Node test2 = new Node(new Point(20,3));
		test2.getPositiveOutputs().add(worldOutputNodes.get(3));
		n.add(test2);

		Node test3 = new Node(new Point(17,7));
		test3.getNegativeOutputs().add(worldOutputNodes.get(3));
		n.add(test3);

		for (int x = 0; x < 5; x++) for (int y = 0; y < 5; y++) {
			WorldInputNode node = new WorldInputNode(new Point(x+1,y+1),w,new Point(x-2,y-2));
			n.add(node);

			if (x==2&&y==3) {
				node.getNegativeOutputs().add(test);
			}

			if (x==3&&y==2) {
				node.getNegativeOutputs().add(test2);
			}
			
			if (x==3&&y==3) {
				//node.getPositiveOutputs().add(test3);
			}
		}
		
		w.triggerWorldUpdate();
	}

	public static void main(String[] args) {
		RenderFrame frame = new RenderFrame(32,16);

		setupSimulation(frame);

		frame.setVisible(true);


		//--Render Thread--
		Thread renderThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					frame.repaintCanvases();

					try {
						Thread.sleep(MILLISECOND_FRAME_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		//--End Render Thread--		
		//--Step Thread--
		Thread stepThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int playerStuckMoves = 0;
				
				while (true) {
					World w = frame.getWorldCanvas().getWorld();
					Network n = frame.getNetworkCanvas().getNetwork();

					boolean moved = false;
					for (Node node : n) {
						if (node.isActive() && node instanceof WorldOutputNode) {
							if (w.movePlayer(new Point(
									(int) (((WorldOutputNode) node).getDirection().getAdjacentPosition().getX() + w.getPlayer().getX()),
									(int) (((WorldOutputNode) node).getDirection().getAdjacentPosition().getY() + w.getPlayer().getY())))) {
								moved=true;
							}
						}
					}
					if (moved) {
						playerStuckMoves=0;
					} else {
						playerStuckMoves++;
					}

					if (playerStuckMoves>PLAYER_STUCK_MOVES_BEFORE_RESET) {
						setupSimulation(frame);
					}

					try {
						Thread.sleep(MILLISECOND_STEP_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		});
		//--End Step Thread--

		stepThread.start();
		renderThread.start();
	}

}
