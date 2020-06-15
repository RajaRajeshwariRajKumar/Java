package assignment4_t1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Package implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String source;
	public String destination;
	public byte[] data;
	public int id;
	public List<Node> path = new ArrayList<Node>();

	public Package(String source, String dertination, byte[] data) {
		this.source = source;
		this.destination = dertination;
		this.id = (new Random()).nextInt();
		this.data = data;
		this.path.add(new Node(System.currentTimeMillis(), source));
	}

	public Package (byte[] data, String currentNode) throws ClassNotFoundException, IOException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(data)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                Package newPackage = (Package) o.readObject();
                this.source = newPackage.source;
        		this.destination = newPackage.destination;
        		this.data = newPackage.data;
        		this.id = newPackage.id;
        		this.path = newPackage.path;
        		this.path.add(new Node(System.currentTimeMillis(), currentNode));
            }
        }
	}


	public byte[] serialize() throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(this);
            }
            return b.toByteArray();
        }
	}
}
