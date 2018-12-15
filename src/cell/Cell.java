package cell;

public class Cell {
	private int row;
	private Column col;
	private int distance;
	
	public Cell(int row, Column col) {
		this.row = row;
		this.col = col;
		this.distance = 0;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public Column getCol() {
		return col;
	}
	public void setCol(Column col) {
		this.col = col;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + ", distance=" + distance + "]\n";
	}
	@Override
	public boolean equals(Object obj) {
		if(this.getRow() == ((Cell)obj).getRow() && this.getCol() == ((Cell)obj).getCol() && this.getDistance() == ((Cell)obj).getDistance())
			return true;
		return false;
	}
}
