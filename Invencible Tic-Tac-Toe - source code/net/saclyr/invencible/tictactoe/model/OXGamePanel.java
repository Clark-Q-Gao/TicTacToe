/*
 * This game is free to play, and the source code is free to use, 
 * for educational purposes.
 * I hope you like it.
 * Pass by http://saclyr.net and support my website and my cause by donating.
 * 
 * Thank you for downloading it.
 */

package net.saclyr.invencible.tictactoe.model;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.saclyr.invencible.tictactoe.data.Computer;
import net.saclyr.invencible.tictactoe.data.GameBoard;
import net.saclyr.invencible.tictactoe.data.Player;

/**
 *
 * @author Saclyr
 */
public class OXGamePanel extends JPanel {
    
    private final GameBoard gameBoard;
    private final Computer computer;
    
    private final JLabel[] oxBoard;
    private final boolean[] oxCellPlayed;
    
    /**
     * The reason I create an anonymous Runnable object, it's because I don't 
     * want it to be external, but to be used in the game only.<br>
     * It's not necessary, but a good practice.
     */
    private final Runnable gameLoop;
    
    private GameBoard.TURN playerTurn;
    
    /**
     * This variable is what keeps the game loop active. Once the isGameOver 
     * equals true it leaves the game loop and it checks if the player want to 
     * play again.
     */
    private boolean isGameOver;
    
    /**
     * isPlaying makes sure that even after game over, you are still 
     * in the game and you might still want to play.
     */
    private boolean isPlaying;
    
    /**
     * This variable only purpose is to guard against spurious wakeups.
     */
    private boolean hasPlayed;
    
