package com.dongah.fastcharger.basefunction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.R;
import com.dongah.fastcharger.pages.AdminPasswordFragment;
import com.dongah.fastcharger.pages.AuthSelectFragment;
import com.dongah.fastcharger.pages.ChargingFinishFragment;
import com.dongah.fastcharger.pages.ChargingFragment;
import com.dongah.fastcharger.pages.ConfigSettingFragment;
import com.dongah.fastcharger.pages.ControlDebugFragment;
import com.dongah.fastcharger.pages.CreditCardFragment;
import com.dongah.fastcharger.pages.CreditCardWaitFragment;
import com.dongah.fastcharger.pages.EnvironmentFragment;
import com.dongah.fastcharger.pages.FaultFragment;
import com.dongah.fastcharger.pages.HeaderFragment;
import com.dongah.fastcharger.pages.InitFragment;
import com.dongah.fastcharger.pages.MemberCardFragment;
import com.dongah.fastcharger.pages.MemberCardWaitFragment;
import com.dongah.fastcharger.pages.MessageYesNoFragment;
import com.dongah.fastcharger.pages.PlugDisconnectFragment;
import com.dongah.fastcharger.pages.PlugWaitFragment;
import com.dongah.fastcharger.pages.QrFragment;
import com.dongah.fastcharger.pages.SmsFragment;
import com.dongah.fastcharger.pages.WebSocketDebugFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FragmentChange {

    public static final Logger logger = LoggerFactory.getLogger(FragmentChange.class);

    public FragmentChange() {
    }

    public void onFragmentChange(int channel, UiSeq uiSeq, String sendText, String type) {
        Bundle bundle = new Bundle();
        bundle.putInt("CHANNEL", channel);
        ((MainActivity) MainActivity.mContext).setFragmentSeq(channel, uiSeq);
        int frameLayoutId = channel == 0 ? R.id.body : R.id.body;
        FragmentTransaction transaction = ((MainActivity) MainActivity.mContext).getSupportFragmentManager().beginTransaction();
//        controlBtnHome(uiSeq, channel);
//        controlBtnHome(uiSeq);
        onFrameLayoutChange(uiSeq);
        switch (uiSeq) {
            case INIT:
                try {
//                    onFrameLayoutChange(true);
                    InitFragment initFragment = new InitFragment();
                    transaction.replace(frameLayoutId, initFragment, sendText);
                    initFragment.setArguments(bundle);
                    transaction.commit();
//                    controlBtnHome(uiSeq);
                } catch (Exception e) {
                    logger.error("onFragmentChange error : INIT {}", e.getMessage());
                }
                break;
            case AUTH_SELECT:
                try {
                    AuthSelectFragment authSelectFragment = new AuthSelectFragment();
                    transaction.replace(frameLayoutId, authSelectFragment, "AUTH_SELECT");
                    authSelectFragment.setArguments(bundle);
                    transaction.commit();
//                    controlBtnHome(uiSeq);
                } catch (Exception e) {
                    logger.error("onFragmentChange error : AUTH_SELECT {}", e.getMessage());
                }
                break;
            case MEMBER_CARD:
                try {
                    MemberCardFragment memberCardFragment = new MemberCardFragment();
                    transaction.replace(frameLayoutId, memberCardFragment, "MEMBER_CARD");
                    memberCardFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : MEMBER_CARD {} ", e.getMessage());
                }
                break;
            case MEMBER_CARD_WAIT:
                try {
                    MemberCardWaitFragment memberCardWaitFragment = new MemberCardWaitFragment();
                    transaction.replace(frameLayoutId, memberCardWaitFragment, "MEMBER_CARD_WAIT");
                    memberCardWaitFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : MEMBER_CARD_WAIT {}", e.getMessage());
                }
                break;
            case CREDIT_CARD:
                try {
                    CreditCardFragment creditCardFragment = new CreditCardFragment();
                    transaction.replace(frameLayoutId, creditCardFragment, "CREDIT_CARD");
                    creditCardFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : CREDIT_CARD {} ", e.getMessage());
                }
                break;
            case CREDIT_CARD_WAIT:
                try {
                    CreditCardWaitFragment creditCardWaitFragment = new CreditCardWaitFragment();
                    transaction.replace(frameLayoutId, creditCardWaitFragment, "CREDIT_CARD_WAIT");
                    creditCardWaitFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : CREDIT_CARD_WAIT {}", e.getMessage());
                }
                break;
            case PLUG_CHECK:
            case CONNECT_CHECK:
                try {
                    PlugWaitFragment plugWaitFragment = new PlugWaitFragment();
                    transaction.replace(frameLayoutId, plugWaitFragment, "PLUG_CHECK");
                    plugWaitFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : PLUG_CHECK {}", e.getMessage());
                }
                break;
            case PLUG_DISCONNECT:
                try {
                    PlugDisconnectFragment plugDisconnectFragment = new PlugDisconnectFragment();
                    transaction.replace(frameLayoutId, plugDisconnectFragment, "PLUG_DISCONNECT");
                    plugDisconnectFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : PLUG_DISCONNECT {}", e.getMessage());
                }
                break;
            case CHARGING:
                try {
                    ChargingFragment chargingFragment = new ChargingFragment();
                    transaction.replace(frameLayoutId, chargingFragment, "CHARGING");
                    chargingFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : CHARGING {} ", e.getMessage());
                }
                break;
            case CHARGING_STOP_MESSAGE:
                try {
                    MessageYesNoFragment messageYesNoFragment = new MessageYesNoFragment();
                    transaction.replace(frameLayoutId, messageYesNoFragment, "CHARGING_STOP_MESSAGE");
                    messageYesNoFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : CHARGING_STOP_MESSAGE {} ", e.getMessage());
                }
                break;
            case FINISH:
                try {
                    ChargingFinishFragment chargingFinishFragment = new ChargingFinishFragment();
                    transaction.replace(frameLayoutId, chargingFinishFragment, "FINISH");
                    chargingFinishFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : FINISH {}", e.getMessage());
                }
                break;
            case SMS:
                try {
                    SmsFragment smsFragment = new SmsFragment();
                    transaction.replace(frameLayoutId, smsFragment, "SMS");
                    smsFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : SMS {}", e.getMessage());
                }
                break;
            case QR_CODE:
                try {
                    QrFragment qrFragment = new QrFragment();
                    transaction.replace(frameLayoutId, qrFragment, "QR_CODE");
                    qrFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : QR_CODE {}", e.getMessage());
                }
                break;
            case FAULT:
                try {
                    FaultFragment faultFragment = new FaultFragment();
                    transaction.replace(frameLayoutId, faultFragment, "FAULT");
                    bundle.putString("param2", "FAULT_MESSAGE");
                    faultFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : FAULT {}", e.getMessage());
                }
                break;
            case REBOOTING:
                try {
                    FaultFragment faultFragment = new FaultFragment();
                    transaction.replace(frameLayoutId, faultFragment, "REBOOTING");
                    bundle.putString("param2", "REBOOTING");
                    bundle.putString("param3", type);
                    faultFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : REBOOTING {}", e.getMessage());
                }
                break;
            case ADMIN_PASS:
                try {
//                    onFrameLayoutChange(false);
                    AdminPasswordFragment adminPasswordFragment = new AdminPasswordFragment();
                    transaction.replace(frameLayoutId, adminPasswordFragment, "ADMIN");
                    adminPasswordFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : ADMIN_PASS {}", e.getMessage());
                }
                break;
            case ENVIRONMENT:
                try {
                    EnvironmentFragment environmentFragment = new EnvironmentFragment();
                    transaction.replace(frameLayoutId, environmentFragment, "ENVIRONMENT");
                    environmentFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : ENVIRONMENT {}", e.getMessage());
                }
                break;
            case CONFIG_SETTING:
                try {
                    ConfigSettingFragment configSettingFragment = new ConfigSettingFragment();
                    transaction.replace(frameLayoutId, configSettingFragment, "configSetting");
                    configSettingFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : configSetting {}", e.getMessage());
                }
                break;
            case WEB_SOCKET:
                try {
                    WebSocketDebugFragment webSocketDebugFragment = new WebSocketDebugFragment();
                    transaction.replace(frameLayoutId, webSocketDebugFragment, "WEBSOCKET");
                    webSocketDebugFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : webSocketDebugFragment {}", e.getMessage());
                }
                break;
            case CONTROL_BOARD_DEBUGGING:
                try {
                    ControlDebugFragment controlDebugFragment = new ControlDebugFragment();
                    transaction.replace(frameLayoutId, controlDebugFragment, "CONTROL");
                    controlDebugFragment.setArguments(bundle);
                    transaction.commit();
                } catch (Exception e) {
                    logger.error("onFragmentChange error : controlDebugFragment {}", e.getMessage());
                }
                break;

        }
    }

    public void onFrameLayoutChange(UiSeq uiSeq) {
        //main activity layout fullScreen change
        try {
    //            FrameLayout frameBody = ((MainActivity) MainActivity.mContext).findViewById(R.id.body);
            FrameLayout frameHeader = ((MainActivity) MainActivity.mContext).findViewById(R.id.header);
            FrameLayout frameFooter = ((MainActivity) MainActivity.mContext).findViewById(R.id.frameFooter);

            switch (uiSeq) {
                case ADMIN_PASS:
                case ENVIRONMENT:
                    case CONFIG_SETTING:
                case WEB_SOCKET:
                case CONTROL_BOARD_DEBUGGING:
                    frameHeader.setVisibility(View.INVISIBLE);
                    frameFooter.setVisibility(View.INVISIBLE);
                    break;
                default:
                    frameHeader.setVisibility(View.VISIBLE);
                    frameFooter.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (Exception e) {
            logger.error("onFrameLayoutChange error : {}", e.getMessage());
        }
    }


    public void onFragmentHeaderChange(int channel, String sendText) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("CHANNEL", channel);
            int frameLayoutId = channel == 0 ? R.id.header : R.id.header;
            FragmentTransaction transaction = ((MainActivity) MainActivity.mContext).getSupportFragmentManager().beginTransaction();
            HeaderFragment headerFragment = new HeaderFragment();
            transaction.replace(frameLayoutId, headerFragment, sendText);
            headerFragment.setArguments(bundle);
            transaction.commit();
        } catch (Exception e) {
            logger.error("onFragmentHeaderChange error : {}", e.getMessage());
        }
    }

//    public void controlBtnHome(UiSeq uiSeq) {
//        ImageView btnHome = ((MainActivity) MainActivity.mContext).findViewById(R.id.btnHome);
//
//        switch (uiSeq) {
//            case INIT:
//            case MEMBER_CARD:
//            case MEMBER_CARD_WAIT:
//            case CREDIT_CARD_WAIT:
//            case CHARGING:
//            case CONNECT_CHECK:
//            case FAULT:
//            case CONFIG_SETTING:
//            case REBOOTING:
//                btnHome.setVisibility(View.INVISIBLE);
//                break;
//            default:
//                btnHome.setVisibility(View.VISIBLE);
//                break;
//        }
//    }

//    public void controlBtnHome(int channel, boolean con) {
//        UiSeq uiSeq = ((MainActivity) MainActivity.mContext).getClassUiProcess(channel).getUiSeq();
//        ImageView btnHome = ((MainActivity) MainActivity.mContext).findViewById(R.id.btnHome);
//        if (con) {
//            btnHome.setVisibility(View.INVISIBLE);
//        } else {
//            btnHome.setVisibility(View.VISIBLE);
//        }
//
//        switch (uiSeq) {
//            case MEMBER_CARD:
//            case MEMBER_CARD_WAIT:
//            case CREDIT_CARD_WAIT:
//            case CHARGING:
//            case CONNECT_CHECK:
//            case FAULT:
//            case CONFIG_SETTING:
//            case REBOOTING:
//                btnHome.setVisibility(View.INVISIBLE);
//                break;
//            default:
//                btnHome.setVisibility(View.VISIBLE);
//                break;
//        }
//    }


    public void onRemoveFragment(int channel, String tag) {
        try {
            int frameLayoutId = channel == 0 ? R.id.body : R.id.body;
            FragmentManager fragmentManager = ((MainActivity) MainActivity.mContext).getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null) fragmentManager.beginTransaction().remove(fragment).commit();
        } catch (Exception e) {
            logger.error("onRemoveFragment error : {}", e.getMessage());
        }
    }

}
