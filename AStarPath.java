import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPath implements PathingStrategy {
    //TODO: needs to return a list of points on the path to follow, then return it. Currently only returns the nearest point. Maybe try a single-step approach?
    @Override
    //currently, effectively returns a list of the closest points as determined by the manhattan distance to the end goal
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        //float distanceTop,distanceBottom,distanceLeft,distanceRight;
        if( withinReach.test(start,end)){
            return new ArrayList<Point>(){{add(start);}};
        }else{
            Comparator<Point> c = new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    if(o1.getY() == o2.getY() && o1.getX() == o2.getX()){
                        return 0;
                    }else{
                        float manhattanDistance1 = Math.abs(o1.getX()-end.getX()) + Math.abs(o1.getY()-end.getY());
                        float manhattanDistance2 = Math.abs(o2.getX()-end.getX()) + Math.abs(o2.getY()-end.getY());
                        if(manhattanDistance1<manhattanDistance2){
                            return -1;
                        }else{
                            return 1;
                        }
                    }

                }
            };
            List<Point> orderedNeighbors = potentialNeighbors.apply(start).filter(x->canPassThrough.test(x)).sorted(c).collect(Collectors.toList());

            return orderedNeighbors;
        }




    }
}
