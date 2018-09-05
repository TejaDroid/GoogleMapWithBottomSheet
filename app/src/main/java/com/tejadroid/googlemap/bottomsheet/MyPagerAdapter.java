package com.tejadroid.googlemap.bottomsheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter
//        extends FragmentStatePagerAdapter
{
    private Context context;
    private ArrayList<LatLng> arrayList;

    public MyPagerAdapter(Context context, ArrayList<LatLng> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        Log.d("MyArrayList", String.valueOf(arrayList));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_view_pager, container, false);
        container.addView(layout);
        return layout;

    }
}
