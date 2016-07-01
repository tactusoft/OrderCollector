package co.tactusoft.ordercollector.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import co.tactusoft.ordercollector.MainActivity;
import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.adapters.FotoDescAdapter;
import co.tactusoft.ordercollector.entities.FotoDesc;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.VehiculosFoto;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.Utils;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentVehiculoFotos extends Fragment {

    GridView androidGridView;
    List<FotoDesc> list;

    OrdenesEntradas ordenesEntradas;
    DataBaseHelper dataBaseHelper;

    int picSelected;

    public FragmentVehiculoFotos() {
        super();
    }

    public static FragmentVehiculoFotos newInstance(OrdenesEntradas selected) {
        FragmentVehiculoFotos f = new FragmentVehiculoFotos();
        f.setOrdenesEntradas(selected);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vehiculo_fotos, container, false);

        MainActivity mainActivity = ((MainActivity)getActivity());
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(getResources().getString(R.string.oe_title),
                    ordenesEntradas.getNumeroDocumentoOrdenCliente()));
        }

        androidGridView = (GridView) rootView.findViewById(R.id.gridview_android_example);
        setupList();
        androidGridView.setAdapter(new FotoDescAdapter(getActivity(), list));
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)  {
                FotoDesc fotoDesc = (FotoDesc) androidGridView.getItemAtPosition(position);
                switch (fotoDesc.getCodigo()) {
                    case "FRENTE":
                        picSelected = 0;
                        break;
                    case "OTROS":
                        picSelected = 1;
                        break;
                    case "IZQUIERDO":
                        picSelected = 2;
                        break;
                    case "DERECHO":
                        picSelected = 3;
                        break;
                    case "PUERTA":
                        picSelected = 4;
                        break;
                }
                selectImage();
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vehiculos, menu);
        super.onCreateOptionsMenu(menu,inflater);
        menu.getItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                for(FotoDesc row:list) {
                    VehiculosFoto vehiculosFoto = new VehiculosFoto();
                    if(row.getFotoOmision() == 0) {
                        String nameFile = "img_" + ordenesEntradas.getId() + "_" + row.getCodigo() + ".jpeg";
                        Utils.saveImage(getActivity(), row.getBitmap(), nameFile);
                        vehiculosFoto.setFoto(nameFile);
                    }
                    vehiculosFoto.setOrdenId(ordenesEntradas.getId());
                    vehiculosFoto.setCodigo(row.getCodigo());
                    vehiculosFoto.setDescripcion(row.getDescripcion());
                    vehiculosFoto.setFotoOmision(row.getFotoOmision());
                    vehiculosFoto.setSincronizado(0);
                    dataBaseHelper.insertVehiculosFoto(vehiculosFoto);
                }
                Toast.makeText(getActivity(), R.string.msg_ok, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == Constants.RESULT_OK
                && null != data) {
            loadPictureAction(data);
        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE
                && resultCode == Constants.RESULT_OK) {
            takePictureAction(data);
        }
    }

    public void setOrdenesEntradas(OrdenesEntradas ordenesEntradas) {
        this.ordenesEntradas = ordenesEntradas;
    }

    private void setupList() {
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_help);
        Bitmap icon = ((BitmapDrawable)drawable).getBitmap();
        dataBaseHelper = new DataBaseHelper(getActivity());
        List<VehiculosFoto> vehiculosFotoList = dataBaseHelper.getListVehiculosFoto(ordenesEntradas.getId());
        list = new LinkedList<>();
        if(vehiculosFotoList.isEmpty()) {
            FotoDesc fotoDesc = new FotoDesc();
            fotoDesc.setCodigo("FRENTE");
            fotoDesc.setBitmap(icon);
            fotoDesc.setFotoOmision(1);
            fotoDesc.setDescripcion("Foto Frente");
            list.add(fotoDesc);
            fotoDesc = new FotoDesc();
            fotoDesc.setCodigo("OTROS");
            fotoDesc.setBitmap(icon);
            fotoDesc.setFotoOmision(1);
            fotoDesc.setDescripcion("Foto Otros");
            list.add(fotoDesc);
            fotoDesc = new FotoDesc();
            fotoDesc.setCodigo("IZQUIERDO");
            fotoDesc.setBitmap(icon);
            fotoDesc.setFotoOmision(1);
            fotoDesc.setDescripcion("Foto Lado Izquierdo");
            list.add(fotoDesc);
            fotoDesc = new FotoDesc();
            fotoDesc.setCodigo("DERECHO");
            fotoDesc.setBitmap(icon);
            fotoDesc.setFotoOmision(1);
            fotoDesc.setDescripcion("Foto Lado Derecho");
            list.add(fotoDesc);
            fotoDesc = new FotoDesc();
            fotoDesc.setCodigo("PUERTA");
            fotoDesc.setBitmap(icon);
            fotoDesc.setFotoOmision(1);
            fotoDesc.setDescripcion("Foto Puerta");
            list.add(fotoDesc);
        } else {
            for(VehiculosFoto vehiculosFoto:vehiculosFotoList) {
                FotoDesc fotoDesc = new FotoDesc();
                fotoDesc.setCodigo(vehiculosFoto.getCodigo());
                fotoDesc.setDescripcion(vehiculosFoto.getDescripcion());
                fotoDesc.setFotoOmision(vehiculosFoto.getFotoOmision());
                if(fotoDesc.getFotoOmision() == 1) {
                    fotoDesc.setBitmap(icon);
                } else {
                    fotoDesc.setBitmap(Utils.loadImageBitmap(getActivity(), vehiculosFoto.getFoto()));
                }
                list.add(fotoDesc);
            }
        }
    }

    private void loadPictureAction(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn,
                null, null, null);
        String picturePath = null;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
        Bitmap pic = BitmapFactory.decodeFile(picturePath);
        list.get(picSelected).setBitmap(pic);
        list.get(picSelected).setFotoOmision(0);
        androidGridView.setAdapter(new FotoDescAdapter(getActivity(), list));
    }

    private void takePictureAction(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap pic = (Bitmap) extras.get("data");
        list.get(picSelected).setBitmap(pic);
        list.get(picSelected).setFotoOmision(0);
        androidGridView.setAdapter(new FotoDescAdapter(getActivity(), list));
    }

    private void selectImage() {
        final CharSequence[] items = { "Tomar Foto", "Seleccionar de la Galería",
                "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Agregar Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(getActivity());
                if (items[item].equals("Tomar Foto")) {
                    if(result) {
                        cameraIntent();
                    }
                } else if (items[item].equals("Seleccionar de la Galería")) {
                    if(result) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        try {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent
                    .resolveActivity((getActivity())
                            .getPackageManager()) != null) {
                startActivityForResult(takePictureIntent,
                        Constants.REQUEST_IMAGE_CAPTURE);
            }
        } catch(ActivityNotFoundException exp){
            Toast.makeText(getActivity(), "Problema al activar la cámara!", Toast.LENGTH_SHORT).show();
        }
    }

    private void galleryIntent() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
        } catch(ActivityNotFoundException exp){
            Toast.makeText(getActivity(), "No File (Manager / Explorer)etc Found In Your Device", Toast.LENGTH_SHORT).show();
        }
    }

}
