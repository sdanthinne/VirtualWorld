import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends Entity implements Executable,Renderable{
    public static final String ORE_KEY = "ore";
    static final int ORE_NUM_PROPERTIES = 5;
    static final int ORE_ID = 1;
    static final int ORE_COL = 2;
    static final int ORE_ROW = 3;
    static final int ORE_ACTION_PERIOD = 4;
    static final int ORE_REACH = 1;//in world
    public static final String ORE_ID_PREFIX = "ore -- ";
    public static final int ORE_CORRUPT_MIN = 20000;
    public static final int ORE_CORRUPT_MAX = 30000;
    public Random rand = new Random();

    //String id;
    //Point position;
    int actionPeriod;
    //List<PImage> images;
    int imageIndex = 0;


    public Ore(String id, Point position, int actionPeriod, List<PImage> images) {
        super.id = id;
        super.position = position;
        this.actionPeriod = actionPeriod;
        super.images = images;
    }





    /*@Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
    }*/

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }

    public void execute(WorldModel world,
                        ImageStore imageStore, EventScheduler scheduler)
    {
       Point pos = position;  // store current position before removing

       world.removeEntity((Entity)this);
       scheduler.unscheduleAllEvents((Entity)this);

       OreBlob blob = new OreBlob(id + OreBlob.BLOB_ID_SUFFIX,
          pos, actionPeriod / OreBlob.BLOB_PERIOD_SCALE,
          OreBlob.BLOB_ANIMATION_MIN +
             rand.nextInt(OreBlob.BLOB_ANIMATION_MAX - OreBlob.BLOB_ANIMATION_MIN),
          imageStore.getImageList(OreBlob.BLOB_KEY));

       world.addEntity((Entity)blob);
       scheduler.scheduleActions((Entity) blob, world, imageStore);
    }

    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
    /*public static Entity createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
       return new Entity(EntityKind.ORE, id, position, images, 0, 0,
          actionPeriod, 0);
    }*/
}
