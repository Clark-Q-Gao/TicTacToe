/*
 * This game is free to play, and the source code is free to use, 
 * for educational purposes.
 * I hope you like it.
 * Pass by http://saclyr.net and support my website and my cause by donating.
 * 
 * Thank you for downloading it.
 */

package net.saclyr.invencible.tictactoe.data;

/**
 * This class encapsulates the gameBoard to be used and respective methods to 
 * keep track of the game, like gameovers, who wins and to return the computer 
 * move.
 * 
 * @author Saclyr
 */
public class GameBoard {
    
    private static final GameBoard gameBoard = new GameBoard();
    
    /**
     * There is only two turns, the X and the O.
     */
    public enum TURN {
        
        /**
         * The player that has X TURN will be the first to play.
         */
        X( new java.awt.Color( 87, 208, 159 ) ), 
        
        /**
         * The player that has O TURN will be the second to play.
         */
        O( new java.awt.Color( 184, 208, 87 ) );
        
        private final java.awt.Color oxColor;
        
        /**
         * The enums can have constructors, but they MUST be private because 
         * they are singleton instance instatiated by the JVM.
         */
        private TURN( java.awt.Color oxColor ) {
            this.oxColor = oxColor;
        }
        
        /**
         * We can retrive the Enum object that represents the oposite turn of 
         * this Enum.
         * <br>
         * Basically, If this Enum is {@link #X}, {@link #O} is returned, 
         * and if this Enum is {@link #O}, {@link #X} is returned.
         */
        public TURN getOpositeTurn() {
            /**
             * This is called a ternary operator.
             * 
             * It is the same as:
             * 
             * if( this == X )
             *     return O;
             * else
             *     return X;
             */
            return this == X ? O : X;
        }
        
        /**
         * I want to make the diferent OX simbols to have diferent colors, 
         * which itwill be retrieved later.
         */
        public java.awt.Color getOxColor() {
            return oxColor;
        }
        
    }
    
    /**
     * &nbsp;&nbsp;j&nbsp;|0 1 2| <br>
     * i&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| <br>
     * ----|_____| <br>
     * 0&nbsp;&nbsp;&nbsp;|_|_|_| <br>
     * 1&nbsp;&nbsp;&nbsp;|_|_|_| <br>
     * 2&nbsp;&nbsp;&nbsp;|_|_|_| <br>
     * ----
     */
    private final TURN[][] board;
    private TURN currentTurn;
    
    private GameBoard() {
        board = new TURN[3][3];
    }
    
    /**
     * It is not necessarie to keep it singleton, 
     * but it is a good practicie to do so.
     */
    public static GameBoard getGameBoard() {
        return gameBoard;
    }
    
    /**
     * Basically, by receiving a value representing the house cell, it converts 
     * to the i and j coorednates of the 2D array (an array of array) that 
     * represents the game board, and fills the zone with the current turn.
     * 
     * @param house Is the value of the cell that varies from 0 to 8. The method 
     * will later add one(1) to the value.
     */
    public void play( int house ) {
        house += 1;
        int i = (house - 1)/3;
        int j = house - 1 - i*3;
        
        board[i][j] = currentTurn;
    }
    
    public void resetGame() {
        for ( int i = 0 ; i < 3; i++ )
            for ( int j = 0 ; j < 3; j++ )
                board[i][j] = null;
        
        currentTurn = TURN.X;
    }
    
    public void changeTurn() {
        currentTurn = currentTurn.getOpositeTurn();
    }
    
    public TURN getCurrentTurn() {
        return currentTurn;
    }
    
