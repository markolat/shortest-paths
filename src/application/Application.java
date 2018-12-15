package application;

import cell.Cell;
import helper.Helper;

import java.util.List;
import java.util.ArrayList;

public class Application {

	public static void main(String[] args) {
		
		if(args.length < 1 || args.length > 2) {
			System.err.println("Invalid number of arguments!");
			return;
		}
		
		long startTime = System.currentTimeMillis();
		
		List<List<Cell>> listOfPaths = new ArrayList<>();
		List<Cell> listOfCells = new ArrayList<>();
		
		String pathToInputFile = args[0];
		String pathToOutputFile = "";
		
		if(args.length == 2)
			pathToOutputFile = args[1];
		
		// Parsing xml file
		listOfCells = Helper.parseXML(pathToInputFile);
		if(listOfCells == null)
			return;
			
		// Helper.makeAdjacencyList returns list of lists (adjacency list) of indices of cells in listOfCells
		List<List<Cell>> adjList = Helper.makeAdjacencyList(listOfCells);
			
		// Running breadth-first search that will calculate distances
		Helper.BFS(listOfCells, adjList);
			
		// Calculating shortest paths from startPoint to endPoint - new ArrayList() is for starting path
		Helper.calculatePaths(Helper.endPoint, Helper.startPoint, listOfCells, new ArrayList<>(), adjList, listOfPaths);
			
		long endTime = System.currentTimeMillis();
		long timeInMillis = endTime - startTime;
		
		// Generating JSON output file with shortest paths
		Helper.generateJSONFile(listOfPaths, timeInMillis, pathToOutputFile);
	}
}
