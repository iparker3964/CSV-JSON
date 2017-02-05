package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConverterTest {
    
	private String csvString;
    private String jsonString;
	private Main main;
   
   private static String readFile(String file) throws IOException {

        File myfile = new File(file);

        Scanner myScanner = new Scanner(myfile);

        String newline = System.getProperty("line.separator");

        String outPut = "";



        try {

            while(myScanner.hasNextLine()) {

                outPut += myScanner.nextLine() + newline;

            }

            return outPut;

        } finally {

            myScanner.close();

        }

    }



    @Before
    public void setUp() {
		main = new Main();
		main.runMain();
        
		try {

            jsonString = readFile("src/test/resources/grades.json");

            csvString = readFile("src/test/resources/grades.csv");

        } catch(IOException e) {}

    }

	@Test
    public void testConvertCSVtoJSON() {
        assertTrue(Converter.jsonStringsAreEqual(Converter.csvToJson(csvString), jsonString));
    }

    @Test
    public void testConvertJSONtoCSV() {
		String csvTest="\"ID\",\"Total\",\"Assignment 1\",\"Assignment 2\",\"Exam 1\"\n" +

                        "\"111278\",\"611\",\"146\",\"128\",\"337\"\n" +

                        "\"111352\",\"867\",\"227\",\"228\",\"412\"\n" +

                        "\"111373\",\"461\",\"96\",\"90\",\"275\"\n" +

                        "\"111305\",\"835\",\"220\",\"217\",\"398\"\n" +

                        "\"111399\",\"898\",\"226\",\"229\",\"443\"\n" +

                        "\"111160\",\"454\",\"77\",\"125\",\"252\"\n" +

                        "\"111276\",\"579\",\"130\",\"111\",\"338\"\n" +

                        "\"111241\",\"973\",\"236\",\"237\",\"500\"\n" 

                        ;
		assertEquals(csvTest, main.csv);
    }


}