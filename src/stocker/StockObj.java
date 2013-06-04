/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Chris
 */
package stocker;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockObj implements QuoteWrapper {

    public String ticker;
    public double rsi;
    public double ult;
    private double input[];
    private int inputInt[];
    private double output[];
    public double outmacd[];
    public double outmacdsignal[];
    public double outmacdhist[];
    private int outputInt[];
    private MInteger outBegIdx;
    private MInteger outNbElement;
    private double[] close;

    private double[] low;
    ArrayList<Double> close_price = new ArrayList<Double>();
    ArrayList<Double> high_price = new ArrayList<Double>();
    ArrayList<Double> low_price = new ArrayList<Double>();
    private RetCode retCode;
    private Core lib;
    private int lookback;
    private double[] high;

    public StockObj(String symbol) throws MalformedURLException, IOException {
        outBegIdx = new MInteger();
        outNbElement = new MInteger();
        ticker = symbol;
        lib = new Core();
        
        getYahooQuotes();
        resetArrayValues();
        
    }

    public void simpleRelativeStrengthIndex(int time) {
        lookback = lib.rsiLookback(time);
        lib.rsi(0, close.length - 1, close, lookback, outBegIdx, outNbElement, output);
        rsi = output[outNbElement.value -1];    
        
        int i;
        for(i = 0; i < output.length; i++) {
        //    System.err.println("Got : " + output[i - 14]);
        }
     }   

    public void simpleMovingAverageCallint(int time) {
        
        lookback = lib.movingAverageLookback(time, MAType.Sma);
        lib.movingAverage(0, close.length - 1, close, lookback + 1, MAType.Sma, outBegIdx, outNbElement, output);
        rsi = output[ (int) outNbElement.value - 1];
    }

    public void simpleMACD(int time) {
        int i;
        lib.macd(0, close.length - 1, close, 12, 26, 9, outBegIdx, outNbElement, outmacd, outmacdsignal, outmacdhist);
        
        for(i = 0; i < close.length; i++){
            //System.out.println("MACD    : " + outmacd[i]);
            //System.out.println("SIGNAL  : " + outmacdsignal[i]);
            //System.out.println("HISTORY : " + outmacdhist[i]);
        }
    }
    
    public void simpleULT() {
        lib.ultOsc(0, close.length-1, high, low, close, 7, 14, 28, outBegIdx, outNbElement, output);
        ult = output[ (int) outNbElement.value -1];
    }
    
    public void calcIndicators() throws MalformedURLException, IOException {
            this.simpleRelativeStrengthIndex(14);
            this.resetArrayValues();
            this.simpleMACD(0);
            this.resetArrayValues();
            this.simpleULT();
            
    }
    
    @Override
    public void getYahooQuotes() {
        String inputLine;
        String[] split;
        int i;

        Double c,h,l;
        URL csv = null;
        try {
            csv = new URL("http://chartapi.finance.yahoo.com/instrument/1.0/" + this.ticker + "/chartdata;type=quote;range=1d/csv");
        } catch (MalformedURLException ex) {
            Logger.getLogger(StockObj.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(csv.openStream()));
        } catch (IOException ex) {
            Logger.getLogger(StockObj.class.getName()).log(Level.SEVERE, null, ex);
        }

        //strip off header
        for (i = 0; i < 15; i++) {
            try {
                in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(StockObj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            //do the parse
            while ((inputLine = in.readLine()) != null) {
                split = inputLine.split(",");
                close_price.add(Double.parseDouble(split[1]));
                low_price.add(Double.parseDouble(split[3]));
                high_price.add(Double.parseDouble(split[2]));
            }
        } catch (IOException ex) {
            Logger.getLogger(StockObj.class.getName()).log(Level.SEVERE, null, ex);
        }

        close = new double[close_price.size()];
        high = new double[close_price.size()];
        low = new double[close_price.size()];
        input = new double[close.length];
        inputInt = new int[close.length];
        output = new double[close.length];
        outputInt = new int[close.length];
        outmacdsignal = new double[close.length];
        outmacdhist = new double[close.length];
        outmacd = new double[close.length];
        
        for (i = 0; i < close_price.size(); i++) {
            c = (Double) close_price.get(i);
            h = (Double) high_price.get(i);
            l = (Double) low_price.get(i);
            close[i] = c.doubleValue();
            high[i] = h.doubleValue();
            low[i] = l.doubleValue();
        }
    }

    public void resetArrayValues() throws MalformedURLException, IOException {
        //provide default "fill" values to avoid nulls.
        Double c,h,l;
        
        for (int i = 0; i < input.length; i++) {
            c = (Double) close_price.get(i);
            h = (Double) high_price.get(i);
            l = (Double) low_price.get(i);
            close[i] = c.doubleValue();
            high[i] = h.doubleValue();
            low[i] = l.doubleValue();
        }
        
        for (int i = 0; i < output.length; i++) {
            output[i] = (double) -999999.0;
            outputInt[i] = -999999;
        }

        //provide some "fail" values up front to ensure completion if correct.
        outBegIdx.value = -1;
        outNbElement.value = -1;
        retCode = RetCode.InternalError;
        lookback = -1;
    }

    public void showFinalOutput() {
        System.out.println("====Ticker Symbol " + this.ticker + "====");
        System.out.println("RSI " + rsi);
        System.out.println("MACD " + outmacd[ (int) outNbElement.value - 1] +  " MACDSIGNAL : " + outmacdsignal[ (int) outNbElement.value - 1]+ "MACDHIST : " + outmacdhist[ (int) outNbElement.value - 1]);
        System.out.println("ULT OSC " + ult);
        
    }
}
