public abstract class Action
{
    /*public static ActionKind kind;
   private static Entity entity;
    public static int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      Action.setEntity(this, entity);
      Activity.setWorld(this, world);
      this.setImageStore(imageStore);
      this.repeatCount = repeatCount;
   }*/
    Entity entity;

     Entity getEntity(){
          return entity;
     }

     void setEntity(Entity entity){
          this.entity = entity;
     }

     abstract void executeAction(EventScheduler scheduler);

}
