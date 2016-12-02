package android.ara_tech.es.aratech.gestion.login;

import android.ara_tech.es.aratech.MainActivity;
import android.ara_tech.es.aratech.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import static android.support.v7.appcompat.R.id.end;
import static android.support.v7.appcompat.R.id.none;

public class InSesionActivity extends AppCompatActivity {

    Button loginButton, btMostrar;
    EditText usuarioLogin, usuarioPass;
    String uLogin,uPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_sesion);

        usuarioLogin = (EditText)findViewById(R.id.etUserLogin);
        usuarioPass = (EditText)findViewById(R.id.etPassLogin);
        btMostrar = (Button)findViewById(R.id.btOjo);
        loginButton = (Button) findViewById(R.id.btEntrar);

        btMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuarioPass.requestFocus(end);
                usuarioPass.setInputType(none);


            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uLogin = usuarioLogin.getText().toString();
                uPass = usuarioPass.getText().toString();

                //Eviamos datos a Parse para la verificación.
                ParseUser.logInInBackground(uLogin, uPass,
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user !=null){
                                    Intent intent = new Intent(
                                            InSesionActivity.this,
                                            MainActivity.class
                                    );
                                    startActivity(intent);
                                    Toast.makeText(InSesionActivity.this, "Logeo exitoso", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(InSesionActivity.this, "Usuario no existe, por favor regístrese", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
