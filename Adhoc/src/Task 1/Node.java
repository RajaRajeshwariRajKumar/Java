package assignment4_t1;

import java.io.Serializable;

public class Node implements Serializable {

	private static final long serialVersionUID = 4357293459L;
	
	long milliseconds;
	String ipAdress;
	
	public Node(long milliseconds, String ipAdress) {
		this.milliseconds = milliseconds;
		this.ipAdress = ipAdress;
	}

	public long getMilliseconds() {
		return milliseconds;
	}
	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	public String getIpAdress() {
		return ipAdress;
	}
	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}
}
