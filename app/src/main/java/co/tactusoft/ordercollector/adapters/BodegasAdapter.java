package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.Bodegas;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class BodegasAdapter extends ArrayAdapter<Bodegas> {
    private Context mContext;
    private LayoutInflater mInflater;

    public BodegasAdapter(Context context, List<Bodegas> objects) {
        super(context, R.layout.fragment_bodegas, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        Bodegas item = getItem(position);
        return (Singleton.getInstance().getUsuario().getBodegaId() !=null &&
                item.getBodegaId().intValue() == Singleton.getInstance().getUsuario().getBodegaId().intValue())
                ? Constants.TYPE_ITEM_COLORED : Constants.TYPE_ITEM_NORMAL;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_bodegas, parent, false);
            holder = new ViewHolder();
            holder.frlItem  = (FrameLayout) convertView.findViewById(R.id.frl_item);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.textView2 = (TextView) convertView.findViewById(R.id.text2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.text3);
            holder.textView4 = (TextView) convertView.findViewById(R.id.text4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Bodegas item = getItem(position);
        if (item != null) {
            holder.textView.setText(item.getBodegaCodigo());
            holder.textView2.setText(item.getBodegaNombre());
            holder.textView3.setText(item.getBodegaCiudadNombre() );
            holder.textView4.setText(item.getBodegaDireccion());
        }

        switch (getItemViewType(position)) {
            case Constants.TYPE_ITEM_COLORED:
                holder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                break;
            case Constants.TYPE_ITEM_NORMAL:
                holder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
        }

        return convertView;
    }

    private class ViewHolder {
        private FrameLayout frlItem;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;

        public ViewHolder() {
        }
    }
}
