package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
		JSONObject json = new JSONObject();

        JSONArray colHeaders = new JSONArray();
        colHeaders.add("Total");
        colHeaders.add("Assignment 1");
        colHeaders.add("Assignment 2");
        colHeaders.add("Exam 1");
        json.put("colHeaders", colHeaders);

        JSONArray rowHeaders = new JSONArray();
        json.put("rowHeaders", rowHeaders);

        JSONArray data = new JSONArray();
        json.put("data", data);

        CSVParser parser = new CSVParser();
		Scanner myScanner = new Scanner(csvString);

        try {
			String line = myScanner.nextLine();
			boolean result; 
			while(result = myScanner.hasNextLine()){
				line = myScanner.nextLine();
				String[] record = parser.parseLine(line);
                rowHeaders.add(record[0]);
                JSONArray row = new JSONArray();
                row.add(new Long(record[1]));
                row.add(new Long(record[2]));
                row.add(new Long(record[3]));
                row.add(new Long(record[4]));
                data.add(row);
			}
            
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        return json.toString();
    }

    public static String jsonToCsv(String jsonString) {
        JSONObject json = null;

        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String csvString = "\"ID\"," + Converter.<String>joinArray((JSONArray) json.get("colHeaders")) + "\n";

        JSONArray headers = (JSONArray) json.get("rowHeaders");
        JSONArray data = (JSONArray) json.get("data");

        for (int i = 0, j = headers.size(); i < j; i++) {
            csvString += ("\""+ (String) headers.get(i) + "\"," + Converter.<Long>joinArray((JSONArray) data.get(i)) + "\n");
        }

        return csvString;
    }

    @SuppressWarnings("unchecked")
    private static <T> String joinArray(JSONArray array) {
        String outPut = "";
        for (int i = 0, j = array.size(); i < j; i++) {
            outPut += "\"" + ((T) array.get(i)) + "\"";
            if (i < j - 1) {
                outPut += ",";
            }
        }
        return outPut;
    }

    public static boolean jsonStringsAreEqual(String str1, String str2) {
        try {
            return jsonEqual(new JSONParser().parse(str1), new JSONParser().parse(str2));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean jsonEqual(Object obj1, Object obj2) {
        if (obj1 instanceof JSONObject && obj2 instanceof JSONObject) {
				return jsonObjectEqual((JSONObject) obj1, (JSONObject) obj2);
        }
		else if (obj1 instanceof JSONArray && obj2 instanceof JSONArray) {
				return jsonArrayEqual((JSONArray) obj1, (JSONArray) obj2);
        } else {
				return obj1.equals(obj2);
        }
    }
    private static boolean jsonObjectEqual(JSONObject obj1, JSONObject obj2) {
        for (Object k : obj1.keySet()) {
            String key = (String) k;

            if (!obj2.containsKey(key) || !jsonEqual(obj1.get(key), obj2.get(key))) {
                return false;
            }
        }
        return true;
    }
    private static boolean jsonArrayEqual(JSONArray arrayOne, JSONArray arrayTwo) {
        int arrayOneSize = arrayOne.size();

        if (arrayOneSize != arrayTwo.size()) {
            return false;
        } else {
            for (int i = 0, j = arrayOneSize; i < j; i++) {
                if (!jsonEqual(arrayOne.get(i), arrayTwo.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}













