package sample;

import javafx.stage.DirectoryChooser;

import java.io.*;

/**
 * Created by Irfaan on 30/03/2016.
 */

public class CsvReadWrite {
    //loads default settings
    public static String[] CsvRead(String dir){
        String csvFile = dir;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String[] settings = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                //username, first name, last name, major
                settings = line.split(cvsSplitBy);
                System.out.println("Username: " + settings[0] + "\n"
                        + "Major:" + settings[3]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Imported Existing user settings.");
        return settings;
    }
    //overwrites default settings
    public static void CsvWrite(String user, String firstName,String lastName,String major){
        try{
        String outfile = "C:\\Users\\Irfaan\\IdeaProjects\\ChatProgramTest\\Settings\\settings.csv";
        FileWriter writer = new FileWriter(outfile);
        writer.append(user);
        writer.append(",");
        writer.append(firstName);
        writer.append(",");
        writer.append(lastName);
        writer.append(",");
        writer.append(major);
        writer.append("\n");
        }catch(IOException e){
            e.printStackTrace();
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current dir using System:" +currentDir);
        }
    }
}
