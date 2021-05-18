package code.a.software.shiftme;

import android.os.Parcel;
import android.os.Parcelable;

public class Stopwatch implements Parcelable {

    private long startTime = 0;
    private long elapsedTime = 0;
    private boolean running = false;

    protected Stopwatch(Parcel in) {
        startTime = in.readLong();
        elapsedTime = in.readLong();
        running = in.readByte() != 0;
    }

    public Stopwatch()
    {

    }

    public static final Creator<Stopwatch> CREATOR = new Creator<Stopwatch>() {
        @Override
        public Stopwatch createFromParcel(Parcel in) {
            return new Stopwatch(in);
        }

        @Override
        public Stopwatch[] newArray(int size) {
            return new Stopwatch[size];
        }
    };

    public void start() {
        reset();
        running = true;
        startTime = System.currentTimeMillis();
    }

    public void reset()
    {
        elapsedTime = 0;
        startTime = 0;
    }

    public void stop()
    {
        if (isRunning())
            elapsedTime += System.currentTimeMillis() - startTime;

       running = false;
    }

    public void pause() {

        if (isRunning())
            elapsedTime += System.currentTimeMillis() - startTime;

        running = false;
    }

    public void resume() {
        running = true;
        startTime = System.currentTimeMillis();
    }

    public long getElapsedTime()
    {
        if (isRunning()) {
            long diff = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            elapsedTime += diff;
        }

        return elapsedTime;
    }

    public long getElapsedMS() { return  (getElapsedTime() / 100) % 1000; }

    public long getElapsedTimeSeconds() { return  (getElapsedTime() / 1000) % 60; }

    public long getElapsedTimeMinutes() { return ((getElapsedTime() / 1000) / 60) % 60; }

    public long getElapsedTimeHours() { return ((getElapsedTime() / 1000) / 60) / 60;  }

    public boolean isRunning() { return running; }
    

    public String toString() {

        long hours = getElapsedTimeHours();
        long minutes = getElapsedTimeMinutes();
        long seconds = getElapsedTimeSeconds();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
        dest.writeLong(elapsedTime);
        dest.writeByte((byte)(running ? 1 : 0));
    }
}