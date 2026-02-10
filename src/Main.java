import java.sql.*;
import java.util.Scanner;

public class Main {
    static Connection conn;
    private static ResultSet searchByBookName(String bookName) throws SQLException {
        PreparedStatement preparedStatement= conn.prepareStatement("select * from BookManagement where bookname=?");
        preparedStatement.setString(1,bookName);
        return preparedStatement.executeQuery();

    }
    private static  int updateBookQty(int newQty,String bookName) throws SQLException {
        PreparedStatement preparedStatement1= conn.prepareStatement("update BookManagement set qty=? where bookname=?");
        preparedStatement1.setInt(1,newQty );
        preparedStatement1.setString(2,bookName);
        return preparedStatement1.executeUpdate();

    }
    private static int addNewBook(String bookName,int qty,String writerName) throws SQLException {
        String query="insert into BookManagement values(?,?,?)";
        PreparedStatement preparedStatement2= conn.prepareStatement(query);
        preparedStatement2.setString(1,bookName);
        preparedStatement2.setInt(2,qty);
        preparedStatement2.setString(3,writerName);
        return preparedStatement2.executeUpdate();

    }
    private static ResultSet showAllBook() throws SQLException {
        Statement statement=conn.createStatement();
        return statement.executeQuery("select * from BookManagement");
    }
    public static void main(String [] args) throws ClassNotFoundException, SQLException,Exception {
        Scanner sc=new Scanner(System.in);
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url=DBConfig.url;
        String userName=DBConfig.userName;
        String password=DBConfig.password;
        conn= DriverManager.getConnection(url,userName,password);

        while(true){
            System.out.println("Enter 1 To add Book \n" +
                    "Enter 2 To Issue Book \n"+
                    "Enter 3 to Search Book By Name \n" +
                    "Enter 4 To Show all Book\n" +
                    "Enter 5 To Exit from Applicaton");
            int var=sc.nextInt();
            switch(var){
                case 1:
                    System.out.println("Enter the Book Name ");
                    String bookName=sc.next().trim().toLowerCase();
                    System.out.println("Enter the quantity");
                    int qty=sc.nextInt();
                    System.out.println("Enter the Writer Name");
                    String writerName=sc.next().trim().toLowerCase();
                    ResultSet rs=searchByBookName(bookName);
                    if(rs.next()){
                        int oldQty=rs.getInt("Qty");
                        int newQty=oldQty+qty;
                        int rowAff=updateBookQty(newQty,bookName);
                        if(rowAff!=0){
                            System.out.println(newQty+" book present in library");
                        }
                        else{
                            System.out.println("Server time Out Error");
                        }
                    }
                    else{
                        int rowAff=addNewBook(bookName,qty,writerName);
                        if(rowAff!=0){
                            System.out.println(" book added");
                        }
                        else{
                            System.out.println("Server time Out Error");
                        }
                    }
                    break;
                case 2:
                    System.out.println("Enter the Book You want to issue");
                    String book=sc.next().trim().toLowerCase();
                    System.out.println("Enter the writer of book");
                    String writer=sc.next().trim().toLowerCase();
                    ResultSet res=searchByBookName(book);
                    if(res.next()){
                        int availableQty= res.getInt("Qty");
                        System.out.println(book + " by writer "+writer+ " is available "+availableQty+" pices");
                        updateBookQty(--availableQty,book);
                    }
                    else{
                        System.out.println("Sorry book is not available in stock, Sorry for The in Convinence ");

                    }
                    break;
                case 3:
                    System.out.println("Enter the Book Name");
                    String bookNam=sc.next();
                    ResultSet r=searchByBookName(bookNam);
                    if(r.next()){
                        String name=r.getString("BookName").toUpperCase();
                        String writerNam=r.getString("Writer").toUpperCase();
                        System.out.println(name +" by "+writerNam +" is Available");
                    }
                    else{

                        System.out.println(bookNam +" is available");
                    }
                    break;
                case 4:
                    ResultSet resultSet=showAllBook();
                    while(resultSet.next()) {
                        System.out.println(resultSet.getString("bookName").toUpperCase() + " by " + resultSet.getString("Writer").toUpperCase());
                    }
                    break;
                case 5:
                    System.exit(0);
            }
        }

    }
}
