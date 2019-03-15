import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class FireBlob extends Entity implements Animatable,Renderable,Executable {
    public static final String FIRE_ID = "splashbackground";
    public static final int FIRE_ANIMATION_PERIOD = 200;
    public static final int FIRE_ACTION_PERIOD = 2000;
    public int imageIndex;

    public FireBlob(Point position, List<PImage> images){
        super.position = position;
        super.images = images;

    }
    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }
    public int getAnimationPeriod(){
        return FIRE_ANIMATION_PERIOD;
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler){
        world.setOccupancyCell(position,this);
        world.removeEntity(this);
        eventScheduler.unscheduleAllEvents(this);
    }
    public int getActionPeriod(){
        return FIRE_ACTION_PERIOD;
    }
    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