    OXGamePanel( Color backGround ) {
        super( new java.awt.GridLayout( 3, 3, 10, 10 ) );
        
        gameBoard = GameBoard.getGameBoard();
        computer = Computer.getComputer();
        
        setBackground( backGround );
        setBorder( javax.swing.BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        
        /**
         * If you analize the anounymous object of MouseListener, you will 
         * notice the following:
         * 
         *     1st - The variable isMousePressed will only turn true if the 
         * mouse is houvering one of its cells, and if the cell hasn't be 
         * played;
         *     2nd - If you press the cell, but move the mouse, by 
         * accident per example, the cell is stil viable to be played;
         *     3rd - If while pressing the the mouse button, the mouse houvers 
         * outside the cell, by drag, the variable isMousePressed will 
         * turn false, so that no other cell is played;
         *     4th - If the mouse is stll houvering the cell, and the
         * mouse is still pressed, once the mouse button is released, 
         * the cell will be played.
         */
        MouseListener myMouseListener = new MouseListener() {
            
            private boolean isMousePressed = false;

            @Override
            public void mouseEntered( MouseEvent e ) {
                mouseMover( e, java.awt.Cursor.HAND_CURSOR );
            }

            @Override
            public void mouseExited( MouseEvent e ) {
                mouseMover( e, java.awt.Cursor.DEFAULT_CURSOR );
            }
            
            /**
             * This is an helper method that returns the number conrespondent 
             * with the source, which we know to ba an JLabel, but we want to 
             * avoid an upcasting.
             */
            private int getHouseSource( Object source ) {
                
                for ( int i = 0 ; i < 9; i++ ) {
                    if( source == oxBoard[i] )
                        return i;
                }
                
                return -1;
                
            }
            
            /**
             * This method is a helper method, to prevent code duplication.<br>
             * If anything goes wrong, it is easier to debug it.
             */
            private void mouseMover( MouseEvent e, int toCursor ) {
                if( !isGameOver ) {
                    int house = getHouseSource( e.getSource() );
                    
                    if( isMousePressed && e.getID() == MouseEvent.MOUSE_EXITED ) {
                        isMousePressed = false;
                    }
                    
                    if( !oxCellPlayed[house] ) {
                        setCursor( Cursor.getPredefinedCursor( toCursor ) );
                    }
                }
            }
            
            @Override
            public void mousePressed( MouseEvent e ) {
                if( !isGameOver && !isMousePressed ) {
                    int house = getHouseSource( e.getSource() );
                    if( !oxCellPlayed[house] ) {
                        isMousePressed = true;
                    }
                }
            }
            
            @Override
            public void mouseReleased( MouseEvent e ) {
                if( !isGameOver && isMousePressed ) {
                    int house = getHouseSource( e.getSource() );
                    makeMove( house );
                    hasPlayed = true;
                    isMousePressed = false;
                    
                    synchronized( gameLoop ) {
                        gameLoop.notify();
                    }
                }
            }

            @Override public void mouseClicked( MouseEvent e ) {}
            
            
        };
        
        oxCellPlayed = new boolean[9];
        oxBoard = new JLabel[9];
        //"Tahoma" or "Default" is the same, but I prefere to leave it explicit.
        Font oxFont = new Font( "Tahoma", 1, 128 );
        for ( int i = 0 ; i < 9; i++ ) {
            oxBoard[i] = new JLabel("   ");
            oxBoard[i].setOpaque( true );//JLabels have default Opaque set as false.
            oxBoard[i].setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
            oxBoard[i].setVerticalAlignment( javax.swing.SwingConstants.CENTER );
            oxBoard[i].setBackground( Color.WHITE );
            oxBoard[i].setFont( oxFont );
            
            oxBoard[i].addMouseListener( myMouseListener );
            
            add( oxBoard[i] );
        }
        
        isGameOver = true;
        
        gameLoop = new Runnable() {

            @Override
            public void run() {
                
                do {
                    
                    newGame();
                    
                    do {
                        
                        GameBoard.TURN turn = gameBoard.getCurrentTurn();
                        
                        if( turn == playerTurn )
                            awaitMove();
                        else
                            computerMove();
                        
                        
                        if( gameBoard.isGameOver() )
                            gameOver();
                        else
                            gameBoard.changeTurn();
                        
                    }
                    while( !isGameOver );
                    
                    
                    int choice = JOptionPane.showConfirmDialog( 
                            OXGamePanel.this, 
                            "Do you wish to play again?", 
                            "Play again?", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE 
                    );
                    
                    isPlaying = choice == JOptionPane.YES_OPTION;
                    if( isPlaying )
                        setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
                    
                }
                while( isPlaying );
                
                Player.getPlayer().saveRecords();
                ((InvencibleTicTacToe)getTopLevelAncestor()).actualizeRecords();
                
                JPanel gameHolder = (JPanel)getParent();
                
                java.awt.CardLayout gameHolderLayout = 
                        (java.awt.CardLayout)gameHolder.getLayout();
                
                gameHolderLayout.show( gameHolder, "Main Panel" );
                
            }
            
            private synchronized void awaitMove() {
                try {
                    while( !hasPlayed )
                        wait();
                }
                catch( InterruptedException ex ) {}
                
                hasPlayed = false;
            }
            
        };
        
        
    }
    
    private void newGame() {
        
        isGameOver = false;
        
        for ( int i = 0 ; i < 9; i++ ){
            oxBoard[i].setText("   ");
            oxBoard[i].setForeground( Color.BLACK );
            oxCellPlayed[i] = false;
        }
        
        gameBoard.resetGame();
    }
    
    private void computerMove() {
        int houseToPlay = gameBoard.playComputer();
        makeMove( houseToPlay );
    }
    
    private void gameOver() {
        isGameOver = true;
        
        GameBoard.TURN winner = gameBoard.getGameOverState();
        
        Player player = Player.getPlayer();
        
        
        player.incrementGamesPlayed();
        
        if( winner == null ) {  //Game tied.
            
            player.incrementGameTies();
            
            JOptionPane.showMessageDialog( this, "The game ended with a draw.", 
                    "Game draw", JOptionPane.INFORMATION_MESSAGE);
        }
        else if( winner == playerTurn ) {   //What?
            
            playerTurn = GameBoard.TURN.X;
            computer.setTurn( GameBoard.TURN.O );
            
            player.incrementPlayerWins();
            
            JOptionPane.showMessageDialog( this, "The game ended with your victory.", 
                    "Wait, WHAT?", JOptionPane.INFORMATION_MESSAGE);
        }
        else {  //Computer wins.
            
            playerTurn = GameBoard.TURN.O;
            computer.setTurn( GameBoard.TURN.X );
            
            JOptionPane.showMessageDialog( this, "The game ended with the computer winning.", 
                    "Computer wins", JOptionPane.INFORMATION_MESSAGE);
            //There is no need to record the computer victorys.
        }
        
    }
    
    private void makeMove( int house ) {
        GameBoard.TURN turn = gameBoard.getCurrentTurn();
        
        oxBoard[house].setText(" " + turn.name() + " ");
        oxBoard[house].setForeground( turn.getOxColor() );
        
        oxCellPlayed[house] = true;
        
        gameBoard.play( house );
        
        if( getCursor() != Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) )
            setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
    }
    
    void playGame() {
        
        newGame();  //To clean everything just in case you leave and enter again.
        
        int choice = JOptionPane.showConfirmDialog( 
                this, 
                "Do you wish to start first?",
                "First to go?", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE 
        );
        
        playerTurn = 
                choice == JOptionPane.YES_OPTION ? 
                    GameBoard.TURN.X : GameBoard.TURN.O;
        
        computer.setTurn( playerTurn.getOpositeTurn() );
        
        Thread gameThread = new Thread( gameLoop, "Game" );
        gameThread.start();
        
        
    }
    
    
}
