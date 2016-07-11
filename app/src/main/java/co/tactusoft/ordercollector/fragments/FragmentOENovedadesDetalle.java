package co.tactusoft.ordercollector.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import co.tactusoft.ordercollector.R;
import co.tactusoft.ordercollector.entities.Novedad;
import co.tactusoft.ordercollector.util.Constants;
import co.tactusoft.ordercollector.util.DataBaseHelper;
import co.tactusoft.ordercollector.util.UniqueFileName;
import co.tactusoft.ordercollector.util.Utils;

/**
 * Created by csarmiento
 * 7/06/16
 * csarmiento@gentemovil.co
 */
public class FragmentOENovedadesDetalle extends DialogFragment implements View.OnClickListener {

    public static final String TAG = FragmentOENovedadesDetalle.class.getSimpleName();

    Novedad selected;
    DataBaseHelper dataBaseHelper;

    ImageView imgViewPic;
    Spinner inputTipo;
    Spinner inputClase;
    EditText inputObs;
    Button btnSave;
    Button btnDelete;

    public FragmentOENovedadesDetalle() {
        super();
    }

    public static FragmentOENovedadesDetalle newInstance(Novedad selected) {
        FragmentOENovedadesDetalle f = new FragmentOENovedadesDetalle();
        f.setSelected(selected);
        return f;
    }

    public void setSelected(Novedad selected) {
        this.selected = selected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBaseHelper = new DataBaseHelper(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_oe_novedades_detalle, container, false);
        imgViewPic = (ImageView) rootView.findViewById(R.id.imgView_pic);
        inputTipo = (Spinner) rootView.findViewById(R.id.input_tipo);
        inputClase = (Spinner) rootView.findViewById(R.id.input_clase);
        inputObs = (EditText) rootView.findViewById(R.id.input_obs);
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnDelete = (Button) rootView.findViewById(R.id.btn_delete);

        imgViewPic.setImageResource(R.drawable.ic_action_help);
        imgViewPic.setTag(R.drawable.ic_action_help);
        imgViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSave.setTransformationMethod(null);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String obs = inputObs.getText().toString();
                if (obs.isEmpty() || imgViewPic.getTag().equals(R.drawable.ic_action_help)) {
                    Toast.makeText(getActivity(), R.string.msg_required, Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap pic = ((BitmapDrawable)imgViewPic.getDrawable()).getBitmap();
                    String nameFile = new UniqueFileName(getActivity()).writeToFileRawData(pic);
                    boolean isNew = false;
                    if(selected.getId() == null) {
                        isNew = true;
                    }
                    selected.setClase(inputClase.getSelectedItem().toString());
                    selected.setTipo(inputTipo.getSelectedItem().toString());
                    selected.setFoto(nameFile);
                    selected.setObservacion(obs);
                    dataBaseHelper.insertNovedad(selected);
                    Toast.makeText(getActivity(), R.string.msg_ok, Toast.LENGTH_SHORT).show();
                    FragmentOENovedades fragmentOENovedades = (FragmentOENovedades)getFragmentManager()
                            .findFragmentByTag(FragmentOENovedades.TAG);
                    if(isNew) {
                        fragmentOENovedades.addNovedad(selected);
                    } else {
                        fragmentOENovedades.refreshAdapter();
                    }
                    dismiss();
                }
            }
        });

        if (selected.getId()!=null) {
            btnDelete.setEnabled(true);
        } else {
            btnDelete.setEnabled(false);
        }

        btnDelete.setTransformationMethod(null);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deleteNovedad(selected.getId());
                Toast.makeText(getActivity(), R.string.msg_ok, Toast.LENGTH_SHORT).show();
                FragmentOENovedades fragmentOENovedades = (FragmentOENovedades)getFragmentManager()
                        .findFragmentByTag(FragmentOENovedades.TAG);
                fragmentOENovedades.removeNovedad(selected);
                dismiss();
            }
        });

        if(selected.getFoto()!=null) {
            Bitmap pic = Utils.loadImageBitmap(getActivity(), selected.getFoto());
            imgViewPic.setImageBitmap(pic);
            imgViewPic.setTag(pic.toString());

            inputClase.setSelection(Utils.getIndexFromArray(getResources().getStringArray(R.array.oe_novedades_clase),
                    selected.getClase()));
            inputObs.setText(selected.getObservacion());
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

    public void onClick(View v) {

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
