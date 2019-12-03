package cell;

import resources.Resource;
import space.GameSpace;

import java.util.Timer;
import java.util.TimerTask;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Cell implements Runnable{

	public final Lock lock = new ReentrantLock();

	private String name;
	boolean alive; //poate trebuie un setter
	private boolean full;

	static int T_FULL = 2;
	static int T_HUNGRY = 2;
	private int counter; //we use the same counter for T_FULL and T_HUNGRY;

	Timer timer;

	private int eatenFood;

	public static GameSpace gameSpace;

	public void setFull(boolean full) {
		this.full = full;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setEatenFood(int eatenFood) {
		this.eatenFood = eatenFood;
	}

	public Cell(String name){
		this.name = name;
		this.alive = true;
		this.full = false;
		this.counter = T_HUNGRY;
		this.eatenFood = 0;
		this.timer = new Timer(name); //Creates a new timer whose associated thread has the specified name.
		startTimer(); //the timer should be started when the cell is instantiated
	}

	private void startTimer(){
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(counter > 0) {
					if(!full)
						eat();
					counter--;
				} else {
					if(full){
						counter = T_HUNGRY;
						full = false;
						//eat();
					} else {
						alive = false;
						die();
					}
				}
			}
		}, 0, 1000);
	}

	private void eat(){
		if(gameSpace.checkSpaceForFood(this)){
			this.eatenFood++;
			System.out.println("Cell " + this.getName() + " ate." + this.eatenFood);
			this.full = true;
			this.counter = T_FULL;
			if(canReproduce())
				reproduce();
		}
	}

	private void die(){
		System.out.println("Cell " + getName() + " died.");
		int randomResources = ThreadLocalRandom.current().nextInt(1, 5);
		Resource minResource = gameSpace.getResourceWithLeastUnits();
		minResource.addUnits(randomResources);
		System.out.println("Cell " + getName() + " has generated " + randomResources + " units of "+ minResource.getName());
		gameSpace.removeCell(this);
		this.timer.cancel();//Terminates this timer, discarding any currently scheduled tasks.
		this.timer.purge();//Removes all cancelled tasks from this timer's task queue.
	}

	public boolean canReproduce(){
		return this.eatenFood >= 10  && this.full;
	}

	public abstract void reproduce(); //this method will be implemented in each subclass: SexualCell and AsexualCell

	public String getName(){
		return this.name;
	}

	public void run()
	{

	}
}
