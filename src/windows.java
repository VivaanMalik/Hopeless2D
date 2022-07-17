import Hopeless2D.Runner;
import Hopeless2D.Test;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class windows extends classes
{
    private enum ActionList
    {
        FILENAMEINPUT,
        SUBMITFORNEWFILE,
        CANCELFORNEWFILE,
        BROWSEFOLDERFORNNEWFILE,
        OPENFILEINPUT,
        BROWSEFOLDERFOROPENFILE,
        OPENFILEFOROPENINGFILE,
        CANCELFOROPENFILE, 
        SAVEFILE, 
        SAVEAS,
        SHOWGAMEWINDOW, 
        PIXELART,
        OPENCODEBUTTON
    }

    // Listener
    private static class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {   
            if (e.getActionCommand()==ActionList.FILENAMEINPUT.name())
            {
                JButton b=(JButton) e.getSource();
                utils.DisableButton(b);
                GetNewFileName();
                CancelOpeningOfNewFile();
            }
            else if (e.getActionCommand()==ActionList.SUBMITFORNEWFILE.name())
            {
                CreateNewFile();
            }
            else if (e.getActionCommand()==ActionList.CANCELFORNEWFILE.name())
            {
                CancelEnteringNameForNewFile();
            }
            else if (e.getActionCommand()==ActionList.BROWSEFOLDERFORNNEWFILE.name())
            {
                BrowseFolderNewFile();
            }
            else if (e.getActionCommand()==ActionList.OPENFILEINPUT.name())
            {
                JButton b=(JButton) e.getSource();
                utils.DisableButton(b);
                GetFileToOpen();
                CancelEnteringNameForNewFile();
            }
            else if (e.getActionCommand()==ActionList.BROWSEFOLDERFOROPENFILE.name())
            {
                BrowseFolderOpenFile();
            }
            else if (e.getActionCommand()==ActionList.OPENFILEFOROPENINGFILE.name())
            {
                OpenFileToOpenFile();
            }
            else if (e.getActionCommand()==ActionList.CANCELFOROPENFILE.name())
            {
                CancelOpeningOfNewFile();
            }
            else if (e.getActionCommand()==ActionList.SAVEFILE.name())
            {
                SaveStuff();
            }
            else if (e.getActionCommand()==ActionList.SAVEAS.name())
            {
                Path tmppath = utils.Saveas(FILEPATH, Window);
                if (tmppath!=null)
                {
                    try
                    {
                        List<String> lines=new ArrayList<String>();
                        lines=Files.readAllLines(Paths.get(Data_Fille_path));
                        String[] updatedLines=lines.toArray(new String[0]);
                        for (int i = 0; i < updatedLines.length; i ++)
                        {
                            if (updatedLines[i].startsWith("ProjectFilePaths"))
                            {
                                String line = updatedLines[i].substring(20, updatedLines[i].length()-1);
                                String[] paths=line.split(", ");
                                File f = FILEPATH.toFile();
                                f=f.getParentFile();
                                for (int j=0; j<paths.length; j++) 
                                {
                                    if (new File(paths[j]).equals(f))
                                    {
                                        paths[j]=tmppath.toFile().getParentFile().toString();
                                    }
                                }
                                String newline="";
                                for (int j=0; j<paths.length; j++)
                                {
                                    if (j!=0)
                                    {
                                        newline=newline+", "+paths[j];
                                    }
                                    else
                                    {
                                        newline=newline+paths[j];
                                    }
                                }
                                updatedLines[i] = updatedLines[i].substring(0, 20)+newline+"|";
                            }
                        }
                        
                        FileWriter fw= new FileWriter(Data_Fille_path);
                        if (updatedLines.length>0)
                        {
                            fw.write("");
                            for (String i : updatedLines)
                            {
                                fw.append(i+"\n");
                            }
                        }
                        fw.close();
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    FILEPATH=tmppath;
                    SaveStuff();
                }
            }
            else if (e.getActionCommand()==ActionList.SHOWGAMEWINDOW.name())
            {
                boolean test = false;
                if (test == true)
                {
                    new Runner(FILEPATH);
                }
                else
                {
                    Test.Log("===NewLog===");
                    Runtime rt = Runtime.getRuntime();
                    try 
                    {
                        rt.exec(new String[]{"java", ".\\Hopeless2D\\Runner.java", FILEPATH.toString()});
                    } 
                    catch (IOException e1) 
                    {
                        e1.printStackTrace();
                    }
                }
            }
            else if (e.getActionCommand()==ActionList.PIXELART.name())
            {
                PixelArt.path = FILEPATH.getParent().toString();
                PixelArt.ShowWindow();
            }
            else if (e.getActionCommand()==ActionList.OPENCODEBUTTON.name())
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
                        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e3) 
                        {
                            e3.printStackTrace();
                        }
                        break;
                    }
                }

                String path = FILEPATH.getParent().toString();

                JFileChooser JFC;
                JFC=new JFileChooser(path);
                JFC.setDialogTitle("Select the location of the next hope destroyer");
                // JFC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                JFC.setFileFilter(new FileFilter() {
                    @Override
                    public String getDescription() 
                    {
                        return "*.java";
                    }

                    @Override
                    public boolean accept(File f) 
                    {
                        if (f.isDirectory())
                        {
                            return true;
                        }
                        return f.getName().endsWith(".java");
                    }
                });
                JFC.setAcceptAllFileFilterUsed(false);
                
                int opt=JFC.showOpenDialog(menuwindow);
                try 
                {
                    UIManager.setLookAndFeel(laf);
                } catch (UnsupportedLookAndFeelException e2) 
                {
                    e2.printStackTrace();
                }
                if (opt==JFileChooser.APPROVE_OPTION)
                {
                    CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp = CodingWindow.openfile(JFC.getCurrentDirectory().toString(), JFC.getSelectedFile().getName());
                    CODEFILENAME = JFC.getSelectedFile().getName();
                    CODEFILEPATH = JFC.getCurrentDirectory().toString();
                    codeWindow.removeAll();
                    SetupCodeWindow(CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp);
                    utils.Repaint(Window);
                }
            }
        }
    }
    

    public static String version="0.0.0";
    public static String Data_Fille_path=System.getenv("APPDATA")+"Local\\Hopeless2D\\data.hopelessdata";

    //menu
    static JFrame menuwindow;
    static int menuwidth;
    static int menuheight;
    static JPanel menulistpanel;
    static JList<String> menulist;
    static OPButton MenuNewFile;
    static OPButton MenuOpenFile;
    static OPButton menuopenfilebrowsbutton;
    static OPButton menuopenfilecancelbutton;
    static OPButton menunewfilesubmitbutton;
    static OPButton menunewfilecancelbutton;
    static OPButton menunewfilebrowsbutton;
    static OPButton menuopenfileopenbutton;
    static OPTextField menunewfilenamefield;
    static JLabel menuLabelName;
    static JLabel menuLabelLocaionPath;
    static JLabel menuLabelLocation;
    static String menuStringDir;
    
    // main window
    static String CODEFILENAME = null;
    static String CODEFILEPATH = null;
    static String CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp = "\n";
    static Path FILEPATH;
    static float Lvl1ResizeWeight;
    static JFrame Window;
    static JPanel codeWindow = new JPanel();
    static JPanel gameWindow = new JPanel();
    static int width;
    static int height;
    static JSplitPane CodeGameSplit;

    // Gme Window
    // public static Hopeless hp;
    // static int hopelesses_count = 0;
    // static GameWindow gw;

    public static void OpenMenuWindow()
    {
        // UIManager.put("Button.hoverBackgroundColor", utils.DarkColor(0.25f));
        menuwindow=new JFrame();
        menuwindow.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        menuwidth = (int)screenSize.getWidth();
        menuheight = (int)screenSize.getHeight();
        menuwidth=Math.round(menuwidth/1.5f);
        menuheight=Math.round(menuheight/1.1f);
        menuwindow.setSize(new Dimension(menuwidth, menuheight));
        menuwindow.setLayout(null);
        menuwindow.getContentPane().setBackground(utils.DarkColor(0.15f));
        menuwindow.setTitle("Hopeless Game Engine "+version);
        menuwindow.setIconImage(Toolkit.getDefaultToolkit().getImage("..\\UI\\Menu\\LogoSymbol.png"));
        menuwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // menuwindow.pack();
        menuwindow.setLocationRelativeTo(null);

        try 
        {
            BufferedImage Logo = ImageIO.read(new File("..\\UI\\Menu\\LogoSymbolMenu.png"));
            JLabel logo= new JLabel();
            logo.setBounds(utils.Percentage2Number(0.05f, menuwidth), utils.Percentage2Number(0.05f, menuheight), utils.Percentage2Number(0.9f, menuwidth), utils.Percentage2Number(0.1721f, utils.Percentage2Number(0.9f, menuwidth)));
            Image logoimg = Logo.getScaledInstance(Math.round(logo.getWidth()*2.3f), Math.round(logo.getHeight()*2.3f), Image.SCALE_SMOOTH);
            logoimg = logoimg.getScaledInstance(Math.round(logo.getWidth()*1.5f), Math.round(logo.getHeight()*1.5f), Image.SCALE_SMOOTH);
            logoimg = logoimg.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(logoimg));

            JLabel tagline=new JLabel("The GameEngine with a future shorter than yours...");
            tagline.setFont(new Font("Serif", Font.ITALIC, 30));
            Dimension tmpsize=tagline.getPreferredSize();
            tagline.setBounds(logo.getX()+logo.getWidth()-utils.Percentage2Number(0.65f, menuwidth), logo.getY()+logo.getHeight()-30, logo.getWidth(), (int)tmpsize.getHeight());
            tagline.setForeground(utils.highlight_color);
            tagline.setBackground(utils.highlight_color);

            menuwindow.getContentPane().add(tagline);
            menuwindow.getContentPane().add(logo);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        // Image img=new ImageIcon("..\\UI\\Menu\\Button.png").getImage();
        int bw=utils.Percentage2Number(0.4f, menuwidth);
        int bh=utils.Percentage2Number(0.1f, menuheight);
        // img=img.getScaledInstance(bw, bh, java.awt.Image.SCALE_FAST);
        MenuNewFile= new OPButton("New File...");
        MenuNewFile.setArcSize(15);
        MenuNewFile.setBackground(utils.highlight_color);
        MenuNewFile.setFont(utils.Verdana(24));
        MenuNewFile.setFocusPainted(false);
        MenuNewFile.setBorderPainted(false);
        MenuNewFile.setHoverBackgroundColor(utils.highlight_color.brighter());
        MenuNewFile.setPressedBackgroundColor(utils.highlight_highlight_color);
        MenuNewFile.setBounds(utils.Percentage2Number(0.05f, menuwidth), utils.Percentage2Number(0.4f, menuheight), bw, bh);
        MenuNewFile.setActionCommand(ActionList.FILENAMEINPUT.name());
        MenuNewFile.addActionListener(new Listener());

        MenuOpenFile=new OPButton("Open File...");
        MenuOpenFile.setArcSize(15);
        MenuOpenFile.setBackground(utils.highlight_color);
        MenuOpenFile.setFont(utils.Verdana(24));
        MenuOpenFile.setFocusPainted(false);
        MenuOpenFile.setBorderPainted(false);
        MenuOpenFile.setHoverBackgroundColor(utils.highlight_color.brighter());
        MenuOpenFile.setPressedBackgroundColor(utils.highlight_highlight_color);
        MenuOpenFile.setBounds(utils.Percentage2Number(0.95f, menuwidth)-bw, utils.Percentage2Number(0.4f, menuheight), bw, bh);
        MenuOpenFile.setActionCommand(ActionList.OPENFILEINPUT.name());
        MenuOpenFile.addActionListener(new Listener());

        try
        {
            List<String> lines=new ArrayList<String>();
            lines=Files.readAllLines(Paths.get(Data_Fille_path));
            String[] updatedLines=lines.toArray(new String[0]);
            for (int i = 0; i < updatedLines.length; i ++)
            {
                if (updatedLines[i].startsWith("ProjectFilePaths"))
                {
                    String line = updatedLines[i].substring(20, updatedLines[i].length()-1);
                    String[] paths=line.split(", ");
                    if (paths.length == 0)
                    {
                        utils.DisableButton(MenuOpenFile);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        menuwindow.add(MenuNewFile);
        menuwindow.add(MenuOpenFile);
        menuwindow.setVisible(true);

    }

    public static void GetFileToOpen()
    {
        int bw=utils.Percentage2Number(0.135f, menuwidth);
        int bh=utils.Percentage2Number(0.1f, menuheight);

        menuopenfilebrowsbutton=new OPButton("Browse...");
        menuopenfilebrowsbutton.setArcSize(15);
        menuopenfilebrowsbutton.setFont(utils.Verdana(16));
        menuopenfilebrowsbutton.setBorder(new EmptyBorder(0, 5, 0, 5));
        menuopenfilebrowsbutton.setBorderPainted(false);
        menuopenfilebrowsbutton.setFocusPainted(false);
        menuopenfilebrowsbutton.setBackground(utils.highlight_color);
        menuopenfilebrowsbutton.setHoverBackgroundColor(utils.highlight_color.brighter());
        menuopenfilebrowsbutton.setPressedBackgroundColor(utils.highlight_highlight_color);
        menuopenfilebrowsbutton.setActionCommand(ActionList.BROWSEFOLDERFOROPENFILE.name());
        menuopenfilebrowsbutton.addActionListener(new Listener());
        menuopenfilebrowsbutton.setBounds(utils.Percentage2Number(0.95f, menuwidth)-bw, utils.Percentage2Number(0.35f, menuheight)+17+bh+Math.round(bh/1.5f), bw, Math.round(bh/1.5f));

        menuopenfileopenbutton=new OPButton("Open...");
        menuopenfileopenbutton.setArcSize(15);
        menuopenfileopenbutton.setFont(utils.Verdana(16));
        menuopenfileopenbutton.setBorder(new EmptyBorder(0, 5, 0, 5));
        menuopenfileopenbutton.setBorderPainted(false);
        menuopenfileopenbutton.setFocusPainted(false);
        menuopenfileopenbutton.setBackground(utils.highlight_color);
        menuopenfileopenbutton.setHoverBackgroundColor(utils.highlight_color.brighter());
        menuopenfileopenbutton.setPressedBackgroundColor(utils.highlight_highlight_color);
        menuopenfileopenbutton.setActionCommand(ActionList.OPENFILEFOROPENINGFILE.name());
        menuopenfileopenbutton.addActionListener(new Listener());
        menuopenfileopenbutton.setBounds(utils.Percentage2Number(0.95f, menuwidth)-bw, utils.Percentage2Number(0.45f, menuheight)+17+bh+Math.round(bh/1.5f), bw, Math.round(bh/1.5f));
        utils.DisableButton(menuopenfileopenbutton);

        menuopenfilecancelbutton=new OPButton("Cancel...");
        menuopenfilecancelbutton.setArcSize(15);
        menuopenfilecancelbutton.setFont(utils.Verdana(16));
        menuopenfilecancelbutton.setBorder(new EmptyBorder(0, 5, 0, 5));
        menuopenfilecancelbutton.setBorderPainted(false);
        menuopenfilecancelbutton.setFocusPainted(false);
        menuopenfilecancelbutton.setBackground(utils.highlight_color);
        menuopenfilecancelbutton.setHoverBackgroundColor(utils.highlight_color.brighter());
        menuopenfilecancelbutton.setPressedBackgroundColor(utils.highlight_highlight_color);
        menuopenfilecancelbutton.setActionCommand(ActionList.CANCELFOROPENFILE.name());
        menuopenfilecancelbutton.addActionListener(new Listener());
        menuopenfilecancelbutton.setBounds(utils.Percentage2Number(0.95f, menuwidth)-bw, utils.Percentage2Number(0.55f, menuheight)+17+bh+Math.round(bh/1.5f), bw, Math.round(bh/1.5f));
        
        menulistpanel=new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        menulistpanel.setBounds(utils.Percentage2Number(0.05f, menuwidth), utils.Percentage2Number(0.525f, menuheight), utils.Percentage2Number(0.75f, menuwidth), utils.Percentage2Number(0.4f, menuheight));
        menulistpanel.setPreferredSize(new Dimension(utils.Percentage2Number(0.75f, menuwidth), utils.Percentage2Number(0.4f, menuheight)));
        menulistpanel.setBackground(utils.highlight_highlight_color);
        menulistpanel.setBorder(null);
        try
        {
            List<String> lines=new ArrayList<String>();
            List<String> updatedLines = new ArrayList<>();
            lines=Files.readAllLines(Paths.get(Data_Fille_path));
            for (String line : lines) 
            {
                if (line.startsWith("ProjectNames"))
                {
                    line=line.substring(16, line.length()-1);
                    for (String i : line.split(", ")) 
                    {
                        updatedLines.add(i);
                    }
                }
                if (line.startsWith("ProjectFilePaths"))
                {
                    line=line.substring(20, line.length()-1);
                    String[] line2=line.split(", ");
                    for (int i=0; i<line2.length; i++) 
                    {
                        updatedLines.set(i, updatedLines.get(i)+"|"+line2[i]);
                    }
                }
            }
            menulist = new JList<String>(updatedLines.toArray(new String[updatedLines.size()]));
            menulist.setBackground(utils.highlight_highlight_color);
            menulist.setBorder(null);
            menulist.setBounds(menulistpanel.getX(), menulistpanel.getY(), menulistpanel.getWidth(), menulistpanel.getHeight()-10);
            menulist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            menulist.setFont(utils.Verdana(19));
            menulist.setFixedCellHeight(utils.Percentage2Number(0.09f, menuheight));
            menulist.setFixedCellWidth(utils.Percentage2Number(0.6f, menuwidth));
            menulist.setLayoutOrientation(JList.VERTICAL);
            menulist.setCellRenderer(utils.OPCellRenderer());
            menulist.addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent e) 
                {
                    if (menulist.getSelectedIndex()!=-1)
                    {
                        utils.EnableButton(menuopenfileopenbutton);
                    }
                }
            });            

            JScrollPane scrollpane = new JScrollPane();
            scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollpane.setPreferredSize(new Dimension(utils.Percentage2Number(0.74f, menuwidth), utils.Percentage2Number(0.39f, menuheight)));
            scrollpane.setAutoscrolls(true);
            scrollpane.setViewportView(menulist);
            scrollpane.setBackground(utils.highlight_highlight_color);
            scrollpane.setForeground(new Color(0f, 0f, 0f, 0f));
            scrollpane.setBorder(null);
            JScrollBar scrollbar=scrollpane.getVerticalScrollBar();
            scrollbar.setBackground(utils.highlight_highlight_color);
            scrollbar.setUI(new MyScrollBarUI());
            // scrollpane.getViewport().setBounds(menulistpanel.getX(), menulistpanel.getY(), menulistpanel.getWidth(), menulistpanel.getHeight());

            menulistpanel.add(scrollpane);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        menuwindow.add(menuopenfilecancelbutton);
        menuwindow.add(menulistpanel);
        menuwindow.add(menuopenfileopenbutton);
        menuwindow.add(menuopenfilebrowsbutton);
        utils.Repaint(menuwindow);
    }
    
    public static void OpenFileToOpenFile()
    {
        List<Object> filedata = utils.ExtractGameData(Paths.get(menulist.getSelectedValue().split("\\|")[1]+"\\"+menulist.getSelectedValue().split("\\|")[0]+".hopls"));
        OpenWindow((String)filedata.get(0), (float)filedata.get(1), (Path)filedata.get(2));
    }

    public static void CancelOpeningOfNewFile()
    {
        try
        {
            menuwindow.remove(menuopenfilebrowsbutton);
            menuwindow.remove(menulistpanel);
            menuwindow.remove(menuopenfileopenbutton);
            menuwindow.remove(menuopenfilecancelbutton);

            utils.EnableButton(MenuOpenFile);
            utils.Repaint(menuwindow);
        }
        catch (NullPointerException n)
        {

        }
    }

    public static void GetNewFileName()
    {
        String defaultprojectname="NewFuturelessProject";

        File file=new File(Data_Fille_path);
        // File file = new File("D:\\Totally_normalstuff\\yup_told ya\\Ha! On yo face\\bruh\\hehe boi\\SECRET\\My_stuff\\geminjin\\Project Hopeless2D\\data.hopelessdata");
        try
        {
            try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                String line;
                int noofprojectwithnem=0;
                while ((line=bf.readLine())!=null && line.startsWith("ProjectNames"))
                {
                    line=line.substring(16, line.length()-1);
                    String[] projectnems=line.split(", ");
                    for (int i=0; i<projectnems.length; i++)
                    {
                        if (projectnems[i].startsWith("NewFuturelessProject"))
                        {
                            noofprojectwithnem++;
                        }
                    }
                }
                if (noofprojectwithnem!=0)
                {
                    defaultprojectname=defaultprojectname+"("+String.valueOf(noofprojectwithnem)+")";
                }
            } 
            catch (FileNotFoundException e) 
            {
                throw e;
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            
            menunewfilenamefield= new OPTextField(defaultprojectname);
            int bw=utils.Percentage2Number(0.3f, menuwidth);
            int bh=utils.Percentage2Number(0.1f, menuheight);
            menunewfilenamefield.setFont(utils.Verdana(18));
            menunewfilenamefield.setBounds(utils.Percentage2Number(0.15f, menuwidth), utils.Percentage2Number(0.4f, menuheight)+10+bh, bw, Math.round(bh/1.5f));
            menunewfilenamefield.setBackground(utils.highlight_color);
            menunewfilenamefield.setForeground(utils.DarkColor(0.1f));
            menunewfilenamefield.setBorder(new EmptyBorder(0, 10, 0, 10));
            menunewfilenamefield.setArcSize(15);
            menunewfilenamefield.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e)
                {
                    menuLabelLocaionPath.setText(menuStringDir+"\\"+menunewfilenamefield.getText());
                }

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
            });

            menunewfilebrowsbutton=new OPButton("Browse...");
            menunewfilebrowsbutton.setArcSize(15);
            menunewfilebrowsbutton.setFont(utils.Verdana(16));
            menunewfilebrowsbutton.setBorder(new EmptyBorder(0, 5, 0, 5));
            menunewfilebrowsbutton.setBorderPainted(false);
            menunewfilebrowsbutton.setFocusPainted(false);
            menunewfilebrowsbutton.setBackground(utils.highlight_color);
            menunewfilebrowsbutton.setHoverBackgroundColor(utils.highlight_color.brighter());
            menunewfilebrowsbutton.setPressedBackgroundColor(utils.highlight_highlight_color);
            menunewfilebrowsbutton.setActionCommand(ActionList.BROWSEFOLDERFORNNEWFILE.name());
            menunewfilebrowsbutton.addActionListener(new Listener());
            menunewfilebrowsbutton.setBounds(utils.Percentage2Number(0.05f, menuwidth), utils.Percentage2Number(0.4f, menuheight)+17+bh+Math.round(bh/1.5f), utils.Percentage2Number(0.925f, utils.Percentage2Number(0.1f, menuwidth)), Math.round(bh/1.5f));
            
            menunewfilesubmitbutton=new OPButton("Create");
            menunewfilesubmitbutton.setArcSize(15);
            menunewfilesubmitbutton.setFont(utils.Verdana(16));
            menunewfilesubmitbutton.setBorderPainted(false);
            menunewfilesubmitbutton.setFocusPainted(false);
            menunewfilesubmitbutton.setBackground(utils.highlight_color);
            menunewfilesubmitbutton.setHoverBackgroundColor(utils.highlight_color.brighter());
            menunewfilesubmitbutton.setPressedBackgroundColor(utils.highlight_highlight_color);
            menunewfilesubmitbutton.setActionCommand(ActionList.SUBMITFORNEWFILE.name());
            menunewfilesubmitbutton.addActionListener(new Listener());
            menunewfilesubmitbutton.setBounds(utils.Percentage2Number(0.15f, menuwidth)+utils.Percentage2Number(0.515f, bw), utils.Percentage2Number(0.4f, menuheight)+17+bh+Math.round(bh/1.5f), utils.Percentage2Number(0.478f, bw), Math.round(bh/1.5f));

            menunewfilecancelbutton=new OPButton("Cancel");
            menunewfilecancelbutton.setArcSize(15);
            menunewfilecancelbutton.setFont(utils.Verdana(16));
            menunewfilecancelbutton.setBorderPainted(false);
            menunewfilecancelbutton.setFocusPainted(false);
            menunewfilecancelbutton.setBackground(utils.highlight_color);
            menunewfilecancelbutton.setHoverBackgroundColor(utils.highlight_color.brighter());
            menunewfilecancelbutton.setPressedBackgroundColor(utils.highlight_highlight_color);
            menunewfilecancelbutton.setActionCommand(ActionList.CANCELFORNEWFILE.name());
            menunewfilecancelbutton.addActionListener(new Listener());
            menunewfilecancelbutton.setBounds(utils.Percentage2Number(0.15f, menuwidth), utils.Percentage2Number(0.4f, menuheight)+17+bh+Math.round(bh/1.5f), utils.Percentage2Number(0.485f, bw), Math.round(bh/1.5f));
            
            menuLabelName=new JLabel("Name:");
            menuLabelName.setBounds(utils.Percentage2Number(0.05f, menuwidth), utils.Percentage2Number(0.4f, menuheight)+10+bh, bw, Math.round(bh/1.5f));
            menuLabelName.setForeground(utils.highlight_color);
            menuLabelName.setFont(utils.Verdana(18));

            menuLabelLocation=new JLabel("Path:");
            menuLabelLocation.setBounds(utils.Percentage2Number(0.05f, menuwidth), utils.Percentage2Number(0.4f, menuheight)+24+bh+Math.round(bh/0.75f), bw, Math.round(bh/1.5f));
            menuLabelLocation.setForeground(utils.highlight_color);
            menuLabelLocation.setFont(utils.Verdana(18));
            
            menuStringDir=GetPrefferedFilePath();
            menuLabelLocaionPath=new JLabel(menuStringDir+"\\"+menunewfilenamefield.getText());
            menuLabelLocaionPath.setBounds(utils.Percentage2Number(0.15f, menuwidth), utils.Percentage2Number(0.4f, menuheight)+24+bh+Math.round(bh/0.75f), menuwidth-utils.Percentage2Number(0.15f, menuwidth)-10, Math.round(bh/1.5f));
            menuLabelLocaionPath.setForeground(utils.highlight_color);
            menuLabelLocaionPath.setFont(utils.Verdana(20));
            
            menuwindow.getContentPane().add(menunewfilesubmitbutton);
            menuwindow.getContentPane().add(menunewfilecancelbutton);
            menuwindow.getContentPane().add(menunewfilenamefield);
            menuwindow.getContentPane().add(menunewfilebrowsbutton);
            menuwindow.getContentPane().add(menuLabelLocaionPath);
            menuwindow.getContentPane().add(menuLabelName);
            menuwindow.getContentPane().add(menuLabelLocation);
            
            utils.Repaint(menuwindow);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("OHNO!");
        }
    }

    public static void CancelEnteringNameForNewFile()
    {
        try
        {
            menuwindow.remove(menunewfilecancelbutton);
            menuwindow.remove(menunewfilesubmitbutton);
            menuwindow.remove(menunewfilenamefield);
            menuwindow.remove(menuLabelName);
            menuwindow.remove(menunewfilebrowsbutton);
            menuwindow.remove(menuLabelLocaionPath);
            menuwindow.remove(menuLabelLocation);
            utils.EnableButton(MenuNewFile);
            utils.Repaint(menuwindow);
        }
        catch (NullPointerException n)
        {

        }
    }
    
    public static void CreateNewFile()
    {
        String name=menunewfilenamefield.getText();
        String[] projectnems;
        boolean isExisting=false;
        boolean isValid=true;
        boolean isNUllOrEmpty=false;

        try
        {
            String line;
            List<String> lines=new ArrayList<String>();
            lines=Files.readAllLines(Paths.get(Data_Fille_path));
            String[] listarray=lines.toArray(new String[0]);
            for (int j=0; j<listarray.length; j++)
            {
                line=listarray[j];
                if (line.startsWith("ProjectNames"))
                {
                    line=line.substring(16, line.length()-1);
                    projectnems=line.split(", ");
                    for (int i=0; i<projectnems.length; i++)
                    {
                        projectnems[i]=projectnems[i].replaceAll("(\\r|\\n|\\t)", "");
                        name=name.replaceAll("(\\r|\\n|\\t)", "");
                        if (projectnems[i].equals(name))
                        {
                            isExisting=true;
                        }
                    }
                }
            }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        String text=menunewfilenamefield.getText();
        if (text.contains("/")||text.contains("<")||text.contains(">")||text.contains(":")||text.contains("\"")||text.contains("\\")||text.contains("|")||text.contains("?")||text.contains("*"))
        {
            isValid=false;
        }

        if (text.isBlank())
        {
            isNUllOrEmpty=true;
        }

        if (!isExisting&&isValid&&!isNUllOrEmpty)
        {
            String[] listarray=new String[0];
            try
            {
                int ProjectNamesCharIndex=0;
                
                // BufferedReader bf = new BufferedReader(new FileReader(Data_Fille_path));
                String line;
                List<String> lines=new ArrayList<String>();
                try
                {
                    lines=Files.readAllLines(Paths.get(Data_Fille_path));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                
                listarray=lines.toArray(new String[0]);                
                for (int j=0; j<listarray.length; j++)
                {
                    line=listarray[j];
                    if (listarray[j].startsWith("ProjectNames"))
                    {
                        ProjectNamesCharIndex=line.length()-1;
                        if (listarray[j].contains("||"))
                        {
                            listarray[j]=listarray[j].substring(0, ProjectNamesCharIndex)+name+listarray[j].substring(ProjectNamesCharIndex)+"\n";
                        }
                        else
                        {
                            listarray[j]=listarray[j].substring(0, ProjectNamesCharIndex)+", "+name+listarray[j].substring(ProjectNamesCharIndex)+"\n";
                        }
                    }
                    else if (listarray[j].startsWith("ProjectFilePaths"))
                    {
                        int ProjectPathsCharIndex=line.length()-1;
                        if (listarray[j].contains("||"))
                        {
                            listarray[j]=listarray[j].substring(0, ProjectPathsCharIndex)+menuStringDir+"\\"+name+listarray[j].substring(ProjectPathsCharIndex)+"\n";
                        }
                        else
                        {
                            listarray[j]=listarray[j].substring(0, ProjectPathsCharIndex)+", "+menuStringDir+"\\"+name+listarray[j].substring(ProjectPathsCharIndex)+"\n";
                        }
                    }
                    else if (listarray[j].startsWith("PrefferedProjectsPath"))
                    {
                        listarray[j]="PrefferedProjectsPath = "+menuStringDir+"\n";
                    }
                }
                FileWriter fw=new FileWriter(Data_Fille_path);
                if (listarray.length>0)
                {
                    fw.write("");
                }
                for (String i : listarray) 
                {
                    fw.append(i);
                }
                fw.close();   
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            try 
            {
                File f=new File(menuLabelLocaionPath.getText()+"\\"+name+".hopls");
                if (f.getParentFile().mkdirs())
                {
                    f.createNewFile();
                    FileWriter fw=new FileWriter(f);
                    fw.write("Hopeless version = "+version+"\nname = "+name+"\nLvl1ResizeWeight = 0.5\n");
                    fw.close();
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            OpenWindow(name, 0.5f, Paths.get(menuLabelLocaionPath.getText()+"\\"+name+".hopls"));
        }
        else if (isExisting)
        {
            UIManager.put("OptionPane.background", utils.DarkColor(0.1f));
            UIManager.put("Panel.background", utils.DarkColor(0.1f));
            UIManager.put("OptionPane.messageForeground", utils.highlight_color);
            JOptionPane err= new JOptionPane("Project name already Exists", JOptionPane.OK_OPTION);
            err.setMessageType(JOptionPane.ERROR_MESSAGE);
            JPanel buttonPanel = (JPanel)err.getComponent(1);
            JButton buttonOk = (JButton)buttonPanel.getComponent(0);
            buttonOk.setBackground(utils.highlight_color);
            buttonOk.setForeground(utils.DarkColor(0.1f));
            buttonOk.setBorderPainted(false);
            buttonOk.setFocusPainted(false);
            JDialog d=err.createDialog(null, "Jeeniyus! You're more hopeless than this...");
            d.setVisible(true);
        }
        else if (!isValid)
        {
            UIManager.put("OptionPane.background", utils.DarkColor(0.1f));
            UIManager.put("Panel.background", utils.DarkColor(0.1f));
            UIManager.put("OptionPane.messageForeground", utils.highlight_color);
            JOptionPane err= new JOptionPane("Enter ONLY valid characters (<, >, :, \", /, \\, |, ?, *)", JOptionPane.OK_OPTION);
            err.setMessageType(JOptionPane.ERROR_MESSAGE);
            JPanel buttonPanel = (JPanel)err.getComponent(1);
            JButton buttonOk = (JButton)buttonPanel.getComponent(0);
            buttonOk.setBackground(utils.highlight_color);
            buttonOk.setForeground(utils.DarkColor(0.1f));
            buttonOk.setBorderPainted(false);
            buttonOk.setFocusPainted(false);
            JDialog d=err.createDialog(null, "Jeeniyus! You're more hopeless than this...");
            d.setVisible(true);
        }
        else if (isNUllOrEmpty)
        {
            UIManager.put("OptionPane.background", utils.DarkColor(0.1f));
            UIManager.put("Panel.background", utils.DarkColor(0.1f));
            UIManager.put("OptionPane.messageForeground", utils.highlight_color);
            JOptionPane err= new JOptionPane("ENTER AN ACTUAL LETTER OR NUMBER!!!", JOptionPane.OK_OPTION);
            err.setMessageType(JOptionPane.ERROR_MESSAGE);
            JPanel buttonPanel = (JPanel)err.getComponent(1);
            JButton buttonOk = (JButton)buttonPanel.getComponent(0);
            buttonOk.setBackground(utils.highlight_color);
            buttonOk.setForeground(utils.DarkColor(0.1f));
            buttonOk.setBorderPainted(false);
            buttonOk.setFocusPainted(false);
            JDialog d=err.createDialog(null, "Jeeniyus! You're more hopeless than this...");
            d.setVisible(true);
        }
    }

    public static void BrowseFolderOpenFile()
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

        String path = GetPrefferedFilePath();

        JFileChooser JFC;
        JFC=new JFileChooser(path);
        JFC.setDialogTitle("Select the location of the next hope destroyer");
        // JFC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        JFC.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() 
            {
                return "*.hopls";
            }

            @Override
            public boolean accept(File f) 
            {
                if (f.isDirectory())
                {
                    return true;
                }
                return f.getName().endsWith(".hopls");
            }
        });
        JFC.setAcceptAllFileFilterUsed(false);
        
        int opt=JFC.showOpenDialog(menuwindow);
        try 
        {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) 
        {
            e.printStackTrace();
        }
        if (opt==JFileChooser.APPROVE_OPTION)
        {
            List<Object> fildata=utils.ExtractGameData(Paths.get(JFC.getCurrentDirectory().toString()+"\\"+JFC.getSelectedFile().getName()));
            OpenWindow((String)fildata.get(0), (float)fildata.get(1), (Path)fildata.get(2));
        }
    }

    public static void BrowseFolderNewFile()
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

        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
        {
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

        String path = GetPrefferedFilePath();

        JFileChooser JFC;
        JFC=new JFileChooser(path);
        JFC.setDialogTitle("Select the location of the next hope destroyer");
        JFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JFC.setAcceptAllFileFilterUsed(false);
        
        int opt=JFC.showOpenDialog(menuwindow);
        if (opt==JFileChooser.APPROVE_OPTION)
        {
            menuLabelLocaionPath.setText(JFC.getCurrentDirectory()+"\\"+JFC.getSelectedFile().getName()+"\\"+menunewfilenamefield.getText());
            menuStringDir=JFC.getCurrentDirectory()+"\\"+JFC.getSelectedFile().getName();
        }
        try 
        {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) 
        {
            e.printStackTrace();
        }
    }
    
    private static String GetPrefferedFilePath() 
    {
        String path=null;

        try (BufferedReader bf = new BufferedReader(new FileReader(Data_Fille_path))) 
        {
            String line;
            List<String> lines=new ArrayList<String>();
            lines=Files.readAllLines(Paths.get(Data_Fille_path));
            String[] listarray=lines.toArray(new String[0]);
            for (int j=0; j<listarray.length; j++)
            {
                line=listarray[j];
                if (line.startsWith("PrefferedProjectsPath"))
                {
                    line=line.replaceAll("(\\r|\\n|\\t)", "");
                    path=line.substring(24);
                    return path;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    }

    public static void SaveStuff()
    {
        utils.Save(FILEPATH, "Lvl1ResizeWeight", String.valueOf(Lvl1ResizeWeight));
        utils.FileIsSaved(Window);
    }

    public static void saveCodeedFileAuto(String txt)
    {
        String path = CODEFILEPATH+"\\"+CODEFILENAME;
        try 
        {
            FileWriter fw = new FileWriter(path);
            fw.write("");
            fw.write(txt);
            fw.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    // Proper window
    public static void OpenWindow(String name, float Lvl1ResizeWeightparam, Path path)
    {
        FILEPATH=path;
        Lvl1ResizeWeight=Lvl1ResizeWeightparam;
        menuwindow.dispose();

        String dirtocheck = FILEPATH.toFile().getParentFile().toPath().toString();
        File testfile = new File(dirtocheck+"\\Main.java");
        if (testfile.exists()==false)
        {
            try 
            {
                testfile.createNewFile();
                FileWriter fw = new FileWriter(testfile);
                fw.write("import Hopeless2D.*;\nimport java.awt.event.KeyEvent;\n\npublic class Main\n{\n    public Hopeless hp;\n    public void setup()\n    {\n        hp.FramesPerSecond=30; // Define the amount of FPS\n        // Runs before the game loop ;)\n        hp.run(); // Start game ;)\n    }\n    public void Frame()\n    {\n        // Runs every frame ;)\n    }\n    public void keyPressed(KeyEvent e)\n    {\n        // Code When a key is pressed\n    }\n    public void keyReleased(KeyEvent e)\n    {\n        // Code When a key is released\n    }\n    public void keyTyped(KeyEvent e)\n    {\n        // Code When a key is typed\n    }\n}");
                fw.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }

        UIManager.put("MenuItem.selectionForeground", utils.DarkColor(0.25f));
        UIManager.put("MenuItem.selectionBackground", utils.highlight_color);
        UIManager.put("MenuItem.acceleratorForeground", utils.highlight_highlight_color);
        UIManager.put("MenuItem.acceleratorSelectionForeground", utils.DarkColor(0.3f));
        UIManager.put("Menu.selectionForeground", utils.DarkColor(0.25f));
        UIManager.put("Menu.selectionBackground", utils.highlight_color);
        // get window size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int)screenSize.getWidth();
        height = (int)screenSize.getHeight();

        // Open window
        Window=new JFrame();
        Window.setSize(width, height);
        Window.setTitle(name);
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        Window.getContentPane().setBackground(utils.DarkColor(0.04f));

        JMenuBar Mb=InitializeMenu();
        Window.setJMenuBar(Mb);
        Window.setVisible(true);

        InitializeWindows(Lvl1ResizeWeight);
        utils.FileIsSaved(Window);
    }

    public static void InitializeWindows(float Lvl1ResizeWeightparam)
    {
        UIManager.put("SplitPane.background", utils.highlight_highlight_color);
        UIManager.put("SplitPane.dividerFocusColor", utils.highlight_highlight_color);
        CodeGameSplit = new JSplitPane(SwingConstants.VERTICAL, gameWindow, codeWindow);
        CodeGameSplit.addPropertyChangeListener(new PropertyChangeListener() 
        {

            @Override
            public void propertyChange(PropertyChangeEvent evt) 
            {
                int location = (int) CodeGameSplit.getDividerLocation();
                Lvl1ResizeWeight=utils.Number2Percentage(location, width);
                utils.FileIsUnsaved(Window);
                CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp = CodingWindow.openfile(CODEFILEPATH, CODEFILENAME);
                gameWindow.setSize(CodeGameSplit.getDividerLocation(), Window.getHeight());
                codeWindow.removeAll();
                gameWindow.removeAll();
                SetupGameWindow();
                SetupCodeWindow(CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp);
            }
            
        });
        CodeGameSplit.setUI(new BasicSplitPaneUI()
        {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() 
            {
                return new BasicSplitPaneDivider(this) 
                {                
                    public void setBorder(Border b) {}
                    
                    @Override
                    public void paint(Graphics g) 
                    {
                        g.setColor(utils.highlight_highlight_color);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        CodeGameSplit.setBackground(utils.highlight_highlight_color);
        CodeGameSplit.setForeground(utils.highlight_highlight_color);
        CodeGameSplit.setOrientation(SwingConstants.VERTICAL);
        CodeGameSplit.setDividerLocation(utils.Percentage2Number(Lvl1ResizeWeightparam, width));
        CodeGameSplit.setBorder(new EmptyBorder(0, 0, 0, 0));
        CodeGameSplit.setDividerSize(5);

        Dimension gdimension=new Dimension(utils.Percentage2Number(0.25f, width), height);
        gameWindow.setMinimumSize(gdimension);
        gameWindow.setBackground(utils.DarkColor(0.1f));
        gameWindow.setBorder(BorderFactory.createEmptyBorder());

        Dimension cdimension=new Dimension(utils.Percentage2Number(0.15f, width), height);
        codeWindow.setMinimumSize(cdimension);
        codeWindow.setBackground(utils.DarkColor(0.08f));
        codeWindow.setBorder(BorderFactory.createEmptyBorder());

        Window.add(CodeGameSplit);
        gameWindow.setSize(CodeGameSplit.getDividerLocation(), Window.getHeight());

    }

    public static JMenuBar InitializeMenu()
    {
        JMenuBar Mb= new JMenuBar();
        Mb.setBackground(utils.DarkColor(0.2f));
        Mb.setForeground(utils.highlight_highlight_color);
        Mb.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, utils.highlight_highlight_color));
        JMenu file=new JMenu("File");
        file.setForeground(utils.highlight_color);
        JMenu windows = new JMenu("Window");
        windows.setForeground(utils.highlight_color);
        JMenuItem[] fileItems = {new JMenuItem("Save"), new JMenuItem("Save as...")};
        int[] Mnemonics = {KeyEvent.VK_S, -1};
        KeyStroke[] Accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), null};
        String[] ActionCommands = {ActionList.SAVEFILE.name(), ActionList.SAVEAS.name()};
        for (int i = 0; i<fileItems.length; i++)
        {
            JMenuItem item=fileItems[i];
            item.setFont(utils.Verdana(12));
            item.setForeground(utils.highlight_color);
            item.setBackground(utils.DarkColor(0.15f));
            item.setActionCommand(ActionCommands[i]);
            item.addActionListener(new Listener());
            if (Mnemonics[i]!=-1)
            {
                item.setMnemonic(Mnemonics[i]);
            }
            if (Accelerators[i]!=null)
            {
                item.setAccelerator(Accelerators[i]);
            }
            file.add(item);
        }
        JMenuItem[] WindowItems = {new JMenuItem("Pixel Art")};
        String[] WindowActionCommands = {ActionList.PIXELART.name()};
        KeyStroke[] WindowsAccellerators = {KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK)};
        int[] windowsmnemonics = {-1};
        for (int i = 0; i < WindowItems.length; i++)
        {
            JMenuItem item=WindowItems[i];
            item.setFont(utils.Verdana(12));
            item.setForeground(utils.highlight_color);
            item.setBackground(utils.DarkColor(0.15f));
            item.setActionCommand(WindowActionCommands[i]);
            item.addActionListener(new Listener());
            if (Mnemonics[i]!=-1)
            {
                item.setMnemonic(windowsmnemonics[i]);
            }
            if (Accelerators[i]!=null)
            {
                item.setAccelerator(WindowsAccellerators[i]);
            }
            windows.add(item);
        }
        Mb.add(file);
        Mb.add(windows);
        return Mb;
    }

    public static void SetupCodeWindow(String CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp)
    {
        JLabel titlelabel= new JLabel("Title");
        titlelabel.setText("Code");
        Font f= utils.Verdana(15);
        titlelabel.setFont(f);
        titlelabel.setForeground(utils.highlight_color);
        titlelabel.setBounds(10, 10, 100, 10);
        codeWindow.add(titlelabel);
        int widthofcodearea = Window.getContentPane().getWidth() -  (int) CodeGameSplit.getDividerLocation();
        JPanel Buttons = new JPanel();
        Buttons.setBounds(10, 40, widthofcodearea, utils.Percentage2Number(0.6f, Window.getHeight()));
        Buttons.setLayout(new BoxLayout(Buttons, BoxLayout.X_AXIS));
        Buttons.setBackground(codeWindow.getBackground());
        OPButton OpenCodeButton = new OPButton("Open File (Code)");
        OpenCodeButton.setArcSize(15);
        OpenCodeButton.setFont(utils.Verdana(16));
        OpenCodeButton.setBorderPainted(false);
        OpenCodeButton.setFocusPainted(false);
        OpenCodeButton.setBackground(utils.highlight_color);
        OpenCodeButton.setHoverBackgroundColor(utils.highlight_color.brighter());
        OpenCodeButton.setPressedBackgroundColor(utils.highlight_highlight_color);
        OpenCodeButton.setActionCommand(ActionList.OPENCODEBUTTON.name());
        OpenCodeButton.addActionListener(new Listener());
        OPButton SaveCodeButton = new OPButton("Save File (Code)");
        SaveCodeButton.setArcSize(15);
        SaveCodeButton.setFont(utils.Verdana(16));
        SaveCodeButton.setBorderPainted(false);
        SaveCodeButton.setFocusPainted(false);
        SaveCodeButton.setBackground(utils.highlight_color);
        SaveCodeButton.setHoverBackgroundColor(utils.highlight_color.brighter());
        SaveCodeButton.setPressedBackgroundColor(utils.highlight_highlight_color);
        SaveCodeButton.addActionListener(new CodingWindow.SaveButtonListener());
        if (CODEFILENAME==null||CODEFILEPATH==null)
        {
            utils.DisableButton(SaveCodeButton);
        }
        Buttons.add(SaveCodeButton);
        Buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        Buttons.add(OpenCodeButton);
        codeWindow.setLayout(new BoxLayout(codeWindow, BoxLayout.Y_AXIS));
        widthofcodearea = Math.round(0.95f*widthofcodearea);
        codeWindow.add(Buttons);
        codeWindow.add(CodingWindow.SetupWindow(widthofcodearea, Window.getHeight(), CodeNotMineButTheUserOfMaGameEngineIKnowItsALongVariableNameButShutUp));
    }

    public static void SetupGameWindow()
    {
        JLabel titlelabel= new JLabel("Title");
        titlelabel.setText("Game Data");
        Font f= utils.Verdana(15);
        titlelabel.setFont(f);
        titlelabel.setForeground(utils.highlight_color);
        gameWindow.setLayout(new BorderLayout());
        titlelabel.setHorizontalAlignment(JLabel.CENTER);
        gameWindow.add(titlelabel, "North");
        JPanel CONTROLAREA = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        
        CONTROLAREA.setPreferredSize(new Dimension (gameWindow.getWidth(), utils.Percentage2Number(0.3f, Window.getHeight())));
        CONTROLAREA.setBackground(utils.highlight_highlight_color.darker().darker());
        CONTROLAREA.setLayout(new BorderLayout());
        CONTROLAREA.setBorder(new EmptyBorder(10, 10, 10, 10));
        OPButton RunButton = new OPButton("Run File");
        RunButton.setArcSize(15);
        RunButton.setFont(utils.Verdana(16));
        RunButton.setBorderPainted(false);
        RunButton.setFocusPainted(false);
        RunButton.setBackground(utils.highlight_color);
        RunButton.setHoverBackgroundColor(utils.highlight_color.brighter());
        RunButton.setPressedBackgroundColor(utils.highlight_highlight_color);
        RunButton.setActionCommand(ActionList.SHOWGAMEWINDOW.name());
        RunButton.addActionListener(new Listener());
        RunButton.setPreferredSize(new Dimension(utils.Percentage2Number(0.2f, CONTROLAREA.getPreferredSize().height), utils.Percentage2Number(0.3f, CONTROLAREA.getPreferredSize().width)));
        RunButton.setMaximumSize(new Dimension(utils.Percentage2Number(0.2f, CONTROLAREA.getPreferredSize().height), utils.Percentage2Number(0.3f, CONTROLAREA.getPreferredSize().width)));
        CONTROLAREA.add(RunButton, "South");  
        gameWindow.add(CONTROLAREA, "South");
        gameWindow.setBorder(new EmptyBorder(0, 10, 10, 10));
    }
}