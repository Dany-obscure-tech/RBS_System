package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.RepairTicketFaults;
import com.dotcom.rbs_system.Classes.TermsAndConditions;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.dotcom.rbs_system.async.AsyncEscPosPrinter;
import com.dotcom.rbs_system.async.AsyncTcpEscPosPrint;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Invoice_preview_repair_ticket extends AppCompatActivity {

    TextView ticket_no_textview;
    AsyncTask asyncTask = null;
    AsyncEscPosPrinter printer;
    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;
    String date, customer_name, customer_email, customer_ID, customer_ph_no, item_name, item_id, keyId, amount, special_condition,ticketNo;
    String shopTermsAndConditions, rbsTermsAndConditions;
    TextView date_textView, customerName_textView, customerEmail_textView, customerID_textView, customerPhno_textView, itemName_textView, itemID_textView, amount_Currency_textView, amount_textView;
    TextView exit_textview, special_condition_textView;
    ImageButton print_image_btn, email_image_btn, whatsapp_image_btn;
    String ticketType,email_message;
    String faultString = "FAULT LIST \n\n";
    String pendingFaultString = "PENDING FAULT LIST \n\n";

    ImageView placeholderLogo_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_preview_repair_ticket);

        initialization();
        onclicklistners();


    }

    private void onclicklistners() {
        exit_textview_listner();
        print_image_btn_listner();
        email_image_btn_listner();
        whatsapp_image_btn_listner();
        cancel_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation_alert.dismiss();
            }
        });


        yes_btn_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                    Toast.makeText(Invoice_preview_repair_ticket.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.setTextToPrint(
                printingData()
        );
    }


    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter2(AsyncEscPosPrinter printer) {
        return printer.setTextToPrint(
                printingData()
        );
    }

    private String printingData() {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        return "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, placeholderLogo_imageView.getDrawable()) + "</img>\n" +
                "[L]\n" +
                "[L]" + UserDetails.getInstance().getShopName() + "\n" +
                "[L]\n" +
                "[L]Phone: " + UserDetails.getInstance().getShopPhno() + "\n" +
                "[L]\n" +
                "[L]Email: " + UserDetails.getInstance().getShopEmail() + "\n" +
                "[L]\n" +
                "[L]Address: " + UserDetails.getInstance().getShopAddress() + "\n" +
                "[L]\n" +
                "[L]Invoice Printed" + "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                "[C]\n" +
                "[L]Ticket no:" + ticket_no_textview.getText().toString() + "\n" +
                "[L]" + ticketType + "\n" +
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
                "[L]Amount: [R]" + amount_Currency_textView.getText().toString() + amount_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Special Condition:" + "\n" +
                "[L]" + special_condition_textView.getText().toString() + "\n" +
                "[C]\n" +
                "[C]<font color='bg-black'> -------------------------------- </font>\n" +
                "[C]\n" +
                rbsTermsAndConditions + "\n" +
                shopTermsAndConditions + "\n" +
                "[C]\n" +
                "[C]\n" +
                "[C]\n" +
                "[L]\n";
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
                confirmation_alert.show();
            }
        });

    }

    private void initialization() {

        confirmation_alert = new Dialog(this);
        confirmation_alert.setContentView(R.layout.exit_confirmation_alert);
        yes_btn_textview = confirmation_alert.findViewById(R.id.yes_btn_textview);
        cancel_btn_textview = confirmation_alert.findViewById(R.id.cancel_btn_textview);

        date = getIntent().getStringExtra("Date");
        ticketType = getIntent().getStringExtra("TICKET_TYPE");
        customer_name = getIntent().getStringExtra("Customer_Name");
        customer_email = getIntent().getStringExtra("Customer_Email");
        customer_ID = getIntent().getStringExtra("Customer_ID");
        customer_ph_no = getIntent().getStringExtra("Customer_Ph_No");
        item_name = getIntent().getStringExtra("Item_Name");
        item_id = getIntent().getStringExtra("Item_ID");
        keyId = getIntent().getStringExtra("KeyID");
        ticketNo = getIntent().getStringExtra("Ticket_No");
        amount = getIntent().getStringExtra("Amount");
        special_condition = getIntent().getStringExtra("Special_condition");

        ticket_no_textview = findViewById(R.id.ticket_no_textview);
        date_textView = findViewById(R.id.date_textView);
        special_condition_textView = findViewById(R.id.special_condition_textView);
        customerName_textView = findViewById(R.id.customerName_textView);
        customerEmail_textView = findViewById(R.id.customerEmail_textView);
        customerID_textView = findViewById(R.id.customerID_textView);
        customerPhno_textView = findViewById(R.id.customerPhno_textView);
        itemName_textView = findViewById(R.id.itemName_textView);
        itemID_textView = findViewById(R.id.itemID_textView);
        amount_Currency_textView = findViewById(R.id.amount_Currency_textView);
        amount_textView = findViewById(R.id.amount_textView);
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
        amount_textView.setText(amount);
        special_condition_textView.setText(special_condition);
        ticket_no_textview.setText(ticketNo);
        amount_Currency_textView.setText(String.valueOf(Currency.getInstance().getCurrency()));

        placeholderLogo_imageView = (ImageView) findViewById(R.id.placeholderLogo_imageView);
        Picasso.get().load(UserDetails.getInstance().getShopLogo()).into(placeholderLogo_imageView);

        shopTermsAndConditions = UserDetails.getInstance().getShopTermsAndConditions();
        rbsTermsAndConditions = TermsAndConditions.getInstance().getTermsAndConditions();


        for (int i = 0;i<RepairTicketFaults.getInstance().getFaultNameList().size();i++){
            faultString = faultString+"Fault: "+String.valueOf(i+1)+": "+RepairTicketFaults.getInstance().getFaultNameList().get(i)+"\n"+
                    "Price: "+Currency.getInstance().getCurrency()+RepairTicketFaults.getInstance().getFaultPriceList().get(i)+"\n\n";
        }

        if (RepairTicketFaults.getInstance().getPendingFaults()){

            for (int i = 0;i<RepairTicketFaults.getInstance().getPendingFaultNameList().size();i++){
                pendingFaultString = pendingFaultString+"Fault: "+String.valueOf(i+1)+": "+RepairTicketFaults.getInstance().getPendingFaultNameList().get(i)+"\n"+
                        "Price: "+Currency.getInstance().getCurrency()+RepairTicketFaults.getInstance().getPendingFaultPriceList().get(i)+"\n\n";
            }
        }else {
            pendingFaultString="";
        }



        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        email_message = UserDetails.getInstance().getShopName() + "\n" +
                "Phone  " + UserDetails.getInstance().getShopPhno() + "\n" +
                "Email  " + UserDetails.getInstance().getShopEmail() + "\n" +
                "Address  " + UserDetails.getInstance().getAddress() + "\n" +
                "Invoice Printed  " + format.format(new Date()) + "\n" +
                "Ticket no: " + ticketNo + "\n" +
                ticketType + "\n" +
                "============================" + "\n" +
                "CUSTOMER" + "\n" +
                "Name: " + customer_name + "\n" +
                "Email: " + customer_email + "\n" +
                "Id: " + customer_ID + "\n" +
                "Phone number: " + customer_ph_no + "\n" +
                "----------------------------" + "\n" +
                "ITEM" + "\n" +
                "Name: " + item_name + "\n" +
                "Id: " + item_id + "\n" +
                faultString + "\n" +
                pendingFaultString + "\n" +
                "============================" + "\n" +
                "Amount: " + amount_Currency_textView.getText().toString() + " " + amount + "\n" +
                "Special Condition: " + special_condition + "\n" +
                "----------------------------" + "\n" +
                "[C]\n" +
                "[L]<font color='black' size='tall'> Terms & Conditions </font>\n" +
                "[C]\n" +
                rbsTermsAndConditions + "\n" +
                shopTermsAndConditions + "\n" +
                "[C]\n" +
                "[L]\n" +
                "[L]\n" +
                "[L]\n";
    }
}