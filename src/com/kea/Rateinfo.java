package com.kea;

public class Rateinfo {
    private final String code;
    private final String desc;
    private final double rate;

    /**
     * @param Code Kode der anvendes for denne valuta
     * @param Desc Beskrivelse til valuta
     * @param Rate Antal danske kr for 100 af den valuta vi omregner til
     */

    public Rateinfo(String Code, String Desc, double Rate) {
        code=Code;
        desc=Desc;
        rate=Rate;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    public double getRate() {
        return rate;
    }

    //Services
    public double exchangeToDKK(double amount){
        return amount * rate/100;
    }
    public double exchangeDKKto(double amount){
        return amount / (rate/100);
    }


    @Override
    public String toString() {
        return desc + "(" + code + "): " + rate;
    }
}
