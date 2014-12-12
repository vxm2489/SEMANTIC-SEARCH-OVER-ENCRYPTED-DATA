import javax.swing.*;import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
public class SearchFrame extends JFrame implements ActionListener
{
JLabel l1;
JTextField tf1;
JButton b1;
JTextArea ta;
JScrollPane sp;
    SearchFrame()
    {
        Container c=getContentPane();
        c.setBackground(Color.lightGray);
        c.setLayout(null);
        ta=new JTextArea();
        sp=new JScrollPane(ta);
sp.setVisible(false);

        l1=new JLabel("Enter Word ");
        b1=new JButton("Search");
        tf1=new JTextField();
        c.add(l1);
        c.add(tf1);
        c.add(b1);
        c.add(sp);
 l1.setBounds(50,100,200,20); 
 tf1.setBounds(160,100,200,30);
 b1.setBounds(450,100,150,30);
 sp.setBounds(160,200,600,400);
 
 b1.addActionListener(this);
  }
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/semantic","root","root");
        System.out.println("connected");
        String s=tf1.getText();
       if (s.length()==0)
       {
           JOptionPane.showMessageDialog(this, "Enter Search Text");
          
       }
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
        int r2=ds2.executeUpdate("delete from analysis1");
        int r3=ds3.executeUpdate("delete from analysis2");


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
        Statement t1,t2,t3;
        t1=con.createStatement();
        t2=con.createStatement();
        t3=con.createStatement();
        String q1,q2,q3;
        ResultSet rr1,rr2,rr3;
        q1="select * from analysis order by wcount desc ";
        q2="select * from analysis1 order by wcount desc";
        q3="select * from analysis2 order by wcount desc";
        rr1=t1.executeQuery(q1);
        rr2=t2.executeQuery(q2);
        rr3=t3.executeQuery(q3);
        ta.setText(null);
        ta.append("\n\n");
        ta.setFont(new Font("arial",Font.BOLD,20));
        ta.setForeground(Color.blue);
        //ta.append("Original Word Search Results \n");
        while(rr1.next())
        {
          
            ta.append(rr1.getString(1).trim()+"\t\t"+rr1.getString(2)+"\n");
                }
     //   ta.append("\n\nSynonym1 Search Results \n");
        while(rr2.next())
        {
            ta.append(rr2.getString(1).trim()+"\t\t"+rr2.getString(2)+"\n");

        }
       // ta.append("\n\nSynonym2 Search Results \n");
        while(rr3.next())
        {
            ta.append(rr3.getString(1).trim()+"\t\t"+rr3.getString(2)+"\n");

        }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        sp.setVisible(true);

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SearchFrame sf1=new SearchFrame();
    sf1.setSize(600,400);
    sf1.setVisible(true);
    }

}
