package com.dotcom.rbs_system.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item_history_class {


    String customer_name, customer_type, shopkeeper_type;

    DatabaseReference customerHistoryRef = FirebaseDatabase.getInstance().getReference("Customer_history");
    DatabaseReference itemHistoryRef = FirebaseDatabase.getInstance().getReference("Item_history");


    public void sixValues(String itemId, String pushId, String shopkeeper_name, String shopkeeper_keyId, String shopkeeper_image, String rbs, Long timestamp, String date) {
        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_name").setValue(shopkeeper_name);
        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_keyId").setValue(shopkeeper_keyId);
        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_image").setValue(shopkeeper_image);
        itemHistoryRef.child(itemId).child(pushId).child("RBS").setValue(rbs);
        itemHistoryRef.child(itemId).child(pushId).child("Timestamp").setValue(timestamp);
        itemHistoryRef.child(itemId).child(pushId).child("Date").setValue(date);
    }

    public void thirteenValues(String itemId, String pushId, String shopkeeper_name, String shopkeeper_keyId, String shopkeeper_image, String shopkeeper_type, String customer_name, String customer_keyId, String customer_type, String customer_image, String rbs, Long timestamp, String date) {
//TODO jub RBS ma add item kartey hay rbs shop screen sa to add kar kay aney kay bad app crash kar jati ha

        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_name").setValue(shopkeeper_name);
        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_keyId").setValue(shopkeeper_keyId);
        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_type").setValue(shopkeeper_type);
        itemHistoryRef.child(itemId).child(pushId).child("Shopkeeper_image").setValue(shopkeeper_image);
        itemHistoryRef.child(itemId).child(pushId).child("Customer_name").setValue(customer_name);
        itemHistoryRef.child(itemId).child(pushId).child("Customer_keyId").setValue(customer_keyId);
        itemHistoryRef.child(itemId).child(pushId).child("Customer_type").setValue(customer_type);
        itemHistoryRef.child(itemId).child(pushId).child("Customer_image").setValue(customer_image);
        itemHistoryRef.child(itemId).child(pushId).child("RBS").setValue(rbs);
        itemHistoryRef.child(itemId).child(pushId).child("Timestamp").setValue(timestamp);
        itemHistoryRef.child(itemId).child(pushId).child("Date").setValue(date);
    }

    public void uploadItemImagetoCustomerHistory(String customerId, String pushId, String item_image) {
        customerHistoryRef.child(customerId).child(pushId).child("Item_image").setValue(item_image);
    }
}
