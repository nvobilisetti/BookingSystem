package com.nareen.movie;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;




/**
 * Created by vobil on 7/29/2017.
 */
public class Screen implements ScreenInterface {
    int screenNumber;
    int noOfRows;
    String typeOfScreen;
    int seatsPerRow;
    ArrayList<Row> rowList;
    int availableSeats;
    int totalSeats;
    Statement stmt;


    public Screen(int screenNumber, int noOfRows, String typeOfScreen, int seatsPerRow, Statement stmt) {
        this.screenNumber = screenNumber;
        this.noOfRows = noOfRows;
        this.typeOfScreen = typeOfScreen.toUpperCase();
        this.seatsPerRow = seatsPerRow;
        this.stmt=stmt;
        this.rowList = new ArrayList<Row>();
        this.availableSeats=0;
        this.totalSeats=0;
       


    }

    public ArrayList<Row> createRowList() throws NoScreenFoundException, SQLException {
        Scanner sc = new Scanner(System.in);
        if (this.typeOfScreen.equals("END STAGE")) {
            for (int i = 1; i <= this.noOfRows; i++) {
                Row temp = new Row(i,this.seatsPerRow,stmt,this.screenNumber);
                temp.createSeatList();
                this.rowList.add(temp);
                this.availableSeats+=seatsPerRow;
                this.totalSeats+=seatsPerRow;
            }
            stmt.execute("INSERT IGNORE INTO screen VALUES("
            		+this.screenNumber+","+"'"+this.typeOfScreen+"'"+
            		","+this.availableSeats+","+this.totalSeats+",1)");
        } else if (this.typeOfScreen.equals("WIDE FAN")) {
            System.out.println("Please enter number of seats for each row of Total "+this.noOfRows+" rows in Screen: "+this.screenNumber);
            String[] seatsForRow=sc.nextLine().split(",");
            for (int i = 1; i <= this.noOfRows; i++) {
                seatsPerRow=Integer.parseInt(seatsForRow[i-1]);
                Row temp = new Row(i, this.seatsPerRow, stmt,this.screenNumber);
                temp.createSeatList();
                this.rowList.add(temp);
                this.availableSeats+=seatsPerRow;
                this.totalSeats+=seatsPerRow;
            }

            stmt.execute("INSERT IGNORE INTO screen VALUES("
        			+this.screenNumber+","+"'"+this.typeOfScreen+"'"+
        			","+this.availableSeats+","+this.totalSeats+",1)");
        } else {
            throw new NoScreenFoundException("Screen type not found");
        }
        return this.rowList;
    }
    void throwException(String excepType,String message){
        if(excepType.equals("NoSeatFoundException")){
            try {
                throw new NoSeatFoundException(message);
            } catch (NoSeatFoundException e) {
                e.printStackTrace();
            }
        }else if(excepType.equals("NoScreenFoundException")){
            try {
                throw new NoScreenFoundException(message);
            } catch (NoScreenFoundException e) {
                e.printStackTrace();
            }
        }else if(excepType.equals("NoRowFoundException")){
            try {
                throw new NoRowFoundException(message);
            } catch (NoRowFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reserveSeat(Screen tempScr) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select ScreenType");
        String screenType = sc.nextLine().toUpperCase();
        if (screenType.equals(tempScr.typeOfScreen)) {
            System.out.println("Enter number of Tickets");
            int noOfTickets = sc.nextInt();
            if (tempScr.availableSeats - noOfTickets >= 0) {
                System.out.print("Enter Row number[Row 1 is near to Screen R: ");
                int row = sc.nextInt();
                if (row <= tempScr.noOfRows) {
                    Row getRow = tempScr.rowList.get(row - 1);
                    if (getRow.availableSeatsPerRow >= noOfTickets) {
                        System.out.println("Enter Booking position");
                        int bookPos = sc.nextInt();
                        if ((getRow.seatList.size() - bookPos+1) >= noOfTickets) {
                            for (int i = 0; i < noOfTickets; i++) {
                                Seat S = getRow.seatList.get(bookPos - 1 + i);
                                if (!S.isReserved) {
                                    S.isReserved = true;
                                    tempScr.availableSeats--;
                                    getRow.availableSeatsPerRow--;
                                } else {
                                    System.out.println("Available seats");
                                    tempScr.display(tempScr);
                                    throwException("NoSeatFoundException", "No Enough seats to  Book");
                                }
                            }
                            System.out.println("Seats after Booking");
                            tempScr.display(tempScr);
                        } else {
                            System.out.println("Available seats");
                            tempScr.display(tempScr);
                           throwException("NoSeatFoundException", "Seat Not Found");
                        }
                    } else {
                        System.out.println("Available seats after Booking");
                        tempScr.display(tempScr);
                        throwException("NoSeatFoundException", "Seats not available in this Row");
                    }
                }else {
                    System.out.println("Available seats");
                    tempScr.display(tempScr);
                    throwException("NoRowFoundException", "Requested Row not found");
                }
            }
            else {
                System.out.println("Available seats");
                tempScr.display(tempScr);
               throwException("NoSeatFoundException", "Seats not available in this screen");
            }
        } else {
            throwException("NoScreenFoundException", "Screen not found");
        }
    }


    @Override
    public void cancelSeat(Screen tempScr) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select ScreenType");
        String screenType =sc.nextLine().toUpperCase();
        if (screenType.equals(tempScr.typeOfScreen)) {
            System.out.println("Enter number of Tickets");
            int noOfTickets = sc.nextInt();
            if ((tempScr.totalSeats - tempScr.availableSeats) >= noOfTickets) {
                System.out.print("Enter Row number[Row 1 is near to Screen R: ");
                int row = sc.nextInt();
                if(row<=tempScr.noOfRows){
                    Row getRow = tempScr.rowList.get(row - 1);
                    if ((getRow.seatsPerRow - getRow.availableSeatsPerRow) >= noOfTickets) {
                        System.out.println("Enter Cancelling position");
                        int cancelPos = sc.nextInt();
                        if ((getRow.seatList.size() - cancelPos+1) >= noOfTickets) {
                            for (int i = 0; i < noOfTickets; i++) {
                                Seat S = getRow.seatList.get(cancelPos - 1 + i);
                                if (S.isReserved) {
                                    S.isReserved = false;
                                    tempScr.availableSeats++;
                                    getRow.availableSeatsPerRow++;
                                } else {
                                    throwException("NoSeatFoundException", "No Enough seats to cancel");
                                }
                            }
                            System.out.println("Seats Available after Cancellation");
                            tempScr.display(tempScr);
                        } else {
                            throwException("NoSeatFoundException", "Seat Not Found");
                        }
                    } else {
                        throwException("NoSeatFoundException", "No Enough seats in a row to cancel");
                    }
                }else{
                    throwException("NoRowFoundException", "Requested Row not found");
                }
            }else {
                throwException("NoSeatFoundException","No Enough seats in Screen to cancel");
            }
        }else{
            throwException("NoScreenFoundException","Screen not found");
        }
    }
    @Override
    public int checkAvailability(Screen tempScr) {
        return tempScr.availableSeats;
    }

    @Override
    public void display(Screen tempScr) {
        for(Row R:tempScr.rowList){
            System.out.print("R"+R.rowNumber+"\t");
            for(Seat S:R.seatList){
                if(S.isReserved==true){
                    System.out.print("X");
                }else if(S.isReserved==false){
                    System.out.print("O");
                }else{
                    System.out.print("-");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }
   
}


