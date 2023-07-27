package de.fff.sumsudokusolver;

public class SumCell extends AbstractCell {

    private int sum;

    public SumCell(int address, int sum) {
        super(address);
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}