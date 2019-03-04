public interface Executable {
    //keep as interface
    void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    int getActionPeriod();
}
