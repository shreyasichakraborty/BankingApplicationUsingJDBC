import java.io.FileInputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class BankingActivity {
	static Connection con;

	public static void setConnection() throws Exception {
		Properties p = new Properties();
		FileInputStream fis = new FileInputStream("C:\\Users\\User\\Desktop\\db.properties");
		p.load(fis);
		String url = p.getProperty("url");
		String user = p.getProperty("user");
		String pwd = p.getProperty("pwd");
		// connection creation//
		Class.forName("oracle.jdbc.OracleDriver");
		con = DriverManager.getConnection(url, user, pwd);
	}

	public void createAccount() throws Exception {
		int x = 0;
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		PreparedStatement pst = con.prepareStatement("Insert into cd values(?,?,?,?,?)");
		ResultSet rs = st.executeQuery("Select count(*) from cd");
		if (rs.next())
			x = rs.getInt(1);
		String accno = "A/CXY0001" + (x + 1);
		System.out.println("Enter name");
		String cname = s.nextLine();
		System.out.println("Enter balance");
		double balance = s.nextDouble();
		java.sql.Date sdate = new java.sql.Date(new java.util.Date().getTime());
		pst.setString(1, accno);
		pst.setString(2, cname);
		pst.setDouble(3, balance);
		pst.setDate(4, sdate);
		pst.setInt(5, 1);
		int success = pst.executeUpdate();
		if (success > 0)
			System.out.println("Inserted successfully");
		else
			System.out.println("Insertion failed");

	}

	public void openFd() throws Exception {
		int x = 0;
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		PreparedStatement pst = con.prepareStatement("Insert into fd values(?,?,?,?,?)");
		ResultSet rs = st.executeQuery("Select count(*) from fd");
		if (rs.next())
			x = rs.getInt(1);

		System.out.println("Enter account no: ");
		String accno = s.nextLine();
		int fdno = x + 1;
		System.out.println("Enter amount");
		double famt = s.nextDouble();
		System.out.println("Enter no of years: ");
		int yrs = s.nextInt();
		java.sql.Date sdate = new java.sql.Date(new java.util.Date().getTime());
		Calendar cal = Calendar.getInstance();
		java.util.Date udate = cal.getTime();
		cal.add(Calendar.YEAR, yrs);
		java.util.Date next = cal.getTime();
		java.sql.Date mdate = new java.sql.Date(next.getTime());
		pst.setInt(1, fdno);
		pst.setString(2, accno);
		pst.setDouble(3, famt);
		pst.setDate(4, sdate);
		pst.setDate(5, mdate);
		int success = pst.executeUpdate();
		if (success > 0)
			System.out.println("Inserted successfully in fd table");
		else
			System.out.println("Insertion failed in fd table");
	
	}

	public void withdraw() throws Exception {
		int tdno = 0;
		Scanner s = new Scanner(System.in);
		System.out.println("Enter your account number: ");
		String wa = s.nextLine();
		System.out.println("Enter the amount to withdraw:");
		double wamt = s.nextDouble();
		s.nextLine();
		Statement st = con.createStatement();
		ResultSet ea = st.executeQuery(String.format("select balance from cd where accno='%s'", wa));
		while (ea.next()) {
			double checkamt = ea.getDouble(1);
			if (checkamt < wamt)
				System.out.println("Insufficient balance");

			else {
				System.out.println("Transaction in progress");
				double rembal = ea.getDouble(1) - wamt;
				System.out.println("Balance " + rembal);
				String sqlQuery = String.format("Update cd set balance=%f where accno='%s'", rembal, wa);
				st.executeUpdate(sqlQuery);
				String details = "Debited " + wamt;
				PreparedStatement pst = con.prepareStatement("Insert into td values(?,?,?,?)");
				ResultSet rs = st.executeQuery("Select count(*) from td");
				java.sql.Date sdate = new java.sql.Date(new java.util.Date().getTime());
				if (rs.next())
					tdno = rs.getInt(1) + 1;
				pst.setInt(1, tdno);
				pst.setString(2, wa);
				pst.setString(3, details);
				pst.setDate(4, sdate);
				pst.execute();
			}
		}
	}

	public void deposit() throws Exception {
		int tdno = 0;
		Scanner s = new Scanner(System.in);
		System.out.println("Enter your account number: ");
		String wa = s.nextLine();
		System.out.println("Enter the amount to deposit:");
		double wamt = s.nextDouble();
		s.nextLine();
		Statement st = con.createStatement();
		ResultSet ea = st.executeQuery(String.format("select balance from cd where accno='%s'", wa));
		while (ea.next()) {

			System.out.println("Transaction in progress");
			double rembal = ea.getDouble(1) + wamt;
			System.out.println("Balance " + rembal);
			String sqlQuery = String.format("Update cd set balance=%f where accno='%s'", rembal, wa);
			st.executeUpdate(sqlQuery);
			String details = "Credited " + wamt;
			PreparedStatement pst = con.prepareStatement("Insert into td values(?,?,?,?)");
			ResultSet rs = st.executeQuery("Select count(*) from td");
			java.sql.Date sdate = new java.sql.Date(new java.util.Date().getTime());
			if (rs.next())
				tdno = rs.getInt(1) + 1;
			pst.setInt(1, tdno);
			pst.setString(2, wa);
			pst.setString(3, details);
			pst.setDate(4, sdate);
			pst.execute();

		}
		
	}

	public void fundTransfer() throws Exception {
		int tdno = 0;
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		System.out.println("Enter your account number: ");
		String sa = s.nextLine();
		System.out.println("Enter the amount to withdraw:");
		double samt = s.nextDouble();
		s.nextLine();
		System.out.println("Enter transfer account number: ");
		String ta = s.nextLine();
		ResultSet tb = st.executeQuery(String.format("select balance from cd where accno='%s'", ta));
		double transfaccbal = 0;
		while (tb.next()) {
			transfaccbal = tb.getDouble(1);
		}
		ResultSet sb = st.executeQuery(String.format("select balance from cd where accno='%s'", sa));
		while (sb.next()) {
			double checkamt = sb.getDouble(1);
			if (checkamt < samt) {
				System.out.println("Insufficient balance in sender account");
			} else {
				System.out.println("Transaction in progress");
				double rembal = sb.getDouble(1) - samt;
				System.out.println("Balance in sender account: " + rembal);
				String sqlQuery = String.format("Update cd set balance=%f where accno='%s'", rembal, sa);
				st.executeUpdate(sqlQuery);
				String details = "Debited " + samt;
				PreparedStatement pst = con.prepareStatement("Insert into td values(?,?,?,?)");
				ResultSet rs = st.executeQuery("Select count(*) from td");
				java.sql.Date sdate = new java.sql.Date(new java.util.Date().getTime());
				if (rs.next())
					tdno = rs.getInt(1) + 1;
				pst.setInt(1, tdno);
				pst.setString(2, sa);
				pst.setString(3, details);
				pst.setDate(4, sdate);
				pst.execute();

				double addbal = transfaccbal + samt;
				System.out.println("Balance in receiver account: " + addbal);
				sqlQuery = String.format("Update cd set balance=%f where accno='%s'", addbal, ta);
				st.executeUpdate(sqlQuery);
				String trs = "Credited " + samt;
				PreparedStatement pst1 = con.prepareStatement("Insert into td values(?,?,?,?)");
				rs = st.executeQuery("Select count(*) from td");

				if (rs.next())
					tdno = rs.getInt(1) + 1;
				pst1.setInt(1, tdno);
				pst1.setString(2, ta);
				pst1.setString(3, trs);
				pst1.setDate(4, sdate);
				pst1.execute();
				System.out.println("Money transfered");
				System.out.println("Transaction successful");
			}

		}
		}

	public void TransactionDetails() throws Exception {
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		System.out.println("Enter your account number: ");
		String accno = s.nextLine();
		String sqlQuery = String.format("select * from td where accno='%s'", accno);
		ResultSet rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			System.out.println(
					rs.getInt(1) + "------" + rs.getString(2) + "-----" + rs.getString(3) + "-----" + rs.getString(4));
		}
		
	}

	public void AccountDetails() throws Exception {
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		System.out.println("Enter your account number: ");
		String accno = s.nextLine();
		String sqlQuery = String.format("select * from cd where accno='%s'", accno);
		ResultSet rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			System.out.println(
					rs.getString(1) + "------" + rs.getString(2) + "-----" + rs.getDouble(3) + "-----" + rs.getDate(4));
		}
	
	}

	public void fdlist() throws Exception {
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		String sqlQuery = String.format("select * from fd ");
		ResultSet rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			System.out.println(rs.getInt(1) + "------" + rs.getString(2) + "-----" + rs.getDouble(3) + "-----"
					+ rs.getString(4) + "-----" + rs.getString(5));
		}
	
	}

	public void accRemoval() throws Exception {
		Scanner s = new Scanner(System.in);
		Statement st = con.createStatement();
		System.out.println("Enter your account number: ");
		String accno = s.nextLine();
		String sqlQuery = String.format("update cd set status=0 where accno='%s'", accno);
		int x = st.executeUpdate(sqlQuery);
		if (x > 0)
			System.out.println("Deletion successful");
		
	}

}
