package com.dotcom.rbs_system.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dotcom.rbs_system.Progress_dialoge;
import com.dotcom.rbs_system.Sale;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class RBSCustomerDetails {
    String check;

    Context context;
    int i,k=0,l=0;

    StorageReference idStorageReference;
    DatabaseReference reference;

    Progress_dialoge pd;

    private static RBSCustomerDetails rbsCustomerDetails = new RBSCustomerDetails();

    String customerName,customerPhNo,customerId,customerDob,customerAddress, customerPostcode,customerHouseNo,customerEmail,key;
    String firstImageUrl;

    List<Uri> imageUrlList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhNo() {
        return customerPhNo;
    }

    public void setCustomerPhNo(String customerPhNo) {
        this.customerPhNo = customerPhNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerDob() {
        return customerDob;
    }

    public void setCustomerDob(String customerDob) {
        this.customerDob = customerDob;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

    public String getCustomerHouseNo() {
        return customerHouseNo;
    }

    public void setCustomerHouseNo(String customerHouseNo) {
        this.customerHouseNo = customerHouseNo;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<Uri> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<Uri> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    private RBSCustomerDetails() {}

    public static RBSCustomerDetails getInstance() {
        return rbsCustomerDetails;
    }

    public void uploadCustomerDetails(Context contextt){
        context = contextt;
        idStorageReference = FirebaseStorage.getInstance().getReference().child("Customer_IDs");
        pd = new Progress_dialoge();

        pd.showProgressBar(context);
        reference = FirebaseDatabase.getInstance().getReference();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            reference.child("Customer_list").child(key).child("Name").setValue(customerName);
            reference.child("Customer_list").child(key).child("Phone_no").setValue(customerPhNo);
            reference.child("Customer_list").child(key).child("ID").setValue(customerId);
            reference.child("Customer_list").child(key).child("DOB").setValue(customerDob);
            reference.child("Customer_list").child(key).child("Address").setValue(customerAddress);
            reference.child("Customer_list").child(key).child("House_door").setValue(customerHouseNo);
            reference.child("Customer_list").child(key).child("Email").setValue(customerEmail);
            reference.child("Customer_list").child(key).child("Postcode").setValue(customerPostcode);
            reference.child("Customer_list").child(key).child("key_id").setValue(key);
            reference.child("Customer_list").child(key).child("added_by").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));



            for (i = 0; i<imageUrlList.size();i++) {

                idStorageReference.child(key).child("image_"+String.valueOf(i+1)).putFile(imageUrlList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        idStorageReference.child(key).child("image_"+String.valueOf(l+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (k==0){
                                    firstImageUrl= String.valueOf(uri);
                                    if (RBSItemDetails.getInstance().getCheck().equals("Sale new item")) {
                                        new Customer_history_class().uploadCustomerImagetoItemHistory(RBSItemDetails.getInstance().getKey(), UniquePushID.getInstance().getUniquePushID(),firstImageUrl);
                                    }
                                }
                                System.out.println("k = "+k+" "+uri);
                                reference.child("Customer_list").child(key).child("ID_Image_urls").child("image_"+(k+1)).setValue(String.valueOf(uri.toString()));

                                k++;

                                if (k>imageUrlList.size()){
                                    pd.dismissProgressBar(context);
                                }
                            }
                        });
                        l++;




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismissProgressBar(context);
                        Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        pd.dismissProgressBar(context);
                    }
                });
            }

        }else {
            Toast.makeText(context, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    public void clearData(){
        idStorageReference = null;
        l=0;
        k=0;
        i=0;
        pd = null;
        reference = null;
        check = null;
        context = null;
        key = null;
        imageUrlList = null;

        context = null;
        customerName = null;
        customerPhNo = null;
        customerId = null;
        customerDob = null;
        customerHouseNo = null;
        customerAddress = null;
        customerPostcode = null;
        customerEmail = null;
        key = null;
        imageUrlList = null;

    }
}
