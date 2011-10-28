/**
 * RTSProfileHolder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/24
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import net.ysuga.corbanaming.CorbaNamingParser;
import net.ysuga.corbanaming.RTNamingContext;
import net.ysuga.rtsbuilder.RTSystemBuilder;
import net.ysuga.rtsystem.profile.RTSystemProfile;

/**
 * 
 * @author ysuga
 * 
 */
public class RTSProfileHolder extends HashMap<String, RTSystemProfile> implements Runnable {

	public static final String ONLINE = "online";

	private Set<String> namingAddressSet;

	public void addNamingAddress(String namingAddress) {
		namingAddressSet.add(namingAddress);
	}

	static {
		instance = new RTSProfileHolder();
	}
	private static RTSProfileHolder instance;

	static public RTSProfileHolder getInstance() {
		return instance;
	}

	private boolean endflag;
	
	public void add(RTSystemProfile profile) {
		put(profile.getName(), profile);
	}

	ExecutorService executor;
	/**
	 * Constructor
	 */
	RTSProfileHolder() {
		super();
		namingAddressSet = new HashSet<String>();
//		executor = Executors.newSingleThreadExecutor();
//		executor.execute(this);
	}

	public void refreshAll() {
		for (String address : namingAddressSet) {
			RTNamingContext nc;
			try {
				nc = CorbaNamingParser.buildRTNamingContext(address);

				RTSystemProfile onlineProfile = RTSProfileHolder.getInstance()
						.get(RTSProfileHolder.ONLINE);
				onlineProfile.addAllComponent(nc);
			} catch (Exception e) {
				
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		
		for(RTSystemProfile rtsProfile:this.values()) {
			try {
				RTSystemBuilder.downwardSynchronization(rtsProfile);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		while(!endflag) {
			System.out.println("Refresh");
			refreshAll();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
