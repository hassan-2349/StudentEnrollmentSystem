import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DatabaseAUTH  extends Auth {

    private final String url= "jdbc:mysql://localhost:3306/mystudents";
    private final String username= "root";
    private final String password= "///";

    private Connection getconnection() throws SQLException  {

     return DriverManager.getConnection(url, username, password);
    }

    @Override
     public boolean login(String username, String password)    {
     String query= "SELECT * FROM auth WHERE username= ? AND password= ?;";
        try(  Connection con= getconnection(); PreparedStatement p= con.prepareStatement(query)) {
            p.setString(1, username);
            p.setString(2, password);
            ResultSet rs= p.executeQuery();
            
            return rs.next();
        } 

        catch(SQLException e)   {

            System.out.println("Error Message:"+e.getMessage());
            return false;
        }
     }
     @Override
     public boolean signup(String username, String password)  {

        String query= "INSERT INTO auth(username, password) VALUES(?, ?);";
        try( Connection con= getconnection(); PreparedStatement ps= con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            int rowsaffected= ps.executeUpdate();
            return rowsaffected>0;
        }catch(SQLException e)  {

            System.out.println("Error Occured: "+e.getMessage());
            return false;
        }
     }
}
