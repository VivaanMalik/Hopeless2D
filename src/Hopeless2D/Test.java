package Hopeless2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Test
{
    public static void Log(String s)
    {
        try 
        {
            System.out.println(s);
            FileWriter fw = new FileWriter("D:\\Totally_normalstuff\\yup_told ya\\Ha! On yo face\\bruh\\hehe boi\\SECRET\\My_stuff\\geminjin\\Project Hopeless2D\\src\\Hopeless2D\\OutputTest.txt", true);
            BufferedWriter bf = new BufferedWriter(fw);
            bf.write(s);
            bf.newLine();
            bf.close();
        } 
        catch (IOException e) 
        {
            System.exit(0);
        }
    }
}
