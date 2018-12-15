package unit_tests;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import cell.Cell;
import cell.Column;
import helper.Helper;

class ApplicationTest {

	@Test
	void testParseXML() {
		assertEquals("'1&' must return null", null, Helper.parseXML("1&:"));
		assertEquals("'c:?/hello' must return null", null, Helper.parseXML("c:?/hello"));
		assertEquals("'1..' must return null", null, Helper.parseXML("1.."));
		assertEquals("'/s' must return null", null, Helper.parseXML("/s&"));
		assertEquals("'test_files/panda.jpg' must return null, Invalid file format", null, Helper.parseXML("test_files/panda.jpg"));
		@SuppressWarnings("unchecked")
		List<Cell> listOfCells = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.B));
			add(new Cell(1, Column.C));
			add(new Cell(1, Column.D));
			add(new Cell(2, Column.B));
			add(new Cell(3, Column.A));
			add(new Cell(3, Column.B));
			add(new Cell(3, Column.C));
			add(new Cell(3, Column.D));
			add(new Cell(4, Column.A));
			add(new Cell(4, Column.C));
			add(new Cell(4, Column.D));
		}};
		assertEquals("Must be equal", listOfCells, Helper.parseXML("test_files/map.xml"));
	}
	
	@Test
	void testMakeAdjacencyList() {
		assertEquals("Must be of length 0", 0, Helper.makeAdjacencyList(new ArrayList<Cell>()).size());
		@SuppressWarnings("unchecked")
		List<Cell> listOfCells = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.B));
			add(new Cell(1, Column.C));
			add(new Cell(1, Column.D));
			add(new Cell(2, Column.B));
			add(new Cell(3, Column.A));
			add(new Cell(3, Column.B));
			add(new Cell(3, Column.C));
			add(new Cell(3, Column.D));
			add(new Cell(4, Column.A));
			add(new Cell(4, Column.C));
			add(new Cell(4, Column.D));
		}};
		assertEquals("Must be of length 12", 12, Helper.makeAdjacencyList(listOfCells).size());
		@SuppressWarnings("unchecked")
		List<Cell> adjacentsOf1B = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.C));
			add(new Cell(2, Column.B));
		}};
		assertEquals("Must be equal", adjacentsOf1B, Helper.makeAdjacencyList(listOfCells).get(1));
		
	}
	
	@Test
	void testBFS() {
		Helper.startPoint = new Cell(1, Column.D);
		Helper.endPoint = new Cell(2, Column.B);
		@SuppressWarnings("unchecked")
		List<Cell> listOfCells = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.B));
			add(new Cell(1, Column.C));
			add(new Cell(1, Column.D));
			add(new Cell(2, Column.B));
		}};
		List<List<Cell>> adjList = Helper.makeAdjacencyList(listOfCells);
		Helper.BFS(listOfCells, adjList);
		
		@SuppressWarnings("unchecked")
		List<Cell> compareList = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.B));
			add(new Cell(1, Column.C));
			add(new Cell(1, Column.D));
			add(new Cell(2, Column.B));
		}};
		compareList.get(0).setDistance(3);
		compareList.get(1).setDistance(2);
		compareList.get(2).setDistance(1);
		compareList.get(3).setDistance(0);
		compareList.get(4).setDistance(3);
		
		assertEquals("Must be equal", compareList, listOfCells);
	}
	
	@Test
	void testCalculatePaths() {
		@SuppressWarnings("unchecked")
		List<Cell> listOfCells = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.B));
			add(new Cell(1, Column.C));
			add(new Cell(1, Column.D));
			add(new Cell(2, Column.B));
		}};
		Helper.startPoint = listOfCells.get(listOfCells.indexOf(new Cell(1, Column.D)));
		Helper.endPoint = listOfCells.get(listOfCells.indexOf(new Cell(1, Column.A)));
		
		@SuppressWarnings("unchecked")
		List<List<Cell>> compareList = new ArrayList() {{
			add(new ArrayList<Cell>() {{
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.D))));
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.C))));
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.B))));
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.A))));
			}});
		}};
		
		List<List<Cell>> adjList = Helper.makeAdjacencyList(listOfCells);
		Helper.BFS(listOfCells, adjList);
		
		List<List<Cell>> listOfPaths = new ArrayList<>();
		Helper.calculatePaths(Helper.endPoint, Helper.startPoint, listOfCells, new ArrayList<Cell>(), adjList, listOfPaths);
		
		assertEquals("listOfPaths should be of length 1", 1, listOfPaths.size());
		assertEquals("first and only path should be of length 4", 4, listOfPaths.get(0).size());
		assertEquals("listOfPaths must be equal to compareList", compareList, listOfPaths);
	}
	
	@Test
	void testGenerateJSONFile() {
		int timeInMillis = 10;
		@SuppressWarnings("unchecked")
		List<Cell> listOfCells = new ArrayList() {{
			add(new Cell(1, Column.A));
			add(new Cell(1, Column.B));
			add(new Cell(1, Column.C));
			add(new Cell(1, Column.D));
			add(new Cell(2, Column.B));
		}};
		@SuppressWarnings("unchecked")
		List<List<Cell>> listOfPaths = new ArrayList() {{
			add(new ArrayList<Cell>() {{
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.D))));
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.C))));
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.B))));
				add(listOfCells.get(listOfCells.indexOf(new Cell(1, Column.A))));
			}});
		}};
		assertEquals("Should return false because of InvalidPathException", false, Helper.generateJSONFile(listOfPaths, timeInMillis, "c^&:"));
		assertEquals("Should return false because of access restrictions", false, Helper.generateJSONFile(listOfPaths, timeInMillis, "C:"));
		assertEquals("Should return false because the system cannot find the path specified", false, Helper.generateJSONFile(listOfPaths, timeInMillis, "folder"));
	}

}
