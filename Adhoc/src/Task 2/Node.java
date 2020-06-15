package assignment4_t2;

import java.io.Serializable;

public class Node implements Serializable {

	private static final long serialVersionUID = 4357293459L;
	
	long milliseconds;
	String sourceName;
	String prevNodeIpAdress;
	
	public Node(long milliseconds, String sourceName, String ipAdress) {
		this.milliseconds = milliseconds;
		this.sourceName = sourceName;
		this.prevNodeIpAdress = ipAdress;
	}
}
