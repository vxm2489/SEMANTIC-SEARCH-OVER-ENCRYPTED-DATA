import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;
class EncFrame extends JFrame implements ActionListener
{
JButton b1,b2,b3;
JLabel l1,l2,l3;
JTextArea ta;
JScrollPane sp1;
ImageIcon img1;
EncFrame()
{ 
Container c=getContentPane();
c.setLayout(null);
ta=new JTextArea();
img1=new ImageIcon("download(2).jpg");
//sp1=new JScrollPane(ta,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
sp1=new JScrollPane(ta);
sp1.setVisible(false);

l1=new JLabel("Semantic Search");
l1.setForeground(Color.blue);
l1.setFont(new Font("arial",Font.BOLD,20));
l1.setBounds(50,50,300,30);
l2=new JLabel("Files are Encrypted and saved in d:\\enc_datasets");
l3=new JLabel("Loaded Encrypted Data");
l2.setForeground(Color.red);
l3.setForeground(Color.red);
l2.setVisible(false);
l2.setBounds(400,120,400,30);
sp1.setBounds(500,180,800,400);
l3.setVisible(false);
l3.setBounds(350,170,350,30);
b1 = new JButton ("Encrypt Files ");
b2=new JButton("Load Encrypted Data ");
b1.setBounds (100, 120, 200, 40);
b2.setBounds (100, 170, 200, 40);
b1.addActionListener(this);
b2.addActionListener(this);
b3=new JButton("Search");
b3.addActionListener(this);
b3.setBounds(540,620,200,40);
b3.setIcon(img1);
c.add (b1);
c.add (b2);
c.add (l1);
c.add (l2);
c.add(l3);
c.add(sp1);
c.add(b3);
c.setBackground(Color.pink);
}//const
public void actionPerformed (ActionEvent ae)
{
if (ae.getSource()==b1)
{
    try
    {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/semantic","root","root");
        System.out.println("connected");
        Statement stmt=con.createStatement();
        int r1=stmt.executeUpdate("delete from data_enc1");
        System.out.println(r1 + " rows are deleted");
        File dir=new File("d:\\data sets");
        String names[]=dir.list();
        for(int i=0;i<names.length;i++)
        {
        FileInputStream fis=new FileInputStream("d:\\data sets\\"+names[i]);
        byte b[]=new byte[fis.available()];
        fis.read(b);
        String s=new String(b);
        //System.out.println(s);
        fis.close();
        String insertstr="insert into data_enc1 values(?,aes_encrypt(? ,555),?)";
        PreparedStatement psmt=con.prepareStatement(insertstr);
        psmt.setString(1,names[i]);
        psmt.setString(2,s);
        psmt.setString(3,s);
        int r=psmt.executeUpdate();
                
        if (r>0)
            System.out.println("inserted");
        else
            System.out.println("error insertion ");
        }//for
        //con.close();
        PreparedStatement psmt1=con.prepareStatement("select * from data_enc1");
        ResultSet rs=psmt1.executeQuery();
        File dir1=new File("d:\\enc_datasets");
        String names1[]=dir1.list();
        for(int i=0;i<names1.length;i++)
        {
            File fo1=new File("d:\\enc_datasets\\"+names1[i]);
        }
        while (rs.next())
        {
            FileWriter fw=new FileWriter("d:\\enc_datasets\\"+rs.getString(1));
            fw.write(rs.getString(2));
            fw.close();
        }
        con.close();
	l2.setVisible(true);
    }//try

catch(Exception ex)
{
    ex.printStackTrace();
}}//true
else if(ae.getSource()==b2)
	{
            try
            {
                       File dirs=new File("d:\\enc_datasets");
                       String fnames[]=dirs.list();
                       for(int i=0;i<fnames.length;i++)
                       {
                           FileInputStream fis=new FileInputStream("d:\\enc_datasets\\"+fnames[i]);
                           byte b[]=new byte[fis.available()];
                           fis.read(b);
                           String s=new String(b);
                           fis.close();
                           ta.append(s);
                                                     
                       }
                                   sp1.setVisible(true);       
	l3.setVisible(true);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }//if
else if (ae.getSource()==b3)
{
    SearchFrame sf1=new SearchFrame();
    sf1.setSize(600,400);
    sf1.setVisible(true);
}
}
public static void main(String args[])
{ 
EncFrame ob = new EncFrame ();
ob.setSize (1500,900);
ob.setTitle ("Encryption");
ob.setVisible (true);
ob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}