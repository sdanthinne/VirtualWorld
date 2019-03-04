import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView
{
   public PApplet screen;
   public WorldModel world;
   public int tileWidth;
   public int tileHeight;
   public Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

    private Point viewportToWorld(Viewport viewport, int col, int row)
    {
        //could be moved to viewport, unsure
       return new Point(col + viewport.getCol(), row + viewport.getRow());
    }

    private int clamp(int value, int low, int high)
    {
       return Math.min(high, Math.max(value, low));
    }

    public void shiftView(int colDelta, int rowDelta)
    {
       int newCol = clamp(this.viewport.getCol() + colDelta, 0,
          this.world.numCols - this.viewport.getNumCols());
       int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
          this.world.numRows - this.viewport.getNumRows());

       this.viewport.shift(newCol, newRow);
    }

    private void drawBackground()
    {
       for (int row = 0; row < viewport.getNumRows(); row++)
       {
          for (int col = 0; col < viewport.getNumCols(); col++)
          {
             Point worldPoint = viewportToWorld(viewport, col, row);
             Optional<PImage> image = world.getBackgroundImage(
                     worldPoint);
             if (image.isPresent())
             {
                screen.image(image.get(), col * tileWidth,
                   row * tileHeight);
             }
          }
       }
    }

    private void drawEntities()
    {
       for (Entity entity : this.world.entities)
       {

          Point pos = entity.getPosition();

          if (this.viewport.contains(pos))
          {
             Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
             this.screen.image(((Renderable)entity).getCurrentImage(),
                viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight);
          }
       }
    }

    public void drawViewport()
    {
       drawBackground();
       drawEntities();
    }
}
