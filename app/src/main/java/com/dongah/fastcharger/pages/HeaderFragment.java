package com.dongah.fastcharger.pages;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.R;
import com.dongah.fastcharger.basefunction.ChargerConfiguration;
import com.dongah.fastcharger.basefunction.UiSeq;
import com.dongah.fastcharger.utils.SharedModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment implements View.OnClickListener {

    static final Logger logger = LoggerFactory.getLogger(HeaderFragment.class);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CHANNEL = "CHANNEL";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mChannel;

    int clickedCnt = 0;
    ImageButton btnLogo;
    ImageView btnHome;
    TextView textViewChargerId;
    ChargerConfiguration chargerConfiguration;
    SharedModel sharedModel;

    public HeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeaderFragment newInstance(String param1, String param2) {
        HeaderFragment fragment = new HeaderFragment();
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
            mChannel = getArguments().getInt(CHANNEL);
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        btnHome = view.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
        btnLogo = view.findViewById(R.id.btnLogo);
        btnLogo.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickedCnt >= 8) {
                        boolean result;
                        UiSeq uiSeq = ((MainActivity) MainActivity.mContext).getClassUiProcess(0).getUiSeq();
                        result = Objects.equals(uiSeq, UiSeq.INIT) || Objects.equals(uiSeq, UiSeq.FAULT);
                        if (!result) {
                            return false;
                        } else {
                            clickedCnt = 0;
                            ((MainActivity) MainActivity.mContext).getClassUiProcess(0).setUiSeq(UiSeq.ADMIN_PASS);
                            ((MainActivity) MainActivity.mContext).getFragmentChange().onFragmentChange(0, UiSeq.ADMIN_PASS,"ADMIN_PASS",null);
                        }
                        return true;
                    }
                    clickedCnt++;
                }
                return false;
            }
        });
        textViewChargerId = view.findViewById(R.id.textViewChargerId);
        chargerConfiguration = ((MainActivity) MainActivity.mContext).getChargerConfiguration();
        textViewChargerId.setText("ID-" + chargerConfiguration.getChargerId());
        return view;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);
            sharedModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<String[]>() {
                @Override
                public void onChanged(String[] strings) {
                    UiSeq uiSeq = ((MainActivity) MainActivity.mContext).getClassUiProcess(mChannel).getUiSeq();
                    switch (uiSeq) {
                        case MEMBER_CARD:
                        case MEMBER_CARD_WAIT:
                        case CREDIT_CARD_WAIT:
                        case CHARGING:
                        case CONNECT_CHECK:
                        case PLUG_CHECK:
                        case FAULT:
                        case CONFIG_SETTING:
                        case REBOOTING:
                            btnHome.setVisibility(View.INVISIBLE);
                            break;
                        default:
                            btnHome.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            logger.error("HeaderFragment onViewCreated : {}", e.getMessage());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(@NonNull View v) {
        int getId = v.getId();
        if (Objects.equals(getId, R.id.btnHome)) {
            ((MainActivity) getActivity()).getClassUiProcess(mChannel).onHome();
        }
    }
}