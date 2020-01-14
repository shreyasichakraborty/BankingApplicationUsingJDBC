import java.util.Properties;
import java.io.*;
import java.sql.*;

public class Bank {
	public static void main(String args[]) throws Exception {
		Properties p = new Properties();
		FileInputStream fis = new FileInputStream("C:\\Users\\User\\Desktop\\db.properties");
		p.load(fis);
		String url = p.getProperty("url");
		String user = p.getProperty("user");
		String pwd = p.getProperty("pwd");
		int x;

		// connection creation//
		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager.getConnection(url, user, pwd);
		Statement st = con.createStatement();

		// Customer table//
		x = st.executeUpdate(
				"Create table cd(accno varchar2(15) primary key,cname varchar2(10) not null,balance number(10,2),opdate varchar2(10),status number(1)");
		if (x == 0)
			System.out.println("cd table created");
		else
			System.out.println("cd table creation failed");

		// Employee table//
		x = st.executeUpdate(
				"Create table emp1(eno varchar2(15) primary key,ename varchar2(10) not null,accno varchar2(15) references cd(accno),balance number(10,2),jdate varchar2(10))");
		if (x == 0)
			System.out.println("emp1 table created");
		else
			System.out.println("emp1 table creation failed");

		// Transaction table//
		st.executeUpdate(
				"Create table td(tdno number(5) primary key,accno varchar2(15) references cd(accno),details varchar2(20),tdate varchar2(20))");
		if (x == 0)
			System.out.println("td table created");
		else
			System.out.println("td table creation failed");

		// FD table//
		st.executeUpdate(
				"Create table fd(fdno number(5) primary key,accno varchar2(15) references cd(accno),famt number(10,2),odate varchar2(10),mdate varchar2(10))");
		if (x == 0)
			System.out.println("fd table created");
		else
			System.out.println("fd table creation failed");

	}
}
