import java.sql.*;
import java.util.Scanner;
import java.util.Random;

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
    public static void printBill(){
        System.out.println("Your Booking Details:");
        System.out.println("NAME: "+ name);
        System.out.println("BUS_ID: "+rbus_id);
        System.out.println("CUSTOMER_ID: "+cst_id);
        System.out.println("Total Price: "+price); 
        System.out.println("========Happy Journey=========");
    }
};


public class BusSystem{
    private static String url="jdbc:mysql://localhost:3306/BusManagement";
    private static String user="root";
    private static String pass="fazal@123";
    static Scanner sc=new Scanner(System.in);
    static Connection connection=null;
    static Booking newBooking(){
        System.out.print("Enter name:");
        String name=sc.next();
        System.out.print("Enter requested bus_id:");
        int rbus_id=sc.nextInt();
        Booking b=new Booking(name,rbus_id);
        try{
        String query="SELECT seats,ticket_price from bus where bus_id=?";
        PreparedStatement idstatement=connection.prepareStatement(query);
        idstatement.setInt(1,rbus_id);
        ResultSet rs=idstatement.executeQuery();
        int s=0;double p=0;
        while(rs.next()){s=rs.getInt("seats");p=rs.getDouble("ticket_price");}
        
        if(s>0){
            b.status=1;// Seat Confirmed
            b.price=p;
            query="UPDATE bus SET seats=seats-1 where bus_id=?";
            PreparedStatement seatstatement=connection.prepareStatement(query);
            seatstatement.setInt(1,rbus_id);
            seatstatement.executeUpdate();
        }
        
        query="INSERT INTO booking(name,cst_id,bus_id,status,price) VALUES (?,?,?,?,?)";
        PreparedStatement preparedstatement=connection.prepareStatement(query);
        preparedstatement.setString(1,name);
        preparedstatement.setInt(2,b.cst_id);
        preparedstatement.setInt(3,rbus_id);
        preparedstatement.setInt(4,b.status);
        preparedstatement.setDouble(5,b.price);
        preparedstatement.executeUpdate();
        
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return b;
    }
    static void Book_a_Bus(){
        while(true){
            
        System.out.print("New Booking(Y/N):");
        char c=sc.next().charAt(0);
        if(c=='n' || c=='N')break;
        Booking booking=newBooking();
        if(booking.status==1)
        System.out.println("Your Booking is Confirmed!");
        else
            System.out.println("No such Bus/Seat not available!");
        
        System.out.print("Print Bill(Y/N):");
        c=sc.next().charAt(0);
        
        if(c=='Y'||c=='y')
        booking.printBill();
    }
    }
    
    static Bus addBus(){
        System.out.println("******New Bus Entry******");
        System.out.print("Enter bus no.");
        String bn=sc.next();
        System.out.print("Enter Depot:");
        String dep=sc.next();
        System.out.print("Enter No. of seats:");
        int se=sc.nextInt();
        System.out.print("Ticket Price:");
        double pr=sc.nextDouble();
        System.out.print("Enter start:");
        String st=sc.next();
        System.out.print("Enter end:");
        String des=sc.next();
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
            if(r>0)System.out.println("New Bus added!");
            else
            System.out.println("Some error occured!");
            
            //getting auto generated bus_id using bus_no.
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
        return nb;
        
    }
    
    static void findBus(String st,String des){
        String q;
        try
        {   PreparedStatement preparedstatement;
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
            while(rs.next()){
                System.out.println("Bus_no:"+rs.getString("bus_no")+" Seats available:"+rs.getInt("seats")+" Ticket_Price:"+rs.getDouble("ticket_price"));
            }
        }
        catch (SQLException sqle)
        {
            System.out.println(sqle.getMessage());
        }
        
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
      /*  
        while(true){
            
        System.out.print("New Booking(Y/N):");
        char c=sc.next().charAt(0);
        if(c=='n' || c=='N')break;
        Booking booking=newBooking(connection);
        if(booking.status==1)
        System.out.println("Your Booking is Confirmed!");
        else
            System.out.println("Sorry seat not available!");
        
        System.out.print("Print Bill(Y/N):");
        c=sc.next().charAt(0);
        
        if(c=='Y'||c=='y')
        booking.printBill();
    }
    */
   
    Book_a_Bus();
    //findBus("","Kanpur"); 
    //addBus();
    }
    
}