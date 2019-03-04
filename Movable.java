public interface Movable {
    boolean moveTo(WorldModel world,
           Entity target, EventScheduler scheduler);
}
