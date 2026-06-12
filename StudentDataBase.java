import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class StudentDataBase {
        
        private final String url= "jdbc:mysql://localhost:3306/mystudents";
        private final String username= "root";
        private final String password= "///";

       private Connection getconnection() throws SQLException {

        return DriverManager.getConnection(url, username, password);
     }


        public boolean addstudent(Student s)    {

             final String query= "INSERT INTO students (name, age, id, contactnum, cnic, fcnic, emailaddress) VALUES(?, ?, ?, ?, ?, ?, ?);";
             try( Connection con= getconnection(); PreparedStatement ps= con.prepareStatement(query)) {
                ps.setString(1, s.getname());
                ps.setInt(2, s.getage());
                ps.setInt(3, s.getid());
                ps.setString(4, s.getcontactnum());
                ps.setString(5, s.getCNIC());
                ps.setString(6, s.getFCNIC());
                ps.setString(7, s.getemailaddress());
                return ps.executeUpdate()>0;

            } catch(SQLException e) {

                System.out.println("Error Occured: "+e.getMessage());
                return false;
            }
        }

        public boolean addCourses(Undergraduate u)   {

        
            final String query= "INSERT INTO courses (studentid, c1, c2, c3, c4, c5, c6, ch1, ch2, ch3, ch4, ch5, ch6) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try(  Connection con= getconnection();PreparedStatement ps= con.prepareStatement(query)) {
                ps.setInt(1, u.getStudentid());
                ps.setString(2, u.getC1());
                ps.setString(3, u.getC2());
                ps.setString(4, u.getC3());
                ps.setString(5, u.getC4());
                ps.setString(6, u.getC5());
                ps.setString(7, u.getC6());
                ps.setInt(8, u.getCh1());
                ps.setInt(9, u.getCh2());
                ps.setInt(10, u.getCh3());
                ps.setInt(11, u.getCh4());
                ps.setInt(12, u.getCh5());
                ps.setInt(13, u.getCh6());
                return ps.executeUpdate()>0;
            } catch(SQLException e) {

                System.out.println("Error Occured: "+e.getMessage());
                return false;
            }
        }


    public List<Undergraduate> getallstudents() {

        List<Undergraduate> list= new ArrayList<>();

        final String query= "SELECT * FROM students;";
        final String query2= "SELECT * FROM courses WHERE studentid= ?;";
        try(Connection con= getconnection(); PreparedStatement ps= con.prepareStatement(query); PreparedStatement ps2= con.prepareStatement(query2);) {

            ResultSet st= ps.executeQuery();
            while(st.next())    {

                Undergraduate u= new Undergraduate();
                u.setname(st.getString("name"));
                u.setage(st.getInt("age"));
                u.setid(st.getInt("id"));
                u.setcontactnum(st.getString("contactnum"));
                u.setCNIC(st.getString("cnic"));
                u.setFCNIC(st.getString("fcnic"));
                u.setemailaddress(st.getString("emailaddress"));
                ps2.setInt(1, u.getid());
                ResultSet rs2= ps2.executeQuery();

                while(rs2.next())   {

                    u.setC1(rs2.getString("c1"));
                    u.setC2 (rs2.getString("c2"));
                    u.setC3 (rs2.getString("c3"));
                    u.setC4 (rs2.getString("c4"));
                    u.setC5 (rs2.getString("c5"));
                    u.setC6 (rs2.getString("c6"));
                    u.setCh1(rs2.getInt("ch1"));
                    u.setCh2(rs2.getInt("ch2"));
                    u.setCh3(rs2.getInt("ch3"));
                    u.setCh4(rs2.getInt("ch4"));
                    u.setCh5(rs2.getInt("ch5"));
                    u.setCh6(rs2.getInt("ch6"));
                    
                }
                list.add(u);
            

            }

        } catch(SQLException e) {

            System.out.println("Error Occured: "+e.getMessage());
        }

        return list;
    }

    public boolean deletestudentbyid(Student s) {

        final String query= "DELETE FROM students WHERE id= ?";
        final String query2= "DELETE FROM courses WHERE studentid= ?";


        try( Connection con= getconnection();  PreparedStatement ps= con.prepareStatement(query); PreparedStatement ps2= con.prepareStatement(query2)) {
            ps2.setInt(1, s.getid());
            ps2.executeUpdate(); 
            ps.setInt(1, s.getid());
            return ps.executeUpdate()>0;
            
        } catch(SQLException e) {
            
            System.out.println("Error Occured: "+e.getMessage());
            return false;
        }

    }

    public boolean updatestudentbyid(Student s, Undergraduate u) {
    final String query = "UPDATE students SET name=?, age=?, contactnum=?, cnic=?, fcnic=?, emailaddress=? WHERE id=?";
    final String query2 = "UPDATE courses SET  c1=?, c2=?, c3=?, c4=?, c5=?, c6=?, ch1=?, ch2=?, ch3=?, ch4=?, ch5=?, ch6=? WHERE studentid=?";
   
    try ( Connection con = getconnection(); PreparedStatement ps = con.prepareStatement(query);PreparedStatement ps2 = con.prepareStatement(query2)) {

        ps.setString(1, s.getname());
        ps.setInt(2, s.getage());
        ps.setString(3, s.getcontactnum());
        ps.setString(4, s.getCNIC());
        ps.setString(5, s.getFCNIC());
        ps.setString(6, s.getemailaddress());
        ps.setInt(7, s.getid());
        ps.executeUpdate();
        ps2.setString(1, u.getC1());
        ps2.setString(2, u.getC2());
        ps2.setString(3, u.getC3());
        ps2.setString(4, u.getC4());
        ps2.setString(5, u.getC5());
        ps2.setString(6, u.getC6());
        ps2.setInt(7, u.getCh1());
        ps2.setInt(8, u.getCh2());
        ps2.setInt(9, u.getCh3());
        ps2.setInt(10, u.getCh4());
        ps2.setInt(11, u.getCh5());
        ps2.setInt(12, u.getCh6());
        ps2.setInt(13, u.getStudentid());
        return ps2.executeUpdate()> 0;

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
        return false;
    }
}
}
