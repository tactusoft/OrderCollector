package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
 * Created by csarmiento on 27/05/16.
 */
public class OrdenEntradaAdapter extends ArrayAdapter<OrdenesEntradas> {
    private Context mContext;;
    private ViewHolder lastViewHolder;
    private DataBaseHelper dataBaseHelper;
    private final LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper;

    private static final int TYPE_ITEM_COLORED = 1;
    private static final int TYPE_ITEM_NORMAL = 0;

    public OrdenEntradaAdapter(Context context, List<OrdenesEntradas> objects) {
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
        return (Constants.TIPOS_ORDENES.ENTRADA.name().equals(Singleton.getInstance().getUsuario().getTipoOrden()) &&
                item.getId().intValue() == Singleton.getInstance().getUsuario().getOrdenId().intValue())
                ? TYPE_ITEM_COLORED : TYPE_ITEM_NORMAL;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_ordenes_entrada, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.frlItem  = (LinearLayout) convertView.findViewById(R.id.frl_item_oe);
            holder.btnEditSkyline = (TextView) convertView.findViewById(R.id.btn_edit_skyline);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
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
            holder.textView.setText(String.valueOf(item.getId()));
            holder.textView2.setText("Estado: " + item.getEstadoOrden());
            holder.textView3.setText("Nro. Orden: " + item.getNumeroDocumentoOrdenCliente());
            holder.textView4.setText("Hora de LLegada: "
                    + item.getFechaPlaneadaEntregaMaxima().replace("00:00",item.getHoraPlaneadaEntregaMaxima()));
            holder.textView5.setText("Cliente: " + item.getClienteCodigo());
            holder.btnEditSkyline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrdenesEntradas selected = getItem(position);
                    int selectedId =  selected.getId();
                    Singleton.getInstance().getUsuario().setOrdenId(selectedId);
                    Singleton.getInstance().getUsuario().setTipoOrden(Constants.TIPOS_ORDENES.ENTRADA.name());
                    Singleton.getInstance().setOrdenesEntradas(selected);
                    dataBaseHelper.insertUsuario(Singleton.getInstance().getUsuario());
                    binderHelper.closeLayout(String.valueOf(selectedId));
                    notifyDataSetChanged();
                    FragmentTransaction ft = ((MainActivity)mContext).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new FragmentOrdenesEntradaDetalle());
                    ft.commit();
                }
            });
        }

        switch (getItemViewType(position)) {
            case TYPE_ITEM_COLORED:
                holder.frlItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                holder.btnEditSkyline.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                break;
            case TYPE_ITEM_NORMAL:
                holder.frlItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.btnEditSkyline.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                break;
        }

        return convertView;
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private class ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private LinearLayout frlItem;
        private TextView btnEditSkyline;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;

        public ViewHolder() {

        }
    }
}
