import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BigBad extends Entity implements Renderable, Animatable,Movable,Executable {
    /*List<PImage> images;*/
    //action_period is not loaded because this entity is never loaded into the game
    static final String BIGBAD_KEY = "bigbad";
    static final int BIGBAD_ACTION_PERIOD = 100;
    static final int BIGBAD_ANIMATION_PERIOD = 10;

    int resourceLimit;
    int actionPeriod;
    int animationPeriod;
    int imageIndex = 0;
    public BigBad(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super.id = id;
        this.resourceLimit = resourceLimit;
        super.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        super.images = images;
    }

    @Override
    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public void execute(WorldModel world,
                        ImageStore imageStore, EventScheduler scheduler) {
        //todo:implement a activity that this will execute on.
        Optional<Entity> killTarget = world.findNearest(position,
                new MinerFull(null,0,null,0,0,null));
        if(killTarget.isPresent()&& moveTo( world, killTarget.get(), scheduler)){
            //When executed, removes the target, and replaces the location of the target with a new Vein that will spawn ore.
            Point oldPos = killTarget.get().getPosition();
            world.removeEntity(killTarget.get());
            scheduler.unscheduleAllEvents(killTarget.get());
            scheduler.unscheduleAllEvents(this);
            world.removeEntity(this);
            //random in middle selects a random time for spawning ore for the vein.
            Vein v = new Vein(Vein.VEIN_KEY,oldPos,rand.nextInt(1000)+10000,imageStore.getImageList(Vein.VEIN_KEY));
            //Ore v = new Ore(Ore.ORE_KEY,oldPos,10000,imageStore.getImageList(Ore.ORE_KEY));

            for(int i = -1; i<=1;i++){
                for(int j=-1;j<=1;j++){
                    Point side = new Point(oldPos.getX()+i,oldPos.getY()+j);
                    if(world.withinBounds(side)&&!(i==0&&j==0)){
                        FireBlob fire = new FireBlob(side,imageStore.getImageList(FireBlob.FIRE_ID));
                        Optional<Entity> isthere = world.getOccupant(side);
                        if(isthere.isPresent()&&!isthere.get().accept(new VeinVisitor())){
                            world.removeEntity(isthere.get());
                            scheduler.unscheduleAllEvents(isthere.get());
                        }

                        world.addEntity(fire);
                        world.setOccupancyCell(side,fire);
                        scheduler.scheduleActions(fire,world,imageStore);

                    }
                }
            }
            world.setOccupancyCell(oldPos,v);
            world.addEntity(v);
            scheduler.scheduleActions(v,world,imageStore);
            //removing current bigbad

            //scheduler.unscheduleAllEvents(this);




        }else
        {
            //continues original bigbad behavior if it is not adjacent to real target
            scheduler.scheduleEvent((Entity) this,
                    new Activity((Entity) this, world, imageStore),
                    actionPeriod);
        }

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
            //Point nextPos = this.nextPositionMiner(world, target.getPosition());
            Point nextPos  = this.nextPositionBigBad(world,target.getPosition());

            if (!position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (!occupant.isPresent())
                {
                    //scheduler.unscheduleAllEvents(occupant.get());
                    world.moveEntity((Entity) this, nextPos);
                }


            }
            return false;
        }
    }
    private Point nextPositionBigBad(WorldModel w, Point target){
        //todo: implement a pathing algorithm for the next position of the bigbad
        PathingStrategy newpath = new AStarPath();
        Predicate<Point> pointPredicate = (point -> w.getOccupant(point).isEmpty());
        BiPredicate<Point,Point> withinReacher = new BiPredicate<Point, Point>() {
            @Override
            public boolean test(Point point, Point point2) {
                return point.adjacent(point2);
            }
        };

        List<Point> listPath = newpath.computePath(position,target,pointPredicate,withinReacher,PathingStrategy.DIAGONAL_NEIGHBORS);
        Point newPos = position;
        if(listPath.size()>0){
            newPos = listPath.get(0);
        }
        return newPos;
    }

    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public void setResourceLimit(int resourceLimit) {
        this.resourceLimit = resourceLimit;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }

    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void setAnimationPeriod(int animationPeriod) {
        this.animationPeriod = animationPeriod;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}
