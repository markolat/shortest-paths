package unit_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import cell.Cell;
import cell.Column;

class CellTest {

	@Test
	void testCellConstructor() {
		Cell cell = new Cell(1, Column.A);
		assertNotNull("Should not be null", cell);
		assertEquals("Should have row attribute value of 1", 1, cell.getRow());
		assertEquals("Should have col attribute value of Column.A", Column.A, cell.getCol());
		assertEquals("Should have distance attribute value of 0", 0, cell.getDistance());
	}
	
	@Test
	void testCellEquals() {
		Cell cell = new Cell(1, Column.A);
		assertEquals("Should be equals", true, cell.equals(new Cell(1, Column.A)));
		assertEquals("Should not be equals", false, cell.equals(new Cell(2, Column.A)));
	}

}
