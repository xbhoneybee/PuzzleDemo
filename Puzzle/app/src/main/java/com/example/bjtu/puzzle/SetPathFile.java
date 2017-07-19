package com.example.bjtu.puzzle;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.example.bjtu.puzzle.Puzzle.n;


/**
 * Created by XBhoneYbee on 2017/7/19.
 */

public class SetPathFile {

    public  static void writeFileSdcard(String fileName, String message) {

        try {

            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

            FileOutputStream fout = new FileOutputStream(fileName);

            byte[] bytes = message.getBytes();

            fout.write(bytes);

            fout.close();

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}
