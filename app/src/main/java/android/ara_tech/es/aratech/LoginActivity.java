package android.ara_tech.es.aratech;

import android.ara_tech.es.aratech.gestion.login.GestorParse;
import android.ara_tech.es.aratech.gestion.login.InSesionActivity;
import android.ara_tech.es.aratech.gestion.login.RegistroActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    final List<String> permissions = Arrays.asList("public_profile", "email");
    boolean isParseInitialized = false;
    //Twitter
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "q53RvlZljShX9Ew70yTBv8baS";
    private static final String TWITTER_SECRET = "u0v543IvLoihDe25KjX082la3a6gGu3uq71cfGlsB7F0sjLfdH";
    private TwitterLoginButton loginButton;

    //--------------------------------Google
    private GoogleApiClient apiCliente;
    private SignInButton loginButtonGoogle;
    private final int RESPUESTA = 12;

    //---------------------------------Facebook
    //Cambiado a un botón normal para establecer el Login con Parse
    private Button loginButtonFacebook;
    //private CallbackManager callbackManager;

    //Carrusel de Imágenes
    //private ImageSwitcher imageSwitcher;

    private int[] gallery = { R.drawable.a, R.drawable.b, R.drawable.c,
            R.drawable.d, R.drawable.e, R.drawable.f };

    private int position;

    private static final Integer DURATION = 3000;

    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----------------------------------------Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Facebook
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //callbackManager = CallbackManager.Factory.create();

        AppEventsLogger.activateApp(this);

        //Iniciamos conexión con Parse
        if(isParseInitialized==false) {
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("aratechParseServer")
                .clientKey("aratechParseServer")
                .server("http://aratech-parse.herokuapp.com/parse")
                .build());
        isParseInitialized = true;
        }
        //-------------------------Inicializamos Facebook con Parse
        ParseFacebookUtils.initialize(this);
        //ParseTwitterUtils.initialize(TWITTER_KEY, TWITTER_SECRET);

        //Comprobamos el logeo manual.(Y en un principio también el de Facebook)
        GestorParse gParse = new GestorParse();

        gParse.comprobarLogeo(this);
        setContentView(R.layout.activity_login);



        //Referenciamos el botón de Twitter
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i("success","Entrando en método succes");
                // The TwitterSession is also available through:
                //Twitter.getInstance().core.getSessionManager().getActiveSession();
                TwitterSession session = result.data;
                //String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                //TODO: En la clase Gestor de Logins, que se comunicará con BBDD y guardará el Token y userId para realizar un nuevo registro.
                TwitterAuthToken token = session.getAuthToken();
                long userId = session.getUserId();
                String username = session.getUserName();

                //Una vez logeado pasamos a la siguiente actividad.
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.i("failure","Entrando en método failure");
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        //-----------------------------------------Google--------------------------------------------
        loginButtonGoogle = (SignInButton)findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiCliente = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(GOOGLE_SIGN_IN_API, gso).build();

        loginButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logueo = Auth.GoogleSignInApi.getSignInIntent(apiCliente);
                startActivityForResult(logueo, RESPUESTA);
            }
        });

        //-----------------------------------Facebook-----------------------------------------
        loginButtonFacebook = (Button)findViewById(R.id.fb_login_button);
        loginButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this,permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        /*imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(LoginActivity.this);
            }
        });*/

        // Set animations
        // https://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
        /*Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        if (timer != null) {
            timer.cancel();
        }
        position = 0;
        startSlider();*/

    }
       /* public void startSlider() {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    // avoid exception:
                    // "Only the original thread that created a view hierarchy can touch its views"
                    runOnUiThread(new Runnable() {
                        public void run() {
                            imageSwitcher.setImageResource(gallery[position]);
                            position++;
                            if (position == gallery.length) {
                                position = 0;
                            }
                        }
                    });
                }

            }, 0, DURATION);
        }*/

        // Stops the slider when the Activity is going into the background
        /*@Override
        protected void onPause() {
            super.onPause();
            if (timer != null) {
                timer.cancel();
            }
        }*/

        /*@Override
        protected void onResume() {
        super.onResume();
        if (timer != null) {
            startSlider();
        }

    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult","Entrando en onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        //Twitter
        loginButton.onActivityResult(requestCode, resultCode, data);
        //Google
        if (requestCode == RESPUESTA) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        //-------------------------------FACEBOOK---------------------------
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }
    //Método Google
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            //Login OK -> Show info
            GoogleSignInAccount gsa = result.getSignInAccount(); //getSignInAccount offer the user active account
            String tokenGoogle = gsa.getIdToken();
            String mailGoogle = gsa.getEmail();
        } else {
            //Login FAILED -> Show disconnected

        }
    }
    //TODO: Revisar método para que nos pase a la MainActivity si el usuario está logueado con google.
    /*@Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(apiCliente);
        if (opr.isDone()) {
            Log.i("isDone","Entra en isDone() de google");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    Log.i("isDone","Entra en onResult");
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }*/


    public void logeo(View view){
        Intent intent = new Intent(this, InSesionActivity.class);
        startActivity(intent);
    }
    //Mandamos a la pantalla de registro al pulsar en Registrame
    public void registro (View v){
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent);
    }
    /*public void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(apiCliente).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //signed out.
                    }
                });
    }*/




}
