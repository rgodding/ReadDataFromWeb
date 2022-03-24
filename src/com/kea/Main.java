package com.kea;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<Rateinfo> rateinfos =new ArrayList<>();

    //Temporary storage of valuta information, refreshes with each line
    private static String tempCode;
    private static String tempDesc;
    private static double tempRate;

    //Getting valutas
    public static void main(String[] args) throws IOException {
        //Instantiating the URL class
        URL url = new URL("https://www.nationalbanken.dk/_vti_bin/DN/DataService.svc/CurrencyRatesXML?lang=da");
        //Retrieving the contents of the specified page
        Scanner sc = new Scanner(url.openStream());
        //Instantiating the StringBuffer class to hold the result
        int i=0;
        while(sc.hasNext()) {
            String tempText=sc.next();
            i++;
            if (i>12 && i<181) {
                getValutas(sc, tempText);
            }
        }
        //Start of user intractable code
        userMenu();
    }
    private static void getValutas(Scanner sc, String tempText) {
        if(tempText.contains("code")) {
            System.out.print(tempText.replaceAll("code=","").replaceAll("\"",""));
            tempCode = tempText.replaceAll("code=","").replaceAll("\"","");
        }
        else if(tempText.contains("desc")) {
            tempText = tempText.replaceAll("desc=","").replaceAll("\"","").replaceAll("�","æ");
            if(tempText.contains("Euro")) {
                System.out.print(" " + tempText);
                tempDesc = tempText;
            } else {
                tempText = tempText + " "+ sc.next();
                tempText = tempText.replaceAll("\"","");
                System.out.print(" " + tempText);
                tempDesc = tempText;
            }
        }
        else if (tempText.contains("rate")) {
            tempText = tempText.replaceAll("rate=","").replaceAll("\"","");
            System.out.print(" "+ tempText);
            String temp = tempText;
            if(!temp.equals("-")) {
                String[] temp2 = temp.split(",");
                tempRate = Double.parseDouble(temp2[0] + "." + temp2[1]);
            }
        }
        if(tempText.contains("/>"))
        {
            System.out.println();
            refreshValuta();
        }

    }

    //User menu
    private static void userMenu(){
        while(true) {
            System.out.println("\nMenu:\n" +
                    "\n1: View Rates" +
                    "\n2: Exchange DKK" +
                    "\n3: Exchange Foreign Valuta" +
                    "\n0: Exit program");
            switch (numberInput()) {
                case 1:
                    printArray();
                    break;
                case 2:
                    exchange("DKK");
                    break;
                case 3:
                    exchange("notDKK");
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("invalid option");
            }
        }
    }
    private static void exchange(String choice){
        System.out.print("Which valuta will it be exchanged into?: ");
        Rateinfo tempValuta = findValuta();
        while(tempValuta==null){
            System.out.print("please enter a valid valuta code: ");
            tempValuta = findValuta();
        }
        System.out.println("The valuta chosen is, " + tempValuta.getDesc() + " with a rate of " + tempValuta.getRate());
        System.out.print("How much needs to be exchanged?: ");
        double amount = numberInput();
        if(choice.equals("DKK")){
            System.out.println("Exchanging " + amount + " of DKK gives you an amount of " +
                    tempValuta.exchangeDKKto(amount) + " in " + tempValuta.getDesc());
        }
        else if (choice.equals("notDKK")){
            System.out.println("Exchanging " + amount + " of " + tempValuta.getCode() + " gives you an amount of " +
                    tempValuta.exchangeToDKK(amount) + " in DKK");
        }
    }

    //Services
    private static Rateinfo findValuta(){
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().toUpperCase();
        for (Rateinfo rateinfo : rateinfos) {
            if (input.equals(rateinfo.getCode())) {
                return rateinfo;
            }
        }
        return null;
    }
    private static void refreshValuta() {
        //Adding the valuta to an array, then refreshing the values for a new valuta to be recorded
        rateinfos.add(new Rateinfo(tempCode, tempDesc, tempRate));
        tempCode = null;
        tempDesc = null;
        tempRate = 0;
    }
    private static int numberInput(){
        //User only able to input positive int values(haven't done that u can put in ´.´too.
        //Prevents program for crashing when putting in invalid values
        Scanner sc = new Scanner(System.in);
        System.out.print("type a number: ");
        String input = sc.nextLine();
        while(!input.matches("\\d+")){
            System.out.print("Enter a valid number: ");
            input = sc.nextLine();
        }
        return Integer.parseInt(input);
    }
    private static void printArray(){
        for (Rateinfo rateinfo : rateinfos) {
            System.out.println(rateinfo);
        }
    }
}
