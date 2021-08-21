package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.async.AsyncEscPosPrinter;
import com.dotcom.rbs_system.async.AsyncTcpEscPosPrint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Invoice_preview extends AppCompatActivity {

    TextView date_textView, customerName_textView, customerEmail_textView, customerID_textView, customerPhno_textView, itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, paid_currency_textView, paid_amount_textView;

    TextView exit_textview;

    ImageButton print_image_btn, email_image_btn, whatsapp_image_btn;

    AsyncTask asyncTask = null;

    AsyncEscPosPrinter printer;

    String date, customer_name, customer_email, customer_ID, customer_ph_no, item_name, item_id, item_price_currency, item_price, paid_amount;

    String email_message, inoice_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_preview);

        initialization();
        onclicklistners();
//Todo print ma shop ki details show karwani rehti hay aur whatsapp ma bhi

    }

    private void onclicklistners() {
        exit_textview_listner();
        print_image_btn_listner();
        email_image_btn_listner();
        whatsapp_image_btn_listner();
    }

    private void whatsapp_image_btn_listner() {
        whatsapp_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean installed = isAppInstalled("com.whatsapp");

                if (installed) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + customer_ph_no + "&text=" + email_message));
                    startActivity(intent);
                } else {
                    Toast.makeText(Invoice_preview.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isAppInstalled(String s) {

        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;

    }

    private void email_image_btn_listner() {
        email_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{customer_email});
                email.putExtra(Intent.EXTRA_SUBJECT, "BUY LOCAL Invoice");
                email.putExtra(Intent.EXTRA_TEXT, email_message);

//need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }

    private void print_image_btn_listner() {
        print_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printTcp();
            }
        });
    }

    private void printTcp() {
        SharedPreferences preferences = getSharedPreferences(RBS_setting.PRINTER_SETTINGS, MODE_PRIVATE);
        String port_value = preferences.getString(RBS_setting.Printer_Port_Number, "printer_port_number");
        String ip_address = preferences.getString(RBS_setting.Printer_Ip_Address, "printer_ip_address");

        if (asyncTask == null) {
            try {
                asyncTask = new AsyncTcpEscPosPrint(this).execute(this.getAsyncEscPosPrinter(new TcpConnection(ip_address, Integer.parseInt(port_value))));
            } catch (NumberFormatException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid TCP port address")
                        .setMessage("Port field must be a number.")
                        .show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
            asyncTask = null;

            try {
                asyncTask = new AsyncTcpEscPosPrint(this).execute(this.getAsyncEscPosPrinter2(printer));
            } catch (NumberFormatException e) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid TCP port address")
                        .setMessage("Port field must be a number.")
                        .show();
                e.printStackTrace();
            }
        }

    }

    private void exit_textview_listner() {
        exit_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization() {

        // getting data

        date = getIntent().getStringExtra("Date");
        customer_name = getIntent().getStringExtra("Customer_Name");
        customer_email = getIntent().getStringExtra("Customer_Email");
        customer_ID = getIntent().getStringExtra("Customer_ID");
        customer_ph_no = getIntent().getStringExtra("Customer_Ph_No");
        item_name = getIntent().getStringExtra("Item_Name");
        item_id = getIntent().getStringExtra("Item_ID");
        item_price_currency = getIntent().getStringExtra("Item_Price_Currency");
        item_price = getIntent().getStringExtra("Item_Price");
        paid_amount = getIntent().getStringExtra("Paid_Amount");

        inoice_no = "12345";
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        email_message = "Invoice Printed  " + format.format(new Date()) + "\n" +
                "Invoice No: " + inoice_no + "\n" +
                "============================" + "\n" +
                "CUSTOMER" + "\n" +
                "Customer Name: " + customer_name + "\n" +
                "Customer Email: " + customer_email + "\n" +
                "Customer Id: " + customer_ID + "\n" +
                "Customer Phone number: " + customer_ph_no + "\n" +
                "----------------------------" + "\n" +
                "ITEM" + "\n" +
                "Item Name: " + item_name + "\n" +
                "Item Id: " + item_id + "\n" +
                "============================" + "\n" +
                "Paid Amount: " + item_price_currency + " " + paid_amount + "\n" +
                "Buy Date: " + date + "\n" +
                "----------------------------" + "\n" +
                "Terms And Conditions" + "\n" +
                "RBS Condition 1, " + "RBS Condition 2, " + "RBS Condition 3, " + "RBS Condition 4, " + "RBS Condition 5, " + "\n" +
                "Shopkeeper Condition 1" + "Shopkeeper Condition 2" + "Shopkeeper Condition 3" + "Shopkeeper Condition 4" + "Shopkeeper Condition 5" + "\n";


        date_textView = findViewById(R.id.date_textView);
        customerName_textView = findViewById(R.id.customerName_textView);
        customerEmail_textView = findViewById(R.id.customerEmail_textView);
        customerID_textView = findViewById(R.id.customerID_textView);
        customerPhno_textView = findViewById(R.id.customerPhno_textView);
        itemName_textView = findViewById(R.id.itemName_textView);
        itemID_textView = findViewById(R.id.itemID_textView);
        itemPriceCurrency_textView = findViewById(R.id.itemPriceCurrency_textView);
        itemPrice_textView = findViewById(R.id.itemPrice_textView);
        paid_currency_textView = findViewById(R.id.paid_currency_textView);
        paid_amount_textView = findViewById(R.id.paid_amount_textView);
        exit_textview = findViewById(R.id.exit_textview);

        email_image_btn = findViewById(R.id.email_image_btn);
        print_image_btn = findViewById(R.id.print_image_btn);
        whatsapp_image_btn = findViewById(R.id.whatsapp_image_btn);


        date_textView.setText(date);
        customerName_textView.setText(customer_name);
        customerEmail_textView.setText(customer_email);
        customerID_textView.setText(customer_ID);
        customerPhno_textView.setText(customer_ph_no);
        itemName_textView.setText(item_name);
        itemID_textView.setText(item_id);
        itemPriceCurrency_textView.setText(item_price_currency);
        itemPrice_textView.setText(item_price);
        paid_currency_textView.setText(item_price_currency);
        paid_amount_textView.setText(paid_amount);


    }

    /**
     * Asynchronous printing
     */

    //Todo format ka variable use nhi howa check it
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.setTextToPrint(
                printingData()
        );
    }
    //Todo format ka variable use nhi howa check it

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter2(AsyncEscPosPrinter printer) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        return printer.setTextToPrint(
                printingData()
        );
    }

    public String printingData() {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        return "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                "[L]\n" +
                "[L]Invoice Printed" +
                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                "[C]\n" +
                "[L]Invoice no: 12345\n" +
                "[L]\n" +
                "[C]<font color='bg-black'>================================</font>\n" +
                "[L]\n" +
                "[C]<font color='black' size='wide'> CUSTOMER </font>\n" +
                "[L]\n" +
                "[L]Name:" + customerName_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Email:" + customerEmail_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]ID:" + customerID_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Phone no:" + customerPhno_textView.getText().toString() + "\n" +
                "[C]--------------------------------\n" +
                "[L]\n" +
                "[C]<font color='black' size='wide'> ITEM </font>\n" +
                "[L]\n" +
                "[L]Name:" + itemName_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Serial no:" + itemID_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[C]================================\n" +
                "[L]\n" +
                "[L]Paid Amount: [R]" + Currency.getInstance().getCurrency() + paid_amount_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Buy Date: [R]" + date_textView.getText().toString() + "\n" +
                "[C]\n" +
                "[C]<font color='bg-black'> -------------------------------- </font>\n" +
                "[C]\n" +
                "[L]<font color='black' size='tall'> Shopkeeper Terms & Conditions </font>\n" +
                "[C]\n" +
                "[L]1.This is the first condition. 2. This is the second condition\n" +
                "[C]\n" +
                "[L]<font color='black' size='tall'> Buy Local Terms & Conditions </font>\n" +
                "[C]\n" +
                "[L]1.This is the first condition. 2. This is the second condition\n" +
                "[L]\n" +
                "[L]\n" +
                "[L]\n";

    }
}