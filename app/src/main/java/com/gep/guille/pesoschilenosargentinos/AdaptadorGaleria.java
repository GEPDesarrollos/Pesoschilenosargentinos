package com.gep.guille.pesoschilenosargentinos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class AdaptadorGaleria extends BaseAdapter {

    private Context context;
    public static Integer[] mImagesIds = {
            R.drawable.i1, R.drawable.i2,
            R.drawable.i3, R.drawable.i4,
            R.drawable.i5, R.drawable.i6,
            R.drawable.i7, R.drawable.i8,
            R.drawable.i9, R.drawable.i10,
            R.drawable.i11 };
    public AdaptadorGaleria(Context c){
        context=c;
    }

    @Override
    public int getCount() {
        return mImagesIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mImagesIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView=new ImageView(context);
        imageView.setImageResource(mImagesIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(240,240));
        return imageView;
    }
}
