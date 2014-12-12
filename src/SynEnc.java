/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author NEW
 */
import java.sql.*;
import java.io.*;
public class SynEnc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        // TODO code application logic here
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/semantic","root","root");
        //System.out.println("connected");
        Statement stmt=con.createStatement();
        String delstr="delete from synonyms_enc";
        int r=stmt.executeUpdate(delstr);
        if (r>0)
            System.out.println("data been deleted");
        else
            System.out.println("No rows are deleted");

        PreparedStatement psmt1=con.prepareStatement("select aes_encrypt(word,555),aes_encrypt(sn1,555),aes_encrypt(sn2,555) from synonyms");
        ResultSet rs=psmt1.executeQuery();
        PreparedStatement psmt2=con.prepareStatement("insert into synonyms_enc values(?,?,?)");
        String ec="";
        while(rs.next())
        {
            String s1,s2,s3;
            s1=rs.getString(1);
            s2=rs.getString(2);
            s3=rs.getString(3);
            psmt2.setString(1, s1);
            psmt2.setString(2, s2);
            psmt2.setString(3, s3);
            int r1=psmt2.executeUpdate();
            ec=ec+s1+s2+s3+"\n";
     
        }
        System.out.println("data been encrypted");
        FileWriter fw=new FileWriter("d:\\syn_enc.txt");
        fw.write(ec);
        fw.close();
        System.out.println("encrypted synonym file is created");
    }

}
