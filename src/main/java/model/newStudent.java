package model;

public class newStudent {
    private int id;
    private String fname;
    private String mname;
    private String lname;
    private String pemail;
    private String pnumber;
    private String pgender;

    public newStudent(int id, String firstName, String middleName, String lastName, String email, String phoneNumber, String gender) {
        this.id = id;
        this.fname = firstName;
        this.mname = middleName;
        this.lname = lastName;
        this.pemail = email;
        this.pnumber = phoneNumber;
        this.pgender = gender;
    }
    public newStudent() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPemail() {
        return pemail;
    }

    public void setPemail(String pemail) {
        this.pemail = pemail;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public String getPgender() {
        return pgender;
    }

    public void setPgender(String pgender) {
        this.pgender = pgender;
    }
}