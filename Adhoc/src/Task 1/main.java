package assignment4_t1;

import java.net.SocketException;
import java.net.UnknownHostException;

public class main {

	public static void main(String[] args) {
		Flooding flooding = null;
		
		try {
			flooding = new Flooding();
		
			final String[] hostaddresses = new String[] { "mcladhoc01", "mcladhoc02", "mcladhoc03", "mcladhoc04", "mcladhoc05" };
			flooding.sendPackagesToAllNodes(hostaddresses);
			
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
	}
}
