package com.nareen.movie;

import java.util.ArrayList;

/**
 * Created by vobil on 7/29/2017.
 */
public interface ScreenInterface{
    void reserveSeat(Screen scr);
    void cancelSeat(Screen scr);
    int checkAvailability(Screen scr);
    void display(Screen scr);
}
