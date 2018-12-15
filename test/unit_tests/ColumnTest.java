package unit_tests;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import cell.Column;

class ColumnTest {

	@Test
	void testGetColumnAtPosition() {
		assertEquals("Should be equals", Column.Z, Column.getColumnAtPosition(25));
		assertEquals("Should be equals", Column.A, Column.getColumnAtPosition(0));
		assertEquals("Should be null", null, Column.getColumnAtPosition(-1));
		assertEquals("Should be null", null, Column.getColumnAtPosition(27));
		assertEquals("Should be null", null, Column.getColumnAtPosition(1002));
	}

}
