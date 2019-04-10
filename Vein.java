import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends Entity implements Executable,Renderable{
    static final String VEIN_KEY = "vein";
    static final int VEIN_NUM_PROPERTIES = 5;
    static final int VEIN_ID = 1;
    static final int VEIN_COL = 2;
    static final int VEIN_ROW = 3;
    static final int VEIN_ACTION_PERIOD = 4;
    public Random rand = new Random();
    //String id;
    //Point position;
    int actionPeriod;
    //List<PImage> images;
    int imageIndex =0;

    public Vein(String id, Point position, int actionPeriod, List<PImage> images) {
        super.id = id;
        super.position = position;
        this.actionPeriod = actionPeriod;
        super.images = images;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }

    /*@Override
    public String getId() {
        return id;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public void setImages(List<PImage> images) {
        this.images = images;
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
    public void setPosition(Point position)find {
        this.position = position;
    }*/

    public void execute(WorldModel world,
                        ImageStore imageStore, EventScheduler scheduler)
    {
        //todo:when vein is spawned only one of the ore appears in the rop left corner
       Optional<Point> openPt = world.findOpenAround(position);

       if (openPt.isPresent())
       {
          Ore ore = new Ore(Ore.ORE_ID_PREFIX + id,
             openPt.get(), Ore.ORE_CORRUPT_MIN +
                rand.nextInt(Ore.ORE_CORRUPT_MAX - Ore.ORE_CORRUPT_MIN),
             imageStore.getImageList(Ore.ORE_KEY));
          world.addEntity((Entity) ore);
          scheduler.scheduleActions((Entity) ore, world, imageStore);
       }

       scheduler.scheduleEvent(this,
          new Activity(this, world, imageStore),
          actionPeriod);
    }

    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
    /*public static Entity createVein(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
       return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
          actionPeriod, 0);
    }*/
}
