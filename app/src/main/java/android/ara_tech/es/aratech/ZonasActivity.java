package android.ara_tech.es.aratech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ZonasActivity extends AppCompatActivity {
    ListView lvZonas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonas);
//TODO: Implementar en un fragmento y recogiendo datos de la funcionalidad de la estrella.
        lvZonas = (ListView)findViewById(R.id.lvZonas);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("C/ Bujalance");
        arrayList.add("C/ Arturo Soria");
        arrayList.add("Avenida de América");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, arrayList);
        lvZonas.setAdapter(arrayAdapter);
    }
}
