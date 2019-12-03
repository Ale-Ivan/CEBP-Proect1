package cell;

public class AsexualCell extends Cell {

	public AsexualCell(String name){
		super(name);
	}

	@Override
	public void reproduce() {
		Cell c1 = new AsexualCell("first child of cell: " + this.getName());
		Cell c2 = new AsexualCell("second child of cell: " + this.getName());
		this.alive = false; //when an AsexualCell divides, it creates 2 new cells and it dies
		gameSpace.removeCell(this);
		this.timer.cancel();//Terminates this timer, discarding any currently scheduled tasks.
		this.timer.purge();//Removes all cancelled tasks from this timer's task queue.
		System.out.println("AsexualCell " + this.getName() + " has divided");

		//we need to add the cells into the space
		gameSpace.addCell(c1);
		gameSpace.addCell(c2);

		//create a new thread for each child
		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);

		//start the two threads
		t1.start();
		t2.start();
	}
}
