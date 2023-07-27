package de.fff.sumsudokusolver;

public class ValueCell extends AbstractCell {

    private int value;

    public ValueCell(int address, int value) {
        super(address);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}