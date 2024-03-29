package com.dotcom.rbs_system.Classes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dotcom.rbs_system.Progress_dialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RBSItemDetails {
    Progress_dialog pd;
    RBSCustomerDetails rbsCustomerDetails = RBSCustomerDetails.getInstance();
    Activity activity;
    String check;
    Context context;
    int i,k=0,l=0;

    Uri firstImageUri;

    StorageReference idStorageReference;
    DatabaseReference reference,spotLightRef;



    private static RBSItemDetails rbsItemDetails = new RBSItemDetails();

    String itemCategory,itemID,addedBy,itemName,itemCondition,personalNotes,itemPrice,itemDescription,key = null,noOfImages;

    List<Uri> imageUrlList;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Uri getFirstImageUri() {
        return firstImageUri;
    }

    public void setFirstImageUri(Uri firstImageUri) {
        this.firstImageUri = firstImageUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getNoOfImages() {
        return noOfImages;
    }

    public void setNoOfImages(String noOfImages) {
        this.noOfImages = noOfImages;
    }

    public List<Uri> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<Uri> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    private RBSItemDetails() {}

    public static RBSItemDetails getInstance() {
        return rbsItemDetails;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void uploadNewItemDetails(Context contextt){
        context = contextt;
        pd = new Progress_dialog();
        idStorageReference = FirebaseStorage.getInstance().getReference().child("Item_Images");

        reference = FirebaseDatabase.getInstance().getReference();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            uploadToDatabase();

        }else {
            Toast.makeText(context, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    public void uploadToDatabase() {
        pd.showProgressBar(context);

        key = reference.push().getKey();

        reference.child("Items").child(itemCategory).child(key).child("Category").setValue(itemCategory);
        reference.child("Items").child(itemCategory).child(key).child("Item_id").setValue(itemID);
        reference.child("Items").child(itemCategory).child(key).child("added_by").setValue(addedBy);
        reference.child("Items").child(itemCategory).child(key).child("Item_name").setValue(itemName);
        reference.child("Items").child(itemCategory).child(key).child("Condition").setValue(itemCondition);
        reference.child("Items").child(itemCategory).child(key).child("Notes").setValue(personalNotes);
        reference.child("Items").child(itemCategory).child(key).child("Price").setValue(itemPrice);
        reference.child("Items").child(itemCategory).child(key).child("Description").setValue(itemDescription);
        reference.child("Items").child(itemCategory).child(key).child("key_id").setValue(key);
        reference.child("Items").child(itemCategory).child(key).child("No_of_images").setValue(String.valueOf(imageUrlList.size()));


        for (i = 0; i<imageUrlList.size();i++) {

            idStorageReference.child(key).child("image_"+String.valueOf(i+1)).putFile(imageUrlList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    idStorageReference.child(key).child("image_"+String.valueOf(l+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(k==0){
                                firstImageUri=uri;

                                if (check=="Add new item"){

                                    spotLightRef = FirebaseDatabase.getInstance().getReference("Spotlight");

                                    spotLightRef.child(key).child("key_id").setValue(key);
                                    spotLightRef.child(key).child("Category").setValue(itemCategory);
                                    spotLightRef.child(key).child("Item_name").setValue(itemName);
                                    spotLightRef.child(key).child("Price").setValue(itemPrice);
                                    spotLightRef.child(key).child("Item_image").setValue(uri.toString());
                                    spotLightRef.child(key).child("shopkeeper").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Category").setValue(itemCategory);
                                    reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Image").setValue(firstImageUri.toString());
                                    reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Item_id").setValue(key);
                                    reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Serial_no").setValue(itemID);
                                    reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Item_name").setValue(itemName);
                                    reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Price").setValue(itemPrice);
                                }

                            }
                            reference.child("Items").child(itemCategory).child(key).child("Image_urls").child("image_"+(k+1)).setValue(String.valueOf(uri.toString()));
                            k++;

                            if (k==imageUrlList.size()){
                                if (check=="Sale new item") {
                                    rbsItemDetails.switchStockSale(rbsCustomerDetails.getKey(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                                }else if (check=="Buy new item"){
                                    uploadToStock();
                                    uploadToRbsInvoiceList(rbsCustomerDetails.getKey(),addedBy);
                                    updateStockOwner(addedBy);

                                }else {
                                    pd.dismissProgressBar(context);
                                }



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
                }
            });
        }
    }

    private void uploadToStock() {

        reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Category").setValue(itemCategory);
        reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Image").setValue(firstImageUri.toString());
        reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Item_id").setValue(key);
        reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Serial_no").setValue(itemID);
        reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Item_name").setValue(itemName);
        reference.child("Stock").child("Shopkeepers").child(addedBy).child(itemCategory).child(key).child("Price").setValue(itemPrice);

        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);

        new Item_history_class().thirteenValues(key,UniquePushID.getInstance().getUniquePushID(), UserDetails.getInstance().getShopName(),FirebaseAuth.getInstance().getCurrentUser().getUid(),UserDetails.getInstance().getShopLogo(),"Buyer",rbsCustomerDetails.getCustomerName(),rbsCustomerDetails.getKey(),"Seller",rbsCustomerDetails.getFirstImageUrl(),"Trade",date.getTime(),currentDateString);
        new Item_history_class().uploadItemImagetoCustomerHistory(rbsCustomerDetails.getKey(),UniquePushID.getInstance().getUniquePushID(), String.valueOf(firstImageUri));
    }

    public void switchStockSale(String buyer, String seller) {
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Stock").child("Customers").child(itemCategory).child(key).child("Item_id").setValue(key);
        reference.child("Stock").child("Customers").child(itemCategory).child(key).child("Category").setValue(itemCategory);
        reference.child("Stock").child("Customers").child(itemCategory).child(key).child("Image").setValue(firstImageUri.toString());
        reference.child("Stock").child("Customers").child(itemCategory).child(key).child("Serial_no").setValue(itemID);
        reference.child("Stock").child("Customers").child(itemCategory).child(key).child("Item_name").setValue(itemName);
        reference.child("Stock").child("Customers").child(itemCategory).child(key).child("Price").setValue(itemPrice);

        reference.child("Stock").child("Shopkeepers").child(seller).child(itemCategory).child(key).removeValue();

        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        new Item_history_class().thirteenValues(key,UniquePushID.getInstance().getUniquePushID(), UserDetails.getInstance().getShopName(),FirebaseAuth.getInstance().getCurrentUser().getUid(),UserDetails.getInstance().getShopLogo(),"Seller",rbsCustomerDetails.getCustomerName(),rbsCustomerDetails.getKey(),"Buyer",rbsCustomerDetails.getFirstImageUrl(),"Trade",date.getTime(),currentDateString);
        new Item_history_class().uploadItemImagetoCustomerHistory(rbsCustomerDetails.getKey(),UniquePushID.getInstance().getUniquePushID(), String.valueOf(firstImageUri));
    }
    
    public void switchStockBuy(String buyer, String seller) {
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Stock").child("Shopkeepers").child(buyer).child(itemCategory).child(key).child("Item_id").setValue(key);
        reference.child("Stock").child("Shopkeepers").child(buyer).child(itemCategory).child(key).child("Category").setValue(itemCategory);
        reference.child("Stock").child("Shopkeepers").child(buyer).child(itemCategory).child(key).child("Image").setValue(firstImageUri.toString());
        reference.child("Stock").child("Shopkeepers").child(buyer).child(itemCategory).child(key).child("Serial_no").setValue(itemID);
        reference.child("Stock").child("Shopkeepers").child(buyer).child(itemCategory).child(key).child("Item_name").setValue(itemName);
        reference.child("Stock").child("Shopkeepers").child(buyer).child(itemCategory).child(key).child("Price").setValue(itemPrice);

        reference.child("Stock").child("Customers").child(itemCategory).child(key).removeValue();


        Date date = Calendar.getInstance().getTime();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(date);

        new Item_history_class().thirteenValues(key,UniquePushID.getInstance().getUniquePushID(), UserDetails.getInstance().getShopName(),FirebaseAuth.getInstance().getCurrentUser().getUid(),UserDetails.getInstance().getShopLogo(),"Buyer",rbsCustomerDetails.getCustomerName(),rbsCustomerDetails.getKey(),"Seller",rbsCustomerDetails.getFirstImageUrl(),"Trade",date.getTime(),currentDateString);
        new Item_history_class().uploadItemImagetoCustomerHistory(rbsCustomerDetails.getKey(),UniquePushID.getInstance().getUniquePushID(), String.valueOf(firstImageUri));
    }

    private void uploadToRbsInvoiceList(String previousOwnerID,String newOwnerID){
        reference.child("Stock").child("RbsInvoiceList").child(previousOwnerID).child(key).removeValue();

        reference.child("Stock").child("RbsInvoiceList").child(newOwnerID).child(key).child("Item_id").setValue(key);
        reference.child("Stock").child("RbsInvoiceList").child(newOwnerID).child(key).child("Category").setValue(itemCategory);
        reference.child("Stock").child("RbsInvoiceList").child(newOwnerID).child(key).child("Item_name").setValue(itemName);
        reference.child("Stock").child("RbsInvoiceList").child(newOwnerID).child(key).child("Price").setValue(itemPrice);
        reference.child("Stock").child("RbsInvoiceList").child(newOwnerID).child(key).child("Serial_no").setValue(itemID);
        reference.child("Stock").child("RbsInvoiceList").child(newOwnerID).child(key).child("Image").setValue(firstImageUri.toString());
    }

    private void updateStockOwner(String ownerID){
        reference.child("Stock").child("Items").child(key).child("in_stock_of").setValue(ownerID);
    }

    public void clearData(){
        idStorageReference = null;
        l=0;
        k=0;
        i=0;
        pd = null;
        reference = null;
        activity = null;
        check = null;
        context = null;
        itemCategory = null;
        itemID = null;
        addedBy = null;
        itemName = null;
        itemCondition = null;
        personalNotes = null;
        itemPrice = null;
        itemDescription = null;
        key = null;
        noOfImages = null;
        imageUrlList = null;
    }

    public void finishActivity(Activity activity){
        activity.finish();
    }
}
