package Hopeless2D;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Hopeless
{
    static List<Entity> Entities = new ArrayList<Entity>();
    public int FramesPerSecond = 30;
    int currentfps = 0;
    public GameWindow gw;
    Thread run;
    public float deltatime = 0;
    public boolean rungame=true;
    public Object FileClassObject;
    Method KeyPressed;
    Method KeyReleased;
    Method KeyTyped;
    Physics P;

    // . . .
    private static int counter;

    public Hopeless() 
    {
        counter++;
    }

    public int getNumOfInstances() 
    {
        return counter;
    }
    // . . .

    public void SetPhysics()
    {
        P = new Physics();
        P.SetOriginalCode(FileClassObject);
    }

    public void Destroy(Object o)
    {
        Entities.remove(o);
    }

    public String ProjectPath()
    {
        return gw.GetPath();
    }

    public float DeltaTime()
    {
        return deltatime;
    }

    public int getCurrentFps()
    {
        return currentfps;
    }

    public void SetFrameRate(int fps)
    {
        FramesPerSecond = fps;
    }

    public void Instantiate(Entity e)
    {
        Entities.add(e);
    }

    public void stop()
    {
        rungame=false;
    }

    public void Log(String s)
    {
        Test.Log(s);
    }
    
    public void run()
    {
        gw.hp = this;
        run = new Thread()
        {
            public void run()
            {
                Timer timer = new Timer();
                timer.schedule(new PrintFPS(), 1000, 1000);
                
                Method Frame = null;
                KeyPressed=null;
                KeyReleased=null;
                KeyTyped=null;
                try
                {
                    Frame = FileClassObject.getClass().getDeclaredMethod("Frame");
                    KeyPressed = FileClassObject.getClass().getDeclaredMethod("keyPressed", new Class[]{KeyEvent.class});
                    KeyReleased = FileClassObject.getClass().getDeclaredMethod("keyReleased", new Class[]{KeyEvent.class});
                    KeyTyped = FileClassObject.getClass().getDeclaredMethod("keyTyped", new Class[]{KeyEvent.class});
                }
                catch (NoSuchMethodException | SecurityException e)
                {
                    Test.Log(e.getMessage());
                }
                
                
                while (rungame==true)
                {
                    long prevtime = System.currentTimeMillis();
                    try 
                    {
                        Frame.invoke(FileClassObject);
                    } 
                    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) 
                    {
                        Test.Log(e1.getMessage());
                    }
                    gw.entityes = P.PhysicsOhFy(Entities);
                    gw.UpdateWindow();
                    currentfps+=1;
                    
                    long aftatime = System.currentTimeMillis();
                    long timedifference = aftatime-prevtime;
                    int mswait = (1000/FramesPerSecond) - (int)timedifference;
                    deltatime = (float) (mswait+(int)timedifference)/1000f;
                    if (deltatime<0)
                    {
                        deltatime = 0f;
                    }
                    if (mswait>0)
                    {
                        try 
                        {
                            Thread.sleep(mswait);
                        } 
                        catch (InterruptedException e) 
                        {
                            Test.Log(e.getMessage());
                        }
                    }
                }
                gw.hp=null;
                gw = null;
                timer.cancel();
                timer.purge();
                timer=null;
                run=null;
                FileClassObject = null;
                Runtime.getRuntime().halt(0);   
                // Runner.close();
            }    
        };
        run.start();
    }

    class PrintFPS extends TimerTask
    {
        @Override
        public void run() 
        {
            // System.out.println(currentfps);
            currentfps=0;
        }
    }

    public void keyTyped(KeyEvent e) 
    {
        if (KeyTyped!=null)
        {
            try 
            {
                KeyTyped.invoke(FileClassObject, e);
            } 
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) 
            {
                Test.Log(e1.getMessage());
            }
        }
    }

    public void keyPressed(KeyEvent e) 
    {
        if (KeyPressed!=null)
        {
            try 
            {
                KeyPressed.invoke(FileClassObject, e);
            } 
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) 
            {
                Test.Log(e1.getMessage());
            }
        }
    }

    public void keyReleased(KeyEvent e) 
    {
        if (KeyReleased!=null)
        {
            try 
            {
                KeyReleased.invoke(FileClassObject, e);
            } 
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) 
            {
                Test.Log(e1.getMessage());
            }
        }
    }
}