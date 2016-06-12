package co.tactusoft.ordercollector.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradaPUT;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.RespuestaDTO;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.Singleton;
import co.tactusoft.ordercollector.util.Utils;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentOrdenesEntradaDetalle extends Fragment {

    OrdenesEntradas selected;
    ImageView imgOeOpcion1;
    ImageView imgOeOpcion2;
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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(String.format(getResources().getString(R.string.oe_title),
                selected.getNumeroDocumentoOrdenCliente()));

        imgOeOpcion1 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_1);
        imgOeOpcion2 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_2);

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

        if(!selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
            imgOeOpcion1.setImageResource(R.drawable.ic_action_clock_disabled);
        }

        imgOeOpcion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
                    new HttpRequestTask().execute();
                }
            }
        });

        imgOeOpcion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, FragmentVehiculo.newInstance(selected));
                ft.commit();
            }
        });

        return rootView;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, RespuestaDTO> {
        @Override
        protected RespuestaDTO doInBackground(Void... params) {
            try {
                OrdenesEntradaPUT ordenesEntradaPUT = new OrdenesEntradaPUT();
                ordenesEntradaPUT.setId(selected.getId());
                ordenesEntradaPUT.setFechaNotificacionDeLlegada(Utils.dateToString(new Date()));
                ordenesEntradaPUT.setFechaRegistroDeLlegada(Utils.dateToString(new Date()));
                ordenesEntradaPUT.setUsuarioActualizacion(Singleton.getInstance().getUsuario().getNombreUsuario());

                final String url = Constants.REST_URL + "ingresos/llegadas/"+selected.getId();
                URI uri = new URI(url);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

                HttpEntity<OrdenesEntradaPUT> requestEntity = new HttpEntity<>(ordenesEntradaPUT, requestHeaders);
                ResponseEntity<RespuestaDTO> responseEntity = restTemplate.exchange(uri , HttpMethod.PUT, requestEntity, RespuestaDTO.class);
                return responseEntity.getBody();
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(RespuestaDTO respuestaDTO) {
            if (respuestaDTO.getSeveridadMaxima().equals(Constants.SEVERIDAD_ERRORES.INFO.name())) {

            }
        }
    }

}
