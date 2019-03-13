import java.util.*;

final class EventScheduler
{
    private PriorityQueue<Event> eventQueue;
   private Map<Entity, List<Event>> pendingEvents;
   private double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

    public void unscheduleAllEvents(Entity entity)
    {
       List<Event> pending = this.pendingEvents.remove(entity);

       if (pending != null)
       {
          for (Event event : pending)
          {
             this.eventQueue.remove(event);
          }
       }
    }

    public void updateOnTime(long time)
    {
       while (!this.eventQueue.isEmpty() &&
          this.eventQueue.peek().getTime() < time)
       {
          Event next = this.eventQueue.poll();

          removePendingEvent(next);

          next.getAction().executeAction(this);
       }
    }

    private void removePendingEvent(Event event)
    {
       List<Event> pending = this.pendingEvents.get(event.entity);

       if (pending != null)
       {
          pending.remove(event);
       }
    }

    public void scheduleEvent(Entity entity, Action action, long afterPeriod)
    {
       long time = System.currentTimeMillis() +
          (long)(afterPeriod * this.timeScale);
       Event event = new Event(action, time, entity);

       this.eventQueue.add(event);

       // update list of pending events for the given entity
       List<Event> pending = this.pendingEvents.getOrDefault(entity,
          new LinkedList<>());
       pending.add(event);
       this.pendingEvents.put(entity, pending);
    }

    public PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public void setEventQueue(PriorityQueue<Event> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public Map<Entity, List<Event>> getPendingEvents() {
        return pendingEvents;
    }

    public void setPendingEvents(Map<Entity, List<Event>> pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public void scheduleActions(Entity entity,
                                WorldModel world, ImageStore imageStore)
    {
       switch (entity.getClass().getName())
       {
       case "MinerFull":
          scheduleEvent(entity,
             new Activity(entity, world, imageStore),
                  ((Executable) entity).getActionPeriod());
          scheduleEvent(entity, (Action) new Animation(entity, 0),
                  ((Animatable)entity).getAnimationPeriod());
          break;





       case "MinerNotFull":
          scheduleEvent(entity,
             new Activity(entity, world, imageStore),
                  ((Executable)entity).getActionPeriod());
          scheduleEvent(entity,
                  (Action) new Animation(entity, 0), ((Animatable)entity).getAnimationPeriod());
          break;

       case "Ore":
          scheduleEvent(entity,
             new Activity(entity, world, imageStore),
                  ((Executable)entity).getActionPeriod());
          break;

       case "OreBlob":
          scheduleEvent(entity,
             new Activity(entity, world, imageStore),
                  ((Executable)entity).getActionPeriod());
          scheduleEvent(entity,
             new Animation(entity, 0), ((Animatable)entity).getAnimationPeriod());
          break;

       case "Quake":
          scheduleEvent(entity,
             new Activity(entity, world, imageStore),
                  ((Executable)entity).getActionPeriod());
          scheduleEvent(entity,
             new Animation(entity, Quake.QUAKE_ANIMATION_REPEAT_COUNT),
                  ((Animatable)entity).getAnimationPeriod());
          break;

       case "Vein":
          scheduleEvent(entity,
             new Activity(entity, world, imageStore),
                  ((Executable)entity).getActionPeriod());
          break;

       case "BigBad":
           scheduleEvent(entity,
                   new Activity(entity, world, imageStore),
                   ((Executable) entity).getActionPeriod());
           scheduleEvent(entity, (Action) new Animation(entity, 0),
                   ((Animatable)entity).getAnimationPeriod());
           break;

       default:
       }
    }
}
