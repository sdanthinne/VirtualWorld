import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MinerFull extends Miner implements Animatable,Movable,Executable,Renderable {
    static final String MINER_KEY = "miner";
    static final int MINER_NUM_PROPERTIES = 7;
    static final int MINER_ID = 1;
    static final int MINER_COL = 2;
    static final int MINER_ROW = 3;
    static final int MINER_LIMIT = 4;
    static final int MINER_ACTION_PERIOD = 5;
    static final int MINER_ANIMATION_PERIOD = 6;
    int resourceLimit;
    int actionPeriod;
    int animationPeriod;
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
                if (!occupant.isPresent())
                {
                    world.moveEntity((Entity) this, nextPos);
                    //scheduler.unscheduleAllEvents(occupant.get());
                }


            }
            return false;
        }
    }

    /*public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {
        // this needs to be condensed and nextpositionminer can be deleted, as the computing returns a list of points that we need to navigate through
        PathingStrategy newpath = new SingleStepPathingStrategy();
        Predicate<Point> pointPredicate = new Predicate<Point>() {
            @Override
            public boolean test(Point point) {
                return world.getOccupant(point).isEmpty();
            }
        };
        BiPredicate<Point,Point> withinReacher = new BiPredicate<Point, Point>() {
            @Override
            public boolean test(Point point, Point point2) {
                return point.adjacent(point2);
            }
        };
        *//*Function<Point, Stream<Point>> potentialNeighbors = new Function<Point, Stream<Point>>() {
            @Override
            public Stream<Point> apply(Point point) {
                return null;
            }
        };*//*
        List<Point> listPath = newpath.computePath(position,destPos,pointPredicate,withinReacher,PathingStrategy.CARDINAL_NEIGHBORS);
        Point newPos = position;
        if(listPath.size()>0){
            newPos = listPath.get(0);
        }
        *//*int horiz = Integer.signum(destPos.getX() - position.getX());
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
        }*//*

        return newPos;
    }*/
}
