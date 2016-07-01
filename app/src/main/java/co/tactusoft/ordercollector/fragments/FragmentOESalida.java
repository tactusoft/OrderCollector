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
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
public class FragmentOESalida extends Fragment {

    OrdenesEntradas ordenesEntradas;
    DataBaseHelper dataBaseHelper;

    ImageView imgViewPic;
    EditText inputObs;

    public FragmentOESalida() {
        super();
    }

    public static FragmentOESalida newInstance(OrdenesEntradas selected) {
        FragmentOESalida f = new FragmentOESalida();
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
        menu.getItem(1).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBaseHelper = new DataBaseHelper(getActivity());
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_oe_salida, container, false);
        imgViewPic = (ImageView) rootView.findViewById(R.id.imgView_pic);
        inputObs = (EditText) rootView.findViewById(R.id.input_obs);

        imgViewPic.setImageResource(R.drawable.ic_action_help);
        imgViewPic.setTag(R.drawable.ic_action_help);
        imgViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        inputObs.setText(ordenesEntradas.getObservacionesSalida());
        if(ordenesEntradas.getImagenSalida()!=null) {
            imgViewPic.setImageBitmap(Utils.loadImageBitmap(getActivity(), ordenesEntradas.getImagenSalida()));
        }

        return rootView;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                String obs = inputObs.getText().toString();
                if (obs.isEmpty() || imgViewPic.getTag().equals(R.drawable.ic_action_help)) {
                    Toast.makeText(getActivity(), R.string.msg_required, Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap pic = ((BitmapDrawable)imgViewPic.getDrawable()).getBitmap();
                    String nameFile = "img_" + ordenesEntradas.getId() + "_SALIDA.jpeg";
                    Utils.saveImage(getActivity(), pic, nameFile);
                    ordenesEntradas.setImagenSalida(nameFile);
                    ordenesEntradas.setObservacionesSalida(obs);
                    dataBaseHelper.insertOrdenesEntradas(ordenesEntradas);
                    Toast.makeText(getActivity(), R.string.msg_ok, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
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
            Toast.makeText(getActivity(), "No se ha podido acceder a la ", Toast.LENGTH_SHORT).show();
        }
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
        imgViewPic.setImageBitmap(pic);
        imgViewPic.setTag(pic.toString());
    }

    private void takePictureAction(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap pic = (Bitmap) extras.get("data");
        imgViewPic.setImageBitmap(pic);
        imgViewPic.setTag(pic.toString());
    }

}
