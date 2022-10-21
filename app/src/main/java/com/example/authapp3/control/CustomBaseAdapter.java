package com.example.authapp3.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.authapp3.R;

import org.w3c.dom.Text;

import java.util.List;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    List<String> evlist1;
    List<String> evlist2;
    List<String> evlist3;
    List<Integer> evimages;
/*    String evList[];
    String evList1[];
    String evList2[];*/
/*    int evImages[];*/
    LayoutInflater inflater;
    //public CustomBaseAdapter(Context ctx, String [] evList,String [] evList1,String [] evList2, int [] evImages){
    public CustomBaseAdapter(Context ctx, List<String> evlist1,List<String> evlist2,List<String> evlist3,List<Integer> evimages){
        this.context = ctx;
        this.evlist1=evlist1;
        this.evlist2=evlist2;
        this.evlist3=evlist3;
/*        this.evList = evList;
        this.evList1 = evList1;
        this.evList2= evList2;*/
        this.evimages = evimages;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return evlist1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_custom_list_view_row,null);
        ImageView evimage = (ImageView) convertView.findViewById(R.id.imageevicon);
        TextView txtView1 = (TextView) convertView.findViewById(R.id.listtext1);
        TextView txtView2 = (TextView) convertView.findViewById(R.id.listtext2);
        TextView txtView3 = (TextView) convertView.findViewById(R.id.listtext3);
        txtView1.setText(evlist1.get(position));
        txtView2.setText(evlist2.get(position));
        txtView3.setText(evlist3.get(position));
        evimage.setImageResource(evimages.get(position));
        return convertView;
    }
}
