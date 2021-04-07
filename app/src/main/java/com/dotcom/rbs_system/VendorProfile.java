package com.dotcom.rbs_system;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorProfile extends Fragment {
    TextView view_users_btn,change_passcode_btn,change_passcode_cancel_btn,change_passcode_submit_btn,change_new_passcode_cancel_btn;
    Dialog change_passcode_alert_dialog,new_passcode_alert_dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VendorProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorProfile newInstance(String param1, String param2) {
        VendorProfile fragment = new VendorProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_vendor_profile, container, false);
        view_users_btn=(TextView)view.findViewById(R.id.view_users_btn);
        change_passcode_btn=(TextView)view.findViewById(R.id.change_passcode_btn);
        change_passcode_alert_dialog = new Dialog(getActivity());
        change_passcode_alert_dialog.setContentView(R.layout.alert_vendor_change_passcode);
        change_passcode_cancel_btn = (TextView) change_passcode_alert_dialog.findViewById(R.id.change_passcode_cancel_btn);
        change_passcode_submit_btn = (TextView) change_passcode_alert_dialog.findViewById(R.id.change_passcode_submit_btn);
        new_passcode_alert_dialog = new Dialog(getActivity());
        new_passcode_alert_dialog.setContentView(R.layout.alert_vendor_new_passcode);
        change_new_passcode_cancel_btn = (TextView) new_passcode_alert_dialog.findViewById(R.id.change_new_passcode_cancel_btn);

        onclicklistners();
        return view;
    }

    private void onclicklistners() {
        view_users_btn_listner();
        change_passcode_btn_listner();
        change_passcode_cancel_btn_listner();
        change_passcode_submit_btn_listner();
        change_new_passcode_cancel_btn_listner();
    }

    private void change_new_passcode_cancel_btn_listner() {
        change_new_passcode_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_passcode_alert_dialog.dismiss();
            }
        });
    }

    private void change_passcode_btn_listner() {
        change_passcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passcode_alert_dialog.show();
            }
        });
    }

    private void change_passcode_cancel_btn_listner() {
        change_passcode_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passcode_alert_dialog.dismiss();
            }
        });
    }

    private void change_passcode_submit_btn_listner() {
        change_passcode_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Yes working", Toast.LENGTH_SHORT).show();
                new_passcode_alert_dialog.show();
                change_passcode_alert_dialog.dismiss();
            }
        });
    }

    private void view_users_btn_listner() {
        view_users_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Vendor_Users_list.class);
                startActivity(intent);
            }
        });
    }

}