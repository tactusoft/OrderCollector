package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.fragments.FragmentOrdenesEntradaDetalle;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class OrdenesEntradaAdapter extends ArrayAdapter<OrdenesEntradas> {
    private Context mContext;
    private DataBaseHelper dataBaseHelper;
    private final LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper;

    public OrdenesEntradaAdapter(Context context, List<OrdenesEntradas> objects) {
        super(context, R.layout.row_ordenes_entrada, objects);
        mInflater = LayoutInflater.from(context);
        binderHelper = new ViewBinderHelper();
        mContext = context;
        binderHelper.setOpenOnlyOne(true);
        dataBaseHelper = new DataBaseHelper(mContext.getApplicationContext());
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
            holder.btnEditSkyline = (TextView) convertView.findViewById(R.id.btn_edit_skyline);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_oe);
            holder.textView2 = (TextView) convertView.findViewById(R.id.text2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.text3);
            holder.textView4 = (TextView) convertView.findViewById(R.id.text4);
            holder.textView5 = (TextView) convertView.findViewById(R.id.text5);
            holder.swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrdenesEntradas item = getItem(position);
        if (item != null) {
            binderHelper.bind(holder.swipeLayout, String.valueOf(item.getId()));
            holder.imageView.setImageResource(R.drawable.ic_oe_1);
            holder.textView2.setText(String.format(mContext.getResources().getString(R.string.oe_estado_par),
                    item.getEstadoOrden()));
            holder.textView3.setText(String.format(mContext.getResources().getString(R.string.oe_cliente_par),
                    item.getNumeroDocumentoOrdenCliente()));
            holder.textView4.setText(String.format(mContext.getResources().getString(R.string.oe_hora_llegada_par),
                    item.getFechaPlaneadaEntregaMaxima().replace("00:00",item.getHoraPlaneadaEntregaMaxima())));
            holder.textView5.setText(String.format(mContext.getResources().getString(R.string.oe_cliente_par),
                    item.getClienteCodigo()));
            holder.btnEditSkyline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrdenesEntradas selected = getItem(position);
                    int selectedId =  selected.getId();
                    dataBaseHelper.getListOrdenesEntradas();
                    OrdenesEntradas ordenesEntradasTemp = dataBaseHelper.getOrdenesEntradas(selectedId);
                    if (ordenesEntradasTemp != null) {
                        selected = ordenesEntradasTemp;
                    }
                    selected.setBloqueado(true);
                    dataBaseHelper.insertOrdenesEntradas(selected);
                    Singleton.getInstance().setOrdenesEntradas(selected);
                    binderHelper.closeLayout(String.valueOf(selectedId));
                    notifyDataSetChanged();
                    FragmentTransaction ft = ((MainActivity)mContext).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new FragmentOrdenesEntradaDetalle());
                    ft.commit();
                }
            });
        }

        switch (getItemViewType(position)) {
            case Constants.TYPE_ITEM_COLORED:
                holder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                holder.btnEditSkyline.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case Constants.TYPE_ITEM_NORMAL:
                holder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.btnEditSkyline.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                break;
        }

        return convertView;
    }

    private class ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private LinearLayout frlItem;
        private TextView btnEditSkyline;
        private ImageView imageView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;

        public ViewHolder() {

        }
    }
}
