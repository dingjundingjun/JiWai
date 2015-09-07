package com.org.great.world.data;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class BaseGamePojo extends BasePojo{
    private ArrayList<GamePojo> message;
    public ArrayList<GamePojo> getMessage() {
        return message;
    }
    public void setMessage(ArrayList<GamePojo> message) {
        this.message = message;
    }
}
