package Hopeless2D;

import java.lang.reflect.Method;
import java.util.List;

public class Physics 
{
    static Object OriginalCode;

    public void SetOriginalCode(Object o)
    {
        OriginalCode = o;
    }
    
    public List<Entity> PhysicsOhFy(List<Entity> in)
    {
        List<Entity> out = in;
        out = Collision(out);
        return out;
    }

    static List<Entity> Collision(List<Entity> in)
    {
        List<Entity> out = in;
        for (int i = 0; i < out.size()-1; i++)
        {
            for (int i2 = i+1; i2<out.size(); i2++)
            {
                if (out.get(i).iscollidable()==true && out.get(i2).iscollidable() == true)
                {
                    if (checkforintersectionXaxis(out.get(i), out.get(i2)))
                    {
                        if (checkforintersectionYaxis(out.get(i), out.get(i2)))
                        {
                            out.get(i).isColliding(true);
                            out.get(i2).isColliding(true);
                            try 
                            {
                                // Class<?>[] string = {String.class};\
                                Method collide1 = OriginalCode.getClass().getDeclaredMethod(out.get(i2).CollisionMethod, String.class);
                                collide1.invoke(OriginalCode, out.get(i).name);
                                if (out.size()>i2)
                                {
                                    Method collide2 = OriginalCode.getClass().getDeclaredMethod(out.get(i).CollisionMethod, String.class);
                                    collide2.invoke(OriginalCode, out.get(i2).name);
                                }
                            } 
                            catch (Exception e) 
                            {
                                Test.Log(e.getMessage());
                                Runner.Error(e.getMessage());
                            }
                        }
                        else
                        {
                            out.get(i).isColliding(false);
                            out.get(i2).isColliding(false);
                        }
                    }
                }
            }
        }
        return out;
    }

    static boolean checkforintersectionXaxis(Entity e1, Entity e2)
    {
        if ((e1.getposition().x()+e1.getSize().x())<e2.getposition().x() || (e2.getposition().x()+e2.getSize().x())<e1.getposition().x())
        {
            return false;
        }
        return true;
    }

    static boolean checkforintersectionYaxis(Entity e1, Entity e2)
    {
        if ((e1.getposition().y()+e1.getSize().y())<e2.getposition().y() || (e2.getposition().y()+e2.getSize().y())<e1.getposition().y())
        {
            return false;
        }
        return true;
    }
}
