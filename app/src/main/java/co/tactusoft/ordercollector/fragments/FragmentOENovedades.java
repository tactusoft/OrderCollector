package co.tactusoft.ordercollector.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.adapters.HLVAdapter;
import co.tactusoft.ordercollector.customcontrols.HorizontalListView;
import co.tactusoft.ordercollector.entities.Novedad;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.util.DataBaseHelper;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentOENovedades extends Fragment {

    public static final String TAG = FragmentOENovedades.class.getSimpleName();

    OrdenesEntradas ordenesEntradas;
    DataBaseHelper dataBaseHelper;

    HorizontalListView hlv;
    HLVAdapter hlvAdapter;

    List<Novedad> novedadList;
    ArrayList<String> alName;
    ArrayList<String> alImage;

    public FragmentOENovedades() {
        super();
    }

    public static FragmentOENovedades newInstance(OrdenesEntradas selected) {
        FragmentOENovedades f = new FragmentOENovedades();
        f.setOrdenesEntradas(selected);
        return f;
    }

    public void setOrdenesEntradas(OrdenesEntradas ordenesEntradas) {
        this.ordenesEntradas = ordenesEntradas;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vehiculos, menu);
        super.onCreateOptionsMenu(menu,inflater);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBaseHelper = new DataBaseHelper(getActivity());
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_oe_novedadaes, container, false);
        hlv = (HorizontalListView) rootView.findViewById(R.id.hlv);
        novedadList =  dataBaseHelper.getListNovedad(ordenesEntradas.getId());
        refreshAdapter();
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Novedad selected = novedadList.get(position);
                FragmentOENovedadesDetalle fragmentOENovedadesDetalle = FragmentOENovedadesDetalle
                        .newInstance(selected);
                fragmentOENovedadesDetalle.show(getFragmentManager(), FragmentOENovedadesDetalle.TAG);
            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Novedad novedad = new Novedad();
        novedad.setOrdenId(ordenesEntradas.getId());
        switch (item.getItemId()) {
            case R.id.item_add:
                FragmentOENovedadesDetalle fragmentOENovedadesDetalle = FragmentOENovedadesDetalle
                        .newInstance(novedad);
                fragmentOENovedadesDetalle.show(getFragmentManager(), FragmentOENovedadesDetalle.TAG);
                break;
        }
        return true;
    }

    public void addNovedad(Novedad novedad) {
        novedadList.add(novedad);
        refreshAdapter();
    }

    public void removeNovedad(Novedad novedad) {
        novedadList.remove(novedad);
        refreshAdapter();
    }

    public void refreshAdapter() {
        alName = new ArrayList<>();
        alImage = new ArrayList<>();
        for(Novedad row:novedadList) {
            alName.add(row.getClase());
            alImage.add(row.getFoto());
        }

        hlvAdapter = new HLVAdapter(getActivity(), alName, alImage);
        hlv.setAdapter(hlvAdapter);
        hlvAdapter.notifyDataSetChanged();
    }

}
