package services;

import java.util.ArrayList;
import java.util.HashMap;
import model.Area;
import model.Cluster;
import model.Robot;
import server.WebsocketServer;

public class IRUpdater {
	
	private WebsocketServer websocketServer;
	private HashMap<Integer, Area> areas;
	
	public IRUpdater(WebsocketServer websocketServer,  HashMap<Integer, Area> areas) {
		this.websocketServer = websocketServer;
		this.areas = areas;
	}

	public void run() {
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while( true ) {
			if( ! areas.isEmpty() ) {
				ArrayList<Area> current_areas = new ArrayList<Area>(areas.values());
				for( Area current_area :  current_areas) {
					ArrayList<Cluster> current_clusters = new ArrayList<Cluster>(current_area.getClusters().values());
	    			for( Cluster current_cluster : current_clusters ) {
	    				ArrayList<Robot> current_robots = new ArrayList<Robot>(current_cluster.getRobots().values());
	    				for( Robot robot : current_robots ) {
	    					robot.updateDownTime();
	    				}
	    				current_cluster.updateDownTime();
	    			}	
	    		}	            
				// Starting service to update clients json.
	            ClientsSender clientsSender = new ClientsSender(websocketServer);
	            
	            // Send updated and structured json to clients.
	            clientsSender.run();
			}
			
			// SLEEP TO KEEP UDPATE TIME AROUND 30 SECONDS WITH 90000 ROBOTS.
			
			try {
				Thread.sleep(10000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}