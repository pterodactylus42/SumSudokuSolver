package de.fff.sumsudokusolver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SudokuField {

    private int recursionDepth;

    private int size;

    private List<AbstractCell> cells;

    private UUID uuid = UUID.randomUUID();

    private UUID parent;

    SudokuField(int[] values) {
        assert Math.pow(Math.sqrt(values.length),2) == values.length;
        this.size = (int) Math.sqrt(values.length);
        this.recursionDepth = 0;
        setValues(values);
    }

    SudokuField() {
        this.cells = new ArrayList<>();
    }

    private void setValues(int[] values) {
        this.cells = new ArrayList<>();
            for(int i = 0; i < values.length; i++) {
                if(values[i] == 0) {
                    cells.add(new ValueCell(i,values[i]));
                } else {
                    cells.add(new SumCell(i,values[i]));
                }
            }
    }

    public SudokuField clone() {
        SudokuField result = new SudokuField();
        for(AbstractCell cell : this.cells) {
            if(cell instanceof ValueCell) {
                result.getCells().add(new ValueCell(cell.getAddress(),((ValueCell) cell).getValue()));
            }
            if(cell instanceof SumCell) {
                result.getCells().add(new SumCell(cell.getAddress(),((SumCell) cell).getSum()));
            }
        }
        result.setSize(this.getSize());
        result.setRecursionDepth(this.getRecursionDepth() + 1);
        result.setParent(this.uuid);
        return result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRecursionDepth() {
        return recursionDepth;
    }

    public void setRecursionDepth(int recursionDepth) {
        this.recursionDepth = recursionDepth;
    }

    public UUID getParent() {
        return parent;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isAllValueCellsEntered() {
        for(ValueCell ac : getValueCells()) {
            if(ac.getValue() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllRowValuesUnique() {
        for(int row = 0; row < size; row++) {
            List<Integer> rowValues = new ArrayList<>();
            for(int col = 0; col < size; col++) {
                int listPosition = (row * size) + col;
                if(cells.get(listPosition) instanceof ValueCell && ((ValueCell) cells.get(listPosition)).getValue() != 0) {
                    if(rowValues.contains(((ValueCell) cells.get(listPosition)).getValue())) {
                        return false;
                    }
                    rowValues.add(((ValueCell) cells.get(listPosition)).getValue());
                }
            }
        }
        return true;
    }

    public boolean isAllColValuesUnique() {
        for(int col = 0; col < size; col++) {
            List<Integer> colValues = new ArrayList<>();
            for(int row = 0; row < size; row++) {
                int listPosition = (row * size) + col;
                if(cells.get(listPosition) instanceof ValueCell) {
                    if(colValues.contains(((ValueCell) cells.get(listPosition)).getValue()) && ((ValueCell) cells.get(listPosition)).getValue() != 0) {
                        return false;
                    }
                    colValues.add(((ValueCell) cells.get(listPosition)).getValue());
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("I am " + uuid + "\n");
        builder.append("my parent is " + getParent() + "\n");

        builder.append("\t");
        builder.append("|\t");
        for(int headerCol = 1; headerCol < size + 1; headerCol++) {
            builder.append(" " + headerCol + "\t");
        }
        builder.append("\n");
        builder.append(" -" + "\t");
        builder.append("|\t");
        for(int headerCol = 1; headerCol < size + 1; headerCol++) {
            builder.append(" " + "-" + "\t");
        }
        builder.append("\n");

        for(int row = 0; row < size; row++) {
            builder.append(" " + (row+1) + "\t");
            builder.append("|\t");
            for(int col = 0; col < size; col++) {
                int listPos = (row * size) + col;
                AbstractCell cell = cells.get(listPos);
                if(cell instanceof ValueCell) {
                    builder.append(" " + ((ValueCell) cell).getValue());
                }
                if(cell instanceof SumCell) {
                    builder.append("|" + ((SumCell) cell).getSum());
                }
                builder.append("\t");
            }
            builder.append("\n");
        }
        builder.append("\trecursionDepth\t" + this.getRecursionDepth());
        builder.append("\n\n");

        return builder.toString();

    }

    public List<AbstractCell> getCells() {
        return cells;
    }

    public AbstractCell getCellAt(final int address) {
        assert address < cells.size();
        return cells.get(address);
    }

    public List<ValueCell> getValueCells() {
        return cells.stream()
                .filter(ac -> ac instanceof ValueCell)
                .map(ValueCell.class::cast)
                .collect(Collectors.toList());
    }

    public List<SumCell> getSumCells() {
        return cells.stream()
                .filter(ac -> ac instanceof SumCell)
                .map(SumCell.class::cast)
                .collect(Collectors.toList());
    }

//    Address distribution 7x7
//    0   1   2   3   4   5   6
//    7   8   9   10  11  12  13
//    14  15  16  17  18  19  20
//    21  22  23  24  25  26  27
//    28  29  30  31  32  33  34
//    35  36  37  38  39  40  41
//    42  43  44  45  46  47  48

//    NorthWest:
//    address - size - 1
//    North:
//    address - size
//    NorthEast:
//    address - size + 1
//    West:
//    address - 1
//            ...
//    East:
//    address + 1
//    SouthWest:
//    address + size - 1
//    South:
//    address + size
//    SouthEast:
//    address + size + 1

//    border criteria:
//    isUpperBorder: address < size
//    isRightBorder: address == (size * x) - 1
//    isLowerBorder: address >= (size^2) - size
//    isLeftBorder: address % size == 0

    private List<ValueCell> getValueCellNeighbors(final SumCell sumCell) {
        List<ValueCell> result = new ArrayList<>();
        int sumCellAddress = sumCell.getAddress();
        System.out.println("Checking neighbors of cell " + sumCellAddress + ":");

        boolean isUpperBorder = sumCellAddress < size;
        boolean isRightBorder = false;
        for(int x = 1; x <= size; x++) {
            if(sumCellAddress == (size * x) - 1) isRightBorder = true;
        }
        boolean isLowerBorder = sumCellAddress >= (Math.pow(size,2)) - size;
        boolean isLeftBorder = sumCellAddress % size == 0;

        System.out.println("isUpperBorder: " + isUpperBorder + " isRightBorder: " + isRightBorder + " isLowerBorder: " + isLowerBorder + " isLeftBorder: " + isLeftBorder);

        // northWest
        if(!isUpperBorder && !isLeftBorder) {
            if(cells.get(sumCellAddress - size - 1) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress - size - 1));
            }
        }
        // north
        if(!isUpperBorder) {
            if(cells.get(sumCellAddress - size) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress - size));
            }
        }
        // northEast
        if(!isUpperBorder && !isRightBorder) {
            if(cells.get(sumCellAddress - size + 1) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress - size + 1));
            }
        }
        // west
        if(!isLeftBorder) {
            if(cells.get(sumCellAddress - 1) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress - 1));
            }
        }
        // east
        if(!isRightBorder) {
            if(cells.get(sumCellAddress + 1) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress + 1));
            }
        }
        // southWest
        if(!isLowerBorder && !isLeftBorder) {
            if(cells.get(sumCellAddress + size - 1) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress + size - 1));
            }
        }
        // south
        if(!isLowerBorder) {
            if(cells.get(sumCellAddress + size) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress + size));
            }
        }
        // southEast
        if(!isLowerBorder && !isRightBorder) {
            if(cells.get(sumCellAddress + size + 1) instanceof ValueCell) {
                result.add((ValueCell) cells.get(sumCellAddress + size + 1));
            }
        }

        return result;
    }

    public boolean isSumValuesCorrect() {
        for(SumCell sc : getSumCells()) {
            List<ValueCell> neighbors = getValueCellNeighbors(sc);
            int actualSum = neighbors.stream().map(vc -> vc.getValue()).collect(Collectors.toList()).stream().mapToInt(i -> i).sum();
            System.out.println("neighbors of SumCell " + sc.getAddress() + ": " + neighbors + " needed sum: " + sc.getSum() + " actual sum: " + actualSum);
            if(actualSum != sc.getSum()) {
                return false;
            }
        }
        return true;
    }
}