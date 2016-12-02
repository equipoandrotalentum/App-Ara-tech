package android.ara_tech.es.aratech.Fragments;

import android.ara_tech.es.aratech.CalendarioActivity;
import android.ara_tech.es.aratech.Entidad.Servicio;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.ara_tech.es.aratech.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;


public class CuidadorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView tvNombreCuidador,tvNacionalidadCuidador, tvEdadCuidador, tvDescripcionCuidador;
    ImageView ivCuidador;
    RatingBar ratingBar;
    Context mContext;
    Servicio serv;
    Button btCalendar;



    public CuidadorFragment() {
        // Required empty public constructor
    }

    public static CuidadorFragment newInstance(String param1, String param2) {
        CuidadorFragment fragment = new CuidadorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuidador, container, false);
        tvNombreCuidador = (TextView)view.findViewById(R.id.tvNombreCuidador);
        ivCuidador = (ImageView)view.findViewById(R.id.imgCuidador);
        tvNacionalidadCuidador = (TextView)view.findViewById(R.id.tvNacionalidadCuidador);
        tvEdadCuidador = (TextView)view.findViewById(R.id.tvEdadCuidador);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        tvDescripcionCuidador = (TextView)view.findViewById(R.id.descripcionCuidador);
        btCalendar = (Button)view.findViewById(R.id.btCalendar);

        tvNombreCuidador.setText(serv.getNombre());
        tvNacionalidadCuidador.setText(serv.getNacionalidad());
        tvEdadCuidador.setText(String.valueOf(serv.getEdad()));
        ratingBar.setRating((float)serv.getPuntuacion());

        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarioActivity.class);
                startActivity(intent);
            }
        });
        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void passData(Context context, Servicio servicio) {
        mContext = context;
        serv = servicio;

    }
}
