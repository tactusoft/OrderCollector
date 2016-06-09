package co.tactusoft.ordercollector.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.adapters.BodegasAdapter;
import co.tactusoft.ordercollector.entities.Bodegas;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentBodegas extends Fragment {

    RecyclerView recyclerView;
    BodegasAdapter adapter;
    List<Bodegas> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bodegas, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_bodegas);
        new HttpRequestTask().execute();
        return rootView;
    }

    private void setupList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BodegasAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<Bodegas>> {
        @Override
        protected List<Bodegas> doInBackground(Void... params) {
            try {
                Integer usuarioId = Singleton.getInstance().getUsuario().getUsuarioId();
                final String url = Constants.REST_URL + "ingresos/bodegas?usuarioId=" + usuarioId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Bodegas[]> result = restTemplate.getForEntity(url, Bodegas[].class);
                return Arrays.asList(result.getBody());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bodegas> bodegasList) {
            list = bodegasList;
            setupList();
        }
    }
}
