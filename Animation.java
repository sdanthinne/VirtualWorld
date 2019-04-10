public class Animation extends Action{
    //Entity entity;
    int repeatCount=0;

    public Animation(Entity entity, int repeatCount) {
        super.entity = entity;
        this.repeatCount = repeatCount;
    }


    /*@Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }*/

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public static void nextImage(Entity entity)
    {
        ((Animatable)entity).setImageIndex((((Animatable) entity).getImageIndex() + 1) % entity.getImages().size());
    }

    /*public static Action createAnimationAction(Entity entity, int repeatCount)
    {
       return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
    }*/

    public void executeAction(EventScheduler scheduler)
    {

       nextImage(entity);

       if (repeatCount != 1)
       {
          scheduler.scheduleEvent(entity,
                  (Action) new Animation(
                     entity, Math.max(repeatCount - 1, 0)),
                  ((Animatable) entity).getAnimationPeriod());
       }
    }
}
