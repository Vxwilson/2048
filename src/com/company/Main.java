package com.company;


import java.io.*;
import java.util.Random;
import java.util.Scanner;


public class Main {

    /////////variables//////////////
    /////
    static final int CELLSIZE = 16;

    static boolean lose, win;

    static Integer dir;
    static short emptycells;

    static Random rand;
    static Scanner s;

    static int[] numbers;

    static long startTime, endTime;

    static PrintWriter writer;
    static BufferedReader fr;

    static int hs;//highscore
    //!!test variables
    static int total, lastTotal;
    static boolean somethingWrong;
    /////

    //!!bot variables
    static boolean swap;//just swapping between two keys
    /////

    /////////variables end//////////////


    //main loop of the game
    public static void main(String[] args) {
        // write your code here
        initialize();

        while(true)
            playGame();

    }

    public static void readHs(){
        try {
            fr = new BufferedReader(new FileReader("hs.txt"));
            hs=Integer.parseInt(fr.readLine());

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void recHs(int a){
        try {
            writer = new PrintWriter(new FileOutputStream("hs.txt", false));
            writer.println(a);
            System.out.println("writing "+a+" ...");
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initialize(){
        lose = false;
        win = false;

        dir = null;
        emptycells = CELLSIZE;

        rand = new Random();
        s = new Scanner(System.in);

        numbers = new int[CELLSIZE];

        startTime = System.currentTimeMillis();
        endTime = 0;
        hs=0;


        total = 0;
        lastTotal = 0;
        somethingWrong = false;

        swap = true;//just swapping between two keys

    }

    public static void gameStart(){
        initialize();

        System.out.println("Game started. highscore: ");
        readHs();
        System.out.println(hs);
    }

    public static void gameEnd() {
        System.out.println("this round has ended. Saving results.");
        if(lastTotal>hs) {
            recHs(lastTotal);
            System.out.println("new High Score! (" + lastTotal + ") overwritten " + hs + " .");
        }
        else{
            System.out.println("highscore: "+hs);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }


    //for bot to play forever(need a while true loop)
    public static void playGame() {
        gameStart();

        while (!lose) {
            clear();

            botupdate();

            draw();

            check();


            if (win) {
                winAnnouncement();
                break;
            }
            if (lose) {
                loseAnnouncement();
                break;
            }
        }

        gameEnd();
    }

    //personal bot to win the game
    public static void botupdate() {
        botinput(false);
        int a = 1;
        while (!render()) {
            if (emptycells < 16) {
                dir = a;
                a++;
            }
        }
    }

    public static void botinput(boolean save) {//uses a static boolean
        if (emptycells < 16) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            if (!save) {
                if (swap) {
                    dir = 3;
                    swap = !swap;
                } else {
                    dir = 4;
                    swap = !swap;
                }
            } else {//no move
                dir = 2;
                swap = false;
            }
        }
    }

    //draw the game board.
    public static void draw() {
        System.out.println(numbers[0] + " " + numbers[1] + " " + numbers[2] + " " + numbers[3]);
        System.out.println(numbers[4] + " " + numbers[5] + " " + numbers[6] + " " + numbers[7]);
        System.out.println(numbers[8] + " " + numbers[9] + " " + numbers[10] + " " + numbers[11]);
        System.out.println(numbers[12] + " " + numbers[13] + " " + numbers[14] + " " + numbers[15]);
    }

    //dirty hack flush system
    public static void clear() {
        System.out.print("\n\n\n\n\n\n");
        //System.out.flush();
    }

    //update the game by accepting input and rendering
    public static void update() {
        input();
        while (!render()) {
            input();
        }
    }

    //accept input after every loop
    public static void input() {
        char c = s.next().charAt(0);
        //int ascii=(int)c;
        //System.out.println("ascii: "+ascii);
        while (c != 'w' && c != 'a' && c != 's' && c != 'd') {
            c = s.next().charAt(0);
        }
        switch (c) {
            case 'w':
                dir = 1;
                break;
            case 'a':
                dir = 2;
                break;
            case 's':
                dir = 3;
                break;
            case 'd':
                dir = 4;
                break;
        }
    }

    ///////////////////////////////////////////////
    //TESTING PURPOSES OR GAME PURPOSE. RAND INPUT, NO KEYBOARD.(chg update in main to rupdate)
    public static void rupdate() {
        rinput();
        while (!render()) {
            rinput();
        }
    }

    public static void rinput() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        int a = rand.nextInt(4) + 1;

        switch (a) {
            case 1:
                dir = 1;
                break;
            case 2:
                dir = 2;
                break;
            case 3:
                dir = 3;
                break;
            case 4:
                dir = 4;
                break;
        }
    }
    /////////////////////////////////////////////////////


    //random number
    public static void randomNumber() {
        int randomint;

        randomint = rand.nextInt(emptycells) + 1;

        for (int i = 0; i < CELLSIZE; i++) {
            if (numbers[i] == 0) {
                randomint--;
                if (randomint == 0) {
                    numbers[i] = 2; //new cell is born!
                    emptycells--;
                    break;
                }
            }
        }
    }

    //move the numbers after receiving input, and randomly adds a new number to the game.
    public static boolean render() {

        //firstloop
        if (emptycells == CELLSIZE) {
            randomNumber();
        } else {
            //check if move success

            if (!move()) return false;

            else {
                if (emptycells > 0) {
                    randomNumber();//randomly adds a number
                }
            }

        }
        return true;
    }


    //move the numbers. Used in render.
    public static boolean move() {

        int totalcount = 0;
        ///////////////////////////////////////////////////////
        ///////////////move////////////////////////////////////
        boolean once = false;
        switch (dir) {
            case 1://up


                for (int a = 0; a < CELLSIZE; a++) {
                    if (numbers[a] != 0) {
                        if (a > 3) {
                            int count = 0;
                            //a number can only match once per loop

                            boolean bordered = false;
                            for (int da = a; da >= 4; da -= 4) {
                                if (numbers[da - 4] == 0) {
                                    count++;
                                    totalcount++;
                                } else if (numbers[da - 4] != numbers[a]) {
                                    bordered = true;
                                }
                                //check if number matches
                                else if (numbers[da - 4] == numbers[a]) {
                                    if (!once && !bordered) {
                                        numbers[a] *= 2;
                                        numbers[da - 4] = 0;
                                        emptycells++;
                                        count++;
                                        totalcount++;

                                        once = true;
                                    }
                                }
                            }
                            if (count > 0) {
                                numbers[a - count * 4] = numbers[a];
                                numbers[a] = 0;
                            }
                        }

                    }
                }
                break;
            case 2://left
                for (int a = 0; a < CELLSIZE; a += 4) {//using two for loops to render in the order of 12356791011131415
                    for (int loop = 1; loop <= 3; loop++) {//we don't want the leftmost column
                        int alias = a + loop;//alias

                        if (numbers[alias] != 0) {//skip if it is empty
                            int count = 0;
                            boolean bordered = false;
                            for (int da = 1; da <= loop; da++) {
                                if (numbers[alias - da] == 0) {
                                    count++; //checking left/right is harder than up/down
                                    totalcount++;
                                } else if (numbers[alias - da] != numbers[alias]) {
                                    bordered = true;
                                }
                                //check if number matches
                                else if (numbers[alias - da] == numbers[alias]) {
                                    if (!once && !bordered) {
                                        numbers[alias] *= 2;
                                        numbers[alias - da] = 0;
                                        emptycells++;
                                        count++;
                                        totalcount++;

                                        once = true;
                                    }
                                }
                            }
                            if (count > 0) {
                                numbers[alias - count] = numbers[alias]; //just simple swap
                                numbers[alias] = 0;
                            }
                        }

                    }
                }
                break;
            case 3://down
                for (int a = CELLSIZE - 1; a >= 0; a--) {
                    if (numbers[a] != 0) {
                        if (a < 12) {
                            int count = 0;
                            boolean bordered = false;
                            for (int da = a; da < 12; da += 4) {
                                if (numbers[da + 4] == 0) {
                                    count++;
                                    totalcount++;
                                } else if (numbers[da + 4] != numbers[a]) {
                                    bordered = true;
                                } else if (numbers[da + 4] == numbers[a]) {

                                    if (!once && !bordered) {
                                        numbers[a] *= 2;
                                        numbers[da + 4] = 0;
                                        emptycells++;
                                        count++;
                                        totalcount++;

                                        once = true;
                                    }
                                }
                            }
                            if (count > 0) {
                                numbers[a + count * 4] = numbers[a];
                                numbers[a] = 0;
                            }
                        }

                    }
                }
                break;
            case 4://right
                for (int a = CELLSIZE - 1; a >= 0; a -= 4) { //checking in the order of 15141311109765321
                    for (int loop = 1; loop <= 3; loop++) {
                        int alias = a - loop;
                        if (numbers[alias] != 0) {
                            int count = 0;
                            boolean bordered = false;
                            for (int da = 1; da <= loop; da++) {
                                if (numbers[alias + da] == 0) {
                                    count++;
                                    totalcount++;
                                } else if (numbers[alias + da] != numbers[alias]) {
                                    bordered = true;
                                }
                                //check if number matches
                                else if (numbers[alias + da] == numbers[alias]) {
                                    if (!once && !bordered) {
                                        numbers[alias] *= 2;
                                        numbers[alias + da] = 0;
                                        emptycells++;
                                        count++;
                                        totalcount++;

                                        once = true;
                                    }
                                }
                            }
                            if (count > 0) {
                                numbers[alias + count] = numbers[alias];
                                numbers[alias] = 0;
                            }
                        }
                    }
                }
                break;
        }

        if (totalcount == 0) return false;
        return true;
    }

    //check if win or lose
    public static void check() {
        if (emptycells == 0) {//lose
            if (!moves()) {
                lose = true;
                for (int i = 0; i < CELLSIZE; i++) {
                    if (numbers[i] == 0) {
                        numbers[i] = 2;
                    }
                }
            }

        } else {

            for (int a = 0; a < CELLSIZE; a++) {
                if (numbers[a] >= 2048) win = true;
            }
        }

        test();
    }

    public static boolean moves() {
        for (int a = 0; a < CELLSIZE; a++) {
            for (int i = -1; i <= 4; i++) {
                if (a % 4 != 3 && i == 1 || i == 4 || a % 4 != 0 && i == -1)
                    if (a + i > 0 && a + i < CELLSIZE)
                        if (numbers[a + i] == numbers[a]) return true;
            }
        }
        return false;
    }

    //FOR TESTING PURPOSES ONLY//
    public static void test() {
        lastTotal = total;
        total = 0;

        for (int a = 0; a < CELLSIZE; a++) {
            total += numbers[a];
        }

        if (total - lastTotal == 2 || lastTotal == 0) {
            if (!somethingWrong) System.out.println("..........");
            else if (lastTotal != 0) {
                System.out.println("Something's wrong! total: " + total + ", last total: " + lastTotal);
            }
        } else {
            somethingWrong = true;
            System.out.println("WARNING: ERROR-- total: " + total + ", last total: " + lastTotal + ", diff: " + (total - lastTotal));
        }
    }

    public static void winAnnouncement() {
        System.out.println("\n" + "you won!");
        setEndTime();
        System.out.println("you won with a low mark of " + lastTotal);
    }

    public static void loseAnnouncement() {
        System.out.println("\n" + "you lose!");
        setEndTime();
        System.out.println("you lose with a whooping marks of " + lastTotal);
    }

    public static void setEndTime() {
        endTime = System.currentTimeMillis();
        System.out.println("took you " + ((double) (endTime - startTime) / 1000L) + " seconds. long enough.");


    }
}
