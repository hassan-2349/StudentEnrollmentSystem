abstract class Auth {
    
    private String user, password;

   public void setuser(String user) { this.user = user;}
   public void setpass(String password) {this.password= password;}

   abstract boolean signup(String username, String password);
   abstract boolean login(String username, String password);
}   
