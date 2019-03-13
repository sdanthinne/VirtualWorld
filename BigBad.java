import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class BigBad extends Entity implements Renderable, Animatable,Movable,Executable {
    /*List<PImage> images;*/
    static final String BIGBAD_KEY = "bigbad";
    static final int BIGBAD_ACTION_PERIOD = 10;
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
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity((Entity) this, nextPos);
            }
            return false;
        }
    }
    private Point nextPositionBigBad(WorldModel w, Point target){
        //todo: implement a pathing algorithm for the next position of the bigbad
        return null;
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