    /**
     * This method returns the winner in case of game over.<br>
     * It can return null if is a draw.
     */
    public TURN getGameOverState() {
        int count = 0;
        TURN turnToReturn;
        
        /**
         * First we iterate over the rows.
         */
        for ( int i = 0 ; i < 3; i++ ) {
            count = 0;
            turnToReturn = null;
            for ( int j = 0 ; j < 3; j++ ) {
                TURN toCheck = board[i][j];
                
                if( toCheck == null )
                    break;
                else if( turnToReturn == null )
                    turnToReturn = toCheck;
                
                if( toCheck == turnToReturn )
                    count++;
                else
                    break;
                
            }
            
            if( count == 3 )
                return turnToReturn;
            
        }
        
        /**
         * Now we iterate over the collums.
         */
        for ( int j = 0 ; j < 3; j++ ) {
            count = 0;
            turnToReturn = null;
            for ( int i = 0 ; i < 3; i++ ) {
                TURN toCheck = board[i][j];
                
                if( toCheck == null )
                    break;
                else if( turnToReturn == null )
                    turnToReturn = toCheck;
                
                if( toCheck == turnToReturn )
                    count++;
                else
                    break;
                
            }
            
            if( count == 3 )
                return turnToReturn;
            
        }
        
        count = 0;
        turnToReturn = null;
        
        /**
         * Now we iterate over the first diagonal, from [0,0] to [2,2].
         */
        for ( int i = 0, j = 0 ; i < 3 && j < 3; i++, j++ ) {
            TURN toCheck = board[i][j];
            
            if( toCheck == null )
                break;
            else if( turnToReturn == null )
                turnToReturn = toCheck;
            
            if( toCheck == turnToReturn )
                count++;
            else
                break;
            
        }
        
        if( count == 3 )
            return turnToReturn;
        
        count = 0;
        turnToReturn = null;
        
        /**
         * Finnaly we iterate over the first diagonal, from [2,0] to [0,2].
         */
        for ( int i = 2, j = 0 ; i >=0 && j < 3; i--, j++ ) {
            TURN toCheck = board[i][j];
            
            if( toCheck == null )
                break;
            else if( turnToReturn == null )
                turnToReturn = toCheck;
            
            if( toCheck == turnToReturn )
                count++;
            else
                break;
            
        }
        
        if( count == 3 )
            return turnToReturn;
        
        return null;    //Game draw.
    }
    
    public boolean isGameOver() {
        
        boolean isDraw = true;
        /**
         * First we check if there is a tie, by verifying if there is at least 
         * one empty house.
         * If there is at least one empty house, it is not a tie.
         */
        DRAW:    //This is a label, useful to break nested loops.
        for ( int i = 0 ; i < 3; i++ )
            for ( int j = 0 ; j < 3; j++ )
                if( board[i][j] == null ){
                    isDraw = false;
                    break DRAW;
                }
        
        if( isDraw )
            return true;
        
        /**
         * If there is no draw, it means that the game might still be playable, 
         * but it doesn't mean that it's not game over, 
         * so we must check if it is.
         */
        
        int count = 0;
        
        /**
         * First we iterate over the rows.
         */
        for ( int i = 0 ; i < 3; i++ ) {
            for ( int j = 0 ; j < 3; j++ ) {
                if( board[i][j] == currentTurn )
                    count++;
                else
                    count = 0;
            }
            
            if( count == 3 )
                return true;
            count = 0;
        }
        
        /**
         * Now we iterate over the collums.
         */
        for ( int j = 0 ; j < 3; j++ ) {
            for ( int i = 0 ; i < 3; i++ ) {
                if( board[i][j] == currentTurn )
                    count++;
                else
                    count = 0;
            }
            
            if( count == 3 )
                return true;
            count = 0;
        }
        
        /**
         * Now we iterate over the first diagonal, from [0,0] to [2,2].
         */
        for ( int i = 0, j = 0 ; i < 3 && j < 3; i++, j++ ) {
            if( board[i][j] == currentTurn )
                count++;
            else
                count = 0;
        }
        
        if( count == 3 )
            return true;
        count = 0;
        
        /**
         * Finnaly we iterate over the first diagonal, from [2,0] to [0,2].
         */
        for ( int i = 2, j = 0 ; i >=0 && j < 3; i--, j++ ) {
            if( board[i][j] == currentTurn )
                count++;
            else
                count = 0;
        }
        
        
        return count == 3;
        
    }
    
    public int playComputer() {
        return Computer.getComputer().play( board ) - 1;
    }
    
}
