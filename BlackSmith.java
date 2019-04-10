import processing.core.PImage;

import java.util.List;

public class BlackSmith extends Entity implements Renderable{
    static final String SMITH_KEY = "blacksmith";
    static final int SMITH_NUM_PROPERTIES = 4;
    static final int SMITH_ID = 1;
    static final int SMITH_COL = 2;
    static final int SMITH_ROW = 3;
    //String id;
    //Point position;
    //List<PImage> images;
    int imageIndex;

    public BlackSmith(String id, Point position, List<PImage> images) {
        super.id = id;
        super.position = position;
        super.images = images;
    }
    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
    /*@Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public void setImages(List<PImage> images) {
        this.images = images;
    }*/
    /*public static Entity createBlacksmith(String id, Point position,
                                          List<PImage> images)
    {
       return new Entity(EntityKind.BLACKSMITH, id, position, images,
          0, 0, 0, 0);
    }*/
}
