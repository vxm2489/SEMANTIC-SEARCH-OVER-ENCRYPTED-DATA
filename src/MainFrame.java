import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
class MainFrame extends JFrame implements ActionListener
{
JButton b1,b2;
String s1="";
JLabel l1,l2;
JList ls1;
MainFrame()
{ 
Container c=getContentPane();
	c.setLayout (null);

File dir=new File("d:\\data sets");
//File[] files=dir.listFiles();
String names[]=dir.list();
ls1=new JList(names);
ls1.setVisible(false);
l1=new JLabel("Semantic Search ");
l1.setForeground(Color.blue);
l1.setFont(new Font("arial",Font.BOLD,20));
l1.setBounds(50,50,300,30);
l2=new JLabel("Files been Loaded ...");
l2.setForeground(Color.red);
l2.setVisible(false);
l2.setBounds(260,120,150,30);
b1 = new JButton ("Load Files");
b2=new JButton("Proceed");
b1.setBounds (100, 120, 105, 40);
b2.setBounds (100, 170, 105, 40);
ls1.setBounds(450,120,200,200);
b1.addActionListener(this);
b2.addActionListener(this);
c.add (b1);
c.add (b2);
c.add (l1);
c.add (l2);
c.add(ls1);
c.setBackground(Color.cyan);
}
public void actionPerformed (ActionEvent ae)
{

if (ae.getSource()==b1)
{
l2.setVisible(true);
ls1.setVisible(true);
}
else if(ae.getSource()==b2)
	{
		EncFrame enc=new EncFrame();
		enc.setSize (1500,900);
        enc.setTitle ("Encryption");
        enc.setVisible (true);
        
	}
}
public static void main(String args[])
{ 
MainFrame ob = new MainFrame ();
ob.setSize (1500,900);
ob.setTitle ("Semantic Search");
ob.setVisible (true);
ob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}