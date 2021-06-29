package com.dotcom.rbs_system.Classes;

import com.google.firebase.database.FirebaseDatabase;

public class UniquePushID {
    private static UniquePushID uniquePushIDObj = new UniquePushID();

    String uniquePushID;

    public String getUniquePushID() {
        return uniquePushID;
    }

    public void generateUniquePushID() {
        uniquePushID = FirebaseDatabase.getInstance().getReference().push().getKey();;
    }

    public void setUniquePushID(String uniquePushID) {
        this.uniquePushID = uniquePushID;
    }

    public void clearUniquePushID() {
        uniquePushID = null;
    }

    private UniquePushID() {}

    public static UniquePushID getInstance() {
        return uniquePushIDObj;
    }
}
