public class ObstacleVisitor extends AllFalseEntityVisitor{
    @Override
    public Boolean visit(Obstacle b){
        return true;
    }
}
