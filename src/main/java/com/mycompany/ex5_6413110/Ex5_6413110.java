package com.mycompany.ex5_6413110;

import java.io.*;
import java.util.*;

/*
 Remaining: make target card of all thread equally
 */

class OneCard {
    private int score; // 0-51
    private int suit; // clubs, diamonds, hearts, spades
    private int rank; // ace, 2-10, jack, queen, king
    private boolean drawn; // whether this card is drawn
    public OneCard(int score, int suit, int rank)
    {
        this.score = score;
        this.suit = suit;
        this.rank = rank;
    }
    public int getScore() { return score; }
    public int getSuit() { return suit; }
    public int getRank() { return rank; }
    public boolean isDrawn() { return drawn; }
    public String cardName() {
        String[] ranks = {null, "Ace", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "Jack", "Queen", "King"};
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String s = ranks[this.rank] + " of " + suits[this.suit];
        return s;
    }


} // end OneCard

class CardThread extends Thread {
    private PrintWriter out; // each thread writes to a separate file
    private ArrayList<OneCard> allCards = new ArrayList<OneCard>(); // a deck of all cards

    private int threadNumber;
    private int targetScore;

     // score of target card
    public CardThread(String n,int t,int threadNumber)
    {
        super(n);
        targetScore = t;
        this.threadNumber = threadNumber;
        createDeck();
        Collections.shuffle(allCards);
    }
    public void createDeck()
    {
        int sc1=0;
        for (int s1=0; s1<= 3; s1++) {
            for (int r1=1; r1<= 13; r1++) {
                OneCard temp = new OneCard(sc1,s1,r1);
                sc1++;
                allCards.add(temp);
            }
        }
    }
    public String printTargetCard()
    {
        int targetCard;
        targetCard = findTargetCard();
        String s = "Target card is "+allCards.get(targetCard).cardName();
        return s;
    }
    public int findTargetCard() {
        for (int i=0; i < allCards.size(); i++) {
            if (allCards.get(i).getScore() == targetScore) {
                return i;
            }
        }
        return -1;
    }
    public int findCard()
    {
        int Attemp = 1;
        int targetCard;
        targetCard = findTargetCard();
        for (int i=0; i<allCards.size(); i++)
        {
            if(allCards.get(i).getScore() == allCards.get(targetCard).getScore()){
                out.printf("Round %2d : %s\n",Attemp, allCards.get(i).cardName());
                break;
            }
            else
            {
                out.printf("Round %2d : %s\n",Attemp, allCards.get(i).cardName());
                Attemp++;
            }
        }
        return Attemp;
    }
    public void checkDeck()
    {
        System.out.println(allCards.size());
    }
    public void threadProcess()
    {
        int rounds;
        String outfile;
        String stn = String.valueOf(threadNumber);
        outfile = "Thread_"+stn+".txt";
        try {
            out = new PrintWriter(outfile);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        rounds = findCard();
        out.close();
        System.out.printf("Thread_%d finishes in %d rounds\n", threadNumber, rounds);
    }


    @Override
    public void run() {
        // 1. Put all 52 cards in the ArrayList
        // 2. Keep drawing a card until finding the target
        // 3. Every time a card is drawn, print round number & that card to output file
        // 4. After finding the target, report #rounds to screen
        // 5. Each card can be drawn only once, so max #rounds = 52
        threadProcess();
        checkDeck();
        try {
            Thread.sleep(2500);
        }
        catch (InterruptedException e){
        }
    }
} // end CardThread

public class Ex5_6413110 {

    public static void main(String[] args) {
        int n;
        int i=0;
        Random random = new Random();
        Scanner input = new Scanner(System.in);
        System.out.print("Number of Thread: ");
        n = input.nextInt();
        final int ftScore = random.nextInt(52);
        CardThread myThread_0 = new CardThread("thread",ftScore,i);
        System.out.println(myThread_0.printTargetCard());
        myThread_0.start();
        for (i=1; i<n; i++)
        {
            CardThread myThread = new CardThread("thread",ftScore,i);
            myThread.start();
        }

    }
}