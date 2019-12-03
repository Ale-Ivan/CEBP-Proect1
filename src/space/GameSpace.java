package space;

import cell.Cell;
import cell.SexualCell;
import resources.Resource;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameSpace {

	private final int MAX_NUMBER_OF_CELLS = 10;
	private final int MAX_NUMBER_OF_RESOURCES = 10;

	private int countCells = 0;
	private int countFood = 0;

	private CopyOnWriteArrayList<Cell> cells = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<Resource> resources = new CopyOnWriteArrayList<>();
	private static GameSpace singleton = null;

	public static GameSpace getGameSpace(){
		if(singleton == null)
			singleton = new GameSpace();
		return singleton;
	}

	public void addCell(Cell cell){
		if(countCells < MAX_NUMBER_OF_CELLS){
			cells.add(cell);
			countCells++;
		}
	}

	public void removeCell(Cell cell){
		if(cells.contains(cell))
			cells.remove(cell);
	}

	public void addResource(Resource resource){
		if (countFood < MAX_NUMBER_OF_RESOURCES) {
			resources.add(resource);
			countFood++;
		}
	}

	public CopyOnWriteArrayList<SexualCell> getSexualCells() {
		CopyOnWriteArrayList<SexualCell> sexualCells = new CopyOnWriteArrayList<>();
		for(Cell cell: cells){
			if(cell instanceof SexualCell){
				sexualCells.add((SexualCell) cell);
			}
		}
		return sexualCells;
	}

	public Resource getResourceWithLeastUnits(){
		int minUnits = 100;
		Resource resourceToReturn = null;
		for(Resource resource : resources){
			int units = resource.getUnits();
			if(units < minUnits){
				minUnits = units;
				resourceToReturn = resource;
			}
		}
		return resourceToReturn;
	}

	public boolean checkSpaceForFood(Cell cell){
		try{
			boolean lockedFood;
			for(Resource resource: resources){
				lockedFood = resource.lock.tryLock();
				if(lockedFood) {
					try {
						//System.out.println(cell.getName() + " locked a resource unit. " + resource.getName());
						//System.out.println("resource: " + resource.getName() + ", units: " + resource.getUnits());
						if (resource.getUnits() > 0) {
							resource.decrementUnits();
							System.out.println("There are " + resource.getUnits() + " units left of " + resource.getName());
							return true;
						}
					} finally {
						//System.out.println(cell.getName() + " unlocked a resource unit");
						resource.lock.unlock(); //to avoid a deadlock
					}
				} else {
					//System.out.println(cell.getName() + " tried to lock a resource unit." + resource.getName());
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}

	public void printCellsAndFood(){
		System.out.print("Cells: ");
		for(Cell cell : cells)
			System.out.print(cell.getName() + " ");
		System.out.println();

		System.out.print("Resource: ");
		for(Resource resource : resources)
			System.out.print(resource.getName() + " ");
		System.out.println();
	}

	public void startGameOfLife() {
		for(Cell cell: cells) {
			Thread t = new Thread(cell);
			t.start();
		}
	}
}

