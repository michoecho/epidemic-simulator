package SymulatorEpidemii;

public class Calendar {

    private int currentDay = 1;
    private int lastDay;
    public Calendar(int length) {
        lastDay = length;
    }

    public int getRandomFutureDay() {
        return currentDay + Rng.nextInt(lastDay + 1 - currentDay) + 1;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public boolean isOver() {
        return currentDay > lastDay;
    }

    public int daysLeft() {
        return lastDay - currentDay;
    }

    public void advance() {
        ++currentDay;
    }

}
