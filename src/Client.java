//Seth Sutton
import java.io.*;
import java.net.*;
import java.util.*;

//playgame method
//port #9877
//need a game over flag
public class Client {
        private static String hostName = "localhost";
        private static DataInputStream inputStream;
        private static DataOutputStream outputStream;
        private static PrintWriter out;
        private static BufferedReader in;
        private static Socket toSeverSocket;
        private static char [] [] board;
        private static int row, col;

        public static void main(String[] args) {
                String host = hostName;
                try {
                        toSeverSocket = new Socket(host, 9877);
                        inputStream = new DataInputStream(toSeverSocket.getInputStream());
                        outputStream = new DataOutputStream(toSeverSocket.getOutputStream());
                        out = new PrintWriter(outputStream, true);
                        in = new BufferedReader(new InputStreamReader(inputStream));

                        row = -1;
                        col = -1;
                        board = new char[4][4];
                        for (int x = 0; x <= 3; x++) {
                                for (int y = 0; y <= 3; y++) {
                                        board[x][y] = ' ';
                                }
                        }
                        playgame(in, out);
                }catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }
        public static void playgame(BufferedReader in, PrintWriter out) throws IOException{

                        Scanner inp = new Scanner(System.in);
                        String response = "";
                        Boolean turn = false;
                        Boolean gameover = false;

                        while (!gameover) {
                                if (turn) {
                                        do {
                                                System.out.println("\n Enter your move (row and column)");
                                                String input = inp.nextLine();
                                                String[] data = input.split("\\s+");

                                                        row = Integer.parseInt(data[0]);
                                                        col = Integer.parseInt(data[1]);

                                        } while (row < 0 || row > 3 || col > 3 || col < 0 || board[row][col] != ' ');

                                        board[row][col] = 'O';
                                        out.println("MOVE " + row + " " + col);
                                } else {
                                        response = in.readLine();

                                        if (!response.equals("CLIENT")) {
                                                String[] args = response.split("\\s+");
                                                if (args.length > 3) {
                                                        row = Integer.parseInt(args[1]);
                                                        col = Integer.parseInt(args[2]);
                                                        if (args[3] != "WIN" && row != -1) {
                                                                board[row][col] = 'X';
                                                        }
                                                        switch (args[3]) {
                                                                case "WIN":
                                                                        System.out.println("\n\nCongratulations!!! You WON the game!");
                                                                        break;
                                                                case "TIE":
                                                                        System.out.println("\nThe game was a TIE!");
                                                                        break;
                                                                case "LOSS":
                                                                        System.out.println("\nSORRY! You LOST the game!");
                                                                        break;
                                                        }
                                                        gameover = true;
                                                } else {
                                                        row = Integer.parseInt(args[1]);
                                                        col = Integer.parseInt(args[2]);
                                                        board[row][col] = 'X';
                                                }
                                        } else {
                                                System.out.println("\nYOU MOVE FIRST");
                                        }
                                }
                                printBoard();
                                turn = !turn;
                        }

                        System.out.println("\n\nHere is the final game board");
                        printBoard();

        }
        public static void printBoard(){
                for(int r = 0; r < 4; r++){
                        for (int c = 0; c < 4; c++){
                                if(c == 3){
                                        System.out.print(" " + board[r][c] + " ");
                                } else {
                                        System.out.print(" " + board[r][c] + " |");
                                }
                        }
                        System.out.println();
                        if(r != 3){
                                System.out.println("---------------");
                        }
                }
                System.out.println();
        }

}


