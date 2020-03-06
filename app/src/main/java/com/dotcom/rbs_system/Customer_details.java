package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class Customer_details extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ImageButton Back_btn;
    Button date_btn,submit_btn;
    TextView date_text;
    DatabaseReference reference;
    EditText ac_title,ac_phoneno,ac_id,ac_address,ac_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        reference = FirebaseDatabase.getInstance().getReference();
        ac_title = (EditText)findViewById(R.id.ac_title);
        ac_phoneno = (EditText)findViewById(R.id.ac_phoneno);
        ac_id = (EditText)findViewById(R.id.ac_id);
        ac_address = (EditText)findViewById(R.id.ac_address);
        ac_email = (EditText)findViewById(R.id.ac_email);

        Back_btn=(ImageButton)findViewById(R.id.Back_btn);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        date_btn=(Button)findViewById(R.id.date_btn);
        submit_btn=(Button)findViewById(R.id.submit_btn);
        date_text=(TextView)findViewById(R.id.date_text);
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFields()==true){
                    detailsSubmit();
                }

            }
        });

    }

    private boolean validateFields() {
        boolean valid=true;

        if (ac_title.getText().toString().isEmpty()){
            ac_title.setError("Please enter your name");
            valid=false;
        }
        if (ac_title.length()>32){
            ac_title.setError("Name character limit is 32");
            valid=false;
        }
        if (ac_phoneno.getText().toString().isEmpty()){
            ac_phoneno.setError("Please enter your phone number");
            valid=false;
        }
        if (ac_phoneno.length()<11){
            ac_phoneno.setError("Please enter valid contact no");
            valid=false;
        }
        if (ac_id.getText().toString().isEmpty()){
            ac_id.setError("Please enter your id");
            valid=false;
        }

        if (date_text.getText().toString().equals("Select date")){
            Toast.makeText(this, "Select date of birth", Toast.LENGTH_LONG).show();
            valid=false;
        }

        if (ac_address.getText().toString().isEmpty()){
            ac_address.setError("Please enter your date of birth");
            valid=false;
        }
        if (ac_email.getText().toString().isEmpty()){
            ac_email.setError("Please enter your email address");
            valid=false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(ac_email.getText())).matches()){
            ac_email.setError("Please enter a valid email");
            valid=false;
        }

        return valid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_text.setText(currentDateString);
    }

    private void detailsSubmit() {
        String key = reference.push().getKey();
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("Name").setValue(ac_title.getText().toString());
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("Phone_no").setValue(ac_phoneno.getText().toString());
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("ID").setValue(ac_id.getText().toString());
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("DOB").setValue(date_text.getText().toString());
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("Address").setValue(ac_address.getText().toString());
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("Email").setValue(ac_email.getText().toString());
        reference.child("Users_databases").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Customer_list").child(key).child("key_id").setValue(key);

        finish();
    }
}
