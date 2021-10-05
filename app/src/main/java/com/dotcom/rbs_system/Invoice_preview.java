package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dotcom.rbs_system.Classes.Currency;
import com.dotcom.rbs_system.Classes.TermsAndConditions;
import com.dotcom.rbs_system.Classes.UserDetails;
import com.dotcom.rbs_system.async.AsyncEscPosPrinter;
import com.dotcom.rbs_system.async.AsyncTcpEscPosPrint;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Invoice_preview extends AppCompatActivity {

    TextView date_textView, invoiceType_textView, customerName_textView, customerEmail_textView, customerID_textView, customerPhno_textView, itemName_textView, itemID_textView, itemPriceCurrency_textView, itemPrice_textView, paid_currency_textView, paid_amount_textView;

    TextView exit_textview;

    ImageButton print_image_btn, email_image_btn, whatsapp_image_btn;

    AsyncTask asyncTask = null;

    AsyncEscPosPrinter printer;

    String date, customer_name, customer_email, customer_ID, customer_ph_no, item_name, item_id, item_price_currency, item_price, paid_amount;

    String email_message, inoice_no, shopTermsAndConditions, rbsTermsAndConditions, invoiceType;

    ImageView placeholderLogo_imageView;

    Button testBT_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_preview);

        initialization();
        onclicklistners();

    }

    private void onclicklistners() {
        exit_textview_listner();
        print_image_btn_listner();
        email_image_btn_listner();
        whatsapp_image_btn_listner();
        testBT_btn_listener();
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

    private void testBT_btn_listener() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 123);
        } else {
            testBT_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EscPosPrinter printer = null;
                    SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
                    try {
                        printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 10);
                    } catch (EscPosConnectionException e) {
                        e.printStackTrace();
                    }
                    try {
                        printer
                                .printFormattedText(
                                        "[L]\n" +
                                                "[L]<font color='black' size='wide'>" + UserDetails.getInstance().getShopName() + "</font>\n" +
                                                "[L]Address: " + UserDetails.getInstance().getShopAddress() + "\n" +
                                                "[L]\n" +
                                                "[L]<u>" + UserDetails.getInstance().getShopPhno() + "</u>\n" +
                                                "[L]" + UserDetails.getInstance().getShopEmail() + "\n" +
                                                "[L]\n" +
                                                "[L]Invoice Printed" + "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                                                "[L]Invoice no: " + inoice_no + "\n" +
                                                "[L]----- " + invoiceType + " -----\n" +
                                                "[C]<font color='bg-black'>================================</font>\n" +
                                                "[L]\n" +
                                                "[C]<font color='black' size='wide'> CUSTOMER </font>\n" +
                                                "[L]\n" +
                                                "[L]" + customerName_textView.getText().toString() + "\n" +
                                                "[L]<u>" + customerPhno_textView.getText().toString() + "</u>\n" +
                                                "[C]--------------------------------\n" +
                                                "[L]\n" +
                                                "[C]<font color='black' size='wide'> ITEM </font>\n" +
                                                "[L]\n" +
                                                "[L]" + itemName_textView.getText().toString() + "\n" +
                                                "[L]\n" +
                                                "[L]Serial no:" + itemID_textView.getText().toString() + "\n" +
                                                "[C]================================\n" +
                                                "[L]Total Price: [R]" + Currency.getInstance().getCurrency() + paid_amount_textView.getText().toString() + "\n" +
                                                "[L]Date: [R]" + date_textView.getText().toString() + "\n" +
                                                "[C]<font color='bg-black'> -------------------------------</font>\n" +
                                                "[L]<font color='black' size='tall'> Terms & Conditions </font>\n" +
                                                "[C]\n<font size='small'>"+
                                                rbsTermsAndConditions + "\n" +
                                                shopTermsAndConditions + "</font>\n" +
                                                "[C]\n" +
                                                "[L]\n"
                                );
                    } catch (EscPosConnectionException e) {
                        e.printStackTrace();
                    } catch (EscPosParserException e) {
                        e.printStackTrace();
                    } catch (EscPosEncodingException e) {
                        e.printStackTrace();
                    } catch (EscPosBarcodeException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

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
        inoice_no = getIntent().getStringExtra("Invoice_No");
        invoiceType = getIntent().getStringExtra("Invoice_Type");
        shopTermsAndConditions = UserDetails.getInstance().getShopTermsAndConditions();
        rbsTermsAndConditions = TermsAndConditions.getInstance().getTermsAndConditions();

        placeholderLogo_imageView = (ImageView) findViewById(R.id.placeholderLogo_imageView);
        Picasso.get().load(UserDetails.getInstance().getShopLogo()).into(placeholderLogo_imageView);

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        email_message = UserDetails.getInstance().getShopName() + "\n" +
                "Phone  " + UserDetails.getInstance().getShopPhno() + "\n" +
                "Email  " + UserDetails.getInstance().getShopEmail() + "\n" +
                "Address  " + UserDetails.getInstance().getAddress() + "\n" +
                "Invoice Printed  " + format.format(new Date()) + "\n" +
                "Invoice No: " + inoice_no + "\n" +
                "============================" + "\n" +
                "CUSTOMER" + "\n" +
                "Name: " + customer_name + "\n" +
                "Email: " + customer_email + "\n" +
                "ID: " + customer_ID + "\n" +
                "Phone no: " + customer_ph_no + "\n" +
                "----------------------------" + "\n" +
                "ITEM" + "\n" +
                "Name: " + item_name + "\n" +
                "Serial No: " + item_id + "\n" +
                "============================" + "\n" +
                "Total Price: " + item_price_currency + " " + paid_amount + "\n" +
                "Buy Date: " + date + "\n" +
                "----------------------------" + "\n" +
                "TERMS & CONDITIONS" + "\n" +
                rbsTermsAndConditions + "\n" +
                shopTermsAndConditions + "\n";

        date_textView = findViewById(R.id.date_textView);
        invoiceType_textView = findViewById(R.id.invoiceType_textView);
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
        testBT_btn = findViewById(R.id.testBT_btn);


        date_textView.setText(date);
        invoiceType_textView.setText(invoiceType);
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

    public String printingData() {
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
                "[L]Invoice no: " + inoice_no + "\n" +
                "[L]\n" +
                "[L]----- " + invoiceType + " -----\n" +
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
                "[L]Total Price: [R]" + Currency.getInstance().getCurrency() + paid_amount_textView.getText().toString() + "\n" +
                "[L]\n" +
                "[L]Buy Date: [R]" + date_textView.getText().toString() + "\n" +
                "[C]\n" +
                "[C]<font color='bg-black'> -------------------------------- </font>\n" +
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