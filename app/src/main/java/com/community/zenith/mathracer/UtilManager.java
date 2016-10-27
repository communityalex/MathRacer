package com.community.zenith.mathracer;

import java.util.Random;

/**
 * Created by Alex on 21/10/2016.
 */
public class UtilManager {

    public static String timeSince(String time){
        return "";
    }

    public static  <T> T chooseRandom(T...args){
        Random random = new Random();
        return args[random.nextInt(args.length)];
    }

    public static int randomInRange(int floor, int ceiling){
        Random r = new Random();
        return r.nextInt(ceiling-floor) + floor;
    }

    public static int randomLessThan(int ceiling){
        Random random = new Random();
        return random.nextInt(ceiling);
    }
}
