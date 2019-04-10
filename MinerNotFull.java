import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner implements Animatable,Movable,Executable,Renderable{

    int resourceLimit;
    int actionPeriod;
    int animationPeriod;
    int resourceCount = 0;
    int imageIndex = 0;

    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super.id = id;
        this.resourceLimit = resourceLimit;
        super.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        super.images = images;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }

    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
       if (resourceCount >= resourceLimit)
       {
          MinerFull miner = new MinerFull(id, resourceLimit,
             position, actionPeriod, animationPeriod,
             images);

          world.removeEntity((Entity) this);
          scheduler.unscheduleAllEvents((Entity)this);

          world.addEntity((Entity) miner);
          scheduler.scheduleActions((Entity) miner, world, imageStore);

          return true;
       }

       return false;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
       Optional<Entity> notFullTarget = world.findNearest(position,
          new Ore(null, null,0,null));

       if (!notFullTarget.isPresent() ||
          !moveTo(world, notFullTarget.get(), scheduler) ||
          !transformNotFull(world, scheduler, imageStore))
       {
          scheduler.scheduleEvent(this,
             new Activity((Entity) this, world, imageStore),
             actionPeriod);
       }
    }


    public int getAnimationPeriod(){
        return animationPeriod;
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
    /*public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
       return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
          resourceLimit, 0, actionPeriod, animationPeriod);
    }*/
    /*public Point nextPositionMiner(WorldModel world,
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
    }*/

    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (!occupant.isPresent())
                {
                    world.moveEntity((Entity) this, nextPos);
                    //scheduler.unscheduleAllEvents(occupant.get());
                }


            }
            return false;
        }
    }
}
