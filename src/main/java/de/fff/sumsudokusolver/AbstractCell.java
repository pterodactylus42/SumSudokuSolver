package de.fff.sumsudokusolver;

public class AbstractCell {
    private final int address;

    public AbstractCell(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + address;
    }
}