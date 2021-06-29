package com.dotcom.rbs_system.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Customer_history_class {




    DatabaseReference customerHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_history");
    DatabaseReference itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history");

    public void twelveValues(String customerId, String pushId, String date, String item_category, String item_imageUrl, String item_keyId, String item_name, String item_serialno, String rbs, String shopkeeper_image, String shopkeeper_keyId, String shopkeeper_name, long timestamp){
        customerHistoryRef.child(customerId).child(pushId).child("Date").setValue(date);
        customerHistoryRef.child(customerId).child(pushId).child("Item_keyCategory").setValue(item_category);
        customerHistoryRef.child(customerId).child(pushId).child("Item_image").setValue(item_imageUrl);
        customerHistoryRef.child(customerId).child(pushId).child("Item_keyId").setValue(item_keyId);
        customerHistoryRef.child(customerId).child(pushId).child("Item_name").setValue(item_name);
        customerHistoryRef.child(customerId).child(pushId).child("Item_serialno").setValue(item_serialno);
        customerHistoryRef.child(customerId).child(pushId).child("RBS").setValue(rbs);
        customerHistoryRef.child(customerId).child(pushId).child("Shopkeeper_image").setValue(shopkeeper_image);
        customerHistoryRef.child(customerId).child(pushId).child("Shopkeeper_keyId").setValue(shopkeeper_keyId);
        customerHistoryRef.child(customerId).child(pushId).child("Shopkeeper_name").setValue(shopkeeper_name);
        customerHistoryRef.child(customerId).child(pushId).child("Timestamp").setValue(timestamp);
    }

    public void uploadCustomerImagetoItemHistory(String itemId, String pushId,String customerFirstImage){
        itemHistoryRef.child(itemId).child(pushId).child("Customer_image").setValue(customerFirstImage);
    }
}
