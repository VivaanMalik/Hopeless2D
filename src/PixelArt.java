import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.awt.event.InputEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.GridBagConstraints;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.event.KeyEvent;

public class PixelArt extends classes
{
    // tool constants
    final static String ERASER = "ERASER";
    final static String PEN = "PEN";
    final static String COLORPICKER = "COLORPICKER";
    
    static int WIDTH=0;
    static int HEIGHT=0;
    static Color CURRENTCOLOR = utils.highlight_color;
    static String path = "";
    static int semnems = 0;
    static String nullpxls = "";
    static String TOOL = PEN;
    static JPanel artarea;
    static JFrame frem;
    static JPanel[] pixels;
    static Border border;
    static List<String> CACHEHISTORYCTRLZANDCTRLY = new ArrayList<String>(); // C:R-G-B:|x,y| colors (c), with val of rgb at x, y       D:|x,y| delets cell at x, y
    static int curret_index_in_timeline=0;
    static boolean firstpixel = false;
  
    static void SetTool(String tool)
    {
        TOOL = tool;
        Toolkit tk = Toolkit.getDefaultToolkit();

        Image eraser = tk.getImage("..\\UI\\Blox\\ERASER.png");
        eraser = eraser.getScaledInstance(32, 32, BufferedImage.SCALE_SMOOTH);
        Cursor erasercursor = tk.createCustomCursor(eraser , new Point(0, 2), "img");

        Image pen = tk.getImage("..\\UI\\Blox\\PEN.png");
        pen = pen.getScaledInstance(32, 32, BufferedImage.SCALE_SMOOTH);
        Cursor pencursor = tk.createCustomCursor(pen , new Point(0, 0), "img");

        Image cp = tk.getImage("..\\UI\\Blox\\COLORPICKER.png");
        cp = cp.getScaledInstance(32, 32, BufferedImage.SCALE_SMOOTH);
        Cursor cpcursor = tk.createCustomCursor(cp , new Point(0, 31), "img");

        for (JPanel i : pixels)
        {
            if (TOOL.equals(ERASER))
            {
                i.setCursor (erasercursor);
            }
            else if (TOOL.equals(PEN))
            {
                i.setCursor(pencursor);
            }
            else if (TOOL.equals(COLORPICKER))
            {
                i.setCursor(cpcursor);
            }
            else
            {
                i.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    static String GetTool()
    {
        return TOOL;
    }

    static void ShowWindow()
    {
        semnems = 0;
        nullpxls = "";
        TOOL = PEN;
        CACHEHISTORYCTRLZANDCTRLY = new ArrayList<String>(); // C:R-G-B:|x,y| colors (c), with val of rgb at x, y       D:|x,y| delets cell at x, y
        curret_index_in_timeline=0;
        WIDTH=0;
        HEIGHT=0;
        CURRENTCOLOR = utils.highlight_color;
        JFrame frem = new JFrame();
        frem.setResizable(false);
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) Math.round(screensize.getWidth()/5f);
        int h = (int) Math.round(screensize.getHeight()/5f);
        frem.setSize(new Dimension(w, h));
        frem.setLocationRelativeTo(null);
        frem.getContentPane().setBackground(Color.BLACK);
        frem.getContentPane().setLayout(new GridBagLayout());
        frem.setTitle("Create new Sprite");
        frem.setVisible(true);
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(256);
        formatter.setAllowsInvalid(true);
        formatter.setCommitsOnValidEdit(true);

        JLabel WidthPixel = new JLabel("Height (pixels):");
        WidthPixel.setForeground(utils.highlight_color);
        WidthPixel.setFont(utils.Verdana(16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        frem.getContentPane().add(WidthPixel, gbc);

        JLabel HeightPixel = new JLabel("Width (pixels):");
        HeightPixel.setForeground(utils.highlight_color);
        HeightPixel.setFont(utils.Verdana(16));
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        frem.getContentPane().add(HeightPixel, gbc);
        
        OPFormattedTextFieldForNumbers widthinput = new OPFormattedTextFieldForNumbers(formatter);
        widthinput.setText("32");
        widthinput.setBorder(new EmptyBorder(5, 5, 5, 5));
        widthinput.setFont(utils.Verdana(18));
        widthinput.setBackground(utils.highlight_color);
        widthinput.setForeground(utils.DarkColor(0.1f));
        widthinput.setBorder(new EmptyBorder(0, 10, 0, 10));
        widthinput.setArcSize(15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        frem.getContentPane().add(widthinput, gbc);

        OPFormattedTextFieldForNumbers heightinput = new OPFormattedTextFieldForNumbers(formatter);
        heightinput.setText("32");
        heightinput.setBorder(new EmptyBorder(5, 5, 5, 5));
        heightinput.setFont(utils.Verdana(18));
        heightinput.setBackground(utils.highlight_color);
        heightinput.setForeground(utils.DarkColor(0.1f));
        heightinput.setBorder(new EmptyBorder(0, 10, 0, 10));
        heightinput.setArcSize(15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        frem.getContentPane().add(heightinput, gbc);

        OPButton createPixelArtThing = new OPButton("Create");
        createPixelArtThing.setArcSize(15);
        createPixelArtThing.setFont(utils.Verdana(16));
        createPixelArtThing.setBorder(new EmptyBorder(5, 5, 5, 5));
        createPixelArtThing.setBorderPainted(false);
        createPixelArtThing.setFocusPainted(false);
        createPixelArtThing.setBackground(utils.highlight_color);
        createPixelArtThing.setHoverBackgroundColor(utils.highlight_color.brighter());
        createPixelArtThing.setPressedBackgroundColor(utils.highlight_highlight_color);
        createPixelArtThing.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                boolean isPowerOf2 = false;
                String text = heightinput.getText().replace(",", "");
                String[] powersof2 = {"2", "4", "8", "16", "32", "64", "128", "256"};
                for (String i : powersof2)
                {
                    if (text.equals(i))
                    {
                        isPowerOf2 = true;
                        break;
                    }
                }

                text = widthinput.getText().replace(",", "");
                if (isPowerOf2==true)
                {
                    isPowerOf2 = false;
                    for (String i : powersof2)
                    {
                        if (text.equals(i))
                        {
                            isPowerOf2 = true;
                            break;
                        }
                    }
                }
                if (isPowerOf2==false)
                {
                    UIManager.put("OptionPane.background", utils.DarkColor(0.1f));
                    UIManager.put("Panel.background", utils.DarkColor(0.1f));
                    UIManager.put("OptionPane.messageForeground", utils.highlight_color);
                    UIManager.put("Button.background", utils.highlight_color);
                    UIManager.put("Button.foreground", utils.DarkColor(0.1f));
                    int result=JOptionPane.showConfirmDialog(null, "ARE YOU SURE that u wants to have a size that is NOT a power of 2??", "Jeeniyus! You're more hopeless than this...", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.YES_OPTION)
                    {
                        frem.dispose();
                        WIDTH = Integer.parseInt(widthinput.getText());
                        HEIGHT = Integer.parseInt(heightinput.getText());
                        ShowFinalWindow();
                    }
                }
                else
                {
                    frem.dispose();
                    WIDTH = Integer.parseInt(widthinput.getText());
                    HEIGHT = Integer.parseInt(heightinput.getText());
                    ShowFinalWindow();
                }
            }
            
        });
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        frem.getContentPane().add(createPixelArtThing, gbc);
        
        OPButton cancelPixelArtThing = new OPButton("Cancel");
        cancelPixelArtThing.setArcSize(15);
        cancelPixelArtThing.setFont(utils.Verdana(16));
        cancelPixelArtThing.setBorder(new EmptyBorder(5, 5, 5, 5));
        cancelPixelArtThing.setBorderPainted(false);
        cancelPixelArtThing.setFocusPainted(false);
        cancelPixelArtThing.setBackground(utils.highlight_color);
        cancelPixelArtThing.setHoverBackgroundColor(utils.highlight_color.brighter());
        cancelPixelArtThing.setPressedBackgroundColor(utils.highlight_highlight_color);
        cancelPixelArtThing.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                frem.dispose();
            }

        });
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        frem.getContentPane().add(cancelPixelArtThing, gbc);
    }

