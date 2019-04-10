import java.util.List;
import processing.core.PImage;

final class Background implements Renderable
{
    static final String BGND_KEY = "background";
    static final int BGND_NUM_PROPERTIES = 4;
    static final int BGND_ID = 1;
    static final int BGND_COL = 2;
    static final int BGND_ROW = 3;
    public String id;
   public List<PImage> images;
   public int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }
   public PImage getCurrentImage(){
      return images.get(imageIndex);
   }
}
