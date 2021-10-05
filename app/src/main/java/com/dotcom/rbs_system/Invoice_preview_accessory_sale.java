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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dotcom.rbs_system.Classes.AccessoryList;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.TermsAndConditions;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.dotcom.rbs_system.async.AsyncEscPosPrinter;
import com.dotcom.rbs_system.async.AsyncTcpEscPosPrint;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Invoice_preview_accessory_sale extends AppCompatActivity {

    AsyncTask asyncTask = null;
    AsyncEscPosPrinter printer;
    Dialog confirmation_alert;
    TextView yes_btn_textview, cancel_btn_textview;
    String date, customer_name, customer_ph_no, amount,ticketNo;
    String shopTermsAndConditions, rbsTermsAndConditions;
    TextView date_textView,invoiceNo_textview, customerName_textView, customerPhno_textView, amount_Currency_textView, amount_textView;
    TextView exit_textview;
    ImageButton print_image_btn, email_image_btn, whatsapp_image_btn;
    String ticketType,email_message;

    String accessories="";
    ImageView placeholderLogo_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_preview_accessory_sale);


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
                    Toast.makeText(Invoice_preview_accessory_sale.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
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
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{customer_email});
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
                "[L]Invoice no:" + ticketNo + "\n" +
                "[L]" + ticketType + "\n" +
                "[L]\n" +
                "[C]<font color='bg-black'>================================</font>\n" +
                "[L]\n" +
                "[C]<font color='black' size='wide'> CUSTOMER </font>\n" +
                "[L]\n" +
                "[L]Name:" + customerName_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Phone no:" + customerPhno_textView.getText().toString() + "\n" +
                "[C]--------------------------------\n" +
                "[L]ACCESSORIES"+"\n" +
                "[L]\n" +
                "[L]" + accessories +
                "[L]\n" +
                "[L]Amount: [R]" + amount_Currency_textView.getText().toString() + amount_textView.getText().toString() + "\n" +
                "[L]\n" +
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
        ticketType = getIntent().getStringExtra("Invoice_Type");
        customer_name = getIntent().getStringExtra("Customer_Name");
        customer_ph_no = getIntent().getStringExtra("Customer_Ph_No");
        ticketNo = getIntent().getStringExtra("Invoice_No");
        amount = getIntent().getStringExtra("Amount");

        date_textView = findViewById(R.id.date_textView);
        invoiceNo_textview = findViewById(R.id.invoiceNo_textview);
        customerName_textView = findViewById(R.id.customerName_textView);
        customerPhno_textView = findViewById(R.id.customerPhno_textView);
        amount_Currency_textView = findViewById(R.id.amount_Currency_textView);
        amount_textView = findViewById(R.id.amount_textView);
        exit_textview = findViewById(R.id.exit_textview);

        email_image_btn = findViewById(R.id.email_image_btn);
        print_image_btn = findViewById(R.id.print_image_btn);
        whatsapp_image_btn = findViewById(R.id.whatsapp_image_btn);

        date_textView.setText(date);
        invoiceNo_textview.setText(ticketNo);
        customerName_textView.setText(customer_name);
        customerPhno_textView.setText(customer_ph_no);
        amount_textView.setText(amount);
        amount_Currency_textView.setText(String.valueOf(Currency.getInstance().getCurrency()));

        placeholderLogo_imageView = (ImageView) findViewById(R.id.placeholderLogo_imageView);
        Picasso.get().load(UserDetails.getInstance().getShopLogo()).into(placeholderLogo_imageView);

        shopTermsAndConditions = UserDetails.getInstance().getShopTermsAndConditions();
        rbsTermsAndConditions = TermsAndConditions.getInstance().getTermsAndConditions();

        for (int i = 0; i< AccessoryList.getInstance().getAccessoryNameList().size(); i++){
            accessories = accessories +"Accessory: "+String.valueOf(i+1)+": "+AccessoryList.getInstance().getAccessoryNameList().get(i)+"\n"+
                    "Quantity: "+AccessoryList.getInstance().getAccessoryQuantityList().get(i)+"\n\n";
        }

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        email_message = UserDetails.getInstance().getShopName() + "\n" +
                "Phone  " + UserDetails.getInstance().getShopPhno() + "\n" +
                "Email  " + UserDetails.getInstance().getShopEmail() + "\n" +
                "Address  " + UserDetails.getInstance().getAddress() + "\n" +
                "Invoice Printed  " + format.format(new Date()) + "\n" +
                "Invoice no: " + ticketNo + "\n" +
                ticketType + "\n" +
                "============================" + "\n" +
                "CUSTOMER" + "\n" +
                "Name: " + customer_name + "\n" +
                "Phone number: " + customer_ph_no + "\n" +
                "============================" + "\n" +
                "Accessories: \n"+
                accessories+ "\n" +
                "Amount: " + amount_Currency_textView.getText().toString() + " " + amount + "\n" +
                "----------------------------" + "\n" +
                "[C]\n" +
                rbsTermsAndConditions + "\n" +
                shopTermsAndConditions + "\n" +
                "[C]\n" +
                "[L]\n" +
                "[L]\n" +
                "[L]\n";
    }
}