public class AllFalseEntityVisitor implements EntityVisitor<Boolean>{

     public Boolean visit(Ore o){
        return false;
    }

    public Boolean visit(OreBlob o){
        return false;
    }
    public Boolean visit(Vein o){
        return false;
    }
    public Boolean visit(MinerFull o){
        return false;
    }
    public Boolean visit(MinerNotFull o){
        return false;
    }
    public Boolean visit(BlackSmith o){
        return false;
    }
    public Boolean visit(Obstacle o){
        return false;
    }
    public Boolean visit(Quake o){
        return false;
    }
    public Boolean visit(BigBad o){return false;}

}
