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
 * This class represents the Invencible AI. The methods are not that hard to 
 * understand, but if you have any doubt, feel free to contact me.
 * 
 * @author Saclyr
 */
public class Computer {
    
    private static final Computer computer = new Computer();
    
    private GameBoard.TURN turn;
    
    private Computer() {}
    
    /**
     * It is not necessarie to keep it singleton, 
     * but it is a good practicie to do so.
     */
    public static Computer getComputer() {
        return computer;
    }
    
    public void setTurn( GameBoard.TURN turn ) {
        this.turn = turn;
    }
    
    private int checkNearWin( GameBoard.TURN[][] board ) {
        int house = 0;
        int count = 0;
        
        for ( int i = 0 ; i < 3; i++ ) {
            for ( int j = 0 ; j < 3; j++ ) {
                if( board[i][j] == turn )
                    count++;
                else if( board[i][j] == null )
                    house = 3*i + j + 1;
                else
                    count = -1;
            }
            
            if( count == 2 )
                return house;
            count = 0;
        }
        
        for ( int j = 0 ; j < 3; j++ ) {
            for ( int i = 0 ; i < 3; i++ ) {
                if( board[i][j] == turn )
                    count++;
                else if( board[i][j] == null )
                    house = 3*i + j + 1;
                else
                    count = -1;
            }
            
            if( count == 2 )
                return house;
            count = 0;
        }
        
        for ( int i = 0, j = 0 ; i < 3 && j < 3; i++, j++ ) {
            if( board[i][j] == turn )
                count++;
            else if( board[i][j] == null )
                house = 3*i + j + 1;
            else
                count = -1;
        }
        
        if( count == 2 )
            return house;
        count = 0;
        
        for ( int i = 0, j = 2 ; i < 3 && j >= 0; i++, j-- ) {
            if( board[i][j] == turn )
                count++;
            else if( board[i][j] == null )
                house = 3*i + j + 1;
            else
                count = -1;
        }
        
        if( count == 2 )
            return house;
        
        return -1;
    }
    
    private int checkNearLoss( GameBoard.TURN[][] board ) {
        int house = 0;
        GameBoard.TURN enemy = turn.getOpositeTurn();
        int count = 0;
        
        for ( int i = 0 ; i < 3; i++ ) {
            for ( int j = 0 ; j < 3; j++ ) {
                if( board[i][j] == enemy )
                    count++;
                else if( board[i][j] == null )
                    house = 3*i + j + 1;
                else
                    count = -1;
            }
            
            if( count == 2 )
                return house;
            count = 0;
        }
        
        for ( int j = 0 ; j < 3; j++ ) {
            for ( int i = 0 ; i < 3; i++ ) {
                if( board[i][j] == enemy )
                    count++;
                else if( board[i][j] == null )
                    house = 3*i + j + 1;
                else
                    count = -1;
            }
            
            if( count == 2 )
                return house;
            count = 0;
        }
        
        for ( int i = 0, j = 0 ; i < 3 && j < 3; i++, j++ ) {
            if( board[i][j] == enemy )
                count++;
            else if( board[i][j] == null )
                house = 3*i + j + 1;
            else
                count = -1;
        }
        
        if( count == 2 )
            return house;
        count = 0;
        
        for ( int i = 0, j = 2 ; i < 3 && j >= 0; i++, j-- ) {
            if( board[i][j] == enemy )
                count++;
            else if( board[i][j] == null )
                house = 3*i + j + 1;
            else
                count = -1;
        }
        
        if( count == 2 )
            return house;
        
        return -1;
    }
    
    private int checkEnemySmartMove( GameBoard.TURN[][] board ) {
        GameBoard.TURN enemy = turn.getOpositeTurn();
        
        if( board[1][0] == enemy && board[0][1] == enemy && 
                board[2][0] == null && board[0][0] == null && board[0][2] == null )
            return 1;
        else if( board[0][1] == enemy && board[1][2] == enemy && 
                board[0][0] == null && board[0][2] == null && board[2][2] == null )
            return 3;
        else if( board[1][2] == enemy && board[2][1] == enemy && 
                board[0][2] == null && board[2][2] == null && board[2][0] == null )
            return 9;
        else if( board[2][1] == enemy && board[1][0] == enemy && 
                board[2][2] == null && board[2][0] == null && board[0][0] == null )
            return 7;
        
        int[] toPlay = null;
        
        if( board[0][0] == enemy && board[2][2] == enemy && 
                board[0][1] == null && board[0][2] == null && board[1][2] == null && 
                board[1][0] == null && board[2][0] == null && board[2][1] == null || 
                
                board[0][2] == enemy && board[2][0] == enemy && 
                board[1][2] == null && board[2][2] == null && board[2][1] == null && 
                board[0][1] == null && board[0][0] == null && board[0][1] == null ){
            
            toPlay = new int[]{2,4,6,8};
        }
        
        if( toPlay != null )
            return toPlay[(int)(Math.random()*4)];
        
        if( board[0][1] == enemy && board[1][0] == null && board[0][0] == null && 
                board[0][2] == null && board[1][2] == null &&
                ( board[2][0] == enemy || board[2][2] == enemy ) ){
            toPlay = new int[]{1,3};
        }
        else if( board[1][2] == enemy && board[0][1] == null && board[0][2] == null && 
                board[2][2] == null && board[2][1] == null &&
                ( board[0][0] == enemy || board[2][0] == enemy ) ){
            toPlay = new int[]{3,9};
        }
        else if( board[2][1] == enemy && board[2][1] == null && board[2][2] == null && 
                board[2][0] == null && board[1][0] == null &&
                ( board[0][2] == enemy || board[0][0] == enemy ) ){
            toPlay = new int[]{7,9};
        }
        else if( board[1][0] == enemy && board[2][1] == null && board[2][0] == null && 
                board[0][0] == null && board[0][1] == null &&
                ( board[2][0] == enemy || board[0][2] == enemy ) ){
            toPlay = new int[]{1,7};
        }
        
        if( toPlay != null )
            return toPlay[(int)(Math.random()*2)];
        
        return -1;
    }
    
    private int checkCorners( GameBoard.TURN[][] board ) {
        
        if(board[0][0] == null || board[0][2] == null 
                || board[2][0] == null || board[2][2] == null ){
            
            int[] toPlay = new int[]{1,3,7,9};
            int toCheck = 0;
            do {
                toCheck = (int)(Math.random()*4);
                
                int house = toPlay[toCheck];
                
                int i = (house - 1)/3;
                int j = house - 1 - i*3;
                
                if( board[i][j] == null )
                    return toPlay[toCheck];
            }
            while ( true );
        }
        
        return -1;
    }
    
    private int randomPlay( GameBoard.TURN[][] board ) {
        
        int toPlay = -1;
        boolean play = false;
        do {
            toPlay = 1 + (int)( Math.random()*9 );
            
            int i = (toPlay - 1)/3;
            int j = toPlay - 1 - i*3;
            
            if( board[i][j] == null )
                play = true;
            
        }
        while ( !play );
        
        return toPlay;
    }
    
    int play( GameBoard.TURN[][] board ) {
        
        if( board[1][1] == null )
            return 5;
        
        int toPlay = checkNearWin( board );
        if( toPlay != -1 )
            return toPlay;
        
        toPlay = checkNearLoss( board );
        if( toPlay != -1 )
            return toPlay;
        
        toPlay = checkEnemySmartMove( board );
        if( toPlay != -1 )
            return toPlay;
        
        toPlay = checkCorners( board );
        if( toPlay != -1 )
            return toPlay;
        
        return randomPlay( board );
    }
    
}
