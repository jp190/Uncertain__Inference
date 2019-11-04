import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardL {


    static int human;
    static int AI;
    static int bestmove;

    static int min = Integer.MAX_VALUE;
    static int max = Integer.MIN_VALUE;

    static int [][] copy = new int[3][3];

    public BoardL() {

    }

    static int[][] bdl = new int[][] { { 0, 0, 0, }, { 0, 0, 0, }, { 0, 0, 0, },

    };

    public static boolean boardIsFull(int[][] bdl) {
        int element = 0;
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j <= 2; j++) {
                if (bdl[i][j] != 0) {
                    element += 1;
                }
            }
        }
        if (element == 9)
            return true;
        else
            return false;
    }

    public static void printBoard() {
        System.out.println();
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print(bdl[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void Move(int column, int playerNumber) { // each state
        for (int i = 2; i >= 0; i--) {
            if (bdl[i][column] == 0) {
                bdl[i][column] = playerNumber;
                break;
            }
        }
    }

    // Actions method which return a single array column action we can make
    public static List<Integer> Actions(int[][] curr_state) { // ??????
        List<Integer> columnList = new ArrayList<Integer>();
        int element = 0;
        for (int j = 0; j <= 2; j++) {
            element = 0;
            for (int i = 2; i >= 0; i--) {
                if (curr_state[i][j] != 0) {
                    element += 1;
                }
            }
            if (element < 3)
                columnList.add(j);

        }
        //System.out.println(columnList);
        return columnList;
    }

    public static int[][] Result(int[][] curr_state, int turn, int r) {
        for (int i = 2; i >= 0; i--) {
            if (curr_state[i][r] == 0) {
                curr_state[i][r] = turn;
                break;
            }
        }
        return curr_state;
    }

    public static boolean isGameOver() {
        if (TStates(bdl, human) || TStates(bdl, AI) || boardIsFull(bdl))
            return true;
        else
            return false;
    }

    public static boolean TStates(int[][] curr_state, int turn) { // terminal states
        int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
        int k = 0;
        for (int i = 2; i >= 0; i--) {
            c1 = c2 = 0;
            for (int j = 0; j <= 2; j++) {
                if (curr_state[i][j] == turn) {
                    c1 += 1;
                }

                if (curr_state[j][i] == turn) {
                    c2 += 1;
                }
            }

            if (curr_state[i][k] == turn) { // check diagonal from bottom left
                c3 += 1;
                k++;
            }
            if (curr_state[i][i] == turn) {
                c4 += 1;
            }
            if (c1 == 3 || c2 == 3 || c3 == 3 || c4 == 3) {
                return true;


            }
        }
        return false;
    }

    public static int Utility(int[][] curr_state) {
        if (TStates(curr_state,human) == true)// human player meets terminal states
            return 1;
        else if (TStates(curr_state,AI) == true)// AI meets terminal states
            return -1;
        else // no one wins(draw)
            return 0;

    }

    public static int min_value(int[][] curr_state, int turn) {
        if (TStates(curr_state, turn) == true)
            return Utility(curr_state);
        else {
            List actions = Actions(curr_state);
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < actions.size(); i++) {
                int[][] next_state = Result(curr_state, turn, (int) actions.get(i));
                if (max_value(next_state, turn + 1) < min)
                    min = max_value(next_state, turn + 1);
            }
        }
        return min;
    }

    public static int max_value(int[][] curr_state, int turn) {
        if (TStates(curr_state, turn) == true)
            return Utility(curr_state);
        else {
            List actions = Actions(curr_state);
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < actions.size(); i++) {
                int[][] next_state = Result(curr_state, turn, (int) actions.get(i));
                if (min_value(next_state, turn - 1) > max)
                    max = min_value(next_state, turn - 1);
            }
        }
        return max;
    }

    public static int minimax(int turn) {// AI max
        copy = new int[3][3];
        for(int i = 0; i <bdl.length; i++) {
            for(int j = 0; j < bdl.length; j++) {
                copy[i][j] = bdl [i][j]; //??? or copy[i][j] = bdl [i][j]
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



    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = 1;

        System.out.println("3*3 Game starts:  [Initial Board]");
        printBoard();

        System.out.println("Do you want to play RED (1) or YELLOW (2)?");
        int answer = sc.nextInt();
        if (answer == 1) {
            human = answer;
            AI = 2;
        } else if (answer == 2) {
            human = answer;
            AI = 1;
        }
        boolean GO = true;
        while (GO) {
            {
                if ((num % 2) == 1) { // human player's turn
                    System.out.println(
                            "Choose a column and place your move 【column from left to right is number as 0 to 2】");
                    int move = sc.nextInt();
                    Move(move, human);
                    System.out.println("[Current Board-You placed down a move]");
                    printBoard();
                    System.out.println();

                    if(TStates(bdl,human) == true && isGameOver() == true) {
                        GO = false;
                        System.out.println("GameOver. You won!");
                    }else if(isGameOver() == true) {
                        GO = false;
                        System.out.println("GameOver. It is a tie.");
                    }

                    num += 1;


                } else {// computer 's turn
                    Move(minimax(AI), AI);
                    System.out.println("[Current Board-AI placed down a move]");
                    printBoard();

                    if(TStates(bdl,AI) == true && isGameOver() == true) {
                        GO = false;
                        System.out.println("GameOver. You lost");

                    }else if(isGameOver() == true) {
                        GO = false;
                        System.out.println("GameOver. It is a tie.");
                    }

                    num += 1;
                }

            }
        }

    }

}