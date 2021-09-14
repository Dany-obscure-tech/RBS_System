package com.dotcom.rbs_system.Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InvoiceNumberGenerator {

    public String generateNumber() throws ParseException {
        String invoiceNumber = null;

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-", Locale.getDefault());
        String formattedDate = df.format(c);
        String year = String.valueOf(c.getYear());
//        String year = String.valueOf(c.getYear()).substring(String.valueOf(c.getYear()).length()-2);

        if (c.getMonth()==0){
            invoiceNumber = "JAN"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==1){
            invoiceNumber = "FEB"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==2){
            invoiceNumber = "MAR"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==3){
            invoiceNumber = "APR"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==4){
            invoiceNumber = "MAY"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==5){
            invoiceNumber = "JUN"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==6){
            invoiceNumber = "JUL"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==7){
            invoiceNumber = "AUG"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==8){
            invoiceNumber = "SEP"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==9){
            invoiceNumber = "OCT"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==10){
            invoiceNumber = "NOV"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }
        if (c.getMonth()==11){
            invoiceNumber = "DEC"+c.getDate()+year+c.getHours()+c.getMinutes()+c.getSeconds();
        }


        return invoiceNumber;
    }
}
