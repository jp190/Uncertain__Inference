import javafx.scene.Node;

import java.util.Scanner;

public class Connect4 {

    Board board;
    Scanner sc  ;
    int maxdepth;
    int player1;
    int AI;

    public Connect4(Board board){
        this.board = board;
        sc = new Scanner(System.in);
    }



    public int getPlayernumber(){
        return player1;
    }



    public void basic(){
        System.out.println("Depth limit?");
        int dp = sc.nextInt();
        maxdepth = dp;
        System.out.println("Do you want to play RED (1) or YELLOW (2)?");
        int answer = sc.nextInt();
        if (answer == 1){
            player1 = answer;
            AI = 2;
        }
        else if (answer == 2){
            player1 = answer;
            AI = 1;
        }
    }


    public void Move(){//exceptions for # of steps
        System.out.println("Your move (1-7): ");
        int move = sc.nextInt();
        while(move<1 || move > 7){
            System.out.println("Invalid move.\n\nYour move (1-7): ");
            move = sc.nextInt();
        }
    }






    public boolean Result(Board bd, int playernumber){
        int utility = 0;
        int YouConnect = 0;
        int AIConnect = 0;
        for (int i=5;i>=0;i--){
            for (int j=6;j>=0;j--){
                if (i>=3){
                    for(int p=0;p<4;p++){
                        if (board.bd[i-p][j]==playernumber)
                            YouConnect += 1;
                        else
                            AIConnect += 1;
                    }
                }
                if (j<=3){
                    for(int p=0;p<4;p++){
                        if(board.bd[i][j+p]==playernumber)
                            YouConnect += 1;
                        else
                            AIConnect += 1;
                    }
                }
                if (i>=3 && j<=3){
                    for(int p=0;p<4;p++){
                        if(board.bd[i-p][j-p]==playernumber)
                            YouConnect += 1;
                        else
                            AIConnect += 1;
                    }
                }
                if (i>=3 && j>=3){
                    for(int p=0;p<4;p++) {
                        if (board.bd[i - p][j + p] == playernumber)
                            YouConnect += 1;
                        else
                            AIConnect += 1;
                    }
                }
            }
        }
        if (YouConnect == 4){
            utility = 1;
            System.out.println("You win");
            return true;
        }
        else if (AIConnect == 4){
            utility = -1;
            System.out.println("You lost");
            return false;
        }
        else
            utility = 0; // maybe add while loop at the front of the for loop?
    }



    public static int minimax(int depth, int Index, boolean Max, int scores[], int maxheight){
        if (depth == maxheight){
            return scores[Index];
        }
        if (Max){
            return Math.max(minimax(depth+1, Index*2, false, scores, maxheight), minimax(depth+1, Index*2+1, false, scores, maxheight));
        }
        else{
            return Math.min(minimax(depth+1, Index*2, true, scores, maxheight), minimax(depth+1, Index*2+1, true, scores, maxheight));

        }
    }


    public int alphabetapruning(Node root, int depth, int a, int b, boolean MaximizePlayer){
        if (depth == 0){
            return // heuristic value of the node
        }
        if (MaximizePlayer == true){
            for (each child of the node){
                a = Math.max(a, alphabetapruning(child, depth-1, a, b, false);
                if (a>=b)
                    break; // prune
            }
            return a;
        }
        else if (MaximizePlayer == false){
            for (each child of the node){
                b = Math.min(b, alphabetapruning(child, depth-1, a, b, true);
                if (a>=b)
                    break;  // prune
            }
            return b;
        }
    }

    alphabetapruning(origin, depth, -infinity, infinity, true);







    public static void main(String[] args){
        Board board = new Board();
        Connect4 four = new Connect4(board);
        four.basic();

    }
}


