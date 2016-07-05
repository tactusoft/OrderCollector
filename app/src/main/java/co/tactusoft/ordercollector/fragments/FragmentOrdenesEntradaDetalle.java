package co.tactusoft.ordercollector.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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
import java.util.Collections;
import java.util.Date;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradaPUT;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.RespuestaDTO;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.DataBaseHelper;
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
    ImageView imgOeOpcion3;
    ImageView imgOeOpcion4;
    ImageView imgOeOpcion5;
    ImageView imgOeOpcion6;
    EditText textOeEstado;
    EditText textOeNroOrden;
    EditText textOeCliente;
    EditText textOeHoraLlegadaEsperada;
    EditText textOeHoraLlegadaReal;
    EditText textOePlaca;

    DataBaseHelper dataBaseHelper;

    public FragmentOrdenesEntradaDetalle() {
        dataBaseHelper = new DataBaseHelper(getActivity());
        selected = Singleton.getInstance().getOrdenesEntradas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ordenes_entrada_detalle, container, false);

        MainActivity mainActivity = ((MainActivity)getActivity());
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(getResources().getString(R.string.oe_title),
                    selected.getNumeroDocumentoOrdenCliente()));
        }

        imgOeOpcion1 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_1);
        imgOeOpcion2 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_2);
        imgOeOpcion3 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_3);
        imgOeOpcion4 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_4);
        imgOeOpcion5 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_5);
        imgOeOpcion6 = (ImageView) rootView.findViewById(R.id.img_oe_opcion_6);

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
        textOeHoraLlegadaReal.setText(selected.getFechaRegistroDeLlegada());
        textOePlaca.setText(selected.getNumeroPlacaVehiculo());

        if(!selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
           enabledOptions();
        } else {
           disabledOptions();
        }

        imgOeOpcion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
                    new HttpRequestTask(getActivity()).execute();
                }
            }
        });

        imgOeOpcion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
                    showFragmentVehiculo();
                }
            }
        });

        imgOeOpcion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
                    showFragmentOENovedades();
                }
            }
        });

        imgOeOpcion6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selected.getEstadoOrden().equals(Constants.ESTADOS_ORDENES.ACEPTADA.name())) {
                    showFragmentOESalida();
                }
            }
        });

        return rootView;
    }

    private void enabledOptions() {
        imgOeOpcion1.setImageResource(R.drawable.ic_action_clock_disabled);
        imgOeOpcion2.setImageResource(R.drawable.ic_action_bus);
        imgOeOpcion3.setImageResource(R.drawable.ic_action_cart);
        imgOeOpcion4.setImageResource(R.drawable.ic_action_bell);
        imgOeOpcion5.setImageResource(R.drawable.ic_action_copy);
        imgOeOpcion6.setImageResource(R.drawable.ic_action_import);
    }

    private void disabledOptions() {
        imgOeOpcion1.setImageResource(R.drawable.ic_action_clock);
        imgOeOpcion2.setImageResource(R.drawable.ic_action_bus_disabled);
        imgOeOpcion3.setImageResource(R.drawable.ic_action_cart_disabled);
        imgOeOpcion4.setImageResource(R.drawable.ic_action_bell_disabled);
        imgOeOpcion5.setImageResource(R.drawable.ic_action_copy_disabled);
        imgOeOpcion6.setImageResource(R.drawable.ic_action_import_disabled);
    }

    public void showFragmentVehiculo() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, FragmentVehiculo.newInstance(selected));
        ft.commit();
    }

    public void showFragmentOENovedades() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, FragmentOENovedades.newInstance(selected), FragmentOENovedades.TAG);
        ft.commit();
    }

    public void showFragmentOESalida() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, FragmentOESalida.newInstance(selected));
        ft.commit();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, RespuestaDTO> {

        ProgressDialog progressDialog;

        public HttpRequestTask(Context context) {
            progressDialog = new ProgressDialog(context);
        }

        public void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getResources().getString(R.string.msg_loading));
            progressDialog.show();
        }

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
            progressDialog.dismiss();
            if (respuestaDTO.getSeveridadMaxima().equals(Constants.SEVERIDAD_ERRORES.INFO.name())) {
                selected.setBloqueado(true);
                selected.setEstadoOrden(Constants.ESTADOS_ORDENES.EN_EJECUCION.name());
                selected.setId(respuestaDTO.getData().getId());
                selected.setFechaNotificacionDeLlegada(respuestaDTO.getData().getFechaNotificacionDeLlegada());
                selected.setFechaRegistroDeLlegada(respuestaDTO.getData().getFechaRegistroDeLlegada());
                selected.setUsuarioActualizacion(respuestaDTO.getData().getUsuarioActualizacion());
                dataBaseHelper.insertOrdenesEntradas(selected);
                Singleton.getInstance().setOrdenesEntradas(selected);
                showFragmentVehiculo();
                enabledOptions();
            }
        }
    }

}
