package com.kea;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static String code;
    private static String desc;
    private static String rate;
    private static ArrayList<Rateinfo> rateinfos =new ArrayList<Rateinfo>();

    public static void main(String[] args) throws IOException {
        //Instantiating the URL class
        URL url = new URL("https://www.nationalbanken.dk/_vti_bin/DN/DataService.svc/CurrencyRatesXML?lang=da");
        //Retrieving the contents of the specified page
        Scanner sc = new Scanner(url.openStream());
        //Instantiating the StringBuffer class to hold the result
//        StringBuffer sb = new StringBuffer();
        int i=0;
        while(sc.hasNext()) {
            String tempText=sc.next();
//            sb.append(tempText);
            i++;

            if (i>12 && i<181) {
                extracted(sc, tempText);
            }
        }
        //Retrieving the String from the String Buffer object
//        String result = sb.toString();
//        System.out.println(result);
        System.out.println("Printing Arrays");
        printArray();
    }


    private static void extracted(Scanner sc, String tempText) {
        if(tempText.contains("code")) {
            System.out.print(tempText.replaceAll("code=","").replaceAll("\"",""));
            code = tempText.replaceAll("code=","").replaceAll("\"","");
        }
        else if(tempText.contains("desc")) {
            tempText = tempText.replaceAll("desc=","").replaceAll("\"","").replaceAll("�","æ");
            if(tempText.contains("Euro")) {
                System.out.print(" " + tempText);
                desc = tempText;
            } else {
                tempText = tempText +" "+ sc.next();
                tempText = tempText.replaceAll("\"","");
                System.out.print(" " + tempText);
            }
        }
        else if (tempText.contains("rate")) {
            tempText = tempText.replaceAll("rate=","").replaceAll("\"","");
            System.out.print(" "+ tempText);
            rate = tempText;
        }
        if(tempText.contains("/>"))
        {
            System.out.println();
            extracted();
        }

    }

    private static void extracted() {
        rateinfos.add(new Rateinfo(code, desc, rate));
        code = null;
        desc = null;
        rate = null;
    }

    private static void printArray(){
        for (int i = 0; i < rateinfos.size(); i++) {
            System.out.println(rateinfos.get(i));
        }
    }
}
