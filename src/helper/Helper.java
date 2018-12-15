package helper;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cell.Cell;
import cell.Column;

/**
 * @author Marko Latinovic
 *
 */
public class Helper {
	
	public static Cell startPoint;
	public static Cell endPoint;
	
	/**
	 * <p> Reads and parse the input file (must be XML file with strictly defined contents) 
	 * and creates list of all cells in the map </p>
	 * @param pathToFile destination to the input file that represents map (graph)
	 * @return returns list of all cells in the map (graph)
	 */
	public static List<Cell> parseXML(String pathToFile){
		List<Cell> listOfCells = new ArrayList<>();
		
		try {
			// Loading the XML file with a map and creating list of all cells, start point and end point cell
			Path path = Paths.get(pathToFile).normalize();
			File xmlFile = path.toFile();
								
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);	
			doc.getDocumentElement().normalize();
				
			// Checking for invalid tags in XML file
			List<String> tagList = Arrays.asList("map", "cells", "cell", "start-point", "end-point");	
			NodeList allNodes = doc.getElementsByTagName("*");
			for(int i = 0; i < allNodes.getLength(); i++) {
				Element element = (Element)allNodes.item(i);
				if(!tagList.contains(element.getNodeName())) {
					System.err.println("Invalid file content!");
					return null;
				}
			}
			
			// Checking if some of the necessary tags does not exist in XML file
			for(String tag : tagList) {
				if(!checkIfTagExistsInXMLFile(doc, tag)) {
					System.err.println("Invalid file content!");
					return null;
				}
			}
						
			// Getting all of the cells from XML document and putting them into the list (listOfCells)
			NodeList nodeList = doc.getElementsByTagName("cell");		
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
							
				Element el = (Element) node;
				int row = Integer.parseInt(el.getAttribute("row"));
				Column col = Column.valueOf(el.getAttribute("col"));
				listOfCells.add(new Cell(row, col));
			}
						
			// Getting the start and end points
			Node start = doc.getElementsByTagName("start-point").item(0);
			int row = Integer.parseInt(((Element) start).getAttribute("row"));
			Column col = Column.valueOf(((Element) start).getAttribute("col"));
			startPoint = listOfCells.get(listOfCells.indexOf( new Cell(row, col)));
						
