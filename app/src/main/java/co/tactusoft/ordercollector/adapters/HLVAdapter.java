package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.util.Utils;

/**
 * Created by csarmiento
 * 4/07/16
 * csarmiento@gentemovil.co
 */
public class HLVAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;

    ArrayList<String> alName;
    ArrayList<String> alImage;

    public HLVAdapter(Context context, ArrayList<String> alName, ArrayList<String> alImage) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
    }

    @Override
    public int getCount() {
        return alName.size();
    }

    @Override
    public Object getItem(int position) {
        return alName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.hlv_inflate, parent, false);
            holder = new ViewHolder();
            holder.imgThumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
            holder.labelImageText = (TextView) view.findViewById(R.id.tv_species);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.labelImageText.setText(alName.get(position));
        holder.imgThumbnail.setImageBitmap(Utils.loadImageBitmap(this.context, alImage.get(position)));
        return view;
    }

    private class ViewHolder {
        public ImageView imgThumbnail;
        public TextView labelImageText;
    }
}
