/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg415.hw1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

/**
 *
 * @author flashomer
 */
public class readJson {
    
    public static JSONObject getreadJson(String jsonUrL) throws Exception {
            File f = new File(jsonUrL);
            if (f.exists()){
                InputStream is = new FileInputStream(jsonUrL);
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                System.out.println(jsonTxt);
                JSONObject json = new JSONObject(jsonTxt);       
                return json;
            } else { return null; }

        }     

}
