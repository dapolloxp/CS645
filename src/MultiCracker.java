import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files


public class MultiCracker
{
    public static ArrayList<String> readFile(String fileName)
    {
        ArrayList<String> localbuffer = new ArrayList<String>();
        try {
            // Try to read file
            File PassFile = new File(System.getProperty("user.dir") + fileName);

            // Create a new file scanner
            int line = 0;
            Scanner fReader = new Scanner(PassFile);
            while (fReader.hasNextLine())
            {
                String data = fReader.nextLine();
                line++;
                localbuffer.add(data);
                /*
                if (line % 1000000 == 0)
                {
                    System.out.println(("line: " + line));
                }*/
            }
            fReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return localbuffer;
    }





    public static void main(String[] args)
    {
        // Loading common-passwords.txt and shadow-simple
        ArrayList<String> PasswordList = new ArrayList<String>();
        ArrayList<String> Shadow = new ArrayList<String>();
        if(args.length < 1)
        {

            PasswordList = readFile("//common-passwords.txt");
            Shadow = readFile("//shadow");
        }
        else if(args.length == 2)
        {
            PasswordList = readFile("//" + args[0]);
            Shadow = readFile("//" + args[1]);
        }
        else
        {
            System.out.println("Error, usage: java ClassName password_file shadow_file");
            System.exit(1);
        }

        //System.out.println("File Loaded...\n\n");

        // 2-Dim ArrayList to store shadow file as a matrix
        ArrayList<ArrayList<String>> shadow_matrix = new ArrayList<>(10);

        for(String line : Shadow)
        {
            List<String> Shadow_line = Arrays.asList(line.split(":"));

            // Create Temp Row to add to the Matrix
            ArrayList<String> row = new ArrayList<>(Shadow_line.toArray().length);
            row.add(Shadow_line.toArray()[0].toString());

            String [] password_split = Shadow_line.toArray()[1].toString().split("\\$");
            row.add(password_split[2]); // salt
            row.add(password_split[3]); // crypt password

            // Add line
            shadow_matrix.add(row);
        }

        ForkJoinPool pool = new ForkJoinPool();

        // the threshold is set to 290000 but this value may change depending on the size of the file
        PasswordTransform computeMD5Task = new PasswordTransform(PasswordList, 290000, 0, PasswordList.size(), shadow_matrix);

        pool.invoke(computeMD5Task);



        /*do
        {
            //System.out.printf("******************************************\n");
           // System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
           // System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
           // System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
           // System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
           // System.out.printf("******************************************\n");
            try
            {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        } while ((!computeMD5Task.isDone()));*/

        pool.shutdown();
    }
}
