package resources;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Resource {

	public final Lock lock = new ReentrantLock();
	private int units;
	private String name;

	public Resource(int units, String name){
		this.units = units;
		this.name = name;
	}

	public int getUnits(){
		return this.units;
	}

	public String getName(){
		return this.name;
	}

	public void addUnits(int numberOfUnits){
		this.units += numberOfUnits;
	}
	public void decrementUnits(){
		this.units--;
	}

}
