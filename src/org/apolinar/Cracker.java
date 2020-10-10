package org.apolinar;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files
import java.security.*;
import java.util.*;



public class Cracker
{

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

        //System.out.print("Testing String to Byte: ");
        /*for(byte b: byte_array){
            System.out.print(b);
        }*/
        try
        {
            // creating the object of MessageDigest and get instance by using getInstance method

            MessageDigest sr = MessageDigest.getInstance("MD5");
            byte[] digest = sr.digest(byte_array);

            // getting the status of MessageDigest object
            String str = sr.toString();

            // printing the status
            //System.out.println("Status : " + str);
            hashed_string = toHex(digest);
            //System.out.println("digest : " + toHex(digest));

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

    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        //System.out.println("test");
        ArrayList<String> PasswordList = readFile("//input//common-passwords.txt");
        ArrayList<String> Shadow = readFile("//input//shadow");

        //System.out.println(Shadow);

        //ArrayList<String> PasswordHashedList = new ArrayList<String>();
        Hashtable<String, String> Password_Hash_Table = new Hashtable<String, String>();

        // 2-Dim ArrayList to store shadow file as a matrix
        ArrayList<ArrayList<String>> shadow_matrix = new ArrayList<>(10);

        for(String line : Shadow)
        {
            List<String> Shadow_line = Arrays.asList(line.split(":"));

            // Create Temp Row to add to the Matrix
            ArrayList<String> row = new ArrayList<>(Shadow_line.toArray().length);
            row.add(Shadow_line.toArray()[0].toString());
            //row.add(Shadow_line.toArray()[1].toString());
            //row.add(Shadow_line.toArray()[2].toString());
            // Add line
            String [] password_split = Shadow_line.toArray()[1].toString().split("\\$");
            //System.out.println("salt: " + password_split[2] + " hash: " + password_split[3]);
            row.add(password_split[2]);
            row.add(password_split[3]);
            shadow_matrix.add(row);
        }
        //System.out.println((shadow_matrix.get(0).get(1)));
        //System.out.println((shadow_matrix.get(0).get(2)));
        //System.out.println(shadow_matrix);
        //System.out.println(toHash(MD5Shadow.crypt(shadow_matrix.get(0).get(1), shadow_matrix.get(0).get(2))));

        PasswordList.forEach((n) ->
        {
            for (ArrayList<String> row : shadow_matrix)
            {
                // Add hash using each user's salt
                //System.out.println("salt " + row.get(1) + " string " + n + " computed hash " + toHash(MD5Shadow.crypt(row.get(1), n)));
                try {
                    Password_Hash_Table.put( toHash(MD5Shadow.crypt(n, row.get(1))), n);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        //System.out.println(Password_Hash_Table);
        // Check each shadow entry against our rainbow table

        for (ArrayList<String> row : shadow_matrix)
        {
            //System.out.println("Checking hash " + toHash(row.get(2)));
            //System.out.println("Checking hash " + toHash(MD5Shadow(row.get(1))));
            if (Password_Hash_Table.containsKey(toHash(row.get(2))))
            {
                System.out.println("Found Password for " + row
                        .get(0) + ": " + Password_Hash_Table.get(toHash(row.get(2))));
            }
            else
            {
                System.out.println("Did not find password for " + row.get(0));
            }
        }
    }
}

