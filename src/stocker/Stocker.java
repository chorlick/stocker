/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * https://code.google.com/p/cfstox/source/browse/trunk/+cfstox/CFStox/TALib+Example+of+Moving+Average.txt?r=6
 */
package stocker;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

/**
 *
 * @author Chris
 */
public class Stocker {
    //you need to instantiate some basic variables

    public LinkedList<StockObj> stock_list = new LinkedList<StockObj>();
    byte buffer[] = new byte[1024];
    String split[];
    String line;
    int readCount;

    public void init_app() {
        StockObj goog;
        try {
            FTPClient ftp = new FTPClient();
            FTPClientConfig config = new FTPClientConfig();
            ftp.configure(config);
            ftp.connect("ftp.nasdaqtrader.com");
            ftp.enterRemotePassiveMode();
            ftp.login("anonymous", "");

            ftp.changeWorkingDirectory("SymbolDirectory");
            InputStream in = ftp.retrieveFileStream("nasdaqlisted.txt");
            BufferedReader inbuf = new BufferedReader(new InputStreamReader(in));
            int i = 0;
            while ((line = inbuf.readLine()) != null) {

                split = line.split("\\|");
                System.out.println(split[0]);
                stock_list.add(new StockObj(split[0]));
                if (i++ >= 20) {
                    break;
                }
            }
            /*
             stock_list.add(new StockObj("RAD"));
             stock_list.add(new StockObj("GOOG"));
             stock_list.add(new StockObj("MSFT"));
             stock_list.add(new StockObj("FNMA"));
             */
            for (StockObj obj : stock_list) {
                if (obj.ticker.compareTo("Symbol") == 0) {
                    System.out.println("Skipping none symbol");

                } else {
                    if (obj.calcIndicators() == 1) {
                        obj.showFinalOutput();
                        if (obj.rsi <= 30) {
                            System.out.println("Look at buying");

                        }
                    }
                }
            }


        } catch (MalformedURLException ex) {
            Logger.getLogger(Stocker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Stocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws MalformedURLException, IOException {

        Stocker app = new Stocker();
        app.init_app();
    }
}