    static void ShowFinalWindow()
    {
        float ratio = 16f/9f;
        frem = new JFrame();
        frem.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        frem.setSize(new Dimension(w, (int)Math.round((float)w/ratio)));
        frem.setLocationRelativeTo(null);
        frem.getContentPane().setBackground(utils.DarkColor(0.3f));
        frem.setLayout(null);
        frem.setTitle("Creat new Sprite...");
        frem.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        artarea = new JPanel();
        pixels = new JPanel[WIDTH*HEIGHT];

        artarea.setBackground(utils.DarkColor(0.2f));
        int possiblescale1 = (int) Math.floor(frem.getHeight()/HEIGHT);
        int possiblescale2 = (int) Math.floor((frem.getWidth()/1.3f)/WIDTH);
        int scale = 0;
        if (possiblescale1>possiblescale2)
        {
            scale = possiblescale2;
        }
        else
        {
            scale = possiblescale1;
        }
        scale = (int) Math.floor(scale*0.95);
        JPanel iconarea = new JPanel();
        artarea.setBounds(frem.getWidth()-(scale*WIDTH), Math.round(frem.getHeight()/2)-Math.round((HEIGHT*scale)/2), WIDTH*scale, HEIGHT*scale);
        artarea.setLayout(null);
        artarea.setBorder(null);
        iconarea.setBounds(0, 0, frem.getWidth()-artarea.getWidth(), frem.getHeight());
        iconarea.setLayout(new GridLayout(3, 2));
        iconarea.setBorder(null);
        iconarea.setBackground(utils.highlight_highlight_color);

        OPTextField nemin = new OPTextField("newDespairingSprite");
        nemin.setFont(utils.Verdana(10));
        nemin.setBackground(utils.DarkColor(0.2f));
        // nemin.setForeground(utils.DarkColor(0.1f));
        nemin.setBorder(new EmptyBorder(0, 0, 0, 0));
        nemin.setHorizontalAlignment(SwingConstants.CENTER);
        nemin.setArcSize(69420);
        nemin.getDocument().addDocumentListener(new DocumentListener() 
        {
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                File folder = new File(path);
                File[] othershitindir = folder.listFiles();
                String text = nemin.getText();
                for (File i : othershitindir)
                {
                    if (i.getName().equals(text))
                    {
                        semnems++;
                    }
                }

            } 
        });
        nemin.setFont(utils.Verdana(16));
        nemin.setForeground(utils.highlight_color);
        int size = Math.round(scale/2);

