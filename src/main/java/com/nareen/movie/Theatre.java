package com.nareen.movie;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
//import java.util.Properties;
import java.util.Scanner;
import java.sql.Statement;

import java.sql.Connection;

/**
 * Created by vobil on 7/30/2017.
 */
public class Theatre {

	public static final String connectionUrl = "jdbc:mysql://localhost:3306/";
	private static Properties dbprops=null;

	public static void main(String[] args) throws NoScreenFoundException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			dbprops = DatabaseProperties.getInstance().getProperties(); 
			conn=DriverManager.getConnection(connectionUrl+dbprops.getProperty("database"), 
								dbprops.getProperty("userName"),dbprops.getProperty("password"));
			stmt = conn.createStatement();
			stmt.execute("INSERT IGNORE INTO theater VALUES(1,4,'Imax','Texas')");
			ArrayList<Screen> screenList = new ArrayList<Screen>();
			Scanner ip = new Scanner(System.in);
			int option;
			int screen;
			Screen tempScr;
			Screen S1 = new Screen(1, 10, "End Stage", 10, stmt);
			S1.createRowList();
			screenList.add(S1);
			Screen S2 = new Screen(2, 8, "Wide Fan", 10, stmt);
			S2.createRowList();
			screenList.add(S2);
			Screen S3 = new Screen(3, 12, "Wide Fan", 10, stmt);
			S3.createRowList();
			screenList.add(S3);
			Screen S4 = new Screen(4, 6, "End Stage", 8, stmt);
			S4.createRowList();
			screenList.add(S4);

			do {
				System.out.println("**************************");
				System.out.println("  Welcome to IMAX Movies  ");
				System.out.println("**************************");
				System.out.println("Please select the options");
				System.out.println("1.Book tickets");
				System.out.println("2.Cancel Tickets");
				System.out.println("3.Check Availability");
				System.out.println("4.Display Available Tickets");
				System.out.println("5.Exit");
				option = ip.nextInt();
				switch (option) {
				case 1:
					System.out.println("Reserve seats");
					System.out.println("Select Screen ");
					screen = ip.nextInt();
					tempScr = screenList.get(screen - 1);
					tempScr.reserveSeat(tempScr);
					break;
				case 2:
					System.out.println("Cancel tickets");
					System.out.println("Select Screen ");
					screen = ip.nextInt();
					tempScr = screenList.get(screen - 1);
					tempScr.cancelSeat(tempScr);
					break;
				case 3:
					System.out.println("Check Availability");
					System.out.println("Select Screen ");
					screen = ip.nextInt();
					tempScr = screenList.get(screen - 1);
					System.out.println(tempScr.checkAvailability(tempScr));
					break;
				case 4:
					System.out.println("Display the Available Tickets");
					System.out.println("Select Screen ");
					screen = ip.nextInt();
					tempScr = screenList.get(screen - 1);
					tempScr.display(tempScr);
					break;
				case 5:
					System.out.println("Thanks for Choosing IMAX Movies");
					break;
				default:
					System.out.println("Invalid choice ");
				}
			} while (option != 5);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}