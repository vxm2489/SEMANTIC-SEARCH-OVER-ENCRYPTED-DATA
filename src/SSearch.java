import java.io.*;
import java.util.*;
import java.sql.*;
public class SSearch
{
    public static void main(String[] args) throws Exception
    {
        // TODO code application logic here
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/semantic","root","root");
        System.out.println("connected");
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Word");
        String s=sc.next();
        Statement stmt=con.createStatement();
        String qstr1="select * from synonyms where word='"+ s+"'";
        ResultSet rs2=stmt.executeQuery(qstr1);
        String s1=null,s2=null;
        if (rs2.next())
        {
            s1=rs2.getString(2);//synonym1
            s2=rs2.getString(3);//synonym2
        }
        PreparedStatement psmt=con.prepareStatement("select fn,odata from data_enc1");
        ResultSet rs=psmt.executeQuery();
        String fn=null;
        Vector v1=null;
        int c=0;
        int c1=0,c2=0;

         v1=new Vector();
         Vector v2=new Vector();
         Vector vs1=new Vector();
         Vector vs11=new Vector();
         Vector vs2=new Vector();
         Vector vs21=new Vector();


         String odata;
         //exact word count logic
        while(rs.next())
        {
           odata=rs.getString(2);
            fn=rs.getString(1);
            StringTokenizer stk1=new StringTokenizer(odata);
            while(stk1.hasMoreTokens())
            {
                String t=stk1.nextToken();
                if (t.equals(s))
                {
                    //System.out.println(fn+t);
                    c++;
                }

               
            }//inner while
            if (c!=0)
            {
            v1.add(fn);
                v2.add(c);
            }
            //synword1 count logic
            StringTokenizer stk2=new StringTokenizer(odata);
            while(stk2.hasMoreTokens())
            {
                String t=stk2.nextToken();
                if (t.equals(s1))
                {
                    //System.out.println(fn+t);
                    c1++;
                }


            }//inner while
            if (c1!=0)
            {
            vs1.add(fn);
                vs11.add(c1);
            }
            //synword2 logic
            StringTokenizer stk3=new StringTokenizer(odata);
            while(stk3.hasMoreTokens())
            {
                String t=stk3.nextToken();
                if (t.equals(s2))
                {
                    //System.out.println(fn+t);
                    c2++;
                }


            }//inner while
            if (c2!=0)
            {
            vs2.add(fn);
                vs21.add(c2);
            }

        }//outer while
        System.out.println(v1.size());
        System.out.println(v2.size());
        /*for(Object o:v1)
            System.out.println(o);
        for(Object o:v2)
            System.out.println(o);*/
        //original word
        Statement ds1,ds2,ds3;
        ds1=con.createStatement();
        ds2=con.createStatement();
        ds3=con.createStatement();
        int r1=ds1.executeUpdate("delete from analysis");
        int r2=ds1.executeUpdate("delete from analysis1");
        int r3=ds1.executeUpdate("delete from analysis2");


        String qstr="insert into analysis values(?,?)";
        PreparedStatement psmt1=con.prepareStatement(qstr);
        for(int i=0;i<v1.size();i++)
        {
            psmt1.setString(1, v1.elementAt(i).toString());//fn of word
            psmt1.setInt(2, Integer.parseInt(v2.elementAt(i).toString()));//count of word
            int r=psmt1.executeUpdate();
        }
        //synonym1
        String qstr3="insert into analysis1 values(?,?)";
        PreparedStatement psmt2=con.prepareStatement(qstr3);
        for(int i=0;i<vs1.size();i++)
        {
            psmt2.setString(1, vs1.elementAt(i).toString());//fn of syn1
            psmt2.setInt(2, Integer.parseInt(vs11.elementAt(i).toString()));//count of syn1
            int r=psmt2.executeUpdate();
        }
        //synonym1
        String qstr4="insert into analysis2 values(?,?)";
        PreparedStatement psmt3=con.prepareStatement(qstr4);
        for(int i=0;i<vs2.size();i++)
        {
            psmt3.setString(1, vs2.elementAt(i).toString());//fn of syn2
            psmt3.setInt(2, Integer.parseInt(vs21.elementAt(i).toString()));//count of syn2
            int r=psmt3.executeUpdate();
        }
      }
}
