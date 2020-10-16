// David Apolinar and Ted Moore
// CS645 - Fall 2020


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicLong;

class PasswordTransform extends RecursiveAction {
    //int[] array;
    int number;
    //int threshold = 100;
    int threshold = 2;
    int start;
    int end;
    ArrayList<String> chunked_password_list;
    ArrayList<ArrayList<String>> shadow_matrix;
    Hashtable<String, String> Password_Hash_Table = new Hashtable<String, String>();

    public PasswordTransform(ArrayList<String> password_list_in, int threshold, int start, int end, ArrayList<ArrayList<String>> shadow_matrix) {
        this.chunked_password_list = password_list_in;
        this.number = number;
        this.start = start;
        this.end = end;
        this.shadow_matrix = shadow_matrix;
        this.threshold = threshold;
    }

    protected void compute() {
        // If the Password Files lines are  under the threshold, do the work

        if (end - start < threshold)
        {
            try
            {
                computeDirectly();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        } else // split it further
        {
            int middle = this.chunked_password_list.size() / 2;
            int end = this.chunked_password_list.size();
            //System.out.println("Current Size: " + this.chunked_password_list.size() + " middle: " + middle + " end: " + end);
            ArrayList<String> temp1 = new ArrayList<String>(this.chunked_password_list.subList(0, middle));
            ArrayList<String> temp2 = new ArrayList<String>(this.chunked_password_list.subList(middle, end));
            PasswordTransform computeMD5Task_s1 = new PasswordTransform(temp1, 4, 0, middle, this.shadow_matrix);
            PasswordTransform computeMD5Task_s2 = new PasswordTransform(temp2, 4, middle, end, this.shadow_matrix);

            invokeAll(computeMD5Task_s1, computeMD5Task_s2);

        }
    }

    protected void computeDirectly() throws NoSuchAlgorithmException
    {

        AtomicLong count = new AtomicLong();
        this.chunked_password_list.forEach((n) ->
        {
            //System.out.println("processing " + n);
            for (ArrayList<String> row : shadow_matrix) //user
            {
                try
                {
                    // if row (user) has equals password hashed, output to screen
                    String password_n_hash = toHash(MD5Shadow.crypt(n, row.get(1)));
                    String user_shadow_hash = toHash(row.get(2));
                    if(password_n_hash.equals(user_shadow_hash))
                    {
                        System.out.println(row.get(0) + ":" + n);
                    }

                } catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }

            }
        });


    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static String toHash(String msg_in) throws NoSuchAlgorithmException {
        byte[] byte_array = msg_in.getBytes();
        String hashed_string = new String();

        try {
            // creating the object of MessageDigest and get instance by using getInstance method
            MessageDigest sr = MessageDigest.getInstance("MD5");
            byte[] digest = sr.digest(byte_array);

            // getting the status of MessageDigest object
            String str = sr.toString();

            hashed_string = toHex(digest);

        } catch (NoSuchAlgorithmException e) {

            System.out.println("Exception thrown : " + e);
        } catch (NullPointerException e) {

            System.out.println("Exception thrown : " + e);
        }
        return hashed_string;

    }
}
