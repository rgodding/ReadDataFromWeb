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
                getValutas(sc, tempText);
            }
        }
        //Retrieving the String from the String Buffer object
//        String result = sb.toString();
//        System.out.println(result);
        userMenu();
    }

    private static void userMenu(){
        while(true) {
            System.out.println("\nMenu:\n" +
                    "\n1: View Rates" +
                    "\n2: Exchange DKK" +
                    "\n3: Exchange Foreign Valuta" +
                    "\n0: Exit program");
            switch (numberInput()) {
                case 1:
                    viewRates();
                    break;
                case 2:
                    exchangeDKK();
                    break;
                case 3:
                    exchangeForeign();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("invalid option");
            }
        }
    }
    private static void viewRates(){
        printArray();
    }
    private static void exchangeDKK(){
        Rateinfo tempRate = findValuta();
        while(tempRate == null){
            tempRate = findValuta();
        }
        System.out.print("How many DKK will be exchanged?: ");
        int input = numberInput();
        System.out.println("the given amount will result in " + exchangeDKK(input, Double.parseDouble(tempRate.getRate())) + " in " + tempRate.getCode());
    }
    private static void exchangeForeign(){
        Rateinfo tempRate = findValuta();
        while(tempRate == null){
            tempRate = findValuta();
        }
        System.out.print("How many " + tempRate.getCode() + " will be exchanged?: ");
        int input = numberInput();
        System.out.println("the given amount of " + tempRate.getCode() + " results in " + exchangeForeign(Double.parseDouble(tempRate.getRate()), input) + " DKK");

    }
    public static Rateinfo findValuta(){
        Scanner sc = new Scanner(System.in);
        System.out.print("which valuta: ");
        String input = sc.nextLine().toUpperCase();
        for (int i = 0; i < rateinfos.size(); i++) {
            if(input.equals(rateinfos.get(i).getCode())){
                return rateinfos.get(i);
            }
        }
        return null;
    }
    private static void getValutas(Scanner sc, String tempText) {
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
                tempText = tempText + " "+ sc.next();
                tempText = tempText.replaceAll("\"","");
                System.out.print(" " + tempText);
                desc = tempText;
            }
        }
        else if (tempText.contains("rate")) {
            tempText = tempText.replaceAll("rate=","").replaceAll("\"","");
            System.out.print(" "+ tempText);
            String temp = tempText;
            if(!temp.equals("-")) {
                String[] temp2 = temp.split(",");
                rate = temp2[0] + "." + temp2[1];
            } else {
                rate = String.valueOf(0);
            }
        }
        if(tempText.contains("/>"))
        {
            System.out.println();
            tempValuta();
        }

    }
    private static void tempValuta() {
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

    //Processor
    private static int numberInput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("type a number: ");
        String input = sc.nextLine();
        while(!input.matches("\\d+")){
            System.out.print("Enter a valid number: ");
            input = sc.nextLine();
        }
        return Integer.parseInt(input);
    }
    private static double exchangeDKK(double amount, double foreignRate){
        return amount / (foreignRate/100);
    }
    private static double exchangeForeign(double foreignRate, double amount){
        return amount * foreignRate/100;
    }
}
