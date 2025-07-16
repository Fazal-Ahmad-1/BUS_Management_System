import java.sql.*;
import java.util.Scanner;
import java.util.Random;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;


class Bus{
    static int bus_id;
    static String bus_no;
    static int seats;
    static double ticket_price;
    static String depot;
    static String start;
    static String destination;
    Bus(String bn,int s,double p,String d,String st,String des){
      //  bus_id=id;
        bus_no=bn;
        seats=s;
        ticket_price=p;
        depot=d;
        start=st;
        destination=des;
    }
};


class Booking{
    static String name;
    static int cst_id;
    static int rbus_id;
    static int status;
    static double price;
    Booking(String n,int b){
        this.name=n;
        this.cst_id=getCST();
        this.rbus_id=b;
        this.status=0;
        this.price=0.0;
    }
    public static int getCST() {
        Random rand = new Random();
        return rand.nextInt(900) + 100; // Generates number from 100 to 999
    }
};

public class BusSystem extends JFrame{
    private static String url="jdbc:mysql://localhost:3306/BusManagement";
    private static String user="root";
    private static String pass="fazal@123";
    static Scanner sc=new Scanner(System.in);
    static Connection connection=null;
    BusSystem(){
        this.setTitle("Pocket_BUS");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(500,750);
        this.setResizable(false);
        
        ImageIcon logo=new ImageIcon("busicon.jpg");
        this.setIconImage(logo.getImage());
        
        Border mainFrameBorder = BorderFactory.createLineBorder(new Color(1,17,10),10);
        JPanel mainframe=new JPanel();
        mainframe.setBackground(new Color(115, 186, 155));
        mainframe.setBorder(mainFrameBorder);
        mainframe.setLayout(null);
        mainframe.setOpaque(true);
        
        
        
        ImageIcon heroImg=new ImageIcon("busicon.png");
        JLabel herosection=new JLabel(heroImg);
        herosection.setBounds(150,50,200,200);
        herosection.setBackground(new Color(227, 209, 11));
        herosection.setHorizontalTextPosition(JLabel.CENTER);
        herosection.setVerticalTextPosition(JLabel.CENTER);
        
        JButton bookbutton=new JButton("Book a Bus");
        bookbutton.setFont(new Font("Consolas", Font.PLAIN, 29));
        bookbutton.setFocusable(false);
        bookbutton.setForeground(Color.white);
        bookbutton.setBackground(new Color(186, 45, 11));
        bookbutton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        bookbutton.addActionListener(e ->{newBooking();});/** Add event*/
        
        JButton busbutton=new JButton("Add a Bus");
        busbutton.setFont(new Font("Consolas", Font.PLAIN, 29));
        busbutton.setFocusable(false);
        busbutton.setForeground(Color.white);
        busbutton.setBackground(new Color(186, 45, 11));
        busbutton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        busbutton.addActionListener(e ->{addBus();});    
        
        JButton findbutton=new JButton("Find a Bus");
        findbutton.setFont(new Font("Consolas", Font.PLAIN, 29));
        findbutton.setFocusable(false);
        findbutton.setForeground(Color.white);
        findbutton.setBackground(new Color(186, 45, 11));
        findbutton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        findbutton.addActionListener(e ->{findBus();}); /** Add event*/
        
        JButton exitbutton=new JButton("Exit");
        exitbutton.setFont(new Font("Consolas", Font.PLAIN, 29));
        exitbutton.setFocusable(false);
        exitbutton.setForeground(Color.white);
        exitbutton.setBackground(new Color(186, 45, 11));
        exitbutton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        exitbutton.addActionListener(e ->{System.exit(0);});
        
        
        JPanel menuButtons=new JPanel();
        menuButtons.setLayout(new GridLayout(4,0,10,10));
        menuButtons.setBounds(100,280,300,400);
        menuButtons.setBackground(new Color(115, 186, 155));
        menuButtons.add(bookbutton);
        menuButtons.add(busbutton);
        menuButtons.add(findbutton);
        menuButtons.add(exitbutton);
        
        mainframe.add(menuButtons);
        mainframe.add(herosection);
        mainframe.setBorder(mainFrameBorder);
        this.add(mainframe,BorderLayout.CENTER);
        this.setVisible(true);
    }
    static void newBooking(){
    JFrame newbookingframe = new JFrame("New Booking Window");
    newbookingframe.setSize(350, 200);
    newbookingframe.setLayout(new FlowLayout());
    newbookingframe.getContentPane().setBackground(new Color(0, 62, 31));
    newbookingframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    newbookingframe.setResizable(false);

    JTextField tname = new JTextField("Enter customer name");
    tname.setPreferredSize(new Dimension(300, 30));
    tname.setFont(new Font("Consolas", Font.BOLD, 15));
    tname.setBackground(new Color(0xD5F2E3));
    tname.setForeground(new Color(0xBA2D0B));

    JTextField trbus_id = new JTextField("Enter requested bus_id");
    trbus_id.setPreferredSize(new Dimension(300, 30));
    trbus_id.setFont(new Font("Consolas", Font.BOLD, 15));
    trbus_id.setBackground(new Color(0xD5F2E3));
    trbus_id.setForeground(new Color(0xBA2D0B));
    
    JButton submitbutton = new JButton("SUBMIT");
    submitbutton.setPreferredSize(new Dimension(100, 40));
    submitbutton.setForeground(Color.white);
    submitbutton.setBackground(Color.orange);
    submitbutton.setFocusable(false);

    submitbutton.addActionListener(e -> {
        String name = tname.getText().trim();
        String idText = trbus_id.getText().trim();

        if (name.isEmpty() || idText.isEmpty() || name.equals("Enter customer name") || idText.equals("Enter requested bus_id")) {
            JOptionPane.showMessageDialog(newbookingframe, "Please enter valid name and bus ID.");
            return;
        }

        int rbus_id;
        try {
            rbus_id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(newbookingframe, "Bus ID must be a valid number.");
            return;
        }

        Booking b = new Booking(name, rbus_id);
        try {
            String query = "SELECT seats, ticket_price FROM bus WHERE bus_id=?";
            PreparedStatement idstatement = connection.prepareStatement(query);
            idstatement.setInt(1, rbus_id);
            ResultSet rs = idstatement.executeQuery();

            int s = 0;
            double p = 0;
            while (rs.next()) {
                s = rs.getInt("seats");
                p = rs.getDouble("ticket_price");
            }

            if (s > 0) {
                b.status = 1;
                b.price = p;
                query = "UPDATE bus SET seats = seats - 1 WHERE bus_id=?";
                PreparedStatement seatstatement = connection.prepareStatement(query);
                seatstatement.setInt(1, rbus_id);
                seatstatement.executeUpdate();
            }

            query = "INSERT INTO booking(name, cst_id, bus_id, status, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1, name);
            preparedstatement.setInt(2, b.cst_id);
            preparedstatement.setInt(3, rbus_id);
            preparedstatement.setInt(4, b.status);
            preparedstatement.setDouble(5, b.price);
            preparedstatement.executeUpdate();

            if (b.status == 1) {
                JOptionPane.showMessageDialog(newbookingframe,
                        "Booking Confirmed!\n\nName: " + b.name +
                        "\nCustomer ID: " + b.cst_id +
                        "\nBus ID: " + b.rbus_id +
                        "\nPrice: ₹" + b.price,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(newbookingframe, "No seats available or invalid Bus ID.");
            }

            newbookingframe.dispose(); 

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(newbookingframe, "Database Error: " + ex.getMessage());
        }
    });

    newbookingframe.add(tname);
    newbookingframe.add(trbus_id);
    newbookingframe.add(submitbutton);
    newbookingframe.setVisible(true);
    }
    
    static void addBus(){
        
        JFrame addbus_frame = new JFrame("Enter new bus details here");
        addbus_frame.setSize(350, 300);
        addbus_frame.setLayout(new FlowLayout());
        addbus_frame.getContentPane().setBackground(new Color(0, 62, 31));
        addbus_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addbus_frame.setResizable(false);
        
        JTextField tbusno = new JTextField("Enter Bus no. here");
        tbusno.setPreferredSize(new Dimension(300, 30));
        tbusno.setFont(new Font("Consolas", Font.BOLD, 15));
        tbusno.setBackground(new Color(0xD5F2E3));
        tbusno.setForeground(new Color(0xBA2D0B));
        
        JTextField tdepot = new JTextField("Enter Bus depot here");
        tdepot.setPreferredSize(new Dimension(300, 30));
        tdepot.setFont(new Font("Consolas", Font.BOLD, 15));
        tdepot.setBackground(new Color(0xD5F2E3));
        tdepot.setForeground(new Color(0xBA2D0B));
        
        JTextField tseats = new JTextField("Enter no. of seats here");
        tseats.setPreferredSize(new Dimension(300, 30));
        tseats.setFont(new Font("Consolas", Font.BOLD, 15));
        tseats.setBackground(new Color(0xD5F2E3));
        tseats.setForeground(new Color(0xBA2D0B));
        
        JTextField tticket_price = new JTextField("Enter ticket price here");
        tticket_price.setPreferredSize(new Dimension(300, 30));
        tticket_price.setFont(new Font("Consolas", Font.BOLD, 15));
        tticket_price.setBackground(new Color(0xD5F2E3));
        tticket_price.setForeground(new Color(0xBA2D0B));
        
        JTextField tstart = new JTextField("Enter start location here");
        tstart.setPreferredSize(new Dimension(300, 30));
        tstart.setFont(new Font("Consolas", Font.BOLD, 15));
        tstart.setBackground(new Color(0xD5F2E3));
        tstart.setForeground(new Color(0xBA2D0B));
        
        
        JTextField tdes = new JTextField("Enter destination here");
        tdes.setPreferredSize(new Dimension(300, 30));
        tdes.setFont(new Font("Consolas", Font.BOLD, 15));
        tdes.setBackground(new Color(0xD5F2E3));
        tdes.setForeground(new Color(0xBA2D0B));
        
        JButton submitbutton=new JButton("Submit");
        submitbutton.setPreferredSize(new Dimension(100, 40));
        submitbutton.setForeground(Color.white);
        submitbutton.setBackground(Color.orange);
        submitbutton.setFocusable(false);
        submitbutton.addActionListener(e ->{
        String bn=tbusno.getText();
        String dep=tdepot.getText();
        int se=Integer.valueOf(tseats.getText());
        double pr=Double.valueOf(tticket_price.getText());
        String st=tstart.getText();
        String des=tdes.getText();
        int bid=0;
        String q="INSERT INTO bus(bus_no,seats,ticket_price,depot,start,destination) VALUES(?,?,?,?,?,?)";
        try
        {
            PreparedStatement addbuss_statement=connection.prepareStatement(q);
            addbuss_statement.setString(1,bn);
            addbuss_statement.setInt(2,se);
            addbuss_statement.setDouble(3,pr);
            addbuss_statement.setString(4,dep);
            addbuss_statement.setString(5,st);
            addbuss_statement.setString(6,des);
            
            int r=addbuss_statement.executeUpdate();
            if(r>0)
            JOptionPane.showMessageDialog(addbus_frame,"Bus Added Successfully","Bus status", JOptionPane.INFORMATION_MESSAGE);
            else
            JOptionPane.showMessageDialog(addbus_frame,"Some Error occured","Bus status", JOptionPane.INFORMATION_MESSAGE);
            q="SELECT bus_id FROM bus where bus_no=?";
            PreparedStatement getBusID_statement=connection.prepareStatement(q);
            getBusID_statement.setString(1,bn);
            ResultSet id=getBusID_statement.executeQuery();
            while(id.next())bid=id.getInt("bus_id");
            
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        Bus nb=new Bus(bn,se,pr,dep,st,des);
        nb.bus_id=bid;
        addbus_frame.dispose();
        });
        
        addbus_frame.add(tbusno);
        addbus_frame.add(tseats);
        addbus_frame.add(tticket_price);
        addbus_frame.add(tdepot);
        addbus_frame.add(tstart);
        addbus_frame.add(tdes);
        addbus_frame.add(submitbutton);
        addbus_frame.setVisible(true);
        
    }

    static void findBus(){
        
    JFrame findbus_frame = new JFrame("New Booking Window");
    findbus_frame.setSize(350, 300);
    findbus_frame.setLayout(new BorderLayout());
    findbus_frame.getContentPane().setBackground(Color.lightGray);
    findbus_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    findbus_frame.setResizable(false);
    
    JPanel header=new JPanel(new GridLayout());
    header.setSize(350,100);
    header.setBackground(Color.black);
    

    JTextField tst = new JTextField("FROM");
    tst.setPreferredSize(new Dimension(300, 30));
    tst.setFont(new Font("Consolas", Font.BOLD, 15));
    tst.setBorder(BorderFactory.createLineBorder(new Color(0x003E1F),2));
    tst.setBackground(new Color(0xD5F2E3));
    tst.setForeground(new Color(0xBA2D0B));

    JTextField tdes = new JTextField("TO");
    tdes.setPreferredSize(new Dimension(300, 30));
    tdes.setBorder(BorderFactory.createLineBorder(new Color(0x003E1F),2));
    tdes.setFont(new Font("Consolas", Font.BOLD, 15));
    tdes.setBackground(new Color(0xD5F2E3));
    tdes.setForeground(new Color(0xBA2D0B));
    
    JTextArea searchresult=new JTextArea();
    searchresult.setBackground(new Color(0xD5F2E3));
    searchresult.setForeground(new Color(0xBA2D0B));
    searchresult.setBorder(BorderFactory.createLineBorder(new Color(0x003E1F),7));
    searchresult.setSize(300,190);
    searchresult.setEditable(false);
    searchresult.setFont(new Font("Consolas", Font.PLAIN, 14));
    searchresult.setLineWrap(true);
    searchresult.setWrapStyleWord(true);

    JButton submitbutton = new JButton("SUBMIT");
    submitbutton.setPreferredSize(new Dimension(100, 40));
    submitbutton.setForeground(Color.white);
    submitbutton.setBackground(Color.orange);
    submitbutton.setFocusable(false);
    submitbutton.addActionListener(e ->{
        String st=tst.getText();
        String des=tdes.getText();
        
        try
        {   PreparedStatement preparedstatement;
             String q;
            if(!st.equals("") && !des.equals("")){
            q="SELECT * from bus where start=? and destination=?";
            preparedstatement=connection.prepareStatement(q);
            preparedstatement.setString(1,st);
            preparedstatement.setString(2,des);}
            else if(!st.equals("")){
            q="SELECT * from bus where start=?";
            preparedstatement=connection.prepareStatement(q);
            preparedstatement.setString(1,st);
            }
            else{
            q="SELECT * from bus where destination=?";
            preparedstatement=connection.prepareStatement(q);
            preparedstatement.setString(1,des);
            }
            ResultSet rs=preparedstatement.executeQuery();
            int flag=0;
            String text="";
            while(rs.next()){
                flag=1;
                text+="Bus_no:"+rs.getString("bus_no")+" Seats available:"+rs.getInt("seats")+" Ticket_Price:"+rs.getDouble("ticket_price")+"\n\n";
            }
            searchresult.setText(text);
            if(flag==0)
            {
                JOptionPane.showMessageDialog(findbus_frame,"No bus available!");
            }
        }
        catch (SQLException sqle)
        {
            System.out.println(sqle.getMessage());
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Invalid Entry detected","Error Occured",JOptionPane.ERROR_MESSAGE);
        }
        });
        
        header.add(tst);
        header.add(tdes);
        header.add(submitbutton);
        
        findbus_frame.add(header,BorderLayout.NORTH);
        findbus_frame.add(searchresult,BorderLayout.CENTER);
        findbus_frame.setVisible(true);
    }
    
    static void main(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
        connection=DriverManager.getConnection(url,user,pass);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
       new BusSystem();
    }
    
};
