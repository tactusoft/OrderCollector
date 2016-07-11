package co.tactusoft.ordercollector.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.OrdenesEntradaPUT;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.RespuestaDTO;
import co.tactusoft.ordercollector.entities.TiposVehiculos;
import co.tactusoft.ordercollector.entities.Transportadoras;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.Singleton;
import co.tactusoft.ordercollector.util.Utils;

/**
 * Created by csarmiento
 * 12/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentVehiculo extends Fragment {

    Spinner spnTransportadoras;
    Spinner spnTiposVehiculos;
    EditText inputVehPlaca;
    EditText inputVehRemolque;
    EditText inputConCedula;
    EditText inputConNombres;
    EditText inputConApellidos;
    EditText inputConCelular;

    ArrayAdapter adapterTransportadoras;
    ArrayAdapter adapterTiposVehiculos;

    OrdenesEntradas ordenesEntradas;
    DataBaseHelper dataBaseHelper;

    public static FragmentVehiculo newInstance(OrdenesEntradas selected) {
        FragmentVehiculo f = new FragmentVehiculo();
        f.setOrdenesEntradas(selected);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBaseHelper = new DataBaseHelper(getActivity());
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_vehiculo, container, false);

        MainActivity mainActivity = ((MainActivity)getActivity());
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(getResources().getString(R.string.oe_title),
                    ordenesEntradas.getNumeroDocumentoOrdenCliente()));
        }

        spnTransportadoras = (Spinner) rootView.findViewById(R.id.spn_transportadoras);
        spnTiposVehiculos = (Spinner) rootView.findViewById(R.id.spn_tipos_vehiculos);
        inputVehPlaca = (EditText) rootView.findViewById(R.id.input_veh_placa);
        inputVehRemolque = (EditText) rootView.findViewById(R.id.input_veh_remolque);
        inputConCedula = (EditText) rootView.findViewById(R.id.input_con_cedula);
        inputConNombres = (EditText) rootView.findViewById(R.id.input_con_nombres);
        inputConApellidos = (EditText) rootView.findViewById(R.id.input_con_apellidos);
        inputConCelular = (EditText) rootView.findViewById(R.id.input_con_celular);

        inputVehPlaca.setText(ordenesEntradas.getNumeroPlacaVehiculo());
        inputVehRemolque.setText(ordenesEntradas.getNumeroPlacaRemolque());
        inputConCedula.setText(ordenesEntradas.getConductorNumeroIdentificacion());
        inputConNombres.setText(ordenesEntradas.getConductorNombres());
        inputConApellidos.setText(ordenesEntradas.getConductorApellidos());
        inputConCelular.setText(ordenesEntradas.getConductorTelefono());

        new TransportadorasHttpRequestTask(getActivity()).execute();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vehiculos, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                Integer tipoVehiculoId = ((TiposVehiculos)spnTiposVehiculos.getSelectedItem()).getTipoVehiculoId();
                Integer transportadoraId =  ((Transportadoras)spnTransportadoras.getSelectedItem()).getTransportadoraId();
                String vehPlaca = inputVehPlaca.getText().toString();
                String vehRemolque = inputVehRemolque.getText().toString();
                String conCedula = inputConCedula.getText().toString();
                String conNombres = inputConNombres.getText().toString();
                String conApellidos = inputConApellidos.getText().toString();
                String conCelular = inputConCelular.getText().toString();
                if(TextUtils.isEmpty(vehPlaca) || TextUtils.isEmpty(vehRemolque) ||
                        TextUtils.isEmpty(conCedula) || TextUtils.isEmpty(conNombres) ||
                        TextUtils.isEmpty(conApellidos) || TextUtils.isEmpty(conCelular) ) {
                    Toast.makeText(getActivity(), R.string.msg_required, Toast.LENGTH_SHORT).show();
                } else {
                    ordenesEntradas.setTipoVehiculoId(tipoVehiculoId);
                    ordenesEntradas.setTransportadoraId(transportadoraId);
                    ordenesEntradas.setNumeroPlacaVehiculo(vehPlaca);
                    ordenesEntradas.setNumeroPlacaRemolque(vehRemolque);
                    ordenesEntradas.setConductorNumeroIdentificacion(conCedula);
                    ordenesEntradas.setConductorApellidos(conApellidos);
                    ordenesEntradas.setConductorNombres(conNombres);
                    ordenesEntradas.setConductorTelefono(conCelular);
                    new SaveHttpRequestTask(getActivity()).execute();
                }
                break;
            case R.id.item_photos:
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, FragmentVehiculoFotos.newInstance(ordenesEntradas));
                ft.commit();
                break;
            default:
                break;
        }
        return true;
    }

    public void setOrdenesEntradas(OrdenesEntradas ordenesEntradas) {
        this.ordenesEntradas = ordenesEntradas;
    }

    private class TransportadorasHttpRequestTask extends AsyncTask<Void, Void, List<Transportadoras>> {
        ProgressDialog progressDialog;

        public TransportadorasHttpRequestTask(Context context) {
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
        protected List<Transportadoras> doInBackground(Void... params) {
            try {
                final String url = Constants.REST_URL + "ingresos/transportadoras";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Transportadoras[]> result = restTemplate.getForEntity(url, Transportadoras[].class);
                return Arrays.asList(result.getBody());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Transportadoras> result) {
            if (result!=null) {
                adapterTransportadoras = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, result);
                spnTransportadoras.setAdapter(adapterTransportadoras);
            }
            new TiposVehiculosHttpRequestTask(getActivity()).execute();
            progressDialog.dismiss();
        }
    }

    private class TiposVehiculosHttpRequestTask extends AsyncTask<Void, Void, List<TiposVehiculos>> {
        ProgressDialog progressDialog;

        public TiposVehiculosHttpRequestTask(Context context) {
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
        protected List<TiposVehiculos> doInBackground(Void... params) {
            try {
                final String url = Constants.REST_URL + "ingresos/tipos_vehiculos";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<TiposVehiculos[]> result = restTemplate.getForEntity(url, TiposVehiculos[].class);
                return Arrays.asList(result.getBody());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TiposVehiculos> result) {
            if (result!=null) {
                adapterTiposVehiculos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, result);
                spnTiposVehiculos.setAdapter(adapterTiposVehiculos);
            }
            progressDialog.dismiss();
        }
    }

    private class SaveHttpRequestTask extends AsyncTask<Void, Void, RespuestaDTO> {
        ProgressDialog progressDialog;

        public SaveHttpRequestTask(Context context) {
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
                ordenesEntradaPUT.setId(ordenesEntradas.getId());
                ordenesEntradaPUT.setFechaNotificacionDeLlegada(Utils.dateToString(new Date()));
                ordenesEntradaPUT.setFechaRegistroDeLlegada(Utils.dateToString(new Date()));
                ordenesEntradaPUT.setUsuarioActualizacion(Singleton.getInstance().getUsuario().getNombreUsuario());
                ordenesEntradaPUT.setTipoVehiculoId(ordenesEntradas.getTipoVehiculoId());
                ordenesEntradaPUT.setTransportadoraId(ordenesEntradas.getTransportadoraId());
                ordenesEntradaPUT.setNumeroPlacaVehiculo(ordenesEntradas.getNumeroPlacaVehiculo());
                ordenesEntradaPUT.setNumeroPlacaRemolque(ordenesEntradas.getNumeroPlacaRemolque());
                ordenesEntradaPUT.setConductorNumeroIdentificacion(ordenesEntradas.getConductorNumeroIdentificacion());
                ordenesEntradaPUT.setConductorNombres(ordenesEntradas.getConductorNombres());
                ordenesEntradaPUT.setConductorApellidos(ordenesEntradas.getConductorApellidos());
                ordenesEntradaPUT.setConductorTelefono(ordenesEntradas.getConductorTelefono());

                final String url = Constants.REST_URL + "ingresos/llegadas/" + ordenesEntradas.getId();
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
                ordenesEntradas.setBloqueado(true);
                ordenesEntradas.setId(respuestaDTO.getData().getId());
                ordenesEntradas.setFechaNotificacionDeLlegada(respuestaDTO.getData().getFechaNotificacionDeLlegada());
                ordenesEntradas.setFechaRegistroDeLlegada(respuestaDTO.getData().getFechaRegistroDeLlegada());
                ordenesEntradas.setUsuarioActualizacion(respuestaDTO.getData().getUsuarioActualizacion());
                dataBaseHelper.insertOrdenesEntradas(ordenesEntradas);
                Singleton.getInstance().setOrdenesEntradas(ordenesEntradas);
                Toast.makeText(getActivity(), R.string.msg_ok, Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).showFragment(new FragmentOrdenesEntradaDetalle(), 0);
            }
        }
    }
}
