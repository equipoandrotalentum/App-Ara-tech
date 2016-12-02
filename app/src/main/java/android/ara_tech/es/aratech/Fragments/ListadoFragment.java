package android.ara_tech.es.aratech.Fragments;

import android.ara_tech.es.aratech.Persistencia.Listado;
import android.ara_tech.es.aratech.R;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListadoFragment extends Fragment {

    Context mContext;
    int posicion;

    private ListView list;
    private String[] strList;
    private ArrayAdapter<String> objAdapter;


    private PerfilFragment.OnFragmentInteractionListener mListener;
    Button bVolver;



    public ListadoFragment() {
        // Required empty public constructor
    }

    public interface seleccion {
        public void seleccion(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_listado, container, false);
        bVolver = (Button)view.findViewById(R.id.btVolver);

        list = (ListView) view.findViewById(R.id.listView);
        strList=getResources().getStringArray(R.array.especificaciones_listtado);
        objAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,
                strList);

        list.setAdapter(objAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

             Listado.getInstance().addPosicion(position);
             Toast.makeText(getActivity(),strList[position]+" añadido a tu perfil",Toast.LENGTH_SHORT ).show();

            }
        });

        bVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new PerfilFragment()).addToBackStack(null).commit();
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

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void passData(Context context, int position) {
        mContext = context;
        posicion = position;

    }

    public interface OnFragmentInteractionListener {
    }

}
