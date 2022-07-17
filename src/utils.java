import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class utils extends classes
{
    public static Color highlight_color = new Color(255, 0, 94);
    public static Color highlight_highlight_color=new Color(190, 0, 54);
    public static Color disabled_highlight_color=new Color(173, 0, 64);

    static Border rounded = new LineBorder(new Color(0, 0, 0, 0), 10, true);
    static Border empty = new EmptyBorder(0, 10, 0, 10);

    public static CompoundBorder RoundedBorder = new CompoundBorder(rounded, empty);

    public static BufferedImage toBufferedImage(Image img)
    {
        BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        buffered.getGraphics().drawImage(img, 0, 0 , null);
        return buffered;
    }

    public static String GetLine(String match, String[] array)
    {
        for (String iterable : array) 
        {
            if (iterable.startsWith(match))
            {
                return iterable.substring(match.length()+3);
            }
        }
        return null;
    }

    public static void Repaint(JFrame frame)
    {
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public static void DisableButton(JButton b)
    {
        b.setEnabled(false);
        b.setBackground(disabled_highlight_color);
    }

    public static void EnableButton(JButton b)
    {
        b.setEnabled(true);
        b.setBackground(highlight_color);
    }
    
    public static Color DarkColor(float val)
    {
        int value=Math.round(255*val);
        Color color = new Color(value, value, value);
        return color;
    }
    
    public static float Number2Percentage(int numerator, int denominator)
    {
        return (float)numerator/denominator;
    }

    public static int Percentage2Number(float percent, int TotalVal)
    {
        return (int)Math.round(percent*TotalVal);
    }

    public static Font Verdana(int size)
    {
        return new Font("Verdana", Font.PLAIN, size);
    }

    public static Font Consolas(int size)
    {
        return new Font("Consolas", Font.PLAIN, size);
    }

    public static List<Object> ExtractGameData(Path path)
    {
        try
        {
            List<String> lines = new ArrayList<String>(0);
            lines = Files.readAllLines(path);
            String[] linesarray=lines.toArray(new String[0]);
            float Lvl1ResizeWeight = Float.valueOf(GetLine("Lvl1ResizeWeight", linesarray));
            String name = GetLine("name", linesarray);
            return Arrays.asList(name, Lvl1ResizeWeight, path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static ListCellRenderer<? super String> OPCellRenderer()
    {
        return new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
            {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                listCellRendererComponent.setSize(listCellRendererComponent.getWidth(), listCellRendererComponent.getHeight()-10);
                JPanel panel=new JPanel()
                {
                    @Override
                    protected void paintComponent(Graphics g)
                    {
                        g.setColor(getBackground());
                        g.fillRoundRect(0+3, 0, getWidth()-6, getHeight()-3, 15, 15);
                        g.setColor(getForeground());
                        g.drawRoundRect(0+3, 0, getWidth()-6, getHeight()-3, 15, 15);
                    }
                };
                if (isSelected)
                {
                    // rect.setBackground(DarkColor(0.2f));
                    panel.setBackground(DarkColor(0.2f));
                }
                else
                {
                    // rect.setBackground(DarkColor(0.1f));
                    panel.setBackground(DarkColor(0.1f));
                }
                panel.setForeground(utils.highlight_highlight_color);
                listCellRendererComponent.setForeground(highlight_color);
                listCellRendererComponent.setFont(Verdana(18));
                listCellRendererComponent.setOpaque(false);
                listCellRendererComponent.setBorder(null);
                listCellRendererComponent.setVerticalAlignment(JLabel.CENTER);
                listCellRendererComponent.setHorizontalAlignment(JLabel.LEFT);
                String[] text = listCellRendererComponent.getText().split("\\|");
                listCellRendererComponent.setText(text[0]);

                JLabel pathtext=new JLabel(text[1]);
                pathtext.setFont(Verdana(14));
                pathtext.setForeground(highlight_highlight_color);
                pathtext.setOpaque(false);
                pathtext.setHorizontalAlignment(JLabel.RIGHT);
                pathtext.setVerticalAlignment(JLabel.BOTTOM);
                panel.add(listCellRendererComponent);
                panel.add(pathtext);
                panel.setLayout(new GridLayout());
                panel.setBorder(new EmptyBorder(10, 10, 10, 10));
                return panel;
                // return listCellRendererComponent;
            }
        };
    }

    public static void Save(Path path, String tofind, String toreplace)
    {
        try
        {
            List<String> lines = new ArrayList<String>(0);
            lines = Files.readAllLines(path);
            String[] linesarray=lines.toArray(new String[0]);
            for (int i=0; i<linesarray.length; i++) 
            {
                if (linesarray[i].startsWith(tofind))
                {
                    linesarray[i]=linesarray[i].substring(0, tofind.length()+3)+toreplace;
                }
            }
            FileWriter fw = new FileWriter(path.toString());
            if (linesarray.length>0)
            {
                fw.write("");
            }
            for (String i : linesarray) 
            {
                fw.append(i+"\n");
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Path Saveas(Path path, JFrame frem)
    {

        LookAndFeel laf=UIManager.getLookAndFeel();

        UIManager.put("control", utils.DarkColor(0.1f));
        UIManager.put("nimbusBlueGrey", utils.DarkColor(0.1f));
        UIManager.put("nimbusBase", utils.highlight_color);
        UIManager.put("nimbusLightBackground", utils.DarkColor(0.25f));
        UIManager.put("controlText", utils.highlight_color);
        UIManager.put("infoText", utils.highlight_color);
        UIManager.put("menuText", utils.highlight_color);
        UIManager.put("textForeground", utils.highlight_color);
        UIManager.put("nimbusSelectedText", utils.highlight_highlight_color);
        UIManager.put("nimbusSelectionBackground", utils.DarkColor(0.3f));
        UIManager.put("nimbusFocus", utils.highlight_highlight_color);

        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try 
                {
                    UIManager.setLookAndFeel(info.getClassName());
                } 
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) 
                {
                    e.printStackTrace();
                }
                break;
            }
        }
        
        try
        {
            JFileChooser jfc = new JFileChooser(path.toString());
            jfc.setDialogTitle("Select the Folder for the new location of the next hope destroyer");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setAcceptAllFileFilterUsed(false);
            int opt=jfc.showOpenDialog(frem);
            try 
            {
                UIManager.setLookAndFeel(laf);
            } 
            catch (UnsupportedLookAndFeelException e) 
            {
                e.printStackTrace();
            }
            if (opt==JFileChooser.APPROVE_OPTION)
            {
                File file = new File(path.toString()).getParentFile();
                File filekabeta = new File(path.toString());
                File newfilelocnewpaththingy = new File(jfc.getCurrentDirectory().toString()+"\\"+jfc.getSelectedFile().getName()+"\\"+file.getName()+"\\"+filekabeta.getName());
                File newfileloc=new File(jfc.getCurrentDirectory().toString()+"\\"+jfc.getSelectedFile().getName()+"\\"+file.getName());
                Files.move(file.toPath(), newfileloc.toPath());
                Path p=newfilelocnewpaththingy.toPath();
                return p;
            }
            else
            {
                throw new IOException("lol");
            }     
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void FileIsUnsaved(JFrame frem)
    {
        if (frem.getTitle().contains("*"))
        {

        }
        else
        {
            frem.setTitle(frem.getTitle()+"*");
        }
    }

    public static void FileIsSaved(JFrame frem)
    {
        if (frem.getTitle().contains("*"))
        {
            String title = frem.getTitle();
            frem.setTitle(title.substring(0, title.length()-1));
        }
    }

    public static int[] FindIndexVector2(JPanel o, JPanel[] pixels, int WIDTH)
    {
        if (pixels.length==0)
        {
            return null;
        }

        for (int i = 0; i < pixels.length; i++)
        {
            if (o==pixels[i])
            {
                int y = (int) Math.floor(i/WIDTH);
                int x = i - (y*WIDTH);
                int[] returnval = {x, y};
                return returnval;
            }
        }
        return null;
    }

    static public String xytoString(int x, int y)
    {
        return "|"+String.valueOf(x)+","+String.valueOf(y)+"|";
    }

    static public String listintxytoString(int[] xy)
    {
        return xytoString(xy[0], xy[1]);
    }

    static Color CONTRASTNotactually(Color color) 
    {
        int y = (int) Math.sqrt((2126 * (color.getRed()^2) + 7152 * (color.getGreen()^2) + 722 * (color.getBlue()^2)) / 10000);
        int rindecrementval = 69;
        int gindecrementval = rindecrementval;
        int bindecrementval = rindecrementval;
        int colormesure = 128;
        if (y>=colormesure)
        {
            if (color.getRed()-rindecrementval<0)
            {
                rindecrementval = color.getRed();
            }
            if (color.getGreen()-gindecrementval<0)
            {
                gindecrementval = color.getGreen();
            }
            if (color.getBlue()-bindecrementval<0)
            {
                bindecrementval = color.getBlue();
            }
        }
        else
        {
            if (color.getRed()+rindecrementval>255)
            {
                rindecrementval = 255 - color.getRed();
            }
            if (color.getGreen()+gindecrementval>255)
            {
                gindecrementval = 255 - color.getGreen();
            }
            if (color.getBlue()+bindecrementval>255)
            {
                bindecrementval = 255 - color.getBlue();
            }
        }
        return y >= colormesure ? new Color(color.getRed()-rindecrementval, color.getGreen()-gindecrementval, color.getBlue()-bindecrementval) : new Color(color.getRed()+rindecrementval, color.getGreen()+gindecrementval, color.getBlue()+bindecrementval);
    }

    static Color rgbwithoffsetadjust(double y, int r, int g, int b)
    {
        int ro = r+y < 0 ? 0 : (int) Math.round(r+y);
        int go = g+y < 0 ? 0 : (int) Math.round(g+y);
        int bo = b+y < 0 ? 0 : (int) Math.round(b+y);
        return new Color(ro, go, bo);
    }
}