        OPButton SAVE = new OPButton("SAVE SPRITE");
        SAVE.setFont(utils.Verdana(30));
        SAVE.setBackground(CURRENTCOLOR);
        SAVE.setHoverBackgroundColor(SAVE.getBackground().brighter());
        SAVE.setFocusPainted(false);
        SAVE.setBorderPainted(false);
        SAVE.setArcSize(69420);
        SAVE.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    BufferedImage image = new BufferedImage(WIDTH*size, HEIGHT*size, BufferedImage.TYPE_INT_ARGB);
                    for (int i = 0; i < pixels.length; i++)
                    {
                        int y = (int) Math.floor(i/WIDTH);
                        int x = i - (y*WIDTH);        
                        if (nullpxls.contains(utils.xytoString(x, y))==true)
                        {
                            // Do nothing :) You earned it
                        }
                        else
                        {
                            for (int xx = 0; xx < size; xx++)
                            {
                                for (int yy = 0; yy < size; yy++)
                                {
                                    image.setRGB((x*size)+xx, (y*size)+yy, pixels[i].getBackground().getRGB());
                                }
                            }
                        }
                    }
                    String suffix = nemin.getText();
                    if (semnems>0)
                    {
                        suffix=suffix+"("+String.valueOf(semnems)+")";
                    }
                    String png = ".png";
                    if (suffix.endsWith(png))
                    {
                        png = "";
                    }
                    File file = new File(path+"\\"+suffix+png);
                    ImageIO.write(image, "png", file);
                } 
                catch (IOException e1) 
                {
                    e1.printStackTrace();
                }
            }
        });

        OPButton ColorPane = new OPButton("Color");
        ColorPane.setBackground(CURRENTCOLOR);
        ColorPane.setForeground(utils.CONTRASTNotactually(CURRENTCOLOR));
        ColorPane.setHoverBackgroundColor(CURRENTCOLOR.brighter());
        ColorPane.setFocusPainted(false);
        ColorPane.setBorderPainted(false);
        ColorPane.setArcSize(69420);
        ColorPane.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Color TMPCURRENTCOLOR = JColorChooser.showDialog(frem, "Choose the new hope-destructing color", CURRENTCOLOR);
                if (TMPCURRENTCOLOR!=null)
                {
                    CURRENTCOLOR=TMPCURRENTCOLOR;
                }
                ColorPane.setBackground(CURRENTCOLOR);
                ColorPane.setForeground(utils.CONTRASTNotactually(CURRENTCOLOR));
                ColorPane.setHoverBackgroundColor(CURRENTCOLOR.brighter());
            }
        });

        OPButton PenTool = new OPButton("Pen Tool");
        PenTool.setBackground(CURRENTCOLOR);
        PenTool.setHoverBackgroundColor(CURRENTCOLOR.brighter());
        PenTool.setFocusPainted(false);
        PenTool.setBorderPainted(false);
        PenTool.setArcSize(69420);
        PenTool.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                SetTool(PEN);
            }
        });

        OPButton EraserTool = new OPButton("Eraser Tool");
        EraserTool.setBackground(CURRENTCOLOR);
        EraserTool.setHoverBackgroundColor(CURRENTCOLOR.brighter());
        EraserTool.setFocusPainted(false);
        EraserTool.setBorderPainted(false);
        EraserTool.setArcSize(69420);
        EraserTool.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                SetTool(ERASER);
            }
        });

        OPButton colorpickerTool = new OPButton("Color Picker");
        colorpickerTool.setBackground(CURRENTCOLOR);
        colorpickerTool.setHoverBackgroundColor(CURRENTCOLOR.brighter());
        colorpickerTool.setFocusPainted(false);
        colorpickerTool.setBorderPainted(false);
        colorpickerTool.setArcSize(69420);
        colorpickerTool.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                SetTool(COLORPICKER);
            }
        });

        iconarea.add(EraserTool);
        iconarea.add(PenTool);
        iconarea.add(colorpickerTool);
        iconarea.add(ColorPane);
        iconarea.add(nemin);
        iconarea.add(SAVE);
        frem.getContentPane().add(iconarea);
        frem.getContentPane().add(artarea);
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                // OPButton b = new OPButton();
                // b.setBorderPainted(false);
                // b.setFocusPainted(false);
                // b.setBackground(artarea.getBackground());
                // b.setHoverBackgroundColor(artarea.getBackground().brighter());
                // b.setBounds(x*scale, y*scale, scale, scale);
                // artarea.add(b);

                int X = x;
                int Y = y;

                JPanel p = new JPanel();
                p.setBackground(new Color(0f, 0f, 0f, 0f));
                if (scale>10)
                {
                    p.setBorder(new LineBorder(utils.DarkColor(0.1f), 1));
                }
                border=p.getBorder();
                p.setBounds(x*scale, y*scale, scale, scale);
                p.addMouseListener(new MouseListener() 
                {
                    @Override
                    public void mouseClicked(MouseEvent e) 
                    {
                        if (GetTool().equals(PEN))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                Drau(p, X, Y);
                            }
                            else if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) 
                            {
                                Irez(p, X, Y);
                            }
                        }
                        else if (GetTool().equals(ERASER))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                Irez(p, X, Y);
                            }
                        }
                        else if (GetTool().equals(COLORPICKER))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                CURRENTCOLOR = p.getBackground();
                                ColorPane.setBackground(CURRENTCOLOR);
                                ColorPane.setForeground(utils.CONTRASTNotactually(CURRENTCOLOR));
                                ColorPane.setHoverBackgroundColor(CURRENTCOLOR.brighter());
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) 
                    {
                        if (GetTool().equals(PEN))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                Drau(p, X, Y);
                            }
                            else if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) 
                            {
                                Irez(p, X, Y);
                            }
                        }
                        else if (GetTool().equals(ERASER))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                Irez(p, X, Y);
                            }
                        }
                        else if (GetTool().equals(COLORPICKER))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                CURRENTCOLOR = p.getBackground();
                                ColorPane.setBackground(CURRENTCOLOR);
                                ColorPane.setForeground(utils.CONTRASTNotactually(CURRENTCOLOR));
                                ColorPane.setHoverBackgroundColor(CURRENTCOLOR.brighter());
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) 
                    {
                        if (GetTool().equals(PEN))
                        {
                            popeverythingafterindexinList(curret_index_in_timeline);
                            curret_index_in_timeline++;
                        }
                        else if (GetTool().equals(ERASER))
                        {
                            popeverythingafterindexinList(curret_index_in_timeline);       
                            curret_index_in_timeline++;
                        }
                        else if (GetTool().equals(COLORPICKER))
                        {
                            SetTool(PEN);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) 
                    {
                        if (GetTool().equals(PEN))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                Drau(p, X, Y);
                            }
                            else if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) 
                            {
                                Irez(p, X, Y);
                            }
                        }
                        else if (GetTool().equals(ERASER))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                Irez(p, X, Y);
                            }
                        }
                        else if (GetTool().equals(COLORPICKER))
                        {
                            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) 
                            {
                                CURRENTCOLOR = p.getBackground();
                                ColorPane.setBackground(CURRENTCOLOR);
                                ColorPane.setForeground(utils.CONTRASTNotactually(CURRENTCOLOR));
                                ColorPane.setHoverBackgroundColor(CURRENTCOLOR.brighter());
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) 
                    {
                        if (GetTool().equals(PEN))
                        {
                            
                        }
                        else if (GetTool().equals(ERASER))
                        {
                            
                        }
                        else if (GetTool().equals(COLORPICKER))
                        {

                        }
                    }
                });
                artarea.add(p);
                int[] xy = {x, y};
                nullpxls+=utils.listintxytoString(xy);
                pixels[(y*WIDTH)+x] = p;
            }
        }
        SetTool(PEN);

        UIManager.put("MenuItem.selectionForeground", utils.DarkColor(0.25f));
        UIManager.put("MenuItem.selectionBackground", utils.highlight_color);
        UIManager.put("MenuItem.acceleratorForeground", utils.highlight_highlight_color);
        UIManager.put("MenuItem.acceleratorSelectionForeground", utils.DarkColor(0.3f));
        UIManager.put("Menu.selectionForeground", utils.DarkColor(0.25f));
        UIManager.put("Menu.selectionBackground", utils.highlight_color);

        JMenuBar mb = new JMenuBar();
        JMenu edit = new JMenu();
        JMenuItem[] edititems = {new JMenuItem("Undo"), new JMenuItem("Redo")};
        int[] Mnemonics = {KeyEvent.VK_S, -1};
        KeyStroke[] Accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK)};
        String[] ActionCommands = {"UNDO", "REDO"};
        for (int i = 0; i<edititems.length; i++)
        {
            JMenuItem item=edititems[i];
            item.setFont(utils.Verdana(12));
            item.setForeground(utils.highlight_color);
            item.setBackground(utils.DarkColor(0.15f));
            item.setActionCommand(ActionCommands[i]);
            item.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    if (e.getActionCommand().equals("UNDO"))
                    {
                        if (curret_index_in_timeline>0)
                        {
                            for (int i = 0; i < CACHEHISTORYCTRLZANDCTRLY.size(); i++)
                            {
                                if (CACHEHISTORYCTRLZANDCTRLY.get(i).isEmpty())
                                {
                                    CACHEHISTORYCTRLZANDCTRLY.remove(i);
                                }
                            }
                            curret_index_in_timeline--;
                            DOWHATHESAYZ(CACHEHISTORYCTRLZANDCTRLY.get(curret_index_in_timeline), 0);
                            CACHEHISTORYCTRLZANDCTRLY.add(curret_index_in_timeline, "");
                        }
                    }
                    else if (e.getActionCommand().equals("REDO"))
                    {
                        for (int i = 0; i < CACHEHISTORYCTRLZANDCTRLY.size(); i++)
                        {
                            if (CACHEHISTORYCTRLZANDCTRLY.get(i).isEmpty())
                            {
                                CACHEHISTORYCTRLZANDCTRLY.remove(i);
                            }
                        }
                        if (curret_index_in_timeline<CACHEHISTORYCTRLZANDCTRLY.size())
                        {
                            DOWHATHESAYZ(CACHEHISTORYCTRLZANDCTRLY.get(curret_index_in_timeline), 1);
                            curret_index_in_timeline++;
                        }
                    }                     
                }
                
            });
            if (Mnemonics[i]!=-1)
            {
                item.setMnemonic(Mnemonics[i]);
            }
            if (Accelerators[i]!=null)
            {
                item.setAccelerator(Accelerators[i]);
            }
            edit.add(item);
        }
        mb.add(edit);
        frem.setJMenuBar(mb);
        frem.setVisible(true);
    }

    static void Drau(JPanel p, int x, int y)
    {
        if (p.getBackground()!=CURRENTCOLOR)
        {
            p.setBackground(CURRENTCOLOR);
            nullpxls = nullpxls.replace(utils.listintxytoString(utils.FindIndexVector2(p, pixels, WIDTH)), "");
            p.setBorder(null);
            
            String strtoset = CACHEHISTORYCTRLZANDCTRLY.size() == curret_index_in_timeline ? "" : CACHEHISTORYCTRLZANDCTRLY.get(curret_index_in_timeline);
            strtoset+="##D:"+String.valueOf(CURRENTCOLOR.getRed())+"-"+String.valueOf(CURRENTCOLOR.getGreen())+"-"+String.valueOf(CURRENTCOLOR.getBlue())+":"+String.valueOf(x)+","+String.valueOf(y)+"##";
            if (curret_index_in_timeline<CACHEHISTORYCTRLZANDCTRLY.size())
            {
                if (firstpixel==true)
                {
                    CACHEHISTORYCTRLZANDCTRLY.set(curret_index_in_timeline, "");
                    firstpixel = false;
                }
                CACHEHISTORYCTRLZANDCTRLY.set(curret_index_in_timeline, strtoset);
            }
            else
            {
                CACHEHISTORYCTRLZANDCTRLY.add(strtoset);
            }
        }
    }
    
    static void Irez(JPanel p, int X, int Y)
    {
        if (!nullpxls.contains(utils.xytoString(X, Y)))
        {
            p.setBackground(artarea.getBackground());
            nullpxls+=utils.listintxytoString(utils.FindIndexVector2(p, pixels, WIDTH));
            p.setBorder(border);
            
            String strtoset = CACHEHISTORYCTRLZANDCTRLY.size() == curret_index_in_timeline ? "" : CACHEHISTORYCTRLZANDCTRLY.get(curret_index_in_timeline);
            strtoset += "##C:"+String.valueOf(CURRENTCOLOR.getRed())+"-"+String.valueOf(CURRENTCOLOR.getGreen())+"-"+String.valueOf(CURRENTCOLOR.getBlue())+":"+String.valueOf(X)+","+String.valueOf(Y)+"##";
            if (curret_index_in_timeline<CACHEHISTORYCTRLZANDCTRLY.size())
            {
                if (firstpixel==true)
                {
                    CACHEHISTORYCTRLZANDCTRLY.set(curret_index_in_timeline, "");
                    firstpixel = false;
                }
                CACHEHISTORYCTRLZANDCTRLY.set(curret_index_in_timeline, strtoset);
            }
            else
            {
                CACHEHISTORYCTRLZANDCTRLY.add(strtoset);
            }
        }
    }

    static void Draumodified(JPanel p, int x, int y)
    {
        p.setBackground(CURRENTCOLOR);
        nullpxls = nullpxls.replace(utils.listintxytoString(utils.FindIndexVector2(p, pixels, WIDTH)), "");
        p.setBorder(null);
    }
    
    static void Irezmodified(JPanel p, int X, int Y)
    {
        p.setBackground(artarea.getBackground());
        nullpxls+=utils.listintxytoString(utils.FindIndexVector2(p, pixels, WIDTH));
        p.setBorder(border);
    }

    static void popeverythingafterindexinList(int index)
    {
        for (int i = index+1; i < CACHEHISTORYCTRLZANDCTRLY.size(); i++)
        {
            CACHEHISTORYCTRLZANDCTRLY.remove(i);
        }
    }

    static void DOWHATHESAYZ(String str, int ru /*undo or redo 0 or 1*/)
    {
        String[] strs = str.split("##");
        for (String substr : strs)
        {
            if (substr.isEmpty())
            {
                continue;
            }
            String[] partsofdata = substr.split(":");
            int x = Integer.valueOf(partsofdata[2].split(",")[0]);  
            int y = Integer.valueOf(partsofdata[2].split(",")[1]);
            int index = (y*WIDTH)+x;
            if ((partsofdata[0].equals("D") && ru == 0) || (partsofdata[0].equals("C") && ru == 1))
            {
                Irezmodified(pixels[index], x, y);
            }
            else if ((partsofdata[0].equals("C") && ru == 0) || (partsofdata[0].equals("D") && ru == 1))
            {
                int r = Integer.valueOf(partsofdata[1].split("-")[0]);
                int g = Integer.valueOf(partsofdata[1].split("-")[1]);
                int b = Integer.valueOf(partsofdata[1].split("-")[2]);
                CURRENTCOLOR = new Color(r, g, b);
                Draumodified(pixels[index], x, y);
            }
        }
    }
}
