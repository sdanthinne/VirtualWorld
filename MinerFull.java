import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Entity implements Animatable,Movable,Executable,Renderable {
    static final String MINER_KEY = "miner";
    static final int MINER_NUM_PROPERTIES = 7;
    static final int MINER_ID = 1;
    static final int MINER_COL = 2;
    static final int MINER_ROW = 3;
    static final int MINER_LIMIT = 4;
    static final int MINER_ACTION_PERIOD = 5;
    static final int MINER_ANIMATION_PERIOD = 6;
    //String id;
    int resourceLimit;
    //Point position;
    int actionPeriod;
    int animationPeriod;
    //List<PImage> images;
    int imageIndex = 0;

    public MinerFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super.id = id;
        this.resourceLimit = resourceLimit;
        super.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        super.images = images;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    /*@Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }*/

    /*@Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }*/

    /*@Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public void setImages(List<PImage> images) {
        this.images = images;
    }
*/
    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {
       int horiz = Integer.signum(destPos.getX() - position.getX());
       Point newPos = new Point(position.getX() + horiz,
               position.getY());

       if (horiz == 0 || world.isOccupied(newPos))
       {
          int vert = Integer.signum(destPos.getY() - position.getY());
          newPos = new Point(position.getX(),
             position.getY() + vert);

          if (vert == 0 || world.isOccupied(newPos))
          {
             newPos = position;
          }
       }

       return newPos;
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
       MinerNotFull miner = new MinerNotFull(id, resourceLimit,
          position, actionPeriod, animationPeriod,
          images);

       world.removeEntity((Entity) this);
       scheduler.unscheduleAllEvents((Entity) this);

       world.addEntity((Entity) miner);
       scheduler.scheduleActions((Entity) miner, world, imageStore);
    }
    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }

    public void execute(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
    {
       Optional<Entity> fullTarget = world.findNearest(position,
          new BlackSmith(null,null,null));

       if (fullTarget.isPresent() &&
          moveTo( world, fullTarget.get(), scheduler))
       {
          this.transformFull(world, scheduler, imageStore);
       }
       else
       {
          scheduler.scheduleEvent((Entity) this,
             new Activity((Entity) this, world, imageStore),
             actionPeriod);
       }
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }

    public boolean moveTo(WorldModel world,
                                      Entity target, EventScheduler scheduler)
    {
       if (position.adjacent(target.getPosition()))
       {
          return true;
       }
       else
       {
          Point nextPos = this.nextPositionMiner(world, target.getPosition());

          if (!position.equals(nextPos))
          {
             Optional<Entity> occupant = world.getOccupant(nextPos);
             if (occupant.isPresent())
             {
                scheduler.unscheduleAllEvents(occupant.get());
             }

             world.moveEntity((Entity) this, nextPos);
          }
          return false;
       }
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

    /*public static Entity createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
       return new Entity(EntityKind.MINER_FULL, id, position, images,
          resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }*/
}
