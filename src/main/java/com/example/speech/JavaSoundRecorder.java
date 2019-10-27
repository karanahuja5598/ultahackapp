package com.example.speech;

import javax.sound.sampled.*;
import javax.xml.transform.Result;
import java.io.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class JavaSoundRecorder extends Application{


    // record duration, in milliseconds
    static final long RECORD_TIME = 5000;  // 1 minute
    static int  status = 0;

    QuickstartSample1 quickStart = new QuickstartSample1();

    // path of the wav file
    String name = "./resources/RecordAudio.wav";
    File wavFile = new File(name);

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;

    ArrayList<String> help;

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    /**
     * Captures the sound and record into a WAV file
     */
    public void start(Stage primarystage) {
        //Instantiating the BorderPane class
        BorderPane layout = new BorderPane();
        BorderPane layout2 = new BorderPane();

        //layout size of the first & second page
        layout.setPrefSize(500,700);
        layout2.setPrefSize(500,700);

        //hbox for the first page & second page for buttons
        HBox startstop = new HBox();
        HBox resultBox = new HBox();
        HBox backsetup = new HBox();

        //Creating a scene1 & scene2 object
        Scene scene = new Scene(layout);
        Scene scene2 = new Scene(layout2);

        //top text box for first page
        Text topText = new Text("Ulta/Google");
        topText.setStyle("-fx-font: 50 arial;");
        layout.setTop(topText);
        layout.setAlignment(topText,Pos.TOP_CENTER);
        //top text box for second page
        Text topText2 = new Text("Search Results");
        topText2.setStyle("-fx-font: 50 arial;");
        layout2.setTop(topText2);
        layout2.setAlignment(topText2,Pos.TOP_CENTER);

        //creating button for first page
        Button startSpeechbutton = new Button("push to talk");
        Button stopSpeechbutton = new Button("push to stop talking");
        Button buttonToresults = new Button("Results");
        //creating button for second page
        Button backbutton = new Button("Go back");

        //Button Modifications
        startSpeechbutton.setStyle("-fx-background-color: #ffffff");
        stopSpeechbutton.setStyle("-fx-background-color: #ffffff");
        buttonToresults.setStyle("-fx-background-color: #ffffff");
        backbutton.setStyle("-fx-background-color: #ffffff");

        startSpeechbutton.setStyle("-fx-border-color: #37474f; -fx-border-width: 2px;");
        stopSpeechbutton.setStyle("-fx-border-color: #37474f; -fx-border-width: 2px;");
        buttonToresults.setStyle("-fx-border-color: #37474f; -fx-border-width: 2px;");
        backbutton.setStyle("-fx-border-color: #37474f; -fx-border-width: 2px;");

        startSpeechbutton.setStyle("-fx-font-size: 1em; ");
        stopSpeechbutton.setStyle("-fx-font-size: 1em; ");
        buttonToresults.setStyle("-fx-font-size: 1em; ");
        backbutton.setStyle("-fx-font-size: 1em; ");

//        //pictures
//        Image image1 = new Image("ulta.jpg");
//        ImageView imageView = new ImageView(image1);
//        layout.getChildren().add(imageView);

        //setting scrolling text of speech in scrolling
        ObservableList<String> speechinput
                = FXCollections.observableArrayList();
        ListView<String> skillListView = new ListView<>(speechinput);
        skillListView.setPrefWidth(100);
        skillListView.setPrefHeight(400);
        //setting scroll test of search results
        ObservableList<String> inputresult
                = FXCollections.observableArrayList();
        ListView<String> resultListView = new ListView<>(inputresult);
        resultListView.setPrefWidth(100);
        resultListView.setPrefHeight(400);

        //vbox for the scrolling list
        VBox textResults = new VBox();
        textResults.getChildren().addAll(skillListView,buttonToresults);
        //vbox for the result scrolling list
        VBox searchresults = new VBox();
        searchresults.getChildren().addAll(resultListView);

        //setting vbox in centerPanel
        layout.setCenter(textResults);
        //setting vbox in centralPanel for page2
        layout2.setCenter(searchresults);

        //adding buttons to the hbox
        startstop.getChildren().addAll(startSpeechbutton,stopSpeechbutton);
        //resultBox.getChildren().addAll(buttonToresults);
        //adding buttons to the hbox
        backsetup.getChildren().addAll(backbutton);

        //layout of hbox page1
        startstop.setAlignment(Pos.BOTTOM_CENTER);
        layout.setBottom(startstop);
        //resultBox.setAlignment(Pos.BOTTOM_CENTER);
        layout.setCenter(textResults);
        //layout of hbox page2
        backsetup.setAlignment(Pos.BOTTOM_CENTER);
        layout2.setBottom(backsetup);

        startSpeechbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //startSpeechbutton.setText("Active");

                ulta(speechinput);

            }
        });

        stopSpeechbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //startSpeechbutton.setText("Stopped");

            }
        });
