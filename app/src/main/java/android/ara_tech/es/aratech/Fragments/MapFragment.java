package android.ara_tech.es.aratech.Fragments;


import android.Manifest;
import android.app.SearchManager;
import android.ara_tech.es.aratech.Entidad.Servicio;
import android.ara_tech.es.aratech.R;
import android.ara_tech.es.aratech.gestion.login.GestorParse;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements
        GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    MapView mMapView;
    GoogleMap googleMap;
    LocationManager locManager;
    private static boolean accedido = false;


    public MapFragment() {

    }


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frament_testmap, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        mMapView.getMapAsync(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //----------------BÚSQUEDA EN EL MAPA-----------------------
        SearchView searchView = (SearchView) view.findViewById(R.id.busca);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchFor(query);
                Log.i("search", "Puede ser que busque");
                LatLng loc = getLatLongFromAddress(query);
                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(loc, 9);
                googleMap.moveCamera(cameraPosition);
                googleMap.animateCamera(cameraPosition);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //filterSearchFor(query);
                return true;
            }
        });


        return view;
    }

    //----------------Método de búsqueda por dirección-------------------

    private LatLng getLatLongFromAddress(String address) {
        double lat = 0.0, lng = 0.0;
        LatLng localizacion = null;

        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                ParseGeoPoint p = new ParseGeoPoint(
                        (addresses.get(0).getLatitude()),
                        (addresses.get(0).getLongitude()));

                lat = p.getLatitude();
                lng = p.getLongitude();
                localizacion = new LatLng(lat, lng);


                Log.d("Latitude", "" + lat);
                Log.d("Longitude", "" + lng);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return localizacion;
    }


    //------------------------------MAPA----------------------------------------------
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;


        //Comprobamos permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //Requiere permisos para Android 6.0
                Log.e("GmapFragment", "No se tienen permisos necesarios!, se requieren.");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
                return;
            } else {
                Log.i("GmapFragment", "Permisos necesarios OK!.");
            }

        } else {
            Log.v("GmapFragment", "Versión de SDK inferior a la 6.0, los permisos ya se dieron cuando se instalo la aplicación");
        }
        //--------------------------------------Comprobamos usuario (Eliminar)

        if (accedido==false) {
            if (ParseUser.getCurrentUser() != null) {
                String nombre = ParseUser.getCurrentUser().getUsername();
                Toast.makeText(getActivity(), "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
            } else if (Twitter.getInstance().core.getSessionManager().getActiveSession() != null) {
                Toast.makeText(getActivity(), "Has iniciado sesión con Twitter", Toast.LENGTH_SHORT).show();
            }
            accedido=true;
        }
        //Muestra la ubicación del usuario.
        Log.i("TestMap", "Estableciendo la localización del usuario (setMyLocationEnabled)");
        googleMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Obtenemos una referencia al LocationManager
            Log.i("GmapFragment", "Entrando en la comprobación de servicios y obteniendo referencia al LocationManager");
            locManager =
                    (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //Obtenemos la última posición conocida
            //ojo, es la ultima conocida, pudiendo ser de hace 1 segundo, 1 hora o 1 mes
            Location loc = null;
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.v("GmapFragment", "Obtenida ultima localización conocida");


            //Mostramos la última posición conocida
            if (loc != null) {
                double latitud = loc.getLatitude();
                double longitud = loc.getLongitude();
                LatLng marker = new LatLng(latitud, longitud);
                Log.i("GmapFragment", "Posicionando si loc es distinto de null");
                //ZOOM Inicial
                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(marker, 10);
                googleMap.moveCamera(cameraPosition);
                googleMap.animateCamera(cameraPosition);
                Log.i("GmapFragment", "Moviendo la Cámara");


            } else {
                Toast.makeText(getActivity(), "Localización nula", Toast.LENGTH_SHORT).show();
            }

            //Proveedor que queremos utilizar
            //Tiempo minimo para actualizar la posicion, en milisegundos
            //Metros minimos para actualizar la posicion, en metros
            //Listener que saltará cuando se actualice la posicion
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locLis);

            Log.v("GmapFragment", "Listener introducido");
        } else {
            Toast.makeText(getActivity(),
                    "No tiene permisos para ejecutar la localización",
                    Toast.LENGTH_SHORT).show();
        }

        GestorParse gp = new GestorParse();
        gp.parseObject(this);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleRegion vRegion = googleMap.getProjection().getVisibleRegion();
                LatLng upperleft = vRegion.farLeft;
                LatLng lowerRight = vRegion.nearRight;
                Log.e("Coordenadas: " + upperleft.toString(),lowerRight.toString());
                Toast.makeText(getActivity(), "Ubicación guardada", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 225) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permisos de Geolocalización ACEPTADOS"
                        , Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

            } else {
                Toast.makeText(getActivity(), "Permisos de Geolocalización DENEGADOS "
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    LocationListener locLis = new LocationListener() {
        //Se ejecuta cuando recibimos un cambio de posicion
        @Override
        public void onLocationChanged(Location location) {
            Log.i("LocationListener", "Cambio de localización " + location);
        }

        //Cuando el proveedor cambia su estado
        //podemos tener
        //OUT_OF_SERVICE (0), TEMPORARILY_UNAVAILABLE(1) o AVAILABLE (2)
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //Se lanza cuando el proveedor se habilita
        @Override
        public void onProviderEnabled(String provider) {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            //TODO: Ver porqué es nulo

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location loc = locManager.getLastKnownLocation(provider);


            Log.i("LocationListener", "Provider enabled " + provider);
            if (loc != null) {
                Log.i("LocationListener", "Entrando en loc != null");
                double latitud = loc.getLatitude();
                double longitud = loc.getLongitude();
                LatLng marker = new LatLng(latitud, longitud);


                Log.i("GmapFragment", "Posicionando si loc es distinto de null");
                //ZOOM Inicial
                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(marker, 10);
                googleMap.moveCamera(cameraPosition);
                googleMap.animateCamera(cameraPosition);
                Log.i("GmapFragment", "Moviendo la Cámara");

            } else {
                Toast.makeText(getActivity(), "Localización Nula", Toast.LENGTH_SHORT).show();
            }

        }

        //lanzado cuando el porveedor se deshabilita
        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();


    }

    //---------------------PINTAR PUNTOS EN EL MAPA------------------------
    public void respuestaServidor(ArrayList<Servicio> array) {
        float color;
        for (int i = 0; i < array.size(); i++) {

            switch ((int) array.get(i).getPuntuacion()) {
                case 1:
                    color = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 2:
                    color = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 3:
                    color = BitmapDescriptorFactory.HUE_YELLOW;
                    break;
                case 4:
                    color = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case 5:
                    color = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                default:
                    color = BitmapDescriptorFactory.HUE_MAGENTA;


            }
            LatLng latLng = new LatLng(array.get(i).getLatitud(), array.get(i).getLongitud());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(array.get(i).getNombre())
                    .snippet("Puntuación: " + array.get(i).getPuntuacion())
                    .icon(BitmapDescriptorFactory.defaultMarker(color));
            googleMap.addMarker(markerOptions).setTag(array.get(i));
            googleMap.setOnInfoWindowClickListener(this);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(false);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        /*Intent intent = new Intent(getContext(),CuidadorFragment.class);
        Servicio servicio = (Servicio)marker.getTag();
        intent.putExtra("servicio",servicio);
        startActivity(intent);*/
        Servicio servicio = (Servicio)marker.getTag();
        CuidadorFragment cF = new CuidadorFragment();
        cF.passData(getActivity(),servicio);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, cF).addToBackStack(null).commit();
    }
    //TODO: Darle funcionalidad a la estrella del mapa.


}
