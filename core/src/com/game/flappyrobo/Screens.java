package com.game.flappyrobo;

import com.badlogic.gdx.utils.Array;

public class Screens {
    private Array<String> screens = new Array<>();
    private int current = 0;
    private long recharge = 1000;
    private long lastTime = System.currentTimeMillis();

    public Screens(){
        screens.add("first");
        screens.add("second");
        screens.add("third");
    }

    public boolean next(){
        if (System.currentTimeMillis() - lastTime  < recharge)
            return false;
        lastTime = System.currentTimeMillis();
        current = (current < screens.size - 1) ? current + 1 : 0;
        FlappyRobo.assets.setScreens(screens.get(current));
        return true;
    }

    public boolean prev(){
        if (System.currentTimeMillis() - lastTime  < recharge)
            return false;
        lastTime = System.currentTimeMillis();
        current = (current > 0) ? current - 1 : screens.size - 1;
        FlappyRobo.assets.setScreens(screens.get(current));
        return true;
    }

    public String current(){
        return screens.get(current);
    }

}
