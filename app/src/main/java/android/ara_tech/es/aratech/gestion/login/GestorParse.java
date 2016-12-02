package android.ara_tech.es.aratech.gestion.login;


import android.ara_tech.es.aratech.Entidad.Servicio;
import android.ara_tech.es.aratech.Entidad.Usuario;
import android.ara_tech.es.aratech.Fragments.MapFragment;
import android.ara_tech.es.aratech.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;
import java.util.List;

public class GestorParse {

    public void comprobarLogeo (Context context){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //Enviamos a la pantalla de bienvenida.
            Usuario usuario =new Usuario();
            usuario.setEmail(currentUser.getEmail());
            usuario.setNombreUsuario(currentUser.getUsername());
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }else if( Twitter.getInstance().core.getSessionManager().getActiveSession()!=null){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    public void parseObject(final MapFragment mapFragment){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Nanny");
        query.findInBackground(new FindCallback<ParseObject>() {
            final ArrayList<Servicio> arrayList = new ArrayList<>();
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.i("Parse","Recibe Objetos " + scoreList.get(0).getParseGeoPoint("location"));

                    for (int i = 0; i <scoreList.size() ; i++) {
                        Servicio s = new Servicio();
                        ParseGeoPoint pg = scoreList.get(i).getParseGeoPoint("location");
                        s.setNombre(scoreList.get(i).getString("name"));
                        s.setLatitud(pg.getLatitude());
                        s.setLongitud(pg.getLongitude());
                        s.setEdad(scoreList.get(i).getInt("age"));
                        s.setPuntuacion(scoreList.get(i).getDouble("score"));
                        s.setNacionalidad(scoreList.get(i).getString("nationality"));
                        s.setType(scoreList.get(i).getInt("type"));
                        arrayList.add(s);
                    }

                    mapFragment.respuestaServidor(arrayList);
                } else {

                }

            }
        });


    }
}

