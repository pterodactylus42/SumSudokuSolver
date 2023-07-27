package de.fff.sumsudokusolver;

public class SolverApplication {

    public static void main(String[] args) {

        int[] values = new int[] {
                0,0,10,9,0,0,3, //
                12,0,0,0,10,10,0, //
                0,16,0,14,0,7,0, //
                0,13,0,0,11,0,10, //
                6,0,14,16,0,0,0, //
                0,10,0,0,0,17,12, //
                3,0,9,0,10,0,0, //
        };

        SudokuField sudokuField = new SudokuField(values);

        System.out.println("initially, my cells are : \n" + sudokuField.toString());
        System.out.println("isAllRowValuesUnique: " + sudokuField.isAllRowValuesUnique());
        System.out.println("isAllColValuesUnique: " + sudokuField.isAllColValuesUnique());
        System.out.println("isAllValueCellsEntered: " + sudokuField.isAllValueCellsEntered());

        SumSolver.solveField(sudokuField);
    }
}