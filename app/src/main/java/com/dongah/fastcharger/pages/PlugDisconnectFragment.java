package com.dongah.fastcharger.pages;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlugDisconnectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlugDisconnectFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(PlugDisconnectFragment.class);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CHANNEL = "CHANNEL";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mChannel;

    ImageView imageViewDisconnect;
    AnimationDrawable animationDrawable;
    Handler handler;


    public PlugDisconnectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlugDisconnectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlugDisconnectFragment newInstance(String param1, String param2) {
        PlugDisconnectFragment fragment = new PlugDisconnectFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plug_disconnect, container, false);
        imageViewDisconnect = view.findViewById(R.id.ImageViewDisconnect);
        imageViewDisconnect.setBackgroundResource(R.drawable.ani_disconnect);
        animationDrawable = (AnimationDrawable) imageViewDisconnect.getBackground();
        animationDrawable.start();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) MainActivity.mContext).getClassUiProcess(mChannel).onHome();
                }
            }, 15000);
        } catch (Exception e) {
            logger.error("PlugDisconnectFragment onViewCreated : {}", e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        try {
            if (animationDrawable != null) {
                animationDrawable.stop();
            }

            if (imageViewDisconnect != null) {
                Drawable bg = imageViewDisconnect.getBackground();
                if (bg instanceof AnimationDrawable) {
                    ((AnimationDrawable) bg).stop();
                }
                imageViewDisconnect.setBackground(null);
            }
        } catch (Exception e) {
            logger.error("PlugDisconnectFragment onDestroyView : {}", e.getMessage());
        }
        super.onDestroyView();
    }
}