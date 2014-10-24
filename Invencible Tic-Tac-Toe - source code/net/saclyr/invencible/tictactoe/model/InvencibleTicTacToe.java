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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import net.saclyr.invencible.tictactoe.data.Player;

/**
 *
 * @author Saclyr
 */
public class InvencibleTicTacToe extends javax.swing.JFrame {
    
    /**
     * This map will have for each quality key the best value that gives 
     * the best resolution.
     */
    final static java.util.Map
            < RenderingHints.Key, Object > awesomeQualityRenderMap;
    final static Color unselectedColor, selectedColor;
    final static java.awt.BasicStroke rectStroke;
    
    static {
        
        awesomeQualityRenderMap = new java.util.HashMap< RenderingHints.Key, Object >();
        awesomeQualityRenderMap.put( 
                RenderingHints.KEY_TEXT_ANTIALIASING, 
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON 
        );
        awesomeQualityRenderMap.put( 
                RenderingHints.KEY_INTERPOLATION, 
                RenderingHints.VALUE_INTERPOLATION_BICUBIC 
        );
        awesomeQualityRenderMap.put( 
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON 
        );
        awesomeQualityRenderMap.put( 
                RenderingHints.KEY_COLOR_RENDERING, 
                RenderingHints.VALUE_COLOR_RENDER_QUALITY 
        );
        awesomeQualityRenderMap.put( 
                RenderingHints.KEY_RENDERING, 
                RenderingHints.VALUE_RENDER_QUALITY 
        );
        
        
        
        rectStroke = new java.awt.BasicStroke( 
                2.5f, java.awt.BasicStroke.CAP_ROUND, 
                java.awt.BasicStroke.JOIN_ROUND 
        );
        
        
        selectedColor = new Color( 226, 86, 27 );
        unselectedColor = new Color( 87, 166, 89 );
        
    }
    
    //Frame structure;
    private final java.awt.CardLayout gameHolderLayout;
    private final JPanel 
            gameHolder, 
            logoPanel, 
            mainPanel, 
            oxGamePanel, 
            aboutPanel, 
            recordPanel;
    
    InvencibleTicTacToe() {
        super( "Invencible Tic-Tac-Toe" );
        
        gameHolderLayout = new java.awt.CardLayout( 0, 0 );
        gameHolder = new JPanel( gameHolderLayout );
        
        gameHolder.setPreferredSize( new java.awt.Dimension( 600, 600 ) );
        
        //++++
        
        logoPanel = new JPanel( new java.awt.GridLayout( 1, 1, 0, 0 ) );
        
        javax.swing.JLabel logoLabel = null;
        
        try {
            java.awt.image.BufferedImage logo = 
                    javax.imageio.ImageIO.read( 
                            getClass().getResourceAsStream( "saclyr_game_logo.png" ) 
                    );
            
            logoLabel = new javax.swing.JLabel( new javax.swing.ImageIcon( logo ) );
        }
        catch( java.io.IOException | IllegalArgumentException ex ) {
            logoLabel = new javax.swing.JLabel( "saclyr.net" );
            logoLabel.setFont( new java.awt.Font( "serif", java.awt.Font.ITALIC, 128 ) );
            logoLabel.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
            logoLabel.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
            logoLabel.setForeground( Color.BLUE.brighter() );
            logoLabel.setOpaque( false );
        }
        
        logoPanel.add( logoLabel );
        
        /**
         * This is an anonymous class object.
         * This anonymous class object has to be final, otherwise, the anonymous 
         * Runnable object bellow would not accept it.
         * 
         * Try to remove the final keyword and see what happens.
         */
        final MouseListener myMouseListener = new MouseListener() {
            
            @Override 
            public void mousePressed( MouseEvent e ) {
                synchronized ( logoPanel.getTreeLock() ) {
                    gameHolderLayout.next( gameHolder );
                }
            }
            
            @Override public void mouseClicked( MouseEvent e ) {}
            @Override public void mouseReleased( MouseEvent e ) {}
            @Override public void mouseEntered( MouseEvent e ) {}
            @Override public void mouseExited( MouseEvent e ) {}
            
        };
        
        /**
         * This thread makes sure that the logo screen passes to the next screen 
         * automatically, just in case the player doesn't click to do so.
         */
        (new Thread( new Runnable() {

            @Override
            public void run() {
                synchronized( logoPanel.getTreeLock() ) {
                    try {
                        logoPanel.getTreeLock().wait( 3000 );
                    }
                    catch ( InterruptedException ex ) {}
                    
                    if( logoPanel.isVisible() )
                        myMouseListener.mousePressed( null );
                }
            }
        } )).start();
        
        logoPanel.addMouseListener( myMouseListener );
        
        //++++
        
        Color backGround = new Color( 94, 232, 204 );
        
        mainPanel = new MainPanel( backGround );
        oxGamePanel = new OXGamePanel( backGround.darker() );
        recordPanel = new RecordPanel( backGround );
        aboutPanel = new AboutPanel( backGround );
        
        //++++
        
        /**
         * CardLayout is pretty cool, try checking about this on Java tutorials.
         * 
         * http://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
         */
        
        gameHolderLayout.addLayoutComponent( logoPanel, "Logo Panel" );
        gameHolderLayout.addLayoutComponent( mainPanel, "Main Panel" );
        gameHolderLayout.addLayoutComponent( oxGamePanel, "Game Board Panel" );
        gameHolderLayout.addLayoutComponent( recordPanel, "Record Panel" );
        gameHolderLayout.addLayoutComponent( aboutPanel, "About Panel" );
        
        gameHolder.add( logoPanel );
        gameHolder.add( mainPanel );
        gameHolder.add( oxGamePanel );
        gameHolder.add( recordPanel );
        gameHolder.add( aboutPanel );
        
        add( gameHolder );
        
        setResizable( false );
        
        /**
         * I could use setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ), but I 
         * prefere this way, because like this I can control other stuff, like 
         * saving records before exiting.
         */
        addWindowListener( new java.awt.event.WindowAdapter() {
            
            @Override
            public void windowClosing( java.awt.event.WindowEvent e ) {
                saveAndQuit();
            }
            
        } );
        
    }
    
    /**
     * The object OXGamePanel does not have direct access to the recordPanel 
     * variable, so this method creates a bridge between these.
     */
    void actualizeRecords() {
        ((RecordPanel)recordPanel).setRecords();
    }
    
    private class MainPanel extends JPanel {
        
        private final Font titleFont, buttonFont, creditFont;
        private final Rectangle[] rectButtons;
        private int selectedItem;
        
        MainPanel( Color backGround ) {
            super( null );
            setBackground( backGround );
            
            titleFont = new Font( "serif", Font.ITALIC, 55 );
            buttonFont = new Font( "arial", Font.BOLD, 28 );
            creditFont = new Font( "tahoma", Font.ITALIC, 22 );
            
            rectButtons = new Rectangle[]{
                new Rectangle( 200, 210, 200, 50 ), 
                new Rectangle( 200, 300, 200, 50 ), 
                new Rectangle( 200, 390, 200, 50 ), 
                new Rectangle( 200, 480, 200, 50 )
            };
            
            selectedItem = -1;
            
            /**
             * If you analize the anounymous object of MouseAdapter, you will 
             * notice the following:
             * 
             *     1st - The variable isMousePressed will only turn true if the 
             * mouse is houvering one of its buttons;
             *     2nd - If you press the button, but move the mouse, for 
             * accident per example, the button is stil viable to be activated;
             *     3rd - If while pressing the button, the mouse houvers 
             * outside the button, by drag, the variable isMousePressed will 
             * turn false, so that no other button is activated;
             *     4th - If the mouse is stll houvering the button, and the
             * mouse is still pressed, once the mouse button is released, it 
             * activates the button.
             */
            java.awt.event.MouseAdapter myMouseAdpater = new java.awt.event.MouseAdapter() {
                
                private boolean isMousePressed = false;
                private int pressedSelected = -1;
                
                @Override
                public void mouseDragged( MouseEvent e ) {
                    mouseMoved( e );
                }

                @Override
                public void mousePressed( MouseEvent e ) {
                    if( selectedItem != -1 && !isMousePressed ) {
                        isMousePressed = true;
                        pressedSelected = selectedItem;
                    }
                }

                @Override
                public void mouseReleased( MouseEvent e ) {
                    if( isMousePressed ) {
                        isMousePressed = false;
                        
                        if( pressedSelected == selectedItem ) {
                            switch ( selectedItem ) {
                                case 0:
                                    gameHolderLayout.show( gameHolder, "Game Board Panel" );
                                    ((OXGamePanel)oxGamePanel).playGame();
                                    break;
                                case 1:
                                    gameHolderLayout.show( gameHolder, "Record Panel" );
                                    break;
                                case 2:
                                    gameHolderLayout.show( gameHolder, "About Panel" );
                                    break;
                                case 3:
                                    String[] options = new String[]{ "Yes", "No" };
                                    
                                    int choice = JOptionPane.showOptionDialog( 
                                            MainPanel.this, 
                                            "Do you wish to leave the game?", 
                                            "Quit game?", 
                                            JOptionPane.YES_NO_OPTION, 
                                            JOptionPane.QUESTION_MESSAGE, 
                                            null, 
                                            options, 
                                            options[1] 
                                    );
                                    
                                    if( choice == JOptionPane.YES_OPTION ){
                                        saveAndQuit();
                                    }
                                    
                                    break;
                                default:
                                    throw new IllegalArgumentException( 
                                            "The given selected case is not "
                                                    + "suported: " + selectedItem 
                                    );
                            }
                        }
                        
                        pressedSelected = -1;
                        selectedItem = -1;
                        
                    }
                }

                @Override
                public void mouseMoved( MouseEvent e ) {
                    int select = -1;
                    
                    for ( int i = 0 ; i < rectButtons.length ; i++ ) {
                        if( rectButtons[i].contains( e.getX(), e.getY() ) ) {
                            select = i;
                            break;
                        }
                    }
                    
                    if( selectedItem != select ) {
                        selectedItem = select;
                        if( selectedItem == -1 ){
                            isMousePressed = false;
                            pressedSelected = -1;
                        }
                        repaint();
                    }
                }
                
            };
            
            addMouseListener( myMouseAdpater );
            addMouseMotionListener( myMouseAdpater );
            
        }

        @Override
        protected void paintComponent( Graphics g ) {
            super.paintComponent( g );
            
            Graphics2D g2 = (Graphics2D)g;
            g2.addRenderingHints( awesomeQualityRenderMap );    //Try to remove this and you will see the diference
            g2.setFont( titleFont );
            g2.drawString( "INVENCIBLE", 155, 90 );
            g2.drawString( "TIC - TAC - TOE", 105, 140 );
            
            g2.setStroke( rectStroke );
            
            for ( int i = 0 ; i < rectButtons.length ; i++ ) {
                Rectangle rect = rectButtons[i];
                
                g2.setColor( Color.LIGHT_GRAY );
                g2.fill( rect );
                
                if( i == selectedItem )
                    g2.setColor( selectedColor );
                else
                    g2.setColor( unselectedColor );
                
                g2.draw( rect );
            }
            
            g2.setColor( Color.BLACK );
            g2.setFont( buttonFont );
            g2.drawString( "Play Game", 230, 245 );
            g2.drawString( "Records", 248, 336 );
            g2.drawString( "About", 260, 425 );
            g2.drawString( "Quit", 273, 515 );
            
            
            g2.setFont( creditFont );
            g2.setColor( Color.DARK_GRAY );
            g2.drawString( "http://saclyr.net", 420, 590 );
            
        }
        
        
        
    }
    
    private class RecordPanel extends JPanel {
        
        private final Font titleFont, buttonFont, creditFont;
        private final Rectangle okRectangle, resetRectangle;
        private boolean isOkSelected, isResetSelected;
        
        private final javax.swing.JLabel table;
        
        RecordPanel( Color backGround ) {
            super( null );
            
            setBackground( backGround );
            titleFont = new Font( "SansSerif", Font.BOLD, 50 );
            buttonFont = new Font( "SansSerif", Font.BOLD, 20 ); 
            creditFont = new Font( "tahoma", Font.ITALIC, 22 );
            
            table = new javax.swing.JLabel();
            table.setBounds( 0, 100, 600, 200 );
            setRecords();
            
            okRectangle = new Rectangle( 270, 550, 61, 32 );
            resetRectangle = new Rectangle( 255, 320, 88, 36 );
            
            /**
             * If you analize the anounymous object of MouseListener, you will 
             * notice the following:
             * 
             *     1st - The variable isOkPressed or isResetPressed will only 
             * turn true if the mouse is houvering the button OK or the button 
             * Reset, respectively;
             *     2nd - If you press the button, but move the mouse, by 
             * accident per example, the button is stil viable to be activated;
             *     3rd - If while pressing the the mouse button, the mouse houvers 
             * outside the button, by drag, the variable "isPressed" will 
             * turn false;
             *     4th - If the mouse is stll houvering the button, and the
             * mouse is still pressed, once the mouse button is released, 
             * the button is activated.
             */
            java.awt.event.MouseAdapter myMouseAdpater = new java.awt.event.MouseAdapter() {
                
                private boolean isOkPressed = false;
                private boolean isResetPressed = false;
                
                @Override
                public void mousePressed( MouseEvent e ) {
                    if( isOkSelected && !isOkPressed ) {
                        isOkPressed = true;
                    }
                    else if( isResetSelected && !isResetPressed ) {
                        isResetPressed = true;
                    }
                }
                
                @Override
                public void mouseReleased( MouseEvent e ) {
                    if( isOkPressed ) {
                        gameHolderLayout.show( gameHolder, "Main Panel" );
                        
                        isOkSelected = false;
                        isOkPressed = false;
                    }
                    else if( isResetPressed ) {
                        String[] options = new String[]{ "Yes", "No" };
                        
                        int choice = JOptionPane.showOptionDialog( 
                                RecordPanel.this, 
                                "Do you wish to reset the records?", 
                                "Reset?", 
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE, 
                                null, 
                                options, 
                                options[1] 
                        );
                        
                        if( choice == JOptionPane.YES_OPTION ){
                            Player.getPlayer().resetRecords();
                            setRecords();
                        }
                        
                        isResetSelected = false;
                        isResetPressed = false;
                    }
                }
                
                @Override
                public void mouseDragged( MouseEvent e ) {
                    mouseMoved( e );
                }
                
                @Override
                public void mouseMoved( MouseEvent e ) {
                    boolean isMouseInsideOkButton = 
                            okRectangle.contains( e.getX(), e.getY() );
                    
                    if( isMouseInsideOkButton != isOkSelected  ){
                        isOkSelected = isMouseInsideOkButton;
                        if( isOkPressed )
                            isOkPressed = false;
                        repaint();
                    }
                    
                    
                    boolean isMouseInsideResetButton = 
                            resetRectangle.contains( e.getX(), e.getY() );
                    
                    if( isMouseInsideResetButton != isResetSelected  ){
                        isResetSelected = isMouseInsideResetButton;
                        if( isResetPressed )
                            isResetPressed = false;
                        repaint();
                    }
                    
                    
                }
                
            };
            
            addMouseListener( myMouseAdpater );
            addMouseMotionListener( myMouseAdpater );
            
            add( table );
        }
        
        final void setRecords() {
            Player player = Player.getPlayer();
            
            int games = player.getGamesPlayed();
            int wins = player.getPlayerWins();
            int draws = player.getGameDraws();
            
            /**
             * You can use HTML and CSS with some swing components, 
             * check JAVA tutorials for more details at 
             * http://docs.oracle.com/javase/tutorial/ .
             * 
             * You can even make tables! :D
             */
            table.setText( 
                    "<html>"
                  + "<body>"
                            + "<table border='2' "
                                + "style='background-color:#FFFFCC;"
                                    + "border-collapse:collapse;"
                                    + "border:2px solid #339933;"
                                    + "color:#660066;"
                                    + "width:596' "//the border takes sapce, so we need to remove some width to fit
                                + "cellpadding='5' cellspacing='1'>"
                            + "<tr>"
                            + "<td><font size='42' face='tahoma'>Games Played: </font></td>" 
                            + "<td><font size='42' face='tahoma'>" + games + "</font></td>" 
                            + "</tr>"
                            
                            + "<tr>"
                            + "<td><font size='42' face='tahoma'>Games Draw: </font></td>" 
                            + "<td><font size='42' face='tahoma'>" + draws + "</font></td>" 
                            + "</tr>"
                            
                            + "<tr>"
                            + "<td><font size='42' face='tahoma'>Games Won: </font></td>" 
                            + "<td><font size='42' face='tahoma'>" + wins + "</font></td>" 
                            + "</tr>"
                  + "</body>"
                  + "</html>" 
            );
            
        }
        
        @Override
        protected void paintComponent( Graphics g ) {
            super.paintComponent( g );
            Graphics2D g2 = (Graphics2D)g;
            g2.addRenderingHints( InvencibleTicTacToe.awesomeQualityRenderMap );
            g2.setFont( titleFont );
            g2.drawString( "Records", 210, 65 );
            
            g2.setStroke( InvencibleTicTacToe.rectStroke );
            
            //OK button
            g2.setColor( Color.LIGHT_GRAY );
            g2.fill( okRectangle );
            
            if( isOkSelected )
                g2.setColor( selectedColor );
            else
                g2.setColor( unselectedColor );
            
            g2.draw( okRectangle );
            
            //Reset button
            g2.setColor( Color.LIGHT_GRAY );
            g2.fill( resetRectangle );
            
            if( isResetSelected )
                g2.setColor( selectedColor );
            else
                g2.setColor( unselectedColor );
            
            g2.draw( resetRectangle );
            
            g2.setColor( Color.BLACK );
            g2.setFont( buttonFont );
            g2.drawString( "OK", 285, 574 );
            g2.drawString( "RESET", 267, 346 );
            
            g2.setFont( creditFont );
            g2.setColor( Color.DARK_GRAY );
            g2.drawString( "http://saclyr.net", 420, 590 );
        }
        
    }
    
    private class AboutPanel extends JPanel {
        
        private final Font titleFont, okButtonFont, creditFont;
        private final javax.swing.JTextPane aboutMessage;
        private final Rectangle okRectangle;
        private boolean isOkSelected;
        
        AboutPanel( Color backGround ) {
            super( null );
            setBackground( backGround );
            titleFont = new Font( "SansSerif", Font.BOLD, 50 );
            okButtonFont = new Font( "SansSerif", Font.BOLD, 20 ); 
            creditFont = new Font( "tahoma", Font.ITALIC, 22 );
            
            okRectangle = new Rectangle( 270, 550, 61, 32 );
            
            aboutMessage = new javax.swing.JTextPane();
            aboutMessage.setBounds( 10, 60, 580, 470 );
            aboutMessage.setEditable( false );
            aboutMessage.setContentType( "text/html" );
            aboutMessage.setOpaque( false );
            aboutMessage.setForeground( Color.DARK_GRAY );
            
            /**
             * You can use HTML and CSS with some swing components, 
             * check JAVA tutorials for more details at 
             * http://docs.oracle.com/javase/tutorial/ .
             */
            aboutMessage.setText( 
                    "<html><head><style>"
                            + "p {"
                                + "font-size: 31pt; "
                                + "font-family: 'Arial', Helvetica, serif;"
                            + "}"
                            + "</style></head>"
                            + "<body>"
                            + "<p>Hi!"
                            + "</p>"
                            + "<p>In the process of learning java, I made an "
                            + "\"Invencible Tic-Tac-Toe\", being this version "
                            + "a remake. It's free to play and the "
                            + "source code is available on my website."
                            + "</p>"
                            + "<p>Go check <i><a href='http://saclyr.net'>saclyr.net</a></i> "
                            + "for many more games, and donate to support the "
                            + "website and my cause."
                            + "</p>"
                            + "<p>I hope you like it, and have a nice game :D</p>"
                            + "</body></html>" 
            );
            
            aboutMessage.addHyperlinkListener( new javax.swing.event.HyperlinkListener() {
                
                @Override
                public void hyperlinkUpdate( HyperlinkEvent e ) {
                    if( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
                        String[] options = new String[]{ "Yes", "No" };
                        
                        /**
                         * It's polite to ask if the user really want to visit 
                         * the website, It could have been an accident, and if 
                         * you notice well, this JOptionPane dialog has a 
                         * tweak: it has the "no" button as default, to prevent 
                         * missclick to open the browser, when it was all an 
                         * accident.
                         */
                        int choice = JOptionPane.showOptionDialog( 
                                aboutPanel, 
                                "You just clicked a hiperlink.\n"
                                        + "Do you wish to visit saclyr.net?", 
                                "Open Web Browser?", 
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE, 
                                null, 
                                options, 
                                options[1] 
                        );
                        
                        if( choice == JOptionPane.YES_OPTION )
                            try {
                                java.net.URL url = e.getURL();
                                
                                java.net.URI uri = url.toURI();
                                java.awt.Desktop.getDesktop().browse( uri );
                            }
                            catch( java.io.IOException | java.net.URISyntaxException ex ) {
                                JOptionPane.showMessageDialog( 
                                        aboutPanel, 
                                        "Unfortunatly an error was encountered, and "
                                                + "\nit is not possible to open your browser.", 
                                        "An error was encountered", 
                                        JOptionPane.ERROR_MESSAGE 
                                );
                            }
                        
                        
                    }
                }
            });
            
            /**
             * If you analize the anounymous object of MouseListener, you will 
             * notice the following:
             * 
             *     1st - The variable isOkPressed will only turn true if the 
             * mouse is houvering the button OK;
             *     2nd - If you press the button, but move the mouse, by 
             * accident per example, the cell is stil viable to be played;
             *     3rd - If while pressing the the mouse button, the mouse houvers 
             * outside the button, by drag, the variable isOkPressed will 
             * turn false;
             *     4th - If the mouse is stll houvering the button, and the
             * mouse is still pressed, once the mouse button is released, 
             * the button is activated.
             */
            java.awt.event.MouseAdapter myMouseAdpater = new java.awt.event.MouseAdapter() {
                
                private boolean isOkPressed = false;
                
                @Override
                public void mousePressed( MouseEvent e ) {
                    if( isOkSelected && !isOkPressed) {
                        isOkPressed = true;
                    }
                }
                
                @Override
                public void mouseReleased( MouseEvent e ) {
                    if( isOkPressed ) {
                        gameHolderLayout.show( gameHolder, "Main Panel" );
                        
                        isOkSelected = false;
                        isOkPressed = false;
                    }
                }
                
                
                
                @Override
                public void mouseDragged( MouseEvent e ) {
                    mouseMoved( e );
                }
                
                
                @Override
                public void mouseMoved( MouseEvent e ) {
                    boolean isMouseInsideButton = 
                            okRectangle.contains( e.getX(), e.getY() );
                    
                    if( isMouseInsideButton != isOkSelected  ){
                        isOkSelected = isMouseInsideButton;
                        if( isOkPressed )
                            isOkPressed = false;
                        repaint();
                    }
                }
                
            };
            
            addMouseListener( myMouseAdpater );
            addMouseMotionListener( myMouseAdpater );
            
            add( aboutMessage );
            
        }
        
