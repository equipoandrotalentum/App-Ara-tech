package android.ara_tech.es.aratech.gestion.login;

import android.ara_tech.es.aratech.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static android.R.attr.inputType;
import static android.support.v7.appcompat.R.id.end;
import static android.support.v7.appcompat.R.id.none;

public class RegistroActivity extends AppCompatActivity {

    private Button reg, btOjo;

    private EditText etUsuario, etEmail, etPass, etTelefono;
    String username,passwd,email,telefono;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        //Referenciamos los elementos del activity
        etUsuario=(EditText)findViewById(R.id.etusuario);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPass=(EditText)findViewById(R.id.etPass);
        etTelefono = (EditText)findViewById(R.id.etTelefono);
        btOjo = (Button) findViewById(R.id.btOjo);
        btOjo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etPass.requestFocus(end);
                etPass.setInputType(none);
            }
        });

        reg=(Button)findViewById(R.id.btnReg);

        reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                username = etUsuario.getText().toString();
                passwd = etPass.getText().toString();
                email = etEmail.getText().toString();
                telefono = etTelefono.getText().toString();

                //No se puede registrar con algún campo vacío.
                if (username.equals("") && passwd.equals("") && email.equals("") && telefono.equals("")){

                    Toast.makeText(RegistroActivity.this, "Por favor completa el formulario", Toast.LENGTH_SHORT).show();

                }else {
                    //Guardamos datos en Parse
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(passwd);
                    user.setEmail(email);
                    user.put("telefono",telefono);


                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Log.i("done","Entrando en done del registro");
                                Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistroActivity.this,InSesionActivity.class );
                                startActivity(intent);
                            }else {
                                String error =e.getMessage();
                                Toast.makeText(RegistroActivity.this, "Error en el registro "+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

}
