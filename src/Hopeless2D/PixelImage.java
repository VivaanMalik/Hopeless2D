package Hopeless2D;

public class PixelImage
{
    int PixelX;
    int PixelY;
    color[][] PixelData;

    public PixelImage(int x, int y)
    {
        PixelX=x;
        PixelY=y;
        PixelData=new color[y][x];
        for (int xval = 0; xval < PixelData.length; xval++) 
        {
            for (int yval = 0; yval < PixelData[xval].length; yval++) 
            {
                PixelData[yval][xval] = new color(0, 0, 0, 0);
            }
        }
    }
    
    public PixelImage(color[][] PixelColorData)
    {
        PixelX=PixelColorData[0].length;
        PixelY=PixelColorData.length;
        PixelData=PixelColorData;
    }

    public void SetPixel(int x, int y, color col)
    {
        PixelData[y][x] = col;
    }

    public color GetPixel(int x, int y)
    {
        return PixelData[y][x];
    }

    public Vector2 GetDimension()
    {
        return new Vector2(PixelX, PixelY);
    }
}