        @Override
        protected void paintComponent( Graphics g ) {
            super.paintComponent( g );
            Graphics2D g2 = (Graphics2D)g;
            g2.addRenderingHints( awesomeQualityRenderMap );
            g2.setFont( titleFont );
            g2.drawString( "About", 230, 65 );
            
            g2.setStroke( rectStroke );
            g2.setColor( Color.LIGHT_GRAY );
            g2.fill( okRectangle );
            
            if( isOkSelected )
                g2.setColor( selectedColor );
            else
                g2.setColor( unselectedColor );
            
            g2.draw( okRectangle );
            g2.setColor( Color.BLACK );
            g2.setFont( okButtonFont );
            g2.drawString( "OK", 285, 574 );
            
            g2.setFont( creditFont );
            g2.setColor( Color.DARK_GRAY );
            g2.drawString( "http://saclyr.net", 420, 590 );
        }
        
    }
    
    private void saveAndQuit() {
        Player.getPlayer().saveRecords();
        System.exit( 0 );
    }
    
    public static void main( String[] args ) {
        
        Player.getPlayer().loadRecords();
        
        try {
            InvencibleTicTacToe invencibleTicTacToe = 
                    new InvencibleTicTacToe();
            invencibleTicTacToe.setVisible( true );
            java.awt.Insets insets = invencibleTicTacToe.getInsets();
            
            /**
             * Actually, I never did this in any of my programs, but I really 
             * wanted to have the game board squared.
             */
            invencibleTicTacToe.setBounds( 0, 0, 
                    600 + insets.left + insets.right, 
                    600 + insets.bottom + insets.top );
            invencibleTicTacToe.setLocationRelativeTo( null );
            
        }
        catch( Throwable ex ) {
            javax.swing.JOptionPane.showMessageDialog( 
                    null, 
                    "IT WAS NOT POSSIBLE TO LOAD THE GAME.", 
                    "SYSTEM FAIL", 
                    javax.swing.JOptionPane.ERROR_MESSAGE 
            );
            
            StringBuilder sb = new StringBuilder(ex.toString());
            for (StackTraceElement ste : ex.getStackTrace()) {
                sb.append("\n\tat ");
                sb.append(ste);
            }
            String trace = sb.toString();
            
            javax.swing.JOptionPane.showMessageDialog( 
                    null, 
                    trace, 
                    "SYSTEM FAIL", 
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            
        }
        
        
    }
    
}
