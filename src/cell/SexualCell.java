package cell;

import java.util.concurrent.CopyOnWriteArrayList;

public class SexualCell extends Cell {

	public SexualCell(String name){
		super(name);
	}

	private void setPropertiesAfterReproduction(Cell cell){
		cell.setFull(false);
		cell.setCounter(T_HUNGRY);
		cell.setEatenFood(0);
	}

	@Override
	public void reproduce() {
		CopyOnWriteArrayList<SexualCell> cellsInGameSpace = gameSpace.getSexualCells();
		boolean lockedCell;
		for(SexualCell cell : cellsInGameSpace){
			if(this != cell && this.canReproduce() && cell.canReproduce()) {

				lockedCell = cell.lock.tryLock();
				if (lockedCell) {
					try {
						Cell child = new SexualCell("Child of: " + this.getName() + " and " + cell.getName());
						gameSpace.addCell(child);
						setPropertiesAfterReproduction(this);
						setPropertiesAfterReproduction(cell);

						System.out.println("Cells " + this.getName() + " and " + cell.getName() + " have reproduced!");
						Thread t = new Thread(child);
						t.start();

					} finally {
						cell.lock.unlock();
					}
				}
			}

		}
	}
}
