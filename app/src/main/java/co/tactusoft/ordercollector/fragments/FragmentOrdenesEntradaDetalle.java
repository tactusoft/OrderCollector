package co.tactusoft.ordercollector.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentOrdenesEntradaDetalle extends Fragment {

    OrdenesEntradas selected;
    EditText textOeEstado;
    EditText textOeNroOrden;
    EditText textOeCliente;
    EditText textOeHoraLlegadaEsperada;
    EditText textOeHoraLlegadaReal;
    EditText textOePlaca;

    public FragmentOrdenesEntradaDetalle() {
        selected = Singleton.getInstance().getOrdenesEntradas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ordenes_entrada_detalle, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(String.format(getResources().getString(R.string.oe_title), selected.getNumeroDocumentoOrdenCliente()));
        textOeEstado = (EditText) rootView.findViewById(R.id.text_oe_estado);
        textOeNroOrden = (EditText) rootView.findViewById(R.id.text_oe_nro_orden);
        textOeCliente = (EditText) rootView.findViewById(R.id.text_oe_cliente);
        textOeHoraLlegadaEsperada = (EditText) rootView.findViewById(R.id.text_oe_hora_llegada_esperada);
        textOeHoraLlegadaReal = (EditText) rootView.findViewById(R.id.text_oe_hora_llegada_real);
        textOePlaca = (EditText) rootView.findViewById(R.id.text_oe_placa);

        textOeEstado.setText(selected.getEstadoOrden());
        textOeNroOrden.setText(selected.getNumeroDocumentoOrdenCliente());
        textOeCliente.setText(selected.getClienteCodigo());
        textOeHoraLlegadaEsperada.setText(selected.getFechaPlaneadaEntregaMaxima().replace("00:00",
                selected.getHoraPlaneadaEntregaMaxima()));

        return rootView;
    }

}
