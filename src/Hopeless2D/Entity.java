package Hopeless2D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Entity
{
    PixelImage[] Sprites;
    Vector2 Size;
    Vector2 position;
    Vector2 OffsetPosition = new Vector2(0, 0);
    int rotation=0;
    boolean CollisionEnabled;
    boolean colliding;
    public String name;
    String CollisionMethod;

    public Entity(PixelImage Sprite, Vector2 size, Vector2 pos, int rot, boolean enablecollision)
    {
        Sprites=new PixelImage[1];
        Sprites[0]=Sprite;
        Size=size;
        rotation = rot;
        position=pos;
        CollisionEnabled=enablecollision;
        name = "Unnamed";
    }

    public Entity(PixelImage[] Sprite, Vector2 size, Vector2 pos, int rot, boolean enablecollision)
    {
        Sprites=Sprite;
        Size=size;
        position=pos;
        rotation = rot;
        CollisionEnabled=enablecollision;
        name = "Unnamed";
    }

    public Entity(BufferedImage[] images, Vector2 size, Vector2 pos, int rot, boolean enablecollision)
    {
        position=pos;
        rotation=rot;
        Size=size;
        Sprites = new PixelImage[images.length];
        for (int i =0; i < images.length; i++)
        {
            PixelImage p = new PixelImage(images[i].getWidth(), images[i].getHeight());
            for (int x = 0; x < images[i].getWidth(); x++)
            {
                for (int y = 0; y < images[i].getHeight(); y++)
                {
                    int rgb=images[i].getRGB(x, y);
                    p.SetPixel(x, y, new color(rgb, false));
                }
            }
            Sprites[i] = p;
        }
        CollisionEnabled=enablecollision;
        name = "Unnamed";
    }

    public void OnCollide(String m)
    {
        CollisionMethod = m;
    }

    public void SetName(String n)
    {
        name = n;
    }

    public boolean iscollidable()
    {
        return CollisionEnabled;
    }

    public void isColliding(boolean c)
    {
        colliding = c;
    }

    public int getRotation()
    {
        return rotation;
    }
    
    public Vector2 getposition()
    {
        return position;
    }

    public Vector2 getoffsetposition()
    {
        return OffsetPosition;
    }

    public PixelImage[] getSprite()
    {
        return Sprites;
    }

    public Vector2 getSize()
    {
        return Size;
    }

    public void setposition(Vector2 pos)
    {
        position = pos;
    }

    public void setrotation(int rot)
    {
        rotation=rot;
        if (rotation>=360)
        {
            rotation-=360;
        }
        else if (rotation<0)
        {
            rotation+=360;
        }
    }
    
    public void setSprites(PixelImage[] pos)
    {
        Sprites = pos;
    }

    public void setSize(Vector2 size)
    {
        position = size;
    }

    public Image GetImage(int index)
    {
        color[][] img = Sprites[index].PixelData;
        BufferedImage BFIMAGE=new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_INT_ARGB);
        for (int yColors = 0; yColors < img.length; yColors++)
        {
            for (int xColor = 0; xColor < img[yColors].length; xColor++)
            {
                color color = img[yColors][xColor];
                BFIMAGE.setRGB(xColor, yColors, 16777216 * color.a + 65536 * color.r + 256 * color.g + color.b);
            }
        }
        if (rotation!=0)
        {
            double rads = Math.toRadians(rotation);
            double sin = Math.abs(Math.sin(rads));
            double cos = Math.abs(Math.cos(rads));
            int w = BFIMAGE.getWidth(null);  
            int h = BFIMAGE.getHeight(null);
            int newWidth = (int) Math.floor(w * cos + h * sin);
            int newHeight = (int) Math.floor(h * cos + w * sin);
            BufferedImage rotated = new BufferedImage(newWidth, newHeight, BFIMAGE.getType());  
            Graphics2D graphic = rotated.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - w) / 2, (newHeight - h) / 2);
            at.rotate(rads, w/2, h/2);
            graphic.setTransform(at);
            try
            {
                graphic.drawImage(BFIMAGE, 0, 0, null);
            }
            catch (Exception e)
            {
                Test.Log(e.getMessage());
            }
            graphic.dispose();
            OffsetPosition = new Vector2(((int)(Size.x() * cos + Size.y() * sin)-Size.x())/2, ((int)(Size.y() * cos + Size.x() * sin)-Size.y())/2);
            return rotated;
        }
        return BFIMAGE;
    }
}