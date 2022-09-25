import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
//SOME BIG THINGS TO DO: Reset deck size
public class Main extends JFrame implements MouseListener
{
    //static JPanel p = new JPanel();
    //static Scanner scan = new Scanner(System.in);

    static Point clicked = new Point();
    static int response;

    static boolean yourTurn = false;
    static boolean computerWon = false;
    static boolean playerWon = false;
    static boolean suitChoose = false;
    static boolean changedSuit = false;
    static boolean playedDouble = false;
    static String globalTempSuit;

    static ArrayList<Integer> selected = new ArrayList<Integer>();
    static ArrayList<String> cards = new ArrayList<String>(Arrays.asList());
    static ArrayList<String> yourCards = new ArrayList<String>();

    static BufferedImage cardImages;

    static ArrayList<Integer> moveScores = new ArrayList<Integer>();
    static ArrayList<String> moveCards = new ArrayList<String>();

    static ArrayList<Integer> comboMoveScores = new ArrayList<Integer>();

    static ArrayList<String> comboMoveCards = new ArrayList<String>();

    static ArrayList<String> deck = new ArrayList<String>();

    static ArrayList<String> addBack = new ArrayList<String>();
    static ArrayList<String> addBackComp = new ArrayList<String>();

    static String floorCard = "s7";

    // card width: 65, height: 100

    public Main()
    {
        super("Crazy 8's - Insanity Edition");
        addMouseListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1800, 2000));
        try
        {
            BufferedImage img = ImageIO.read(new File(".//assets//imgs//deck of heart suited cards.jpg"));
            setIconImage(img);
            cardImages = ImageIO.read(new File(".//assets//imgs//Great work today.png"));
        }
        catch(Exception e)
        {
        }
        setVisible(true);
    }

    @Override
    public void repaint(long time, int x, int y, int width, int height)
    {
        //super.repaint(time, x, y, width, height);
        Graphics g = this.getGraphics();
        g.setColor(Color.BLACK);
        BufferedImage backImage = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        Graphics backg = backImage.getGraphics();
        backg.drawString("Pick up Card", 10, 450);
        backg.drawString("Skip Turn after 1", 10, 470);
        backg.drawString("Eight: Choose New Suit", 250, 60);
        backg.drawString("Jack: Skip Turn", 250, 75);
        backg.drawString("Two: Enemy Picks Up Two Cards", 250, 90);
        if (computerWon)
        {
            backg.drawString("Sorry, You Lost...", 500, 300);
        }
        else if (playerWon)
        {
            backg.drawString("Nice, You Won!", 500, 300);
        }
        if (suitChoose)
        {
            backg.drawString("Choose the new suit by clicking on the card with the suit you want", 500, 300);
        }
        if (changedSuit)
        {
            backg.drawString("Computer changed suit to " + globalTempSuit, 500, 300);
        }
        if (yourTurn)
        {
            backg.drawString("Your Turn", 100, 300);
        }
        for (int i = 0; i < cards.size() ; i++)
        {
            //ArrayList<Integer> source = findSource(cards.get(i));
            //backg.drawImage(cardImages, 100 + 65 * i, 100, 165 + 65 * i, 200, source.get(0), source.get(1), source.get(2), source.get(3), null);
            backg.drawImage(cardImages, 100 + 65 * i, 100, 165 + 65 * i - 1, 200, 500, 500, 565, 600, null);
        }
        for (int i = 0; i < yourCards.size() ; i++)
        {
            int adder = 0;
            if (selected.contains(i))
            {
                adder += 50;
            }
            ArrayList<Integer> source = findSource(yourCards.get(i));
            backg.drawImage(cardImages, 100 + 65 * i, 400 - adder, 165 + 65 * i, 500 - adder, source.get(0), source.get(1), source.get(2), source.get(3), null);
        }
        if (moveCards.size() != 0 || comboMoveCards.size() != 0)
        {
            if (getBest(moveCards, moveScores, comboMoveCards, comboMoveScores).contains("-"))
            {
                ArrayList<String> combo = splitCombo(getBest(moveCards, moveScores, comboMoveCards, comboMoveScores));
                for (int i = 0; i < combo.size(); i++)
                {
                    ArrayList<Integer> sourceBest = findSource(combo.get(i));
                    backg.drawImage(cardImages, 300 + 65 * i, 300, 365 + 65 * i, 400, sourceBest.get(0), sourceBest.get(1), sourceBest.get(2), sourceBest.get(3), null);
                }
            }
            else
            {
                ArrayList<Integer> sourceBest = findSource(getBest(moveCards, moveScores, comboMoveCards, comboMoveScores));
                backg.drawImage(cardImages, 300, 300, 365, 400, sourceBest.get(0), sourceBest.get(1), sourceBest.get(2), sourceBest.get(3), null);
            }
        }
        ArrayList<Integer> floorCoord = findSource(floorCard);
        // drawing floorcard
        backg.drawImage(cardImages, 300, 250, 365, 350, floorCoord.get(0), floorCoord.get(1), floorCoord.get(2), floorCoord.get(3), null);
        /*try
        {
            Thread.sleep(1000);
        }
        catch(Exception E)
        {

        } */
        g.drawImage(backImage, 0, 0, null);
    }



    public static void main(String args[]) {

        Main gameWindow = new Main();
        
        for (int suite = 0; suite < 4; suite++)
        {
            for (int num = 1; num < 14; num++)
            {
                String letter = "";
                if (suite == 0)
                {
                    letter = "s";
                }
                else if (suite == 1)
                {
                    letter = "h";
                }
                else if (suite == 2)
                {
                    letter = "d";
                }
                else if (suite == 3)
                {
                    letter = "c";
                }
                letter += String.valueOf(num);
                deck.add(letter);
            }
        }

        floorCard = dealRegular(deck); //MAKE SURE FLOORCARD CANNOT BE 8, JACK or TWO


        for (int i = 0; i < 8; i++)
        {
            cards.add(dealCard(deck));
        }
        //cards.add("s8");
        //cards.add("c8");
        //cards.add("d8");
        //cards.add("h8");
        for (int i = 0; i < 8; i++)
        {
            yourCards.add(dealCard(deck));
        }
        //yourCards.add("s3");
        //yourCards.add("c3");
        //yourCards.add("d3");
        //yourCards.add("h3");
        //System.out.println(deck);
        //System.out.println("Your cards: " + yourCards);

        //String playerEightSuit = "";
        String tempSuit = "";
        int k = 1;
        boolean skipEnemy = false;
        boolean skipPlayer = false;
        boolean computerPickUp = false;
        while (cards.size() != 0 && yourCards.size() != 0)
        {
            try
            {
                Thread.sleep(1000 * k);
                k *= 0;
            }
            catch(Exception E)
            {
                return;
            }
            gameWindow.repaint();

            if (skipEnemy == false) {
                for (int i = 0; i < cards.size(); i++) {
                    //System.out.println(playerEightSuit);
                    evaluate1(floorCard, cards.get(i), moveCards, moveScores, cards, tempSuit);
                }
                for (int i = 0; i < moveCards.size(); i++) {
                    evaluateMore(moveCards.get(i), cards, comboMoveCards, comboMoveScores, tempSuit);
                }
            }

            System.out.println("Computer Cards: " + cards);
            System.out.println("Floor Card: " + floorCard);
            //System.out.println("Possible Single Moves: " + moveCards);
            //System.out.println("Possible Single Scores: " + moveScores);
            //System.out.println("Possible Combo Moves: " + comboMoveCards);
            //System.out.println("Possible Combo Scores: " + comboMoveScores);

            if (cards.size() != 0)
            {
                if (moveCards.size() != 0 || comboMoveCards.size() != 0)
                {
                    String best = getBest(moveCards, moveScores, comboMoveCards, comboMoveScores);
                    System.out.println("Computer Move: " + best); //best move

                    if (!best.contains("-"))
                    {
                        cards.remove(best);
                        addBackComp.add(best);
                        floorCard = best;
                        if (getNum(best) == 8)
                        {
                            tempSuit = mostSuit(cards);
                        }
                        else if (getNum(best) == 2)
                        {
                            for (int i = 0; i < 2; i++)
                            {
                                yourCards.add(dealCard(deck));
                            }
                        }
                        else if (getNum(best) == 11)
                        {
                            skipPlayer = true;
                        }
                        //moveScores.remove(moveCards.indexOf(best));
                        //moveCards.remove(best);
                    } else {
                        ArrayList<String> remover = splitCombo(best);
                        addBackComp.addAll(remover);
                        for (int i = 0; i < remover.size(); i++) {
                            cards.remove(remover.get(i));
                            //comboMoveScores.remove(comboMoveCards.indexOf(best));
                            //comboMoveCards.remove(best);
                            floorCard = remover.get(i);
                        }
                    }
                    moveCards.clear();
                    moveScores.clear();
                    comboMoveCards.clear();
                    comboMoveScores.clear();
                    if (getNum(floorCard) == 8)
                    {
                        globalTempSuit = tempSuit;
                        changedSuit = true;
                        System.out.println("The suit was changed to " + tempSuit);
                    }
                    gameWindow.repaint();
                    deck.addAll(addBack);
                    addBack.clear();
                }
                else if(skipEnemy == false && computerPickUp == false)
                {
                    computerPickUp = true;
                    cards.add(dealCard(deck));
                    continue;
                }
                //else if(getNum(floorCard) == 8)
                {
                    //tempSuit = playerEightSuit; // if computer couldnt go then you take the playereightsuit  back
                }
                skipEnemy = false;
            }
            else
            {
                break;
            }
            if (cards.size() == 0)
            {
                suitChoose = false;
                yourTurn = false;
                changedSuit = false;
                computerWon = true;
                gameWindow.repaint();
                break;
            }
            System.out.println("///////////////////////////////");
            System.out.println("Your cards: " + yourCards);
            System.out.println("Floor Card: " + floorCard);
            //Scanner myObj = new Scanner(System.in);
            computerPickUp = false;
            boolean cardPickUp = false;
            while(skipPlayer == false)
            {
                playedDouble = false;
                //int chooseCard = myObj.nextInt();
                int chooseCard = 0;
                System.out.println("Choose Card");
                yourTurn = true;
                while(true)
                {
                    response = -2;
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch(Exception e)
                    {

                    }
                    if (response >= -1 && response < yourCards.size())
                    {
                        chooseCard = response + 1;
                        if (selected.contains(chooseCard - 1))
                        {
                            selected.remove(Integer.valueOf(chooseCard - 1));
                        }
                        else if (response == -1)
                        {
                            break;
                        }
                        else if (getNum(yourCards.get(chooseCard - 1)) == 8 || getNum(yourCards.get(chooseCard - 1)) == 2 || getNum(yourCards.get(chooseCard - 1)) == 11)
                        {
                            break;
                        }
                        else if (selected.size() == 0)
                        {
                            selected.add(chooseCard - 1);
                        }
                        else if (getNum(yourCards.get(chooseCard - 1)) == getNum(yourCards.get(selected.get(0))))
                        {
                            selected.add(chooseCard - 1);
                        }
                        //System.out.println(selected.size());
                    }
                    if (chooseCard - 1 >= 0 && (selected.size() == numCount(yourCards.get(chooseCard - 1), yourCards) || selected.size() == 4))
                    {
                        break;
                    }
                    gameWindow.repaint();
                }
                if (chooseCard == 0) // choose you cant go for now (now pick up)
                {
                    //if (getNum(floorCard) == 8)
                    {
                        //playerEightSuit = tempSuit; // if you couldnt go then the computer takes the tempsuit of their eight back
                    }
                    if (cardPickUp == false)
                    {
                        selected.clear();
                        yourCards.add(dealCard(deck));
                        gameWindow.repaint();
                        cardPickUp = true;
                        continue;
                    }
                    else
                    {
                        selected.clear();
                        gameWindow.repaint();
                        System.out.println("///////////////////////////////");
                        break;
                    }
                }
                // play doubles and such
                if (selected.size() > 1)
                {
                    suitChoose = false;
                    changedSuit = false;
                    for (int i = 0; i < selected.size(); i++)
                    {
                        if (getNum(yourCards.get(selected.get(i))) == getNum(floorCard))
                        {
                            gameWindow.repaint();
                            System.out.println("///////////////////////////////");
                            floorCard = yourCards.get(selected.get(selected.size() - 1));
                            Collections.sort(selected);
                            for (int j = selected.size() - 1; j >= 0; j--)
                            {
                                addBack.add(yourCards.get(selected.get(j)));
                                yourCards.remove((int) selected.get(j));
                            }
                            playedDouble = true;
                            break;
                        }
                        else if ((getSuit(yourCards.get(selected.get(i))).equals(getSuit(floorCard)) && getNum(floorCard) != 8) || (getNum(floorCard) == 8 && getSuit(yourCards.get(selected.get(i))).equals(tempSuit)))
                        {
                            //System.out.println("TEMPSUIT " + tempSuit);
                            gameWindow.repaint();
                            if (i == selected.size() - 1)
                            {
                                floorCard = yourCards.get(selected.get(selected.size() - 2));
                            }
                            else
                            {
                                floorCard = yourCards.get(selected.get(selected.size() - 1));
                            }
                            Collections.sort(selected);
                            for (int j = selected.size() - 1; j >= 0; j--)
                            {
                                addBack.add(yourCards.get(selected.get(j)));
                                yourCards.remove((int) selected.get(j));
                            }
                            playedDouble = true;
                            break;
                        }
                    }
                    if (playedDouble == true)
                    {
                        gameWindow.repaint();
                        deck.addAll(addBackComp);
                        addBackComp.clear();
                        break;
                    }
                    gameWindow.repaint();
                    selected.clear();
                }
                if (playedDouble == false && getNum(floorCard) == 8)
                {
                    if (isValidEight(yourCards.get(chooseCard - 1), tempSuit))
                    {
                        System.out.println("You chose: " + yourCards.get(chooseCard - 1));
                        if (getNum(yourCards.get(chooseCard - 1)) == 8)
                        {
                            System.out.println("New Suit?");
                            changedSuit = false;
                            suitChoose = true;
                            gameWindow.repaint();
                            while (true)
                            {
                                //Scanner myReader = new Scanner(System.in);
                                //String chooseSuit = myReader.nextLine();
                                response = -1;
                                try
                                {
                                    Thread.sleep(5);
                                }
                                catch(Exception e)
                                {

                                }
                                if (response >= 0 && response < yourCards.size())
                                {
                                    tempSuit = getSuit(yourCards.get(response));
                                    break;
                                }
                                /*if (chooseSuit.toLowerCase().equals("c"))
                                {
                                    tempSuit = "c";
                                    break;
                                }
                                else if (chooseSuit.toLowerCase().equals("d"))
                                {
                                    tempSuit = "d";
                                    break;
                                }
                                else if (chooseSuit.toLowerCase().equals("s"))
                                {
                                    tempSuit = "s";
                                    break;
                                }
                                else if (chooseSuit.toLowerCase().equals("h"))
                                {
                                    tempSuit = "h";
                                    break;
                                }
                                else
                                {
                                    System.out.println("Invalid Suit");
                                }*/
                            }
                            suitChoose = false;
                        }
                        else if (getNum(yourCards.get(chooseCard - 1)) == 2)
                        {
                            for (int i = 0; i < 2; i++)
                            {
                                cards.add(dealCard(deck));
                            }
                        }
                        else if (getNum(yourCards.get(chooseCard - 1)) == 11)
                        {
                            skipEnemy = true;
                        }
                        changedSuit = false;
                        gameWindow.repaint();
                        System.out.println("///////////////////////////////");
                        floorCard = yourCards.get(chooseCard - 1);
                        addBack.add(yourCards.get(chooseCard - 1));
                        yourCards.remove(chooseCard - 1);
                        break;
                    }
                    else
                    {
                        System.out.println("Invalid Card");
                    }
                }
                else if (playedDouble == false && isValid(yourCards.get(chooseCard - 1), floorCard))
                {
                    System.out.println("You chose: " + yourCards.get(chooseCard - 1));
                    if (getNum(yourCards.get(chooseCard - 1)) == 8)
                    {
                        System.out.println("New Suit?");
                        changedSuit = false;
                        suitChoose = true;
                        gameWindow.repaint();
                        while (true)
                        {
                            //Scanner myReader = new Scanner(System.in);
                            //String chooseSuit = myReader.nextLine();
                            response = -1;
                            try
                            {
                                Thread.sleep(5);
                            }
                            catch(Exception e)
                            {

                            }
                            if (response >= 0 && response < yourCards.size())
                            {
                                tempSuit = getSuit(yourCards.get(response));
                                break;
                            }
                            /*if (chooseSuit.toLowerCase().equals("c"))
                            {
                                tempSuit = "c";
                                break;
                            }
                            else if (chooseSuit.toLowerCase().equals("d"))
                            {
                                tempSuit = "d";
                                break;
                            }
                            else if (chooseSuit.toLowerCase().equals("s"))
                            {
                                tempSuit = "s";
                                break;
                            }
                            else if (chooseSuit.toLowerCase().equals("h"))
                            {
                                tempSuit = "h";
                                break;
                            }
                            else
                            {
                                System.out.println("Invalid Suit");
                            }*/
                        }
                        suitChoose = false;
                    }
                    else if (getNum(yourCards.get(chooseCard - 1)) == 2)
                    {
                        for (int i = 0; i < 2; i++)
                        {
                            cards.add(dealCard(deck));
                        }
                    }
                    else if (getNum(yourCards.get(chooseCard - 1)) == 11)
                    {
                        skipEnemy = true;
                    }
                    changedSuit = false;
                    gameWindow.repaint();
                    System.out.println("///////////////////////////////");
                    addBack.add(yourCards.get(chooseCard - 1));
                    floorCard = yourCards.get(chooseCard - 1);
                    yourCards.remove(chooseCard - 1);
                    break;
                }
                else if (playedDouble == false)
                {
                    System.out.println("Invalid Card");
                }
                yourTurn = false;
            }
            if (skipPlayer == true)
            {
                changedSuit = false;
                System.out.println("Your turn was skipped");
                System.out.println("///////////////////////////////");
                skipPlayer = false;
            }

            selected.clear();
            gameWindow.repaint();
            deck.addAll(addBackComp);
            addBackComp.clear();
            if (yourCards.size() == 0)
            {
                suitChoose = false;
                yourTurn = false;
                changedSuit = false;
                playerWon = true;
                gameWindow.repaint();
                break;
            }
        }
    }

    public static String dealCard(ArrayList<String> deck)
    {
        Random r = new Random();
        int randomNum = r.nextInt(deck.size());
        String card = deck.get(randomNum);
        deck.remove(randomNum);
        return card;
    }

    public static String dealRegular(ArrayList<String> deck)
    {
        Random r = new Random();
        int randomNum = r.nextInt(deck.size());
        String card;
        while(true)
        {
            card = deck.get(randomNum);
            if(getNum(card) == 8 || getNum(card) == 11 || getNum(card) == 2)
            {
                randomNum = r.nextInt(deck.size());
            }
            else
            {
               break;
            }
        }
        deck.remove(randomNum);
        return card;
    }

    public static ArrayList<Integer> findSource(String card)
    {
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        int x1;
        int x2;
        int y1;
        int y2;

        int row = 0;
        if (getSuit(card).equals("h"))
        {
            row = 1;
        }
        else if (getSuit(card).equals("d"))
        {
            row = 2;
        }
        else if (getSuit(card).equals("s"))
        {
            row = 3;
        }
        else if (getSuit(card).equals("c"))
        {
            row = 4;
        }
        x1 = (getNum(card) - 1) * 65;
        y1 = (row - 1) * 100;
        x2 = (getNum(card) - 1) * 65 + 65;
        y2 = (row - 1) * 100 + 100;
        coordinates.add(x1);
        coordinates.add(y1);
        coordinates.add(x2);
        coordinates.add(y2);
        return coordinates;
    }

    public static String getBest(ArrayList<String> moveCards, ArrayList<Integer> moveScores, ArrayList<String> comboCards, ArrayList<Integer> comboScores)
    {
        int largest;
        int largestCombo;
        try
        {
            largest = Collections.max(moveScores);
        }
        catch(Exception e)
        {
            largest = -1000;
        }
        try
        {
            largestCombo = Collections.max(comboScores);
        }
        catch(Exception e)
        {
            largestCombo = -1000;
        }
        if (largest > largestCombo)
        {
            int index = moveScores.indexOf(largest);
            return moveCards.get(index);
        }
        else
        {
            int index = comboScores.indexOf(largestCombo);
            return comboCards.get(index);
        }
    }

    public static String getSuit(String floorCard)
    {
        return floorCard.substring(0,1);
    }

    public static int getNum(String floorCard)
    {
        return Integer.parseInt(floorCard.substring(1));
    }

    public static int suitCount(String myCard, ArrayList<String> cards)
    {
        int suitCount = 0;
        for (int i = 0; i < cards.size(); i++)
        {
            if (getSuit(myCard).equals(getSuit(cards.get(i))) && getNum(cards.get(i)) != 8)
            {
                suitCount += 1;
            }
        }
        return suitCount;
    }

    // Gets the suit count for every card in an array of cards
    public static ArrayList<Integer> suitCounts(ArrayList<String> myCards, ArrayList<String> cards)
    {
        ArrayList<Integer> suitCounts = new ArrayList<Integer>();
        for (int i = 0; i < myCards.size(); i++)
        {
            int suitCount = 0;
            String suit = getSuit(myCards.get(i));
            for (int j = 0; j < cards.size(); j++)
            {
                if (suit.equals(getSuit(cards.get(j))) && getNum(cards.get(j)) != 8) {
                    suitCount += 1;
                }
            }
            suitCounts.add(suitCount);
        }
        return suitCounts;
    }

    public static int numCount(String myCard, ArrayList<String> cards)
    {
        int num = getNum(myCard);
        int numCount = 0;
        for (int i = 0; i < cards.size(); i++)
        {
            if (getNum(myCard) == getNum(cards.get(i)))
            {
                numCount += 1;
            }
        }
        return numCount;
    }

    public static boolean islastsuit(String myCard, ArrayList<String> cards)
    {
        for (int i = 0; i < cards.size(); i++)
        {
            if (getSuit(myCard).equals(getSuit(cards.get(i))) && !myCard.equals(cards.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValid(String myCard, String floorCard)
    {
        if (getNum(myCard) == 8)
        {
            return true;
        }
        else if (getNum(floorCard) == getNum(myCard))
        {
            return true;
        }
        else if (getSuit(floorCard).equals(getSuit(myCard)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static boolean isValidEight(String myCard, String tempSuit)
    {
        if (getNum(myCard) == 8)
        {
            return true;
        }
        else if (getSuit(myCard).equals(tempSuit))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Gets a card in your hand that has the same number as the card you give it
    public static String sameNum(String myCard, ArrayList<String> cards)
    {
        String sameNum = null;
        for (int i = 0; i < cards.size(); i++)
        {
            if (getNum(myCard) == getNum(cards.get(i)) && !myCard.equals(cards.get(i)))
            {
                sameNum = cards.get(i);
            }
        }
        return sameNum;
    }

    // Similar to sameNum but instead returns multiple
    public static ArrayList<String> sameNums(String myCard, ArrayList<String> cards)
    {
        ArrayList<String> sameNums = new ArrayList<String>();
        for (int i = 0; i < cards.size(); i++)
        {
            if (getNum(myCard) == getNum(cards.get(i)) && !myCard.equals(cards.get(i)))
            {
                sameNums.add(cards.get(i));
            }
        }
        return sameNums;
    }

    public static String mostSuit(ArrayList<String> cards)
    {
        int hcount = 0;
        int scount = 0;
        int ccount = 0;
        int dcount = 0;

        for (int i = 0; i < cards.size(); i++)
        {
            String card = cards.get(i);
            if (getSuit(card).equals("h"))
            {
                hcount += 1;
            } else if (getSuit(card).equals("d"))
            {
                dcount += 1;
            } else if (getSuit(card).equals("s"))
            {
                scount += 1;
            } else if (getSuit(card).equals("c"))
            {
                ccount += 1;
            }
        }
        ArrayList<Integer> counts = new ArrayList<Integer>();
        counts.add(hcount);
        counts.add(dcount);
        counts.add(scount);
        counts.add(ccount);
        int value = Collections.max(counts);
        if (counts.indexOf(value) == 0)
        {
            return "h";
        }
        else if (counts.indexOf(value) == 1)
        {
            return "d";
        }
        else if (counts.indexOf(value) == 2)
        {
            return "s";
        }
        else if (counts.indexOf(value) == 3)
        {
            return "c";
        }
        else
        {
            return "fail";
        }
    }

    // All numbers in an integer arraylist equal to an integer
    public static boolean allEqualTo(ArrayList<Integer> numbers, int num)
    {
        for (int i = 0; i < numbers.size(); i++)
        {
            if (numbers.get(i) != num)
            {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> splitCombo(String comboCards)
    {
        String[] separated = comboCards.split("-");
        ArrayList<String> converted = new ArrayList<String>();
        for (int i = 0; i < separated.length; i++)
        {
            converted.add(separated[i]);
        }
        return converted;
    }

    public static void evaluate1(String floorCard, String myCard, ArrayList<String> movecards, ArrayList<Integer> movescores, ArrayList<String> cards, String tempEightSuit)
    {
        int score = 0;
        String floorsuit = getSuit(floorCard);
        int floornumber = getNum(floorCard);
        String suit = getSuit(myCard);
        int number = getNum(myCard);

        // Placing 8
        if (number == 8)
        {
            score -= 100;
            movecards.add(myCard);
            movescores.add(score);
            return;
        }

        if (floornumber == 8)
        {
            floorsuit = tempEightSuit;
            //System.out.println("floorsuit " + floorsuit);
        }

        if (!floorsuit.equals(suit) && floornumber != number)
        {
            return;
        }

        // Place a card
        if (floornumber == number || floorsuit.equals(suit))
        {
            score += 2;

            // How many cards of that suit it will lead to
            score += suitCount(myCard, cards);
        }

        // Placing Jack
        if (number == 11)
        {
            score += 7;
            // Since jack skips enemy turn it is very bad for you to use a jack and not be able to place anything on it afterwards
            if (suitCount(myCard, cards) == 1 && numCount(myCard, cards) == 1)
            {
                score -= 50;
            }
            else if (suitCount(myCard, cards) == 1 && numCount(myCard, cards) == 2 && suitCount(sameNum(myCard, cards), cards) == 1)
            {
                score -= 25;
            }
            else if (suitCount(myCard, cards) == 1 && numCount(myCard, cards) == 3 && allEqualTo(suitCounts(sameNums(myCard, cards), cards), 1))
            {
                score -= 10;
            }

            if (floornumber == 11 && numCount(myCard, cards) == 2 && suitCount(sameNum(myCard, cards), cards) >= 2)
            {
                score += 3 * suitCount(sameNum(myCard, cards), cards);
            }
            else if (floornumber == 11 && numCount(myCard, cards) == 3)
            {
                ArrayList<Integer> elevensuitcounts = suitCounts(sameNums(myCard, cards), cards);
                //System.out.println(sameNums(myCard, cards));
                //System.out.println(suitCounts(sameNums(myCard, cards), cards));
                score += 3 * (elevensuitcounts.get(0) + elevensuitcounts.get(1));
            }
        }

        // Placing 2
        // think about how many cards the opponent has -> NAH
        if (number == 2)
        {
            score += 5;
        }

        // Place last card of a specific suit
        if (islastsuit(myCard, cards))
        {
            score -= 3;
        }



        movecards.add(myCard);
        movescores.add(score);
    }

    public static void evaluateMore(String myCard, ArrayList<String> cards, ArrayList<String> comboMoveCards, ArrayList<Integer> comboMoveScores, String tempEightSuit)
    {
        int score = 0;
        String suit = getSuit(myCard);
        int number = getNum(myCard);

        if (number == 8 || number == 11 || number == 2)
        {
            return;
        }

        String combo = myCard + "-";
        // Finding all combo cards
        if (numCount(myCard, cards) == 1)
        {
            return;
        }
        else if (numCount(myCard, cards) > 1)
        {
            ArrayList<String> multiple = sameNums(myCard, cards);
            if (multiple.size() == 1)
            {
                score += 4;
            }
            else if (multiple.size() == 2)
            {
                score += 8;
            }
            else if (multiple.size() == 3)
            {
                score += 16;
            }
            combo += String.join("-", multiple);
        }

            comboMoveCards.add(combo);
            comboMoveScores.add(score);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        response = ResolveClick(e.getPoint());
    }

    public int ResolveClick(Point pos)
    {
        // calculate which card was clicked
        int index = (pos.x - 100)/65;
        int yChecker = pos.y;
        if (pos.y < 501 && pos.y > 399 && pos.x > 99)
        {
            //System.out.println(index);
            return index;
        }
        else if (pos.x < 100 && pos.x > 30 && pos.y < 501 && pos.y > 399)
        {
            return -1;
        }
        else
        {
            return -2;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
