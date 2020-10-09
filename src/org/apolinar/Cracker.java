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
            /*
            Scanner fReader2 = new Scanner(ShadowFile);
            while (fReader2.hasNextLine())
            {
                String data = fReader2.nextLine();
                SimpleShadow.add(data);
                //System.out.println(data);
            }
            fReader2.close();*/

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return localbuffer;
    }

    public static void main(String[] args)
    {
        // Testing string to byte output

        String str2 = new String("aaahhhhhhh");
        byte[] array1 = str2.getBytes();
        System.out.print("Testing String to Byte: ");
        for(byte b: array1){
            System.out.print(b);
        }


        // Creating MessageDigest using MD5

        try
        {
            // creating the object of MessageDigest
            // and getting instance
            // By using getInstance() method
            MessageDigest sr = MessageDigest.getInstance("MD5");
            byte[] digest = sr.digest(array1);
            System.out.println(toHex(digest));
            // getting the status of MessageDigest object
            String str = sr.toString();

            // printing the status
            System.out.println("Status : " + str);
        }
        catch (NoSuchAlgorithmException e)
        {

            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e)
        {

            System.out.println("Exception thrown : " + e);
        }


        // Loading common-passwords.txt and shadow-simple
        ArrayList<String> PasswordList = readFile("//input//common-passwords.txt");
        ArrayList<String> SimpleShadow = readFile("//input//shadow-simple");


        ArrayList<String> PasswordHashedList = new ArrayList<String>();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        // write your code here

        //MD5Shadow test = new MD5Shadow();
        System.out.println((MD5Shadow.crypt("aaa", "qtoUil1J")));
        System.out.println("-------------");
        System.out.println(PasswordList);
        PasswordList.forEach((n) -> PasswordHashedList.add(MD5Shadow.crypt(n, "123")));
        //System.out.println(PasswordHashedList);

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
        for (ArrayList<String> row : shadow_matrix)
        {
            System.out.println(("userid: " + row.get(0) + " salt: " + row.get(1) + " hash: " + row.get(2)));
        }

        // Break input string delimiter

        // Convert to crypto with salt and
    }
}
