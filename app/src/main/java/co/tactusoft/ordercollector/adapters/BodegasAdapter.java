package co.tactusoft.ordercollector.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.Bodegas;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class BodegasAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Bodegas> mDataSet = new ArrayList<>();
    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private ViewHolder lastViewHolder;
    private DataBaseHelper dataBaseHelper;

    public BodegasAdapter(Context context, List<Bodegas> dataSet) {
        mContext = context;
        mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);
        binderHelper.setOpenOnlyOne(true);
        dataBaseHelper = new DataBaseHelper(mContext.getApplicationContext());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_bodegas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;
        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            final Bodegas data = mDataSet.get(position);
            binderHelper.bind(holder.swipeLayout, String.valueOf(data.getBodegaId()));
            holder.btnEditSkyline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastViewHolder != null) {
                        lastViewHolder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    }
                    holder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                    Bodegas selected = mDataSet.get(holder.getAdapterPosition());
                    int bodegaId =  selected.getBodegaId();
                    Singleton.getInstance().getUsuario().setBodegaId(bodegaId);
                    dataBaseHelper.insertUsuario(Singleton.getInstance().getUsuario());
                    binderHelper.closeLayout(String.valueOf(selected.getBodegaId()));
                    lastViewHolder = holder;
                }
            });

            if(Singleton.getInstance().getUsuario().getBodegaId().intValue() == data.getBodegaId().intValue()) {
                holder.frlItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                lastViewHolder = holder;
            }

            holder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private FrameLayout frlItem;
        private TextView btnEditSkyline;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            frlItem  = (FrameLayout) itemView.findViewById(R.id.frl_item);
            btnEditSkyline = (TextView) itemView.findViewById(R.id.btn_edit_skyline);
            textView = (TextView) itemView.findViewById(R.id.text);
            textView2 = (TextView) itemView.findViewById(R.id.text2);
            textView3 = (TextView) itemView.findViewById(R.id.text3);
            textView4 = (TextView) itemView.findViewById(R.id.text4);
        }

        public void bind(Bodegas data) {
            textView.setText(data.getBodegaCodigo());
            textView2.setText(data.getBodegaNombre());
            textView3.setText(data.getBodegaCiudadNombre() );
            textView4.setText(data.getBodegaDireccion());
        }
    }
}
