final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.setNumRows(numRows);
      this.setNumCols(numCols);
   }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public void shift(int col, int row)
    {
       setCol(col);
       setRow(row);
    }

    public Point worldToViewport(int col, int row)
    {
       return new Point(col - getCol(), row - getRow());
    }

    public boolean contains(Point p)
    {
       return p.getY() >= getRow() && p.getY() < getRow() + getNumRows() &&
          p.getX() >= getCol() && p.getX() < getCol() + getNumCols();
    }
}
