final class Event
{
   private Action action;
   private long time;
   public Entity entity;

   public Event(Action action, long time, Entity entity)
   {
      this.setAction(action);
      this.setTime(time);
      this.entity = entity;
   }

   public Action getAction() {
      return action;
   }

   public void setAction(Action action) {
      this.action = action;
   }

   public long getTime() {
      return time;
   }

   public void setTime(long time) {
      this.time = time;
   }
}
