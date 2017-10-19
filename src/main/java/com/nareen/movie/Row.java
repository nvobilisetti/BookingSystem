package com.nareen.movie;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by vobil on 7/30/2017.
 */
public class Row {

    ArrayList<Seat> seatList;
    int rowNumber;
    int seatsPerRow;
    int availableSeatsPerRow;
    Statement stmt;
    int screenNumber;

    public Row(int rowNumber,int seatsPerRow,Statement stmt,int screenNumber){
        this.rowNumber=rowNumber;
        this.seatsPerRow=seatsPerRow;
        this.stmt=stmt;
        this.seatList=new ArrayList<Seat>();
        this.availableSeatsPerRow=0;
        this.screenNumber=screenNumber;


    }
    public ArrayList<Seat> createSeatList() throws SQLException{
        for(int i=1;i<=seatsPerRow;i++){
        	Seat S=new Seat(i,false);
            this.seatList.add(S);
            this.availableSeatsPerRow++;  
        stmt.execute("INSERT IGNORE INTO seat(theatre_id,screen_id,row_id,seat_id,is_booked)"
        		+" VALUES(1,"+this.screenNumber+","+this.rowNumber+","+S.seatNumber+","+"'"+S.isReserved+"')");
        } 
         
        return this.seatList;
    }
}
