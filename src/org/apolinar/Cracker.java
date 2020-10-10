package org.apolinar;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.MessageFormat;
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
    /*
    static String theHash(String message) throws NoSuchAlgorithmException {
        if (null == messageHash)
            messageHash = MessageDigest.getInstance("MD5");
        byte[] digest = messageHash.digest(message.trim().getBytes());
        String rv = toHex(digest);
        return rv;
    }*/

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
            // creating the object of MessageDigest
            // and getting instance
            // By using getInstance() method
            MessageDigest sr = MessageDigest.getInstance("MD5");
            byte[] digest = sr.digest(byte_array);
            //System.out.println(toHex(digest));
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

    public static ArrayList<String> readFile(String fileName)
    {
        ArrayList<String> localbuffer = new ArrayList<String>();
        try {
            File PassFile = new File(System.getProperty("user.dir") + fileName);
            File ShadowFile = new File(System.getProperty("user.dir") + "//input//shadow-simple");
            //System.out.println("file_name: " + PassFile);
            //System.out.println("shadow file: " + ShadowFile);
            Scanner fReader = new Scanner(PassFile);
            while (fReader.hasNextLine())
            {
                String data = fReader.nextLine();
                localbuffer.add(data);
                //System.out.println(data);
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

        // Testing MD5 Hashing


        // Loading common-passwords.txt and shadow-simple
        ArrayList<String> PasswordList = readFile("//input//common-passwords.txt");
        ArrayList<String> SimpleShadow = readFile("//input//shadow-simple");


        ArrayList<String> PasswordHashedList = new ArrayList<String>();
        Hashtable<String, String> Password_Hash_Table = new Hashtable<String, String>();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        // write your code here

        //MD5Shadow test = new MD5Shadow();
        //System.out.println((MD5Shadow.crypt("aaa", "qtoUil1J")));
        //System.out.println("-------------");
       // System.out.println(PasswordList);

        // Generate password to MD5 table


        //System.out.println(Password_Hash_Table.get("D599EAE7A636D54C1C707514B1A76D77") );
        //System.out.println(Password_Hash_Table.containsKey("D599EAE7A636D54C1C707514B1A76D77"));

        // Get Simple Shadow File

        // 2-Dim ArrayList  to store shadow file as a matrix
        ArrayList<ArrayList<String>> shadow_matrix = new ArrayList<>(10);

        //ArrayList<String> listOfStrings = new ArrayList<String>(SimpleShadow.size());
        //ArrayList<ArrayList<String>> shadow_matrix = new ArrayList<>(SimpleShadow.size());
        for(String line : SimpleShadow)
        {
            //System.out.println(val.split(":"));
            List<String> Shadow_line = Arrays.asList(line.split(":"));

            // Create Temp Row to add to the Matrix
            ArrayList<String> row = new ArrayList<>(Shadow_line.toArray().length);
            row.add(Shadow_line.toArray()[0].toString());
            row.add(Shadow_line.toArray()[1].toString());
            row.add(Shadow_line.toArray()[2].toString());
            shadow_matrix.add(row);

        }

        // Create Table using salts

        PasswordList.forEach((n) ->
        {
            try
            {
                for (ArrayList<String> row : shadow_matrix)
                {
                    Password_Hash_Table.put(toHash(row.get(1) + n), n);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        ArrayList<String> passwordDict = new ArrayList<String>();
        ArrayList<String> hashes = new ArrayList<String>();


        /*
        for (ArrayList<String> row : shadow_matrix)
        {
            //System.out.println(("userid: " + row.get(0) + " salt: " + row.get(1) + " hash: " + row.get(2)));
            System.out.println("Adding: " + toHash(MD5Shadow.crypt(row.get(1), row.get(2))));
            hashes.add(toHash(MD5Shadow.crypt(row.get(1), row.get(2))));
        }*/

        //System.out.println(hashes);

        for (ArrayList<String> row : shadow_matrix)
        {

            //System.out.println("Checking hash " + row.get(2));
            if (Password_Hash_Table.containsKey(row.get(2)))
            {
                System.out.println("Found User Password: " + "Hash: " + row.get(2) + " --> " + Password_Hash_Table.get(row.get(2)));
            }
            //hashes.add(row.get(1));
        }



    }
}
