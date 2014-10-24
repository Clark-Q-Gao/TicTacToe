/*
 * This game is free to play, and the source code is free to use, 
 * for educational purposes.
 * I hope you like it.
 * Pass by http://saclyr.net and support my website and my cause by donating.
 * 
 * Thank you for downloading it.
 */

package net.saclyr.invencible.tictactoe.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Saclyr
 */
public class Player implements java.io.Serializable {
    
    /**
     * If I'm going to serialize it, I have to create a field called 
     * serialVersionUID, because this keeps all the serialized objects 
     * compatible with each other. 
     * If I didn't do this, the Java Machine would give it's own serial number, 
     * and sometimes it gives a diferent number, which will throw an 
     * InvalidClassException.
     */
    private static final long serialVersionUID = 1872564129990379925L;
    private static final File records = new File( "records.dat" );
    private static final Player player = new Player();
    
    private int gamesPlayed;
    private int playerWins;
    private int gameDraws;
    
    private Player() {}
    
    /**
     * It is not necessarie to keep it singleton, 
     * but it is a good practicie to do so.
     */
    public static Player getPlayer() {
        return player;
    }
    
    private transient boolean isError;
    public void loadRecords() {
        if( isError )
            return;
        
        if( records.exists() ) {
            FileInputStream fileInput = null;
            ObjectInputStream load = null;
            
            try {
                fileInput = new FileInputStream( records );
                load = new ObjectInputStream( fileInput );
                
                Player playerToLoad = (Player)load.readObject();
                
                gamesPlayed = playerToLoad.gamesPlayed;
                playerWins = playerToLoad.playerWins;
                gameDraws = playerToLoad.gameDraws;
                
            }
            catch ( IOException | ClassNotFoundException ex ) {
                
                /**
                 * In case of an exception, the isError variable is set to true, so 
                 * tht in the rest of the game the player does not have to worry 
                 * again with an error.
                 */
                isError = true;
                JOptionPane.showMessageDialog( 
                        null, 
                        "It was not possible to load the records", 
                        "Loading not possible", 
                        JOptionPane.WARNING_MESSAGE 
                );
            }
            finally {
                
                if( load != null )
                    try{
                        load.close();
                    } catch ( IOException ex ) {}
                
                if( fileInput != null )
                    try{
                        fileInput.close();
                    } catch ( IOException ex ) {}
                
            }
        }
        else
            saveRecords();
        //bla bla bla
    }
    
    public void saveRecords() {
        
        if( isError )
            return;
        
        FileOutputStream fileOutput = null;
        ObjectOutputStream save = null;
        try {
            fileOutput = new FileOutputStream( records );
            save = new ObjectOutputStream( fileOutput );
            save.writeObject( player );
            
        }
        catch ( IOException ex ) {
            
            /**
             * In case of an exception, the isError variable is set to true, so 
             * tht in the rest of the game the player does not have to worry 
             * again with an error.
             */
            isError = true;
            JOptionPane.showMessageDialog( 
                    null, 
                    "It was not possible to save the records", 
                    "Loading not possible", 
                    JOptionPane.WARNING_MESSAGE                 
            );
        }
        finally {
            
            if( save != null )
                try{
                    save.close();
                } catch ( IOException ex ) {}
            
            if( fileOutput != null )
                try{
                    fileOutput.close();
                } catch ( IOException ex ) {}
            
        }
    }
    
    public void resetRecords() {
        gamesPlayed = 0;
        playerWins = 0;
        gameDraws = 0;
        saveRecords();
    }
    
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    
    public int getPlayerWins() {
        return playerWins;
    }
    
    public int getGameDraws() {
        return gameDraws;
    }
    
    public void incrementGamesPlayed() {
        gamesPlayed++;
    }
    
    public void incrementPlayerWins() {
        playerWins++;
    }
    
    public void incrementGameTies() {
        gameDraws++;
    }
}
