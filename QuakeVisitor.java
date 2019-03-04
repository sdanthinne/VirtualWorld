public class QuakeVisitor extends AllFalseEntityVisitor {
    @Override
    public Boolean visit(Quake b){
        return true;
    }
}
