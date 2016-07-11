package co.tactusoft.ordercollector.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.adapters.OrdenesEntradaAdapter;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.PaginadorOrdenesEntrada;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.Singleton;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentOrdenesEntrada extends Fragment {

    ListView listView;
    OrdenesEntradaAdapter adapter;
    List<OrdenesEntradas> allList;
    List<OrdenesEntradas> list;

    private MenuItem mOEConfirmadaMenuItem = null;
    private MenuItem mOEAceptadaMenuItem = null;
    private MenuItem mOEEnEjecucionMenuItem = null;
    private MenuItem mOEFinalizadaMenuItem = null;

    private DataBaseHelper dataBaseHelper;

    public FragmentOrdenesEntrada() {
        dataBaseHelper = new DataBaseHelper(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ordenes_entrada, container, false);
        listView = (ListView) rootView.findViewById(R.id.lsv_ordenes_entrada);
        TextView emptyText = (TextView)rootView.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                OrdenesEntradas selected = list.get(position);
                int selectedId =  selected.getId();
                dataBaseHelper.getListOrdenesEntradas();
                OrdenesEntradas ordenesEntradasTemp = dataBaseHelper.getOrdenesEntradas(selectedId);
                if (ordenesEntradasTemp != null) {
                    selected = ordenesEntradasTemp;
                }
                selected.setBloqueado(true);
                dataBaseHelper.insertOrdenesEntradas(selected);
                Singleton.getInstance().setOrdenesEntradas(selected);
                adapter.notifyDataSetChanged();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new FragmentOrdenesEntradaDetalle());
                ft.commit();
            }
        });
        new HttpRequestTask(getActivity()).execute();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ordenes_entrada, menu);
        mOEConfirmadaMenuItem = menu.getItem(0);
        mOEAceptadaMenuItem = menu.getItem(1);
        mOEEnEjecucionMenuItem = menu.getItem(2);
        mOEFinalizadaMenuItem = menu.getItem(3);

        // Also set the topo basemap menu item to be checked, as this is the default.
        mOEAceptadaMenuItem.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itm_estado_oe_aceptada:
                mOEAceptadaMenuItem.setChecked(true);
                break;
            case R.id.itm_estado_oe_confirmada:
                mOEConfirmadaMenuItem.setChecked(true);
                break;
            case R.id.itm_estado_oe_enjecucion:
                mOEEnEjecucionMenuItem.setChecked(true);
                break;
            case R.id.itm_estado_oe_finalizada:
                mOEFinalizadaMenuItem.setChecked(true);
                break;
        }

        setupList();
        return true;
    }


    private void setupList() {
        list = new LinkedList<>();
        if(allList!=null && !allList.isEmpty()) {
            for (OrdenesEntradas row : allList) {
                if (mOEAceptadaMenuItem.isChecked() && row.getEstadoOrden().equals("ACEPTADA")) {
                    list.add(row);
                } else if (mOEConfirmadaMenuItem.isChecked() && row.getEstadoOrden().equals("CONFIRMADA")) {
                    list.add(row);
                } else if (mOEEnEjecucionMenuItem.isChecked() && row.getEstadoOrden().equals("EN_EJECCUCION")) {
                    list.add(row);
                } else if (mOEFinalizadaMenuItem.isChecked() && row.getEstadoOrden().equals("FINALIZADA")) {
                    list.add(row);
                }
            }
        } else {
            list.add(Singleton.getInstance().getOrdenesEntradas());
        }
        adapter = new OrdenesEntradaAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<OrdenesEntradas>> {
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
        protected List<OrdenesEntradas> doInBackground(Void... params) {
            try {
                Integer bodegaId = Singleton.getInstance().getUsuario().getBodegaId();
                if(bodegaId != null) {
                    final String url = Constants.REST_URL + "/ingresos?bodegaId=" + bodegaId;
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    PaginadorOrdenesEntrada paginadorOrdenesEntrada = restTemplate.getForObject(url, PaginadorOrdenesEntrada.class);
                    return Arrays.asList(paginadorOrdenesEntrada.getContent());
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return new LinkedList<>();
        }

        @Override
        protected void onPostExecute(List<OrdenesEntradas> entryList) {
            allList = entryList;
            setupList();
            progressDialog.dismiss();
        }
    }
}
