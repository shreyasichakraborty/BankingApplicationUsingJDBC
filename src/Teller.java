import java.util.*;

public class Teller {
	public static void main(String args[]) throws Exception {
		Scanner s = new Scanner(System.in);
		BankingActivity b = new BankingActivity();
		BankingActivity.setConnection(); // static method call sets up database connection
		int ch;
		do{
		
		System.out.println("MENU\n1.Create Account\n2.Open Fixed Deposit Account\n3.Withdrawal\n4.Deposit\n5.Fund Transfer\n6.Transaction Details\n7.Account Details\n8.FD list\n9.Account removal\n10.Exit");
		System.out.println("Enter your choice: ");
		ch = s.nextInt();
		s.nextLine();
		switch (ch) {
		case 1:
			b.createAccount();
			break;

		case 2:
			b.openFd();
			break;

		case 3:
			b.withdraw();
			break;

		case 4:
			b.deposit();
			break;

		case 5:
			b.fundTransfer();
			break;

		case 6:
			b.TransactionDetails();
			break;

		case 7:
			b.AccountDetails();
			break;

		case 8:
			b.fdlist();
			break;

		case 9:
			b.accRemoval();
			break;

		case 10:
				System.exit(0);
				break;
		default:System.out.println("Invalid entry");
		}	
		
		}while(ch!=11);	
		}
	}

