package com.dongah.fastcharger.pages;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.R;
import com.dongah.fastcharger.basefunction.ChargingCurrentData;
import com.dongah.fastcharger.basefunction.GlobalVariables;
import com.dongah.fastcharger.basefunction.PaymentType;
import com.dongah.fastcharger.basefunction.TariffFileUpdater;
import com.dongah.fastcharger.basefunction.UiSeq;
import com.dongah.fastcharger.utils.SharedModel;
import com.dongah.fastcharger.websocket.socket.SocketReceiveMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthSelectFragment extends Fragment implements View.OnClickListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthSelectFragment.class);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CHANNEL = "CHANNEL";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mChannel;

    View viewMember, viewNoMember;
    TextView textViewMemberUnitInput, textViewNoMemberUnitInput;
    ChargingCurrentData chargingCurrentData;
    Handler uiCheckHandler;
    SocketReceiveMessage socketReceiveMessage;
    double aUnitPrice, bUnitPrice;

    SharedModel sharedModel;


    public AuthSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthSelectFragment newInstance(String param1, String param2) {
        AuthSelectFragment fragment = new AuthSelectFragment();
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_select, container, false);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);
        chargingCurrentData = ((MainActivity) getActivity()).getChargingCurrentData();

        socketReceiveMessage = ((MainActivity) MainActivity.mContext).getSocketReceiveMessage();
        textViewMemberUnitInput = view.findViewById(R.id.textViewMemberUnitInput);
        textViewNoMemberUnitInput = view.findViewById(R.id.textViewNoMemberUnitInput);

        viewMember = view.findViewById(R.id.viewMember);
        viewMember.setOnClickListener(this);
        viewNoMember = view.findViewById(R.id.viewNoMember);
        viewNoMember.setOnClickListener(this);

        //사용 단가 갖고 오기
        try {
            TariffFileUpdater tariffFileUpdater = new TariffFileUpdater();
            String memberPrice = tariffFileUpdater.getPrice("A").replace(".0","");
            String nonPrice = tariffFileUpdater.getPrice("B").replace(".0","");
            textViewMemberUnitInput.setText(getString(R.string.chargeUnitFormat, String.valueOf(memberPrice)));
            textViewNoMemberUnitInput.setText(getString(R.string.chargeUnitFormat, String.valueOf(nonPrice)));
            aUnitPrice = Double.parseDouble(memberPrice);
            bUnitPrice = Double.parseDouble(nonPrice);
        } catch (Exception e) {
            logger.error(" price error : {}", e.getMessage());
        }

        return view;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            uiCheckHandler = new Handler();
            uiCheckHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) MainActivity.mContext).getClassUiProcess(mChannel).onHome();
                }
            }, 60000);
        } catch (Exception e) {
            logger.error("AuthSelectFragment onViewCreated error : {}", e.getMessage());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        try {
            int getId = v.getId();
            if (Objects.equals(getId, R.id.viewMember)) {
                chargingCurrentData.setPaymentType(PaymentType.MEMBER);
                chargingCurrentData.setPowerUnitPrice(aUnitPrice);
                ((MainActivity) MainActivity.mContext).getClassUiProcess(mChannel).setUiSeq(UiSeq.MEMBER_CARD);
                ((MainActivity) MainActivity.mContext).getFragmentChange().onFragmentChange(mChannel,UiSeq.MEMBER_CARD, "MEMBER_CARD", null);
            } else if (Objects.equals(getId, R.id.viewNoMember)) {
                GlobalVariables.setHumaxUserType("B");
                chargingCurrentData.setPaymentType(PaymentType.CREDIT);
                chargingCurrentData.setPowerUnitPrice(bUnitPrice);
                ((MainActivity) MainActivity.mContext).getClassUiProcess(mChannel).setUiSeq(UiSeq.CREDIT_CARD);
                ((MainActivity) MainActivity.mContext).getFragmentChange().onFragmentChange(mChannel,UiSeq.CREDIT_CARD, "CREDIT_CARD", null);
            }
        } catch (Exception e) {
            logger.error("AuthSelectFragment onClick error : {}", e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            if (uiCheckHandler != null) {
                uiCheckHandler.removeCallbacksAndMessages(null);
                uiCheckHandler.removeMessages(0);
                uiCheckHandler = null;
            }

            // back image
            String[] requestStrings = new String[1];
            sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);
            requestStrings[0] = String.valueOf(mChannel);
            sharedModel.setMutableLiveData(requestStrings);
        } catch (Exception e) {
            logger.error(" Auth Select onDetach error : {}", e.getMessage());
        }
    }

}