package assignment4_t2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import assignment4_t2.*;

public class Flooding {

	private final String MyHostName;
	private DatagramSocket socket = null;
	final List<Integer> idList = new ArrayList<Integer>();

	public Flooding() throws UnknownHostException, SocketException {
		this.MyHostName = InetAddress.getLocalHost().getHostName();
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		// 5000 + teamnumber
		this.socket = new DatagramSocket(5009);
		this.floodThread.start();
	}

	private Thread floodThread = new Thread(new Runnable() {
		final byte[] data = new byte[4000];

		@Override
		public void run() {
			DatagramPacket packet = new DatagramPacket(data, data.length);
			while (true) {
				try {
					socket.receive(packet);
					processPackage(packet);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void processPackage(DatagramPacket packet) {
			
			try {
				// create a "Package" object from the data received
				Package myPackage = new Package(packet.getData());
				System.out.println("got package " + myPackage.id);
				
				if(!idList.contains(myPackage.id)) {
					idList.add(myPackage.id);
					
	        		myPackage.path.add(new Node(System.currentTimeMillis(), MyHostName, packet.getAddress().toString()));
					
					if (myPackage.destination.equals(MyHostName)) {
						// package received at destination
						System.out.println("got package from " + myPackage.source + ": " + new String(myPackage.data) + " id: " + myPackage.id);
						long time = myPackage.path.get(0).milliseconds;
						for (Node n : myPackage.path) {
							System.out.println(n.sourceName + " " + (n.milliseconds - time) + "ms");
							time = n.milliseconds;
						}
						
						myPackage.routeBack = true;
						send(myPackage);
					} else {
						// forward package to destination
						System.out.println("relay message from: " + myPackage.source + "to: " + myPackage.destination + " id: " + myPackage.id);
						send(myPackage);
					}
				} else {
					if(myPackage.routeBack) {
						if (myPackage.source.equals(MyHostName)) {

							// got route to the destination back
							long currentTime = System.currentTimeMillis();
							long startTime = myPackage.path.get(0).milliseconds;
							
							System.out.println("got path back in: " + (currentTime - startTime) + "ms, from " + myPackage.destination + " id: " + myPackage.id);
							long time = myPackage.path.get(0).milliseconds;
							for (Node n : myPackage.path) {
								System.out.println(n.sourceName + " " + (n.milliseconds - time) + "ms");
								time = n.milliseconds;
							}
						} else {
							// forward package towards the source node
							System.out.println("send back from: " + myPackage.source + " to: " + myPackage.destination + " id: " + myPackage.id);
							send(myPackage);	
						}
					}
				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	});

	private void send(Package myPackage) {
		try {
			byte[] bcast_msg = myPackage.serialize();
			DatagramPacket packet = null;
			
			if(!myPackage.routeBack) {
				// send package to all receivers
				packet = new DatagramPacket(bcast_msg, bcast_msg.length,
						InetAddress.getByName("192.168.210.255"), 5009);
			}
			else {
				// find the ip address of the previous node
				String prevIp = "";
				for (int i = myPackage.path.size() - 1; i >= 0; i--) {
					if(myPackage.path.get(i).sourceName.equals(MyHostName)){
						prevIp = myPackage.path.get(i).prevNodeIpAdress;
					}
				}
				
				// send the package back to the previous node 
				System.out.println("send back: " + prevIp);
				packet = new DatagramPacket(bcast_msg, bcast_msg.length,
						InetAddress.getByName(prevIp.replace("/",  "")), 5009);	
			}
			
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// send packages out to each receiver from the hostaddresses array
	public void sendPackagesToAllNodes(String[] hostaddresses) throws UnknownHostException {
		for (String address : hostaddresses) {
			if(!address.equals(MyHostName)) {
				// create new package and send it
				System.out.println("send new message to: " + address);
				Package newPackage = new Package(MyHostName, address , "DATA".getBytes(), "");
				idList.add(newPackage.id);
				send(newPackage);
				
				// wait for 1sec
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
