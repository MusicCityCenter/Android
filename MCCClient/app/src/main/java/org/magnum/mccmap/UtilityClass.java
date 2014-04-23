package org.magnum.mccmap;

/**
 * Created by yaopan on 4/21/14.
 */
public class UtilityClass {
    public static String server = "http://0-1-dot-mcc-backend.appspot.com";
    public static int port = 80;
    public static String baseUrl = "/mcc";
    public static String NAV_PATH = "/path/";

    //event time currently stored in server is 1120, format it to 11:20
    public static String formatTime(String t){
        String min, hour;
        if(t.length() == 3){
            hour = t.substring(0, 1);
            min = t.substring(1, 3);
        }
        else {
            hour = t.substring(0, 2);
            min = t.substring(2, 4);
        }
        return hour + ":"+ min;
    }

}
