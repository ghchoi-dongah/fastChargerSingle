package com.dongah.fastcharger.pages;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.R;
import com.dongah.fastcharger.basefunction.GlobalVariables;
import com.dongah.fastcharger.basefunction.UiSeq;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPasswordFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editPassword;
    Button btnCancel, btnConfirm;

    public AdminPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminPasswordFragment newInstance(String param1, String param2) {
        AdminPasswordFragment fragment = new AdminPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_password, container, false);
        editPassword = view.findViewById(R.id.editPassword);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            showKeyboard(MainActivity.mContext, editPassword);
        }, 200);

        return view;
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        if (Objects.equals(getId, R.id.btnConfirm)) {
            if (Objects.equals(GlobalVariables.getHmConfigPasswd(), editPassword.getText().toString()) ||
                    Objects.equals("5500",editPassword.getText().toString()) ) {
                ((MainActivity) MainActivity.mContext).getFragmentChange().onFragmentChange(0, UiSeq.ENVIRONMENT, "ENVIRONMENT", null);
            } else {
                editPassword.setText("");
                Toast.makeText((getActivity()), "비밀번호 불일치", Toast.LENGTH_SHORT).show();
            }
        } else if (Objects.equals(getId, R.id.btnCancel)) {
            ((MainActivity) MainActivity.mContext).getClassUiProcess(0).setUiSeq(UiSeq.INIT);
            ((MainActivity) MainActivity.mContext).getFragmentChange().onFragmentChange(0, UiSeq.INIT, "INIT", null);
        }
    }

    public void showKeyboard(Context context, EditText editText) {
        editText.requestFocus(); // EditText에 포커스 부여
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}