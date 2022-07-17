import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class MainFile
{
    public static void main(String args[])
    {
        File datafile = new File(System.getenv("APPDATA")+"Local\\Hopeless2D\\data.hopelessdata");
        if (!datafile.exists())
        {
            if (datafile.getParentFile().mkdirs())
            {
                try 
                {
                    datafile.createNewFile();
                    FileWriter fw = new FileWriter(datafile);
                    fw.write("ProjectNames = ||\nProjectFilePaths = ||\nPrefferedProjectsPath = "+FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString());
                    fw.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
        windows.OpenMenuWindow();
    }
}