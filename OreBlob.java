import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends Entity implements Animatable,Movable,Executable,Renderable{
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;
    public static final String BLOB_KEY = "blob";

    //String id;
    //Point position;
    int actionPeriod;
    int animationPeriod;
    //List<PImage> images;
    int imageIndex = 0;

    public OreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super.id = id;
        super.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
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
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
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

    public void execute(WorldModel world,
                        ImageStore imageStore, EventScheduler scheduler)
    {
       Optional<Entity> blobTarget = world.findNearest(
               position, new Vein(null,null,0,null));
       long nextPeriod = actionPeriod;

       if (blobTarget.isPresent())
       {
          Point tgtPos = blobTarget.get().getPosition();

          if (moveTo( world, blobTarget.get(), scheduler))
          {
             Quake quake = new Quake(tgtPos,
                imageStore.getImageList(Quake.QUAKE_KEY));

             world.addEntity((Entity) quake);
             nextPeriod += actionPeriod;
             scheduler.scheduleActions((Entity) quake, world, imageStore);
          }
       }

       scheduler.scheduleEvent((Entity)this,
          new Activity((Entity) this, world, imageStore),
          nextPeriod);
    }

    private Point nextPositionOreBlob(WorldModel world,
                                      Point destPos)
    {
       int horiz = Integer.signum(destPos.getX() - position.getX());
       Point newPos = new Point(position.getX() + horiz,
               position.getY());

       Optional<Entity> occupant = world.getOccupant(newPos);

       if (horiz == 0 ||
          (occupant.isPresent() && !(occupant.get() instanceof Ore)))
       {
          int vert = Integer.signum(destPos.getY() - position.getY());
          newPos = new Point(position.getX(), position.getY() + vert);
          occupant = world.getOccupant(newPos);

          if (vert == 0 ||
             (occupant.isPresent() && !(occupant.get() instanceof Ore)))
          {
             newPos = position;
          }
       }

       return newPos;
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }
    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }

    public boolean moveTo( WorldModel world,
                                         Entity target, EventScheduler scheduler)
    {
       if (position.adjacent(target.getPosition()))
       {
          world.removeEntity(target);
          scheduler.unscheduleAllEvents(target);
          return true;
       }
       else
       {
          Point nextPos = this.nextPositionOreBlob(world, target.getPosition());

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

    /*public static Entity createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
       return new Entity(EntityKind.ORE_BLOB, id, position, images,
             0, 0, actionPeriod, animationPeriod);
    }*/
}
