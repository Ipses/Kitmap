package me.kitmap.koth;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class Koth {

    protected String name;
    protected Location min, max;
    protected Player capper;
    protected long defaultCaptureTime, remainingTime;

    public Koth(String name, Location min, Location max, long defaultCaptureTime){
        this.name = name;
        this.min = min;
        this.max = max;
        setDefaultCaptureTime(defaultCaptureTime);
    }

    public String getName(){
        return this.name;
    }

    public Location getMin(){
        return this.min;
    }

    public Location getMax(){
        return this.max;
    }

    public boolean isActive(){
        return getRemainingTime() > 0;
    }

    public long getDefaultCaptureTime() { return this.defaultCaptureTime; }

    public void setDefaultCaptureTime(long time){ this.defaultCaptureTime = time; }

    public long getRemainingTime(){
        if(this.capper == null){
            return this.defaultCaptureTime;
        }
        return this.remainingTime - System.currentTimeMillis();
    }

    public Player getCapper(){
        return this.capper;
    }

    public void setCapper(@Nullable Player player){
        this.capper = player;
        if(player == null){
            this.remainingTime = this.defaultCaptureTime;
        } else {
            this.remainingTime = this.defaultCaptureTime + System.currentTimeMillis();
        }
    }
}
