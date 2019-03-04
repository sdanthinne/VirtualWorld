import java.util.Random;

public class Activity extends Action{
    //Entity entity;
    //private static final Random rand = new Random();
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void setWorld(WorldModel world) {
        this.world = world;
    }

    /*@Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }*/


/*public static Action createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore)
    {
       return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
    }*/

    public ImageStore getImageStore() {
        return imageStore;
    }

    public WorldModel getWorld() {
        return world;
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((Executable) entity).execute(getWorld(),
                getImageStore(), scheduler);
       /*switch (getEntity().kind)
       {
       case MINER_FULL:
           getEntity().executeMinerFullActivity(getWorld(),
                   getImageStore(), scheduler);
          break;

       case MINER_NOT_FULL:
          getEntity().executeMinerNotFullActivity(getWorld(),
                  getImageStore(), scheduler);
          break;

       case ORE:
          Ore.executeOreActivity(getEntity(),  getWorld(), getImageStore(),
             scheduler);
          break;

       case ORE_BLOB:
          getEntity().execute(getWorld(),
                  getImageStore(), scheduler);
          break;

       case QUAKE:
          getEntity().execute(getWorld(), getImageStore(),
             scheduler);
          break;

       case VEIN:
          Vein.executeVeinActivity(getEntity(), getWorld(), getImageStore(),
             scheduler);
          break;

       default:
          throw new UnsupportedOperationException(
             String.format("executeActivityAction not supported for %s",
             getEntity().kind));
       }*/
    }

    public void setImageStore(ImageStore imageStore) {
        this.imageStore = imageStore;
    }
}
