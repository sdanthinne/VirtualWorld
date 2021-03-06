import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Miner extends Entity{
    //int resourceLimit;
    public abstract <R> R accept(EntityVisitor<R> visitor);
    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {

        PathingStrategy newpath = new AStarPath();
        Predicate<Point> pointPredicate = (point -> world.getOccupant(point).isEmpty());
        BiPredicate<Point,Point> withinReacher = ((point, point2) -> point.adjacent(point2));

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
