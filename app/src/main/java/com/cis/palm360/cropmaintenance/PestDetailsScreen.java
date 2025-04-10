package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cis.palm360.R;
import com.cis.palm360.ui.BaseFragment;

public class PestDetailsScreen extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View rootView;

    @Override
    public void Initialize() {
        mContext = getActivity();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        rootView = inflater.inflate(R.layout.pest_details_screen, null);

        baseLayout.addView(rootView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTile(getString(R.string.pest_details));
    }

    @Override
    public void onClick(View v) {

    }
}

