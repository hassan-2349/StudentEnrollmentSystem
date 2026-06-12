public class Student {

    private String name;
    private int age;
    private int id;
    private String contactnum;
    private String CNIC;
    private String FCNIC;
    private String emailaddress;

   public void setname(String name) { this.name= name;}
  public  void setage(int age)    {this.age= age;}
   public void setid(int id)  {this.id= id;}
   public void setcontactnum(String contactnum) {this.contactnum = contactnum;}
  public  void setCNIC(String CNIC)   {this.CNIC= CNIC;}
  public  void setFCNIC(String FCNIC) {this.FCNIC = FCNIC;}
 public   void setemailaddress(String emailaddress)   {this.emailaddress= emailaddress; }

   public String getname()    {return name;}
   public int getage()    {return age;}
   public int getid() {return id;}
   public String getcontactnum()  {return contactnum;}
   public String getCNIC()    {return CNIC;}
   public String getFCNIC()   {return FCNIC;}
   public String getemailaddress()    {return emailaddress;}

    public String getName()         { return name; }
    public int    getAge()          { return age; }
    public int    getId()           { return id; }
    public String getContactnum()   { return contactnum; }
    public String getEmailaddress() { return emailaddress; }

}