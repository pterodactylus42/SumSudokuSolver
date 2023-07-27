package de.fff.sumsudokusolver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SumSolver {

    private static final int MAX_VALUE = 4;

    private static final List<SudokuField> solutions = new ArrayList<>();

    public static boolean solveField(final SudokuField field) {
//        System.out.println("solveField:\n" + field.toString());
        while (!field.isAllValueCellsEntered()) {
            ValueCell valueCell = field.getValueCells().stream().filter(vc -> vc.getValue() == 0).findFirst().get();

            boolean valueMatchFound = true;
            for(int value = 1; value <= MAX_VALUE; value++) {
                valueCell.setValue(value);

                field.isSumValuesCorrect(); // too early

                if (field.isAllRowValuesUnique() && field.isAllColValuesUnique()) {
                    if (field.isAllValueCellsEntered()) {
//                        System.out.println("All preconditions okay.\n" + field.toString());
                        solutions.add(field);
                        return true;
                    } else {
                        SudokuField fieldForRecursion = field.clone();
                        if (solveField(fieldForRecursion)) {
//                            return true;
                            // instead of returning, continue the search
//                            System.out.println("Success at recursion depth " + fieldForRecursion.getRecursionDepth() + ". Current solutions:");
                            for(SudokuField solution : solutions) {
                                if(solution.isSumValuesCorrect()) {
                                    System.out.println(solution.toString());
                                    return true;
                                }

                            }

                        }
                    }
                } else {
//                    System.out.println("revert value " + value + " in address " + valueCell.getAddress() + " // Backtracking: " + field.getUuid());
                    valueCell.setValue(0);
                    valueMatchFound = false;
                }
            }
            if(!valueMatchFound) {
                return false;
            }

        }
//        System.out.println("This point should never be reached.");
        return false;
    }

    public static int getRandomValue() {
        Random random = new Random(new Date().getTime());
        return random.nextInt(MAX_VALUE+1);
    }
}