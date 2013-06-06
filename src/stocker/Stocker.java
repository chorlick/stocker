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

    public void getNASDAQ() throws IOException {

        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        ftp.connect("ftp.nasdaqtrader.com");
        System.out.println("Connected...");
        //ftp.enterRemotePassiveMode();
        ftp.enterLocalPassiveMode();
        ftp.login("anonymous", "");
        ftp.setBufferSize(1024 * 1024);

        System.out.println("Connected...");
        ftp.changeWorkingDirectory("SymbolDirectory");
        InputStream in = ftp.retrieveFileStream("nasdaqlisted.txt");
        BufferedReader inbuf = new BufferedReader(new InputStreamReader(in));
        int i = 0;
        System.out.println("Connected...");
        while ((line = inbuf.readLine()) != null) {

            split = line.split("\\|");
            System.out.println(split[0]);
            stock_list.add(new StockObj(split[0]));

        }

    }

    public void init_app() {

        System.out.println("Starting...");



    }
}
