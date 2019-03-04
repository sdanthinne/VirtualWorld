import processing.core.PImage;

import java.awt.image.renderable.RenderContext;
import java.util.List;

public class Obstacle extends Entity implements  Renderable {
    static final String OBSTACLE_KEY = "obstacle";
    static final int OBSTACLE_NUM_PROPERTIES = 4;
    static final int OBSTACLE_ID = 1;
    static final int OBSTACLE_COL = 2;
    static final int OBSTACLE_ROW = 3;
    //String id;
    //Point position;
    //List<PImage> images;
    int imageIndex = 0;


    public Obstacle(String id, Point position, List<PImage> images) {
        super.id = id;
        super.position = position;
        super.images = images;
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

    public PImage getCurrentImage(){
        return images.get(imageIndex);
    }
    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }
    /*public static Entity createObstacle(String id, Point position,
                                        List<PImage> images)
    {
       return new Entity(EntityKind.OBSTACLE, id, position, images,
          0, 0, 0, 0);
    }*/
}
