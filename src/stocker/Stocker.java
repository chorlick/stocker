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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

    
    
/**
 *
 * @author Chris
 */
public class Stocker {

    //you need to instantiate some basic variables

    public LinkedList<StockObj> stock_list = new LinkedList<StockObj>();
   
    public void init_app() {
        StockObj goog;
        try {
            goog = new StockObj("GOOG");
            
            goog.simpleRelativeStrengthIndex(14);
           // goog.resetArrayValues();
            goog.simpleMACD(0);
            goog.showFinalOutput();
            
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
