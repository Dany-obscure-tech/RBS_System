package com.dotcom.rbs_system.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dotcom.rbs_system.Item_detail;
import com.dotcom.rbs_system.Progress_dialoge;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class RBSItemDetails {
    Context context;
    int i,k=0,l=0;

    StorageReference idStorageReference;
    DatabaseReference reference;

    Progress_dialoge pd;

    private static RBSItemDetails rbsItemDetails = new RBSItemDetails();

    String itemCategory,itemID,addedBy,itemName,itemCondition,personalNotes,itemPrice,itemDescription,key,key2,noOfImages;

    List<Uri> imageUrlList;

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

    public void uploadItemDetails(Context contextt){
        context = contextt;
        idStorageReference = FirebaseStorage.getInstance().getReference().child("Item_Images");
        pd = new Progress_dialoge();

        pd.showProgressBar(context);
        reference = FirebaseDatabase.getInstance().getReference();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

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

                key2 = reference.push().getKey();
                idStorageReference.child(key).child("image_"+String.valueOf(i+1)).putFile(imageUrlList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        idStorageReference.child(key).child("image_"+String.valueOf(l+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println("k = "+k+" "+uri);
                                reference.child("Items").child(itemCategory).child(key).child("Image_urls").child("image_"+(k+1)).setValue(String.valueOf(uri.toString()));

                                k++;
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
            pd.dismissProgressBar(context);

//            for (int i = 0;i<imageUrlList.size();i++){
//                taskSnapshotList.get(i).getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        System.out.println(uri);
//                    }
//                });
//            }


        }else {
            Toast.makeText(context, "Internet is not Connected", Toast.LENGTH_SHORT).show();
            connected = false;
        }

    }

    public void clearData(){
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
        key2 = null;
        noOfImages = null;
        imageUrlList = null;
    }
}
