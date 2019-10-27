package com.example.speech;

import javax.sound.sampled.*;
import javax.xml.transform.Result;
import java.io.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class JavaSoundRecorder {
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
    void start() {
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

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

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

    public static void runSQL(String query) {
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

            if (rs!=null){
                System.out.println("DISPLAY_NAME");
                while(rs.next())
                {
                    System.out.println(rs.getString("DISPLAY_NAME"));
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

    /**
     * Entry to run the program
     */
    public static void main(String[] args) {
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
                                String query = "select * " +
                                        "from store_hours " +
                                        "where city = '" +
                                        tokens.nextToken() +
                                        "';";
                                runSQL(query);
                                j++;
                                break;
                            }
                            j++;

                        }


                        /*
                        for (int i = 0; i < recorder.help.size(); i++) {
                            System.out.printf("Transcription: %s%n", recorder.help.get(i));
                            if(recorder.help.get(i).toLowerCase().compareTo("store") == 0) {
                                String query = "select * " +
                                        "from store_hours " +
                                        "where city = '" +
                                        recorder.help.get(i+1) +
                                        "';";
                                runSQL(query);
                            }
                        }*/
                    } catch (Exception f) {
                        System.out.println("Why did this happen?");
                    }
                }
            }
        });

        stopper.start();

        // start recording
        recorder.start();
    }
}