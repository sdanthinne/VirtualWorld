import processing.core.PImage;

import java.util.*;

final class WorldModel
{
    private static final int PROPERTY_KEY = 0;
    public int numRows;
   public int numCols;
   public Background[][] background;
   public Entity[][] occupancy;
   public Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

    private Optional<Entity> nearestEntity(List<Entity> entities,
                                                 Point pos)
    {
       if (entities.isEmpty())
       {
          return Optional.empty();
       }
       else
       {
          Entity nearest = entities.get(0);
          int nearestDistance = distanceSquared(nearest.getPosition(), pos);

          for (Entity other : entities)
          {
             int otherDistance = distanceSquared(other.getPosition(), pos);

             if (otherDistance < nearestDistance)
             {
                nearest = other;
                nearestDistance = otherDistance;
             }
          }

          return Optional.of(nearest);
       }
    }

    private int distanceSquared(Point p1, Point p2)
    {
       int deltaX = p1.getX() - p2.getX();
       int deltaY = p1.getY() - p2.getY();

       return deltaX * deltaX + deltaY * deltaY;
    }

    public Optional<Point> findOpenAround(Point pos)
    {
       for (int dy = -Ore.ORE_REACH; dy <= Ore.ORE_REACH; dy++)
       {
          for (int dx = -Ore.ORE_REACH; dx <= Ore.ORE_REACH; dx++)
          {
             Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
             if (withinBounds(newPt) &&
                !isOccupied(newPt))
             {
                return Optional.of(newPt);
             }
          }
       }

       return Optional.empty();
    }

    private void tryAddEntity(Entity entity)
    {
       if (isOccupied(entity.getPosition()))
       {
          // arguably the wrong type of exception, but we are not
          // defining our own exceptions yet
          throw new IllegalArgumentException("position occupied");
       }

       addEntity(entity);
    }

    /*
               Assumes that there is no entity currently occupying the
               intended destination cell.
            */
    public void addEntity(Entity entity)
    {
       if (withinBounds(entity.getPosition()))
       {
          setOccupancyCell(entity.getPosition(), entity);
          this.entities.add(entity);
       }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
       if (withinBounds(pos))
       {
          return Optional.of(getBackgroundCell(pos).getCurrentImage());
       }
       else
       {
          return Optional.empty();
       }
    }

    private void setBackground(Point pos,
                              Background background)
    {
       if (withinBounds(pos))
       {
          setBackgroundCell(pos, background);
       }
    }

    public Optional<Entity> getOccupant(Point pos)
    {
       if (isOccupied(pos))
       {
          return Optional.of(getOccupancyCell(pos));
       }
       else
       {
          return Optional.empty();
       }
    }

    private Entity getOccupancyCell(Point pos)
    {
       return this.occupancy[pos.getY()][pos.getX()];
    }

    private void setOccupancyCell(Point pos,
                                 Entity entity)
    {
       this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    private Background getBackgroundCell(Point pos)
    {
       return this.background[pos.getY()][pos.getX()];
    }

    private void setBackgroundCell(Point pos,
                                  Background background)
    {
       this.background[pos.getY()][pos.getX()] = background;
    }

    private boolean withinBounds(Point pos)
    {
       return pos.getY() >= 0 && pos.getY() < this.numRows &&
          pos.getX() >= 0 && pos.getX() < this.numCols;
    }

    public boolean isOccupied(Point pos)
    {
       return withinBounds(pos) &&
          getOccupancyCell(pos) != null;
    }

    public Optional<Entity> findNearest(Point pos,
                                        Object kind)
    {
       List<Entity> ofType = new LinkedList<>();
       for (Entity entity : this.entities)
       {
           //May be problematic, look into comparing types
          if ((entity.getClass().getName()).equals((kind.getClass().getName())))
          {
             ofType.add(entity);
          }
       }

       return nearestEntity(ofType, pos);
    }

    public void moveEntity(Entity entity, Point pos)
    {
       Point oldPos = entity.getPosition();
       if (withinBounds(pos) && !pos.equals(oldPos))
       {
          setOccupancyCell(oldPos, null);
          removeEntityAt(pos);
          setOccupancyCell(pos, entity);
          entity.setPosition(pos);
       }
    }

    public void removeEntity(Entity entity)
    {
       removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos)
    {
       if (withinBounds(pos)
          && getOccupancyCell(pos) != null)
       {
          Entity entity = getOccupancyCell(pos);

          /* this moves the entity just outside of the grid for
             debugging purposes */
          entity.setPosition( new Point(-1, -1));
          this.entities.remove(entity);
          setOccupancyCell(pos, null);
       }
    }

    public boolean processLine(ImageStore imageStore, String line)
    {
       String[] properties = line.split("\\s");
       if (properties.length > 0)
       {
          switch (properties[PROPERTY_KEY])
          {
          case Background.BGND_KEY:
             return parseBackground(properties, imageStore);
          case MinerFull.MINER_KEY:
             return parseMiner(properties, imageStore);
          case Obstacle.OBSTACLE_KEY:
             return parseObstacle(properties, imageStore);
          case Ore.ORE_KEY:
             return parseOre(properties, imageStore);
          case BlackSmith.SMITH_KEY:
             return parseSmith(properties, imageStore, this);
          case Vein.VEIN_KEY:
             return parseVein(properties, imageStore);
          }
       }

       return false;
    }

    private boolean parseBackground(String[] properties,
                                   ImageStore imageStore)
    {
       if (properties.length == Background.BGND_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
             Integer.parseInt(properties[Background.BGND_ROW]));
          String id = properties[Background.BGND_ID];
          setBackground(pt,
             new Background(id, imageStore.getImageList(id)));
       }

       return properties.length == Background.BGND_NUM_PROPERTIES;
    }


    private boolean parseMiner(String[] properties,
                              ImageStore imageStore)
    {
       if (properties.length == MinerFull.MINER_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[MinerFull.MINER_COL]),
             Integer.parseInt(properties[MinerFull.MINER_ROW]));

          MinerNotFull entity = new MinerNotFull(properties[MinerFull.MINER_ID],
             Integer.parseInt(properties[MinerFull.MINER_LIMIT]),
             pt,
             Integer.parseInt(properties[MinerFull.MINER_ACTION_PERIOD]),
             Integer.parseInt(properties[MinerFull.MINER_ANIMATION_PERIOD]),
             imageStore.getImageList(MinerFull.MINER_KEY));
          tryAddEntity(entity);
       }

       return properties.length == MinerFull.MINER_NUM_PROPERTIES;
    }

    private boolean parseObstacle(String[] properties,
                                 ImageStore imageStore)
    {
       if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES)
       {
          Point pt = new Point(
             Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
             Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));
          Obstacle entity = new Obstacle(properties[Obstacle.OBSTACLE_ID],
             pt, imageStore.getImageList(Obstacle.OBSTACLE_KEY));
          tryAddEntity(entity);
       }

       return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
    }

    private  boolean parseOre(String[] properties,
                             ImageStore imageStore)
    {
       if (properties.length == Ore.ORE_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Ore.ORE_COL]),
             Integer.parseInt(properties[Ore.ORE_ROW]));
          Ore entity = new Ore(properties[Ore.ORE_ID],
             pt, Integer.parseInt(properties[Ore.ORE_ACTION_PERIOD]),
             imageStore.getImageList(Ore.ORE_KEY));
          tryAddEntity((Entity) entity);
       }

       return properties.length == Ore.ORE_NUM_PROPERTIES;
    }

    private boolean parseSmith(String[] properties,
                              ImageStore imageStore, WorldModel worldModel)
    {
       if (properties.length == BlackSmith.SMITH_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[BlackSmith.SMITH_COL]),
             Integer.parseInt(properties[BlackSmith.SMITH_ROW]));
          BlackSmith entity = new BlackSmith(properties[BlackSmith.SMITH_ID],
             pt, imageStore.getImageList(BlackSmith.SMITH_KEY));
          tryAddEntity(entity);
       }

       return properties.length == BlackSmith.SMITH_NUM_PROPERTIES;
    }

    private boolean parseVein(String[] properties,
                             ImageStore imageStore)
    {
       if (properties.length == Vein.VEIN_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Vein.VEIN_COL]),
             Integer.parseInt(properties[Vein.VEIN_ROW]));
          Vein entity = new Vein(properties[Vein.VEIN_ID],
             pt,
             Integer.parseInt(properties[Vein.VEIN_ACTION_PERIOD]),
             imageStore.getImageList(Vein.VEIN_KEY));
          tryAddEntity(entity);
       }

       return properties.length == Vein.VEIN_NUM_PROPERTIES;
    }
}
