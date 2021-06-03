import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class log_processing {

    public static String parseLogFile(String filename) {
        String averageTS = "";
        String averageTJ = "";
        long TS = 0;
        long TJ = 0;
        double count = 0;
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String split[]= myReader.nextLine().split(" ");
                TS += Long.parseLong(split[0]);
                TJ += Long.parseLong(split[1]);
                count++;
            }
            averageTS = String.valueOf(TS/count);
            averageTJ = String.valueOf(TJ/count);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "sum TS: " + TS + " sum TJ: " + TJ + " count: " + count;
    }

    public static void main(String[] args) {

        System.out.println(parseLogFile("logs.txt"));
    }

}
