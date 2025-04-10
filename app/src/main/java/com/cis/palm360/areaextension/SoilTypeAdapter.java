package com.cis.palm360.areaextension;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.dbmodels.PlotIrrigationTypeXref;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by latitude on 04-05-2017.
 */

//Soil/Power details display adapter
public class SoilTypeAdapter extends RecyclerView.Adapter<SoilTypeAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<PlotIrrigationTypeXref> msoilTypeIrrigationModelList;
    private OnCartChangedListener onCartChangedListener;
    LinkedHashMap<String, String> irrigationMap;
    private DataAccessHandler dataAccessHandler;


    public SoilTypeAdapter(Context mContext, ArrayList<PlotIrrigationTypeXref> irrigationModelList, LinkedHashMap<String, String> irrigationMap) {
        this.mContext = mContext;
        this.msoilTypeIrrigationModelList = irrigationModelList;
        this.irrigationMap = irrigationMap;
    }


    @Override
    public SoilTypeAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View bookingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_adapter, null);
        SoilTypeAdapter.MyHolder myHolder = new SoilTypeAdapter.MyHolder(bookingView);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(SoilTypeAdapter.MyHolder holder, final int position) {
        dataAccessHandler = new DataAccessHandler(mContext);
        LinkedHashMap<String, String> typeofirrigationMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("36"));
        holder.typeOfIrrigation.setText(msoilTypeIrrigationModelList.get(position).getName());
        holder.irrigationRecommendation.setText(typeofirrigationMap.get(""+msoilTypeIrrigationModelList.get(position).getRecmIrrgId()));
        Log.v("@@@id",""+msoilTypeIrrigationModelList.get(position).getRecmIrrgId());
        Log.v("@@@nameid",""+typeofirrigationMap.get(""+msoilTypeIrrigationModelList.get(position).getRecmIrrgId()));

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartChangedListener.setCartClickListener("delete",position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return msoilTypeIrrigationModelList != null ? msoilTypeIrrigationModelList.size() : 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView typeOfIrrigation, irrigationRecommendation;
        private ImageView deleteIcon;
        private View convertView;

        public MyHolder(View view) {
            super(view);
            convertView = view;
            typeOfIrrigation = (TextView) itemView.findViewById(R.id.typeOfIrigationCount);
            irrigationRecommendation = (TextView) itemView.findViewById(R.id.typeOfIrigationResult);
            deleteIcon = (ImageView) itemView.findViewById(R.id.trashIcon);


        }
    }

    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }

    public interface OnCartChangedListener {
        void setCartClickListener(String status,int position);
    }
}
