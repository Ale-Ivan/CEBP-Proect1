package main;

import cell.AsexualCell;
import cell.Cell;
import cell.SexualCell;
import resources.Resource;
import space.GameSpace;

public class Main {

	public static void main(String[] args){

		GameSpace gameSpace = GameSpace.getGameSpace();

		Resource resource1 = new Resource(13, "tomatoes");
		Resource resource2 = new Resource(19, "peppers");
		Resource resource3 = new Resource(15, "cucumbers");

		gameSpace.addResource(resource1);
		gameSpace.addResource(resource2);
		gameSpace.addResource(resource3);

		Cell cell_A = new AsexualCell("A");
		Cell cell_B = new AsexualCell("B");

		Cell cell_C = new SexualCell("C");
		Cell cell_D = new SexualCell("D");
		Cell cell_E = new SexualCell("E");

		gameSpace.addCell(cell_A);
		gameSpace.addCell(cell_B);
		gameSpace.addCell(cell_C);
		gameSpace.addCell(cell_D);
		gameSpace.addCell(cell_E);

		Cell.gameSpace = gameSpace;

		gameSpace.printCellsAndFood();

		gameSpace.startGameOfLife(); //creates the threads for the original cells
	}
}
