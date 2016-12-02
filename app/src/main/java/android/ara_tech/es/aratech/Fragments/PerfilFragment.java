package android.ara_tech.es.aratech.Fragments;

import android.annotation.TargetApi;
import android.ara_tech.es.aratech.Persistencia.Listado;
import android.ara_tech.es.aratech.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v7.appcompat.R.id.end;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PerfilFragment extends Fragment {

    private static String APP_DIRECTORY = "MyPerfilApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "AratechApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView mSetImage;
    private Button mOptionButton, btGuardar, btDNI, btTelefono, btEmail, btDireccion, btUsuario;
    private RelativeLayout mRlView;
    private EditText etUsuario, etMail, etTelefono, etDireccion, etDNI, etOtros;
    private TextView tvEspecificaciones;
    private ListView lista;
    private Context mContext;
    private ArrayList<Integer> arrayListPos = new ArrayList<Integer>();
    private CheckBox chAncianos, chNiños;



    public static final String POSICION = "position";
    int position = -1;

    private String mPath;

    String username,direccion,email,telefono, dni, lista2;

    private OnFragmentInteractionListener mListener;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        mSetImage = (ImageView)view.findViewById(R.id.imageView);


        //Pedimos permisos de activación de cámara y acceso a galería. Una vez que se han aceptado, cuando se pulsa sobre el boton "+" nos da a elegir una de las
        //opciones descritos en el método showOptions.

        //TODO: hacer que la foto de perfil que hemos elegido se mantenga cada vez que se inicia sesioón.
        mOptionButton = (Button)view.findViewById(R.id.btImagen);
        if(mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(true);
        mOptionButton.setOnClickListener(new View.OnClickListener() {

            //Añadimos el reto de botones para la edicion de los diferentes campos del perfil

            @Override
            public void onClick(View v) {
                showOptions();

            }
        });

        chAncianos = (CheckBox)view.findViewById(R.id.cbAncianos);
        chNiños = (CheckBox)view.findViewById(R.id.cbNiños);





        //El boton de Guardar tiene varias funciones
        btGuardar = (Button)view.findViewById(R.id.btGuardar);
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View argo0) {

                //Pone todos los EditText inactivos para que no se puedan editar y
                //oculta el teclado.

                etDNI.setEnabled(false);
                etTelefono.setEnabled(false);
                etUsuario.setEnabled(false);
                etDireccion.setEnabled(false);
                etMail.setEnabled(false);
                etOtros.setEnabled(false);

                btGuardar.setInputType(InputType.TYPE_NULL);

                //Actualiza los datos del usuario
                username = etUsuario.getText().toString();
                email = etMail.getText().toString();
                telefono = etTelefono.getText().toString();
                direccion = etDireccion.getText().toString();
                dni = etDNI.getText().toString();
                lista2 = etOtros.getText().toString();


                ParseUser currentuser = ParseUser.getCurrentUser();
                currentuser.setUsername(username);
                currentuser.setEmail(email);
                currentuser.put("telefono",telefono);
                currentuser.put("dni", dni);
                currentuser.put("direccion", direccion);
                currentuser.put("listado", lista2);

                currentuser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null){
                            Log.i("done","Entrando en done del registro");

                            Toast.makeText(getActivity(), "Se han actualizado los datos correctamente", Toast.LENGTH_SHORT).show();


                        }else {
                            String error =e.getMessage();

                            Toast.makeText(getActivity(), "Actualización fallida: "+ error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });



            }
        });

        //Los botones de editar (lapiceros) hace que el EditText correspondiente
        //se active, el foco aparezca al final de lo que haya escrito y aparece
        //el telcado.
        etDNI = (EditText)view.findViewById(R.id.tvDNI);
        etDNI.setText(ParseUser.getCurrentUser().getString("dni"));
        btDNI = (Button)view.findViewById(R.id.btLapizDNI);
        btDNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etDNI.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                etDNI.requestFocus(end);
                etDNI.setEnabled(true);

            }
        });

        etTelefono = (EditText)view.findViewById(R.id.tvTelefono);
        etTelefono.setText(ParseUser.getCurrentUser().getString("telefono"));
        btTelefono = (Button)view.findViewById(R.id.btLapizTelefono);
        btTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etTelefono.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                etTelefono.requestFocus(end);
                etTelefono.setEnabled(true);

            }
        });

        etDireccion = (EditText)view.findViewById(R.id.tvDireccion);
        etDireccion.setText(ParseUser.getCurrentUser().getString("direccion"));
        btDireccion = (Button)view.findViewById(R.id.btDireccion);
        btDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etDireccion.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                etDireccion.requestFocus(end);
                etDireccion.setEnabled(true);
            }
        });

        etUsuario = (EditText)view.findViewById(R.id.etUsuario);
        etUsuario.setText(ParseUser.getCurrentUser().getUsername());
        btUsuario = (Button)view.findViewById(R.id.btLapizUsu);
        btUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etUsuario.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                etUsuario.requestFocus(end);
                etUsuario.setEnabled(true);


            }
        });

        etMail = (EditText)view.findViewById(R.id.tvEmail);
        etMail.setText(ParseUser.getCurrentUser().getEmail());
        btEmail = (Button)view.findViewById(R.id.btEmail);
        btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputMethodManager =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etMail.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                etMail.requestFocus(end);
                etMail.setEnabled(true);

            }
        });

        etOtros = (EditText)view.findViewById(R.id.etOtros);
        etOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etOtros.requestFocus(end);
                etOtros.setEnabled(true);
            }
        });



        //Al pulsar sobre el texto nos lleva al fragmento del listado
        tvEspecificaciones = (TextView)view.findViewById(R.id.tvEspecificaciones);
        tvEspecificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace( R.id.content_frame, new ListadoFragment(), "Listado");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }


        });

        //Mostramos los datos del ListadoFragment en el ListView del perfil, además, al escribir en Otros,
        //debe añadirse al arraylist y mostrarse aqui.
        lista = (ListView)view.findViewById(R.id.lvEspecificaciones);

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getActivity(),
        android.R.layout.simple_list_item_1,
        Listado.getInstance().getArrayPosiciones());

        lista.setAdapter(arrayAdapter);

        mRlView = (RelativeLayout)view.findViewById(R.id.content_perfil_fragment);

        return view;
    }


    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    //Mostramos opciones diferentes para cambiar la imagen de perfil.
    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }





    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    //Este metodo debería dejarnos sacar la foto y ponerla como foto de perfil, pero no sale.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getContext(),
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    mSetImage.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);

            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilFragment.this.getActivity());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.show();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PerfilFragment.OnFragmentInteractionListener) {
            mListener = (PerfilFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void passData(Context context, ArrayList<Integer> arrayPos) {
        mContext = context;
        arrayListPos = arrayPos;


    }
}
