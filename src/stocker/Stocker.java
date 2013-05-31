/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * https://code.google.com/p/cfstox/source/browse/trunk/+cfstox/CFStox/TALib+Example+of+Moving+Average.txt?r=6
 */
package stocker;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Stocker {

    /**
     * @param args the command line arguments
     */
    static private Core lib;
    static private double output[];
    static private MInteger outBegIdx;
    static private MInteger outNbElement;
    static ArrayList<Double> close = new ArrayList<Double>();
    static ArrayList<Double> high = new ArrayList<Double>();
    static ArrayList<Double> low = new ArrayList<Double>();
    static ArrayList<Double> open = new ArrayList<Double>();
    static ArrayList<Double> volume = new ArrayList<Double>();

    public static void main(String[] args) throws MalformedURLException, IOException {

        URL oracle = new URL("http://chartapi.finance.yahoo.com/instrument/1.0/GOOG/chartdata;type=quote;range=1d/csv");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        int i;
        int lookback = 1;
        int retCode = 0;
        String inputLine;
        String split[] = null;
        double[] stuff;

        for (i = 0; i < 15; i++) {
            in.readLine();
        }

        //System.out.println("Timestamp,close,high,low,open,volume");
        while ((inputLine = in.readLine()) != null) {

            split = inputLine.split(",");
            ///////////////////Timestamp          close            high           low            open               volume
            // System.out.println(split[0] + " ," +split[1] + " ," +split[2] + " ," +split[3] + " ," +split[4] + " ," +split[5]);
            close.add(Double.parseDouble(split[1]));
            high.add(Double.parseDouble(split[2]));
            low.add(Double.parseDouble(split[3]));
            open.add(Double.parseDouble(split[4]));
            volume.add(Double.parseDouble(split[5]));
            output = new double[close.size()];

        }

        lookback = lib.movingAverageLookback(10, MAType.Sma);
        stuff = new double[close.size()];
        for (i = 0; i < close.size(); i++) {
            stuff[i] = close.get(i);

        }
        lib.movingAverage(0, close.size() - 1, stuff, lookback + 1, MAType.Sma, outBegIdx, outNbElement, output);
        System.out.println("Printing the default TALIB output to the screen");
        System.out.println("lookback = " + lookback + ", outBegIdx=" + outBegIdx.value
                + ", outNbElement=" + outNbElement.value + "\nretCode=" + retCode);
        System.out.println("\nClose  \t" + "Indicator");
        int j = 0;
        for (i = 0; i < output.length; i++) {
            System.out.println(close.get(i) + ",\t " + output[i]);
        }
        in.close();

    }
}
