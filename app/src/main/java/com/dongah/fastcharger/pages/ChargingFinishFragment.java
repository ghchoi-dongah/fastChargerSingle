package com.dongah.fastcharger.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.R;
import com.dongah.fastcharger.TECH3800.TLS3800;
import com.dongah.fastcharger.basefunction.ChargingCurrentData;
import com.dongah.fastcharger.basefunction.ClassUiProcess;
import com.dongah.fastcharger.utils.SharedModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargingFinishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargingFinishFragment extends Fragment implements View.OnClickListener {

    private static final Logger logger = LoggerFactory.getLogger(ChargingFinishFragment.class);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CHANNEL = "CHANNEL";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mChannel;

    Button btnStopConfirm;
    TextView txtPrepayment, txtPaymentReturn, txtActualPayment, txtChargeTime, txtAmountOfCharge, txtSoc;
    CircularProgressIndicator progressCircular;

    ClassUiProcess classUiProcess;
    ChargingCurrentData chargingCurrentData;
    SharedModel sharedModel;
    String[] requestStrings = new String[1];
    DecimalFormat payFormatter = new DecimalFormat("#,###,##0");
    DecimalFormat unitPriceFormatter = new DecimalFormat("#,###,##0.0");
    DecimalFormat powerFormatter = new DecimalFormat("#,###,##0.00");
    Handler uiCheckHandler, paymentHandler;


    public ChargingFinishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChargingFinishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChargingFinishFragment newInstance(String param1, String param2) {
        ChargingFinishFragment fragment = new ChargingFinishFragment();
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charging_finish, container, false);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);
        requestStrings[0] = String.valueOf(mChannel);
        sharedModel.setMutableLiveData(requestStrings);
        classUiProcess = ((MainActivity) MainActivity.mContext).getClassUiProcess(mChannel);
        chargingCurrentData = ((MainActivity) MainActivity.mContext).getChargingCurrentData();
        txtSoc = view.findViewById(R.id.txtProgress);
        txtPrepayment = view.findViewById(R.id.txtPrepayment);
        txtPaymentReturn = view.findViewById(R.id.txtPaymentReturn);
        txtActualPayment = view.findViewById(R.id.txtActualPayment);
        txtChargeTime = view.findViewById(R.id.txtChargeTime);
        txtAmountOfCharge = view.findViewById(R.id.txtAmountOfCharge);
        btnStopConfirm = view.findViewById(R.id.btnStopConfirm);
        btnStopConfirm.setOnClickListener(this);
        progressCircular = view.findViewById(R.id.progressCircular);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            //unplug check 후 초기 화면
            progressCircular.isIndeterminate();
            uiCheckHandler = new Handler();
            uiCheckHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!((MainActivity) getActivity()).getControlBoard().getRxData(mChannel).isCsPilot()) {
                        btnStopConfirm.performClick();
                    }
                    uiCheckHandler.postDelayed(this, 60000);
                }
            }, 60000);

            //charging finish info
            ((MainActivity) MainActivity.mContext).runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    if (chargingCurrentData.getPowerMeterUsePay() < chargingCurrentData.getPrePayment()) {
                        chargingCurrentData.setPartialCancelPayment((int)(chargingCurrentData.getPrePayment() - chargingCurrentData.getPowerMeterUsePay()));
                    }
                    txtSoc.setText(((MainActivity) MainActivity.mContext).getChargingCurrentData().getSoc() + "%");
                    progressCircular.setProgress(((MainActivity) MainActivity.mContext).getChargingCurrentData().getSoc(), true);
                    txtPrepayment.setText(payFormatter.format(chargingCurrentData.getPrePayment()) + " 원");
                    txtPaymentReturn.setText(payFormatter.format(chargingCurrentData.getPartialCancelPayment()) + " 원");
                    txtActualPayment.setText(payFormatter.format(chargingCurrentData.getPowerMeterUsePay()) + " 원");
                    txtChargeTime.setText(chargingCurrentData.getChargingUseTime());
                    txtAmountOfCharge.setText(powerFormatter.format(chargingCurrentData.getPowerMeterUse() * 0.01) + " kWh" );
                }
            });

            // 신용 카드 결제
            if (chargingCurrentData.isPrePaymentResult()) onTls3800Payment();

        } catch (Exception e) {
            logger.error("onViewCreated : {}", e.getMessage());
        }
    }

    private void onTls3800Payment() {
        try {
            paymentHandler = new Handler();
            paymentHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        int gapAmt = chargingCurrentData.getPrePayment() - (int) chargingCurrentData.getPowerMeterUsePay();
                        //
                        //선 결제에 의한 무카드 취소 (4:무카드 취소)(5:부분 취소)
                        if (Objects.equals(chargingCurrentData.getPrePayment(), gapAmt)) {
                            chargingCurrentData.setPartialCancelPayment(chargingCurrentData.getPrePayment());
                            ((MainActivity) MainActivity.mContext).getTls3800().onTLS3800Request(mChannel, TLS3800.CMD_TX_PAYCANCEL, 4);
                        } else if (gapAmt > 0) {
                            // HumaxEv 부분 취소(5)는 서버에서 진행.......무카드 취소(4)는 충전기에서
                            int surTax = 0, rate = 10;
                            surTax = (gapAmt * rate) / (100 * rate);
                            chargingCurrentData.setPartialCancelPayment(gapAmt);
                            chargingCurrentData.setSurtax(surTax);
                            chargingCurrentData.setTip(0);
//                            ((MainActivity) MainActivity.mContext).getTls3800().onTLS3800Request(mChannel, TLS3800.CMD_TX_PAYCANCEL, 5);
                        }
                    } catch (Exception e) {
                        logger.error("paymentHandler error : {}", e.getMessage());
                    }
                }
            }, 500);

        } catch (Exception e) {
            logger.error("onTls3800Payment error : {}", e.getMessage());
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        int getId = v.getId();
        if (Objects.deepEquals(getId, R.id.btnStopConfirm)) {
            ((MainActivity) getActivity()).getClassUiProcess(mChannel).onHome();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (uiCheckHandler != null) {
            uiCheckHandler.removeCallbacksAndMessages(null);
            uiCheckHandler.removeMessages(0);
            uiCheckHandler = null;
        }
        requestStrings[0] = String.valueOf(mChannel);
        sharedModel.setMutableLiveData(requestStrings);
    }
}