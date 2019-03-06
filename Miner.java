import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Miner extends Entity{
    //int resourceLimit;
    public abstract <R> R accept(EntityVisitor<R> visitor);
    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {

        //PathingStrategy newpath = new SingleStepPathingStrategy();
        PathingStrategy newpath = new AStarPath();
        Predicate<Point> pointPredicate = new Predicate<Point>() {
            @Override
            public boolean test(Point point) {
                return world.getOccupant(point).isEmpty();
            }
        };
        BiPredicate<Point,Point> withinReacher = new BiPredicate<Point, Point>() {
            @Override
            public boolean test(Point point, Point point2) {
                return point.adjacent(point2);
            }
        };
        /*Function<Point, Stream<Point>> potentialNeighbors = new Function<Point, Stream<Point>>() {
            @Override
            public Stream<Point> apply(Point point) {
                return null;
            }
        };*/
        List<Point> listPath = newpath.computePath(position,destPos,pointPredicate,withinReacher,PathingStrategy.ALL_NEIGHBORS);
        Point newPos = position;
        if(listPath.size()>0){
            newPos = listPath.get(0);
        }
        /*int horiz = Integer.signum(destPos.getX() - position.getX());
        Point newPos = new Point(position.getX() + horiz,
                position.getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - position.getY());
            newPos = new Point(position.getX(),
                    position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = position;
            }
        }*/

        return newPos;
    }
}
