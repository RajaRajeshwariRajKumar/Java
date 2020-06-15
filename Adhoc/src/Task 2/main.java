package assignment4_t2;

import java.net.SocketException;
import java.net.UnknownHostException;

public class main {

	public static void main(String[] args) {
		Flooding flooding = null;
		
		try {
			flooding = new Flooding();
		
			// list of host names
			final String[] hostaddresses = new String[] { "mcladhoc01", "mcladhoc02", "mcladhoc03", "mcladhoc04", "mcladhoc05" };
			
			// send package to all the hosts
			flooding.sendPackagesToAllNodes(hostaddresses);
			
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
	}
}
