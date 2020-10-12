// David Apolinar and Ted Moore
// CS645 - Fall 2020

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files
import java.security.*;
import java.util.*;

public class SimpleCracker
{
    // Compute Hex
    public static String toHex(byte[] bytes)
    {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    // Create Hash
    public static String toHash(String msg_in) throws NoSuchAlgorithmException
    {
        byte[] byte_array = msg_in.getBytes();
        String hashed_string = new String();

        try
        {
            // creating the object of MessageDigest and get instance by using getInstance method
            MessageDigest sr = MessageDigest.getInstance("MD5");
            byte[] digest = sr.digest(byte_array);

            // getting the status of MessageDigest object
            String str = sr.toString();

            // conver to Hex string
            hashed_string = toHex(digest);

        }
        catch (NoSuchAlgorithmException e)
        {

            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e)
        {

            System.out.println("Exception thrown : " + e);
        }
        return hashed_string;

    }

    // Load File
    public static ArrayList<String> readFile(String fileName)
    {
        ArrayList<String> localbuffer = new ArrayList<String>();
        try {
            // Try to read file
            File PassFile = new File(System.getProperty("user.dir") + fileName);

            // Create a new file scanner
            Scanner fReader = new Scanner(PassFile);
            while (fReader.hasNextLine())
            {
                String data = fReader.nextLine();
                localbuffer.add(data);
            }
            fReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return localbuffer;
    }

    public static void main(String[] args) throws IOException
    {

        // Loading common-passwords.txt and shadow-simple
        ArrayList<String> PasswordList = new ArrayList<String>();
        ArrayList<String> SimpleShadow = new ArrayList<String>();
        if(args.length < 1)
        {

            PasswordList = readFile("//common-passwords.txt");
            SimpleShadow = readFile("//shadow-simple");
        }
        else if(args.length == 2)
        {
            PasswordList = readFile(args[0]);
            SimpleShadow = readFile(args[1]);
        }
        else
        {
            System.out.println("Error, usage: java ClassName password_file shadow_file");
            System.exit(1);
        }


        //ArrayList<String> PasswordHashedList = new ArrayList<String>();
        Hashtable<String, String> Password_Hash_Table = new Hashtable<String, String>();

        // 2-Dim ArrayList to store shadow file as a matrix
        ArrayList<ArrayList<String>> shadow_matrix = new ArrayList<>(10);


        // Create Shadow Matrix File User:Salt:Password per row
        for(String line : SimpleShadow)
        {
            List<String> Shadow_line = Arrays.asList(line.split(":"));

            // Create Temp Row to add to the Matrix
            ArrayList<String> row = new ArrayList<>(Shadow_line.toArray().length);
            row.add(Shadow_line.toArray()[0].toString());
            row.add(Shadow_line.toArray()[1].toString());
            row.add(Shadow_line.toArray()[2].toString());

            // Add line
            shadow_matrix.add(row);
        }

        // Create rainbow table using salts
        PasswordList.forEach((n) ->
        {
            try
            {
                for (ArrayList<String> row : shadow_matrix)
                {
                    // Add hash using each user's salt
                    Password_Hash_Table.put(toHash(row.get(1) + n), n);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        // Check each shadow entry against our rainbow table
        for (ArrayList<String> row : shadow_matrix)
        {
            if (Password_Hash_Table.containsKey(row.get(2)))
            {
                System.out.println("Found user password for " + row.get(0) +  " --> " + Password_Hash_Table.get(row.get(2)));
            }
        }
    }
}
