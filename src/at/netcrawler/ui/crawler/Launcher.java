package at.netcrawler.ui.crawler;

import java.util.Set;

import javax.swing.UIManager;

import at.andiwand.library.util.collections.CollectionUtil;
import at.netcrawler.network.topology.HashTopology;


public class Launcher {
	
	public static void main(String[] args) {
		Set<String> arguments = CollectionUtil.arrayToHashSet(args);
		
		if (arguments.contains("nogui")) {
			// TODO: start cmd-interface and http-server for third-party
			// interaction and automation
		} else {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.out.println("Couldn't apply system look and feel");
			}
			
			new GUI(new HashTopology());
		}
	}
	
}