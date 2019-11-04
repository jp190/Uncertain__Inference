import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Board {
    int[][] bd = new int[6][7];

    public Board() {
        bd = new int[][]{
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
        };
    }

    public void printBoard() {
        System.out.println();
        for (int i = 0; i <= 5; ++i) {
            for (int j = 0; j <= 6; ++j) {
                System.out.print(bd[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    public int makeaMove(int column, int playerNumber){
        for (int i=5;i>=0;i--){
            if(bd[i][column]==0) {
                bd[i][column]= playerNumber;
            }
        }
    }






    -----------------------------------------------------------------------------------------------



    public static int Utility(int[][] curr_state) {
        if (TStates(curr_state,human) == true)// human player meets terminal states
            return 1;
        else if (TStates(curr_state,AI) == true)// AI meets terminal states
            return -1;
        else // no one wins(draw)
            return 0;

    }

    public static double min_value(int[][] curr_state, int turn, double alpha, double beta ) {
        if (TStates(curr_state, turn) == true)
            return Utility(curr_state);
        else {
            List actions = Actions(curr_state);
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < actions.size(); i++) {
                int[][] next_state = Result(curr_state, turn, (int) actions.get(i));
                if (max_value(next_state, turn + 1, alpha, beta) < min)
                    min = max_value(next_state, turn + 1, alpha, beta);
                if (min<=alpha)
                    return min;
                if (beta>min)
                    beta = min;
            }
        }
        return min;
    }

    public static double max_value(int[][] curr_state, int turn, double alpha, double beta) {
        if (TStates(curr_state, turn) == true)
            return Utility(curr_state);
        else {
            List actions = Actions(curr_state);
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < actions.size(); i++) {
                int[][] next_state = Result(curr_state, turn, (int) actions.get(i));
                if (min_value(next_state, turn - 1, alpha, beta) > max)
                    max = min_value(next_state, turn - 1, alpha, beta);
                if (max>=beta)
                    return max;
                if (alpha<max)
                    alpha = max;
            }
        }
        return max;
    }

    public static int alphabeta(int turn, double alpha, double beta, int depth) { // AI max
        alpha = Double.NEGATIVE_INFINITY;
        beta = Double.POSITIVE_INFINITY;
        copy = new int[6][7];
        for(int i = 0; i <bdl.length; i++) {
            for(int j = 0; j < bdl.length; j++) {
                copy[i][j] = bdl [i][j];
            }
        }
        List <Integer>actions = Actions(copy); // call method Actions to find out the  best-move column
        double max_value = Double.NEGATIVE_INFINITY;
        // column
        for (int i = 0; i < actions.size(); i++) {
            int[][] next_state = Result(copy,turn, (Integer) actions.get(i));
            if (min_value(next_state, turn - 1) > max_value) {
                max_value = min_value(next_state, turn - 1);
                bestmove = (int) actions.get(i);
//				System.out.println(actions.get(i));
            }
        }
        return bestmove;//best-move column for AI

    }
}