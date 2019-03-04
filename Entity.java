import java.util.List;
import java.util.Random;

import processing.core.PImage;

public abstract class Entity
{

    //public EntityKind kind;
   public String id;
   public Point position;
   public List<PImage> images;
   //public int imageIndex;
   /*public int resourceLimit;
   public int resourceCount;
   public int actionPeriod;
   public int animationPeriod;*/
   public String getId(){
    return id;

   }

   public void setId(String id){
      this.id=id;
   }

   public Point getPosition(){
      return position;
   }

   public void setPosition(Point position){
      this.position = position;

   }

   public List<PImage> getImages(){
      return images;
   }

   public void setImages(List<PImage> images){
      this.images = images;
   }

   public abstract <R> R accept(EntityVisitor<R> visitor);


   /*
   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }*/

    /*public static int getAnimationPeriod(Entity entity)
    {
       switch (entity.kind)
       {
       case MINER_FULL:
       case MINER_NOT_FULL:
       case ORE_BLOB:
       case QUAKE:
          return entity.animationPeriod;
       default:
          throw new UnsupportedOperationException(
             String.format("getAnimationPeriod not supported for %s",
             entity.kind));
       }
    }*/

   /*public static PImage getCurrentImage(Object entity)
    {
       if (entity instanceof Background)
       {
          return ((Background)entity).images
             .get(((Background)entity).imageIndex);
       }
       else if (entity instanceof Entity)
       {
          return ((Entity)entity).images.get(((Entity)entity).imageIndex);
       }
       else
       {
          throw new UnsupportedOperationException(
             String.format("getCurrentImage not supported for %s",
             entity));
       }
    }*/
}
