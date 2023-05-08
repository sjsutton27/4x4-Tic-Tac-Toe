//Seth Sutton
import java.io.*;
import java.net.*;
import java.util.*;

//port #9877
//This is a game of tictactoe between a client and a server, each have 50% of winning

    public class ServerThread extends Thread {
        private Socket toclientsocket;
        private DataInputStream instream;
        private DataOutputStream outstream;
        private PrintWriter out;
        private BufferedReader in;
        private Random gen; // for turns
        private char[][] board; //4x4 board

        private int row, col; //current row and column

        public ServerThread(Socket connectToClientSocket) throws IOException{
            toclientsocket = connectToClientSocket;
            gen = new Random();

            try {
                instream = new DataInputStream(connectToClientSocket.getInputStream());
                outstream = new DataOutputStream(connectToClientSocket.getOutputStream());
                out = new PrintWriter(outstream, true);
                in = new BufferedReader(new InputStreamReader(instream));

                board = new char[4][4];
                for (int x = 0; x <= 3; x++) {
                    for (int y = 0; y <= 3; y++) {
                        board[x][y] = ' ';

                    }
                }
                row = -1;
                col = -1;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        @Override
        public void run() {
            int counter = 0;
            String response = "";
            Boolean gameover = false;
            Boolean turn = false;
            if (gen.nextInt() % 2 != 0) {
                turn = true; //client makes first move
                out.println("CLIENT");
            }
            while (!gameover) {
                if (turn) {
                    try {
                        response = in.readLine();
                    } catch (IOException e) {
                        System.out.println("Error reading input from client");
                    }

                    String[] data = response.split("\\s+");

                        row = Integer.parseInt(data[1]);
                        col = Integer.parseInt(data[2]);


                    board[row][col] = 'O';
                    printBoard();
                    counter++;

                    if (checkWin() || counter == 16) {
                        gameover = true;
                        if (checkWin()) {
                            out.println("MOVE -1 -1 WIN");
                        } else {
                            out.println("MOVE -1 -1 TIE");
                        }
                    }
                } else {
                    makeMove();
                    counter++;
                    board[row][col] = 'X';
                    printBoard();

                    if (checkWin() || counter == 16) {
                        gameover = true;
                        if (checkWin()) {
                            out.println("MOVE " + row + " " + col + " LOSS");
                        } else {
                            out.println("MOVE " + row + " " + col + " TIE");
                        }
                    } else {
                        out.println("MOVE " + row + " " + col);
                    }
                }
                turn = !turn;
            }
        }

        public void makeMove() {
            do {
                row = gen.nextInt(4);
                col = gen.nextInt(4);
            }while (board[row][col] != ' ');
        }

        public boolean checkWin() {
            // Checks for a row-win
            for (int x = 0; x <= 3; x++) {
                if (board[x][0] == board[x][1] &&
                        board[x][1] == board[x][2] &&
                        board[x][2] == board[x][3] &&
                        board[x][0] != ' ') {
                    return true;
                }
            }
            //Checks for vertical win
            for (int y = 0; y < 4; y++) {
                if (board[0][y] == board[1][y] &&
                        board[1][y] == board[2][y] &&
                        board[2][y] == board[3][y] &&
                        board[0][y] != ' ') {
                    return true;
                }
            }

            // Checks for a diagonal win
            if (board[0][0] == board[1][1] &&
                    board[1][1] == board[2][2] &&
                    board[2][2] == board[3][3] &&
                    board[0][0] != ' ') {
                return true;
            }

            // Checks for a diagonal win
            if (board[3][0] == board[2][1] &&
                    board[2][1] == board[1][2] &&
                    board[1][2] == board[0][3] &&
                    board[3][0] != ' ') {
                return true;
            }
            return false;
        }
        public void printBoard(){
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



