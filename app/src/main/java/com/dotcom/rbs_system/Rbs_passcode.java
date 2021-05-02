package com.dotcom.rbs_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Rbs_passcode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rbs_passcode extends Fragment {

    Button enter_button;
    View view;
    EditText passcode_editText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Rbs_passcode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rbs_passcode.
     */
    // TODO: Rename and change types and number of parameters
    public static Rbs_passcode newInstance(String param1, String param2) {
        Rbs_passcode fragment = new Rbs_passcode();
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
        view = inflater.inflate(R.layout.fragment_rbs_passcode, container, false);

        Initialize();
        OnClicks();

        return  view;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialize() {
        passcode_editText = (EditText) view.findViewById(R.id.passcode_editText);

        enter_button = (Button)view.findViewById(R.id.enter_button);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void OnClicks() {
        enterButtonClick();
    }

    private void enterButtonClick() {
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passcode_editText.getText().toString().equals("1234")){
                    Intent intent = new Intent(getActivity(), Rbs_options.class);
                    startActivity(intent);
                }else {
                    passcode_editText.setError("Wrong passcode!");
                }

            }
        });
    }
}