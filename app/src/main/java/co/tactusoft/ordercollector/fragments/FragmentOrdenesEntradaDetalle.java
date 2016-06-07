package co.tactusoft.ordercollector.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FragmentOrdenesEntradaDetalle extends Fragment {

    OrdenesEntradas selected;

    public FragmentOrdenesEntradaDetalle() {
        selected = Singleton.getInstance().getOrdenesEntradas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Orden de Entrada: " + selected.getNumeroDocumentoOrdenCliente());
        return rootView;
    }

}
