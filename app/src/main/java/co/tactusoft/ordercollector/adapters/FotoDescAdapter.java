package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.FotoDesc;

/**
 * Created by csarmiento
 * 13/06/16
 * csarmiento@gentemovil.co
 */
public class FotoDescAdapter extends BaseAdapter {

    private Context context;
    private List<FotoDesc> list;

    public FotoDescAdapter(Context context, List<FotoDesc> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public FotoDesc getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_foto_desc, viewGroup, false);
        }

        ImageView imgPic = (ImageView) view.findViewById(R.id.img_pic);
        TextView labelPic = (TextView) view.findViewById(R.id.label_pic);

        final FotoDesc item = getItem(position);
        imgPic.setImageBitmap(item.getBitmap());
        labelPic.setText(item.getDescripcion());

        return view;
    }

}
