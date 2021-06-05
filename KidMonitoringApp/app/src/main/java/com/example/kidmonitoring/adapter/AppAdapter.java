package com.example.kidmonitoring.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.model.Application;

import java.util.List;

public class AppAdapter extends BaseAdapter {


    private Context context;
    private int layout;
    private List<Application> appList;

    public AppAdapter(Context context, int layout, List<Application> appList) {
        this.context = context;
        this.layout = layout;
        this.appList = appList;
    }
    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgView, imgLock;
        TextView txtTen, txtMoTa;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=new ViewHolder();
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder = new ViewHolder();
            holder.txtTen = (TextView) convertView.findViewById(R.id.textViewTen);
            holder.txtMoTa = (TextView) convertView.findViewById(R.id.textViewMoTa);
            holder.imgView = (ImageView) convertView.findViewById(R.id.imageViewHinh);
            holder.imgLock = (ImageView) convertView.findViewById(R.id.iv_lock) ;
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }


        Application application = appList.get(position);
        holder.txtTen.setText(application.getmName());
        holder.txtMoTa.setText(application.getmPackage());
        byte[] b = application.getmIcon();
        boolean check=application.isChecked();
        if(check==true)
        {
            holder.imgLock.setImageResource(R.drawable.ic_baseline_lock_24);
        }
        else{
            holder.imgLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
        }
        if(b!=null){

            Drawable image = new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));
            holder.imgView.setImageDrawable(image);
        }
        return convertView;
    }
}