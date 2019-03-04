public interface EntityVisitor<R> {
    R visit(Ore o);
    R visit(OreBlob o);
    R visit(Quake o);
    R visit(Vein o);
    R visit(BlackSmith o);
    R visit(MinerFull o);
    R visit(MinerNotFull o);
    R visit(Obstacle o);


}
