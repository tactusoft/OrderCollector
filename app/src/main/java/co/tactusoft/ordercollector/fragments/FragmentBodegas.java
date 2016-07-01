package co.tactusoft.ordercollector.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

    ListView listView;
    BodegasAdapter adapter;
    List<Bodegas> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bodegas, container, false);
        listView = (ListView) rootView.findViewById(R.id.lsv_bodegas);
        TextView emptyText = (TextView)rootView.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);
        new HttpRequestTask(getActivity()).execute();
        return rootView;
    }

    private void setupList() {
        if(list!=null) {
            adapter = new BodegasAdapter(getActivity(), list);
            listView.setAdapter(adapter);
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<Bodegas>> {

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
            progressDialog.dismiss();
        }
    }
}
