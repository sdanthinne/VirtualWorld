import processing.core.PImage;

import java.util.List;

public class Quake extends Entity implements Animatable,Renderable,Executable{
    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    //Point position;
    //List<PImage> images;
    int imageIndex = 0;

    public Quake(Point position, List<PImage> images) {
        super.position = position;
        super.images = images;
        super.id = QUAKE_ID;
    }

    public int getActionPeriod(){
        return QUAKE_ACTION_PERIOD;
    }
    /*public String getId(){
        return QUAKE_ID;
    }
    public void setId(String s){
        return;

    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public void setImages(List<PImage> images) {
        this.images = images;
    }
*/
    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public void execute(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
       scheduler.unscheduleAllEvents((Entity)this);
       world.removeEntity((Entity)this);
    }

    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }
    public int getAnimationPeriod(){
        return QUAKE_ANIMATION_PERIOD;
    }

    /*public static Entity createQuake(Point position, List<PImage> images)
    {
       return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
          0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }*/
}
