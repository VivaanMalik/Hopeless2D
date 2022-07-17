package Hopeless2D;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Runner 
{
    static Object tmfftstit;
    static Field hpField;
    static Hopeless hp;

    public static void main(String[] args)
    {
        Path FILEPATH = Paths.get(args[0]);
        runner(FILEPATH);
    }

    public Runner()
    {

    }

    public Runner(Path fILEPath)
    {
        runner(fILEPath);
    }

    public static void runner(Path FILEPATH)
    {
        GameWindow gw= new GameWindow();
        gw.SetPath(FILEPATH.getParent().toString());
        gw.entityes = new ArrayList<Entity>();
        gw.ShowWindow();
        hp = new Hopeless();
        hp.gw = gw;
        try
        {
            
            // Class<?> mainfile = new tempmainfilefortheshitthatistesting().getClass();
            // tempmainfilefortheshitthatistesting tmfftstit = new tempmainfilefortheshitthatistesting();
            // tmfftstit.hp = hp;
            // tmfftstit.setup();
            String msg="";
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(new String[]{"javac", /*"-cp", ".\\Hopeless2D\\Hopeless2D", */FILEPATH.getParent().toString()+"\\*.java"});

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) 
            {
                msg+=line;
            }

            
            try (final BufferedReader b = new BufferedReader(new InputStreamReader(process.getErrorStream()))) 
            {
                if ((line = b.readLine()) != null)
                {
                    msg+="\n"+line;
                }
            }

            process.waitFor();
            if (process.exitValue()!=0)
            {
                Error(msg);
            }
            else
            {
                URLClassLoader loader = new URLClassLoader(new URL[] 
                {
                    FILEPATH.getParent().toUri().toURL()
                });
                Class<?> mainfile = loader.loadClass("Main");
                loader.close();
                
                tmfftstit = mainfile.getDeclaredConstructor().newInstance();
                hp.FileClassObject = tmfftstit;
                hp.SetPhysics();
                hpField = tmfftstit.getClass().getDeclaredField("hp");
                hpField.set(tmfftstit, hp);
                Method Setup = tmfftstit.getClass().getDeclaredMethod("setup");
                Setup.invoke(tmfftstit);
            }
            // Method numinstance = tmfftstit.getClass().getDeclaredMethod("getNumOfInstances");
            // System.out.println("GameWindow Instances: "+ String.valueOf(gw.getNumOfInstances())+"\nHopeless Instances: "+String.valueOf(hp.getNumOfInstances())+"\nMain Instances: "+String.valueOf(numinstance.invoke(tmfftstit)));
        }
        catch (Exception e3)
        {
            Test.Log(e3.getMessage());
        }
    }

    public static void close()
    {
        try 
        {
            hpField.set(tmfftstit, null);
            hp=null;
        } 
        catch (IllegalArgumentException | IllegalAccessException e) 
        {
            Test.Log(e.getMessage());
        }
        Test.Log("Closing");
        tmfftstit=null;  
        // System.exit(0);  
        Runtime.getRuntime().halt(0);    
    }

    public static void Error(String message)
    {
        UIManager.put("OptionPane.background", new Color(.1f, .1f, .1f));
        UIManager.put("Panel.background", new Color(.1f, .1f, .1f));
        UIManager.put("OptionPane.messageForeground", new Color(255, 0, 94));
        JOptionPane err= new JOptionPane(message, JOptionPane.OK_OPTION);
        err.setMessageType(JOptionPane.ERROR_MESSAGE);
        JPanel buttonPanel = (JPanel)err.getComponent(1);
        JButton buttonOk = (JButton)buttonPanel.getComponent(0);
        buttonOk.setBackground(new Color(255, 0, 94));
        buttonOk.setForeground(new Color(.1f, .1f, .1f));
        buttonOk.setBorderPainted(false);
        buttonOk.setFocusPainted(false);
        buttonOk.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.exit(0);
            }
            
        });
        JDialog d=err.createDialog(null, "Are you dumb??");
        d.setVisible(true);
    }
}
