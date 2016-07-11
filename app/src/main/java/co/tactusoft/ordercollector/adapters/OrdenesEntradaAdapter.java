package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class OrdenesEntradaAdapter extends ArrayAdapter<OrdenesEntradas> {
    private Context mContext;
    private final LayoutInflater mInflater;

    public OrdenesEntradaAdapter(Context context, List<OrdenesEntradas> objects) {
        super(context, R.layout.row_ordenes_entrada, objects);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        OrdenesEntradas item = getItem(position);
        return (Singleton.getInstance().getOrdenesEntradas() !=null &&
                Singleton.getInstance().getOrdenesEntradas().getId() !=null &&
                item.getId().intValue() == Singleton.getInstance().getOrdenesEntradas().getId().intValue())
                ? Constants.TYPE_ITEM_COLORED : Constants.TYPE_ITEM_NORMAL;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_ordenes_entrada, parent, false);
            holder = new ViewHolder();
            holder.frlItem  = (LinearLayout) convertView.findViewById(R.id.frl_item_oe);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_oe);
            holder.textView2 = (TextView) convertView.findViewById(R.id.text2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.text3);
            holder.textView4 = (TextView) convertView.findViewById(R.id.text4);
            holder.textView5 = (TextView) convertView.findViewById(R.id.text5);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrdenesEntradas item = getItem(position);
        if (item != null) {
            holder.imageView.setImageResource(R.drawable.ic_oe_1);
            holder.textView2.setText(String.format(mContext.getResources().getString(R.string.oe_estado_par),
                    item.getEstadoOrden()));
            holder.textView3.setText(String.format(mContext.getResources().getString(R.string.oe_cliente_par),
                    item.getNumeroDocumentoOrdenCliente()));
            holder.textView4.setText(String.format(mContext.getResources().getString(R.string.oe_hora_llegada_par),
                    item.getFechaPlaneadaEntregaMaxima().replace("00:00", item.getHoraPlaneadaEntregaMaxima())));
            holder.textView5.setText(String.format(mContext.getResources().getString(R.string.oe_cliente_par),
                    item.getClienteCodigo()));
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
        private LinearLayout frlItem;
        private ImageView imageView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;

        public ViewHolder() {

        }
    }
}