//testt
        backbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //backbutton.setText("go");
                primarystage.setScene(scene);
            }
        });

        buttonToresults.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //buttonToresults.setText("To results");
                primarystage.setScene(scene2);
            }
        });

        //Adding scene to the stage
        primarystage.setScene(scene);

        //Displaying the contents of the primarystage
        primarystage.show();



    }

    void startR(){
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing


            //System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            //System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    public static void runSQL(String query, ObservableList<String> speechinput) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 3: Execute a query
            System.out.println("Creating tables in given database...");

            ArrayList<String> initStatements = new ArrayList<String>();
            initStatements.add(
                    "DROP TABLE IF EXISTS Store_Details;" +
                            "CREATE TABLE Store_Hours AS SELECT * FROM " +
                            "CSVREAD('C:\\Users\\User\\Documents\\hackathon\\ultahackapp\\resources\\1_Store_Details.csv');"
            );
            initStatements.add(
                    "DROP TABLE IF EXISTS Store_Hours;" +
                            "CREATE TABLE Store_Details AS SELECT * FROM " +
                            "CSVREAD('C:\\Users\\User\\Documents\\hackathon\\ultahackapp\\resources\\2_Store_Hours.csv');"
            );
            initStatements.add(
                    "DROP TABLE IF EXISTS Product_Catalog;" +
                            "CREATE TABLE Product_Catalog AS SELECT * FROM " +
                            "CSVREAD('C:\\Users\\User\\Documents\\hackathon\\ultahackapp\\resources\\3_Product_Catalog.psv', null, 'charset=UTF-8 fieldSeparator=|');"
            );
            initStatements.add(
                    "DROP TABLE IF EXISTS Sku_Metadata;" +
                            "CREATE TABLE Sku_Metadata AS SELECT * FROM " +
                            "CSVREAD('C:\\Users\\User\\Documents\\hackathon\\ultahackapp\\resources\\4_Sku_MetaData.csv');"
            );
            initStatements.add(
                    "DROP TABLE IF EXISTS Store_Inventory;" +
                            "CREATE TABLE Store_Inventory AS SELECT * FROM " +
                            "CSVREAD('C:\\Users\\User\\Documents\\hackathon\\ultahackapp\\resources\\5_Store_Inventory.csv');"
            );

            boolean loadDatabase = false;
            if(loadDatabase)
                for(int i = 0; i < initStatements.size(); i++) {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(initStatements.get(i));
                }

            System.out.println("Created tables in given database...");

            // STEP 4: Clean-up environment
            //stmt.close();
            //conn.close();

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            //System.out.println("querying SELECT * FROM XXX");
            int columnsNumber = rsmd.getColumnCount();
            String display = "";
            //int columnsNumber = size;
            if(rs != null) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rsmd.getColumnName(i);
                    if (i > 1) display += "|  ";
                    display += rsmd.getColumnName(i);
                }
                speechinput.add(display);
                display = "";
                display += "";
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) display += "|  ";
                        String columnValue = rs.getString(i);
                        display += columnValue;
                    }
                    display += "";
                    speechinput.add(display);
                    display = "";
                }

            }
        }
        catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            }
            catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            }
            catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        } //end try

        System.out.println("Goodbye!");
    }

    public static void ulta(ObservableList<String> speechinput){
        final JavaSoundRecorder recorder = new JavaSoundRecorder();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
                status = 1;
                if (status == 1) {
                    try {

                        System.out.println(recorder.name);
                        recorder.help = QuickstartSample1.start(recorder.name);
                        StringTokenizer tokens = new StringTokenizer(recorder.help.get(0));
                        int j = 0;
                        System.out.printf("Transcription: ");
                        while(tokens.hasMoreTokens()) {
                            String temp = tokens.nextToken();
                            System.out.printf("%s ", temp);

                            if(temp.toLowerCase().compareTo("store") == 0) {
                                temp = tokens.nextToken();
                                System.out.printf("%s ", temp);
                                STOREcity(temp,speechinput);
                                j++;
                                break;
                            }
                            if(temp.toLowerCase().compareTo("city") == 0) {
                                temp = tokens.nextToken();
                                System.out.printf("%s ", temp);
                                CITYcity(temp,speechinput);
                                j++;
                                break;
                            }
                            if(temp.toLowerCase().compareTo("brand") == 0){
                                temp = tokens.nextToken();
                                System.out.printf("%s ", temp);
                                products(temp,speechinput);
                                j++;
                                break;
                            }
                            if(temp.toLowerCase().compareTo("type") == 0){
                                temp = tokens.nextToken();
                                System.out.printf("%s ", temp);
                                types(temp,speechinput);
                                j++;
                                break;
                            }
                            j++;
                        }
                    } catch (Exception f) {
                        System.out.println("Why did this happen?");
                    }
                }
            }
        });

        stopper.start();

        // start recording
        recorder.startR();
    }

    /**
     * Entry to run the program
     */
    public static void main(String args[]) {
        launch(args);
    }

    private static void STOREcity(String city,ObservableList<String> speechinput) {
        String query = "select DISPLAY_NAME " +
                "from store_details " +
                "where city = '" +
                city +
                "';";
        runSQL(query,speechinput);
    }

    private static void CITYcity(String city, ObservableList<String> speechinput) {
        String query = "select * " +
                "from store_details " +
                "where city = '" +
                city +
                "';";
        runSQL(query, speechinput);
    }

    public static void products(String value, ObservableList<String> speechinput){
        String query = "Select DISPLAY_NAME, BRAND_NAME, LIST_PRICE, CATEGORY_NAME, DESCRIPTION " +
                "from product_catalog " +
                "where brand_name = '" +
                value + "';";

        runSQL(query, speechinput);
    }

    public static void types(String type, ObservableList<String> speechinput){
        String name = type;
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        String query = "Select BRAND_NAME, DISPLAY_NAME, LIST_PRICE " +
                "from product_catalog " +
                "where category_name = '" +
                name + "';";
        runSQL(query, speechinput);
    }

}