			Node end = doc.getElementsByTagName("end-point").item(0);
			row = Integer.parseInt(((Element) end).getAttribute("row"));
			col = Column.valueOf(((Element) end).getAttribute("col"));
			endPoint = listOfCells.get(listOfCells.indexOf( new Cell(row, col)));
			
		}catch(Exception e) {
			System.err.println("Could not load XML file!\n\n"
					+ "Possible reasons:\n\n"
					+ "Invalid argument format;\n"
					+ "Can not find the path specified (File does not exist);\n"
					+ "Invalid file format;\n"
					+ "start-point and/or end-point cell are invalid;\n\n");
			return null;
		}
		return listOfCells;
	}
	
	
	/**
	 * <p> Checks if node with the given tag name exists in document </p>
	 * @param doc Document object used for parsing XML file
	 * @param tag string that represents node with given tag name
	 * @return returns true if node with the given tag name exists in document or false if not
	 */
	private static boolean checkIfTagExistsInXMLFile(Document doc, String tag) {
		NodeList nl = doc.getElementsByTagName(tag);
		if(nl.getLength() > 0)
			return true;
		return false;
	}

	/**
	 * <p> Creates adjacency list for cells in the map (graph)</p>
	 * @param listOfCells list of all cells in the map (graph)
	 * @return returns adjacency list for all of the cells in the map (graph)
	 */
	public static List<List<Cell>> makeAdjacencyList(List<Cell> listOfCells) {
		List<List<Cell>> adjList = new ArrayList<>();
		
		// For every cell check if top, bottom, left and right neighbour exists in listOfCells. 
		// If it does, add it to cellNeighbours list of that cell. 
		// For every cell add it's list of neighbours to adjacency list.
		for(Cell cell : listOfCells) {
			List<Cell> cellNeighbours = new ArrayList<>(); 
			for(int row = -1; row < 2; row++) {
				for(int col = -1; col < 2; col++) {
					if(Math.abs(row) - Math.abs(col) == 0)
						continue;
					Cell adjCell = new Cell(cell.getRow() + row, 
							Column.getColumnAtPosition(cell.getCol().getPosition() + col));
					int indexOfAdjCell = listOfCells.indexOf(adjCell);
					if(indexOfAdjCell > -1) {
						cellNeighbours.add(listOfCells.get(listOfCells.indexOf(adjCell)));
					}
				}
			}
			adjList.add(cellNeighbours);
		}
		return adjList;
	}

	/**
	 * <p> Breadth-first search algorithm that will calculate distances for every cell in the map (graph) </p>
	 * @param listOfCells list of all cells in the map (graph)
	 * @param adjList adjacency list for every cell in the map (graph)
	 */
	public static void BFS(List<Cell> listOfCells, List<List<Cell>> adjList){		
		Queue<Cell> queue = new LinkedList<>();
		queue.add(startPoint);
		
		while(!queue.isEmpty()) {
			Cell currentCell = queue.poll();
			int indexOfCurrentCell = listOfCells.indexOf(currentCell);
			
			for(int i = 0; i < adjList.get(indexOfCurrentCell).size(); i++) {
				Cell adjCell = adjList.get(indexOfCurrentCell).get(i);
				if(adjCell.getDistance() == 0 && !adjCell.equals(startPoint)) {
					adjCell.setDistance(currentCell.getDistance() + 1);
					queue.add(adjCell);
				}
			}
		}
	}
	
	/**
	 * <p> Recursively calculates shortest paths </p>
	 * @param currentCell current cell that is being added to the path (start value should be end-point cell - going from the end to the start)
	 * @param startCell  start-point cell that's being used for checking of the base case
	 * @param listOfCells list of all cells in the map (graph)
	 * @param path list of cells that's being updated with current cell along the way to start-point cell
	 * @param adjList adjacency list for every cell in the map (graph)
	 * @param listOfPaths list of paths that's being updated with all shortest paths
	 */
	public static void calculatePaths(Cell currentCell, Cell startCell,  List<Cell> listOfCells, List<Cell> path, List<List<Cell>> adjList, List<List<Cell>> listOfPaths){
		// Add this cell to the path
		path.add(currentCell);

		if(currentCell.equals(startCell)) {
			Collections.reverse(path);
			listOfPaths.add(path);
			return;
		}
		
		List<Cell> adjacentCells = getAdjacentsWithLessDistance(currentCell, listOfCells, adjList);
		for(Cell nextCell : adjacentCells) {
			// Add previous cells to the new path
			List<Cell> newPath = new ArrayList<>(path);
			calculatePaths(nextCell, startCell, listOfCells, newPath, adjList, listOfPaths);
		}	
	}
	
	/** <p> Returns adjacent cells with less distance than cell </p>
	 * @param cell cell for which neighbours with less distance than it's distance will be added to neighbourCells list
	 * @param listOfCells list of all cells in the map (graph)
	 * @param adjList adjacency list for every cell in the map (graph)
	 * @return returns list of cells with less distance from start-point than of cell (first parameter)
	 */
	private static List<Cell> getAdjacentsWithLessDistance(Cell cell, List<Cell> listOfCells, List<List<Cell>> adjList){
		List<Cell> neighbourCells = new ArrayList<>();		
		for(Cell nextCell : adjList.get(listOfCells.indexOf(cell))) {
			if(nextCell.getDistance() == cell.getDistance()-1)
				neighbourCells.add(nextCell);
		}
		return neighbourCells;
	}
	
	/**
	 * <p> Generates JSON output file with list of shortest paths from startPoint to endPoint node(cell) </p>
	 * @param listOfPaths list of generated shortest paths from start-point to end-point cell in map (graph)
	 * @param timeInMillis value of time in milliseconds that describes execution time of program
	 * @param pathToFile destination path to output file that tells where should it be written
	 */
	public static boolean generateJSONFile(List<List<Cell>> listOfPaths, long timeInMillis, String pathToFile) {
		try {
			Path path = Paths.get(pathToFile, "shortest_paths.json");
			File file = new File(path.toString());

			JsonFactory factory = new JsonFactory();
			JsonGenerator generator = factory.createGenerator(file, JsonEncoding.UTF8);
			generator.setPrettyPrinter(new DefaultPrettyPrinter());
			
			generator.writeStartObject();
			generator.writeStringField("execution_time_in_ms", String.valueOf(timeInMillis));
			generator.writeArrayFieldStart("paths");
			for(List<Cell> list : listOfPaths) {
				generator.writeStartObject();
				generator.writeArrayFieldStart("points");
				for(Cell cell : list) {
					generator.writeStartObject();
					generator.writeStringField("row", String.valueOf(cell.getRow()));
					generator.writeStringField("col", String.valueOf(cell.getCol()));
					generator.writeEndObject();
				}
				generator.writeEndArray();
				generator.writeEndObject();
			}
			generator.writeEndArray();
			generator.writeEndObject();
			generator.close();
			
			System.out.println("JSON file created at " + file.getAbsolutePath());
			return true;
			
		}catch(Exception e) {
			System.err.println("Could not generate JSON file!\n\n"
					+ "Possible reasons:\n\n"
					+ "Invalid argument format;\n"
					+ "Access is denied;\n"
					+ "File not found - Can not find the path specified;\n\n");
			e.printStackTrace();
			return false;
		}
	}
}
