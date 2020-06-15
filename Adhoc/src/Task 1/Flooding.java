package assignment4_t1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import assignment4_t1.*;

public class Flooding {

	private final String MyHostAddress;
	private DatagramSocket socket = null;
	final List<Integer> idList = new ArrayList<Integer>();

	public Flooding() throws UnknownHostException, SocketException {
		this.MyHostAddress = InetAddress.getLocalHost().getHostName();
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
					processPackage(packet.getData());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void processPackage(byte[] data2) {
			
			try {
				Package myPackage = new Package(data2, MyHostAddress);

				System.out.println("got package " + myPackage.id);
				
				/*
				boolean alreadyVisited = false;
				for (int i = 0; i < myPackage.path.size() - 1; i++) {
					if (myPackage.path.get(i).ipAdress.equals(MyHostAddress)) {
						alreadyVisited = true;
						break;
					}
				}
				
				if (!alreadyVisited && 
						*/
				
				if(!idList.contains(myPackage.id)) {
					idList.add(myPackage.id);
					
					if (myPackage.destination.equals(MyHostAddress)) {
						System.out.println("got package from " + myPackage.source + ": " + new String(myPackage.data) + " id: " + myPackage.id);
						long time = myPackage.path.get(0).milliseconds;
						for (Node n : myPackage.path) {
							System.out.println(n.ipAdress + " " + (n.milliseconds - time) + "ms");
							time = n.milliseconds;
						}
					} else {
						System.out.println("relay message from: " + myPackage.source + "to: " + myPackage.destination + " id: " + myPackage.id);
						sendAll(myPackage);
					}
				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	});

	private void sendAll(Package myPackage) {
		try {
			//socket.setBroadcast(true);
			byte[] bcast_msg = myPackage.serialize();
			DatagramPacket packet = new DatagramPacket(bcast_msg, bcast_msg.length,
					InetAddress.getByName("192.168.210.255"), 5009);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPackagesToAllNodes(String[] hostaddresses) {
		for (String address : hostaddresses) {
			if(!address.equals(MyHostAddress)) {
				System.out.println("send new message to: " + address);
				Package newPackage = new Package(MyHostAddress, address , "DATA".getBytes());
				idList.add(newPackage.id);
				sendAll(newPackage);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
