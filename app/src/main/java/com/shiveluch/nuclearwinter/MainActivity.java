package com.shiveluch.nuclearwinter;

import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, MediaPlayer.OnCompletionListener,GoogleApiClient.OnConnectionFailedListener {

    ImageView send, showmap, showme, search, centralbutton, showloc;
    EditText message, passcode;
    TextView t_map, t_showme, t_find, t_loc, t_enc, t_radio, t_inventory, persname, player;
    LinearLayout chatLL, encyclopedia, infoRL, locLL;
    RelativeLayout mapRL;
    Button contactapprove;
    GoogleMap mMap;
    float getZoom=17;
    boolean isPLAYING=false;
    boolean mapReady=false;
    String resDomain="http://a0568345.xsph.ru/winter/res/";
    String getchat = "";
    String getNewChat="";
    String city = "";
    String sellitem="";
    String selldesc="";
    String itemfield="";
    String invitemfield="";
    int itemquantity=0;
    int sellprice=0, buyprice=0;
    int contactID=0;
    int botcounter=0;
    int chatcounter=0;
    //BotMutant krovosos, snork, controler;
    BotMutant [] mutant;

    String getStalkers="http://a0568345.xsph.ru/winter/getstalkers.php";
    String getVersion="http://a0568345.xsph.ru/winter/getversion.php";
    String getLocs="";
    String getMonsters = "http://a0568345.xsph.ru/winter/getmonsters.php";
    String getCount="http://a0568345.xsph.ru/winter/getlast.php";
    String getaudio = "https://volcanorp.ru/tracks/getfilescount.php";
    String getCitiesLoc = "http://a0568345.xsph.ru/winter/getcities.php";
    String getUser="", getSellPrices="", getMoney="", getBuyPrices="", getinventory="", getinfquan="";
    String getperc="";
    int currentloc=0;
    int currentposition=0;
    int currentreceiver=0;
    int lastchatid=0;
    int tradeOperationCode=0;
    Button newcontact;
    Marker [] mutantMarker = new Marker[10];
    int timerCounter=0;

    SupportMapFragment mapFragment;
    ListView chat, stalkerslist, menu, info;
    Context context;
    Marker imhere;
    Marker stalker;
    ImageView [] shopitems;
    RelativeLayout [] playeritems;
    RelativeLayout [] invitems;
    TextView [] unitsquantity;
    TextView [] invunitsquantity;
    String email="", pass="", nick="";
    private int isMoney=0, tradequantity=0;
    SharedPreferences settings;
    public final String SETNAME="setname";
    public final String SETRADIO="radio";
    public final static String CITIES = "cities";
    public final static String LOCS = "locations";
    public final static String EMAIL = "email";
    public final static String PASSCODE = "passcode";
    public final static String NICK = "nick";
    public final static String ID = "id";
    public final static String START = "start";
    public final static String DISTANCE = "distance";
    public final static String WEAPON = "weapon";
    public final static String SUIT = "suit";

    private GoogleApiClient mGoogleApiClient;
    //ArrayList<Drawables> drawables = new ArrayList();
    ArrayList<Shopunits> shopunits = new ArrayList();
    ArrayList<PlayerUnits> playerunits = new ArrayList();
    ArrayList<Contacts> contacts=new ArrayList<>();
    ArrayList<Integer> weapons = new ArrayList<>();
    ArrayList<Integer> suits = new ArrayList<>();
    ArrayList<Mutants> mutants = new ArrayList<>();
    ArrayList<BotMutant> mutantbase = new ArrayList<>();
    ArrayList<Integer> mutantIcons=new ArrayList<>();
    ArrayList<Integer> mutantPics=new ArrayList<>();
    ArrayList<Integer> mutantSounds=new ArrayList<>();





    boolean giveLoc=true;
    int audiodata;
    LinearLayout bottompanel, enc, invLL;

    int[] stalkerpics={R.drawable.st1,R.drawable.st3,R.drawable.st2, R.drawable.st5,R.drawable.st6};
    String [] locnames ={"Кордон", "База Долга", "База Свободы", "Янтарь", "База Чистое небо"};

    String [] menuArray={"Персонажи", "Группировки", "Аномалии","Артефакты","Оружие" };
    String [] menyScriptsArray={"getpers", "getgroup", "getanomaly","getarts", "getweapon"};

ArrayList<String> alllocations=new ArrayList<>();


    final static String LOGTAG = "FIRE";
    ArrayList<Posts> posts = new ArrayList<Posts>();
    LocationManager locationManager;
    boolean GpsStatus = false;
    Location comLocation;
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    private Timer botMoveTimer;
    private BotTimerTask botTimerTask;


    LatLng position = new LatLng(66, 94);
    String lastID="0";
    boolean mode=true;
    boolean sendmessage=true;
    Activity activity;
    ArrayList<CityLoc> cityLocs = new ArrayList<>();
    Weapons weapon = new Weapons();
    Suits suit = new Suits();
    AlertDialog.Builder authalert;
    AlertDialog authdialog;

    AlertDialog.Builder conalert;
    AlertDialog condialog;
    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        activity=this;
        mutants.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 50);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 50);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        if (giveLoc)         showloc.setColorFilter(Color.argb(255,0,255,0));
        if (!giveLoc)         showloc.setColorFilter(Color.argb(255,255,0,0));

        checkEnabled();
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 3000, 1000);

        botMoveTimer = new Timer();
        botTimerTask = new BotTimerTask();
        botMoveTimer.schedule(botTimerTask, 1000, 5000);

        weapons = weapon.setWeapons();
        suits = suit.setSuits();

        //Log.d("Suit", "suit is "+suits.get(1));
        send = findViewById(R.id.send);
        send.setOnClickListener(v -> {

            if (currentreceiver==0)
            {
                setToast("Не выбран получатель");
                return;

            }
            String text = message.getText().toString();
            if (text.length()<2)
            {
                setToast("Недостаточная длина сообщения");
                return;
            }
          //  if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (!sendmessage)
            {
                setToast ("Нельзя отправлять сообщения слишком часто");
            }
            if (sendmessage)
            {
                double millis=System.currentTimeMillis();
                new uploadAsyncTask().execute("addpost", ""+millis, text, settings.getString(ID,""),""+currentreceiver);
                KeyHelper.get(this, "SHA1");
               // new uploadVolcAsyncTask().execute("addpost", KeyHelper.fpr, text, settings.getString(ID,""),""+currentreceiver,"","","","" );
            sendmessage=!sendmessage;
            message.setText("");}

        //}
        });

        getJSON(getMonsters);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==115)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pda);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        context = getApplicationContext();
        activity=this;

        getJSON(getVersion);



       // setToast("Проверка аутентификации");
        setShopUnits();
        KeyHelper.get(this,"SHA1");

   //     //Log.e("Statickey", KeyHelper.fpr);

     //   new uploadVolcAsyncTask().execute("loginauth",KeyHelper.fpr,"","","","","","","");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        getJSON(getaudio);
        settings = getSharedPreferences(SETNAME,Context.MODE_PRIVATE);
        email=settings.getString(EMAIL,"");
        pass=settings.getString(PASSCODE,"");
        nick=settings.getString(NICK,"");
        if (email.equals("")||pass.equals("")||nick.equals(""))
        {        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, 115);
        RelativeLayout mainRL=findViewById(R.id.mainRL);
        mainRL.setVisibility(View.GONE);
        }
        else
        { Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, 115);
            RelativeLayout mainRL=findViewById(R.id.mainRL);
            mainRL.setVisibility(View.VISIBLE);
       //     //Log.e("isStart", "IsData");
            new uploadVolcAsyncTask().execute("getcontact",KeyHelper.fpr,settings.getString(ID,""),"","","","","","");


        }


        String cities = settings.getString(CITIES,"");
        if (cities.length()==0)
        {
          //  //Log.d("cities", "noCities");
            getJSON(getCitiesLoc);}
        else {cityLocs.clear();setCityLocArray();}
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        comLocation = new Location(LocationManager.GPS_PROVIDER);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showloc=findViewById(R.id.showloc);

        chat = findViewById(R.id.chat);
        message = findViewById(R.id.message);
        player = findViewById(R.id.locinfo);
        passcode = findViewById(R.id.passcode);
        bottompanel=findViewById(R.id.bottompanel);
        showmap=findViewById(R.id.showmap);
        showme=findViewById(R.id.showme);
        search=findViewById(R.id.search);
        stalkerslist=findViewById(R.id.stalkerslist);
        t_map=findViewById(R.id.t_map);
        t_showme=findViewById(R.id.t_showme);
        t_find=findViewById(R.id.t_find);
        t_loc=findViewById(R.id.t_loc);
        t_inventory = findViewById(R.id.t_inventory);
        t_enc=findViewById(R.id.t_enc);
        t_radio=findViewById(R.id.t_radio);
        enc=findViewById(R.id.encyclopedia);
        menu=findViewById(R.id.menu);
        info=findViewById(R.id.info);
        bottompanel.setVisibility(View.GONE);
        newcontact = findViewById(R.id.newcontact);
        mapRL = findViewById(R.id.MapRL);
        infoRL=findViewById(R.id.infoRL);
        chatLL=findViewById(R.id.chatLL);
        encyclopedia = findViewById(R.id.encyclopedia);
        locLL=findViewById(R.id.locationLL);
        invLL = findViewById(R.id.invLL);

        contactlistselector();

        newcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDialog("");
            }
        });
        t_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t_radio.setText("Запускаю...");
                isPLAYING=!isPLAYING;
                if (isPLAYING) {
                    startPlay();

                }

                else
                {
                t_radio.setTextColor(Color.parseColor("#ff0000"));
                t_radio.setText("Радио OFF");
                releaseMP();
                }
            }
        });

t_inventory.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        getinventory = "https://volcanorp.ru/winter/getinventory.php/?nom="+email;
        getParametricJSON(getinventory, 10123,"","");
    }
});

        t_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               infoRL.setVisibility(View.GONE);
               chatLL.setVisibility(View.GONE);
               mapRL.setVisibility(View.GONE);
               locLL.setVisibility(View.GONE);
               invLL.setVisibility(View.GONE);
               enc.setVisibility(View.VISIBLE);
                mode=false;
                ArrayList<Menu> listmenu = new ArrayList<Menu>();

                for (int i=0;i<menuArray.length;i++)
                {
                    listmenu.add(new Menu(menuArray[i],menyScriptsArray[i]));
                }
                MenuAdapter menuAdapter=new MenuAdapter(context, listmenu,activity);
                menu.setAdapter(menuAdapter);
                getperc="http://a0568345.xsph.ru/winter/getpers.php";
                getJSON(getperc);
            }
        });


        t_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mapReady) return;
                infoRL.setVisibility(View.GONE);
                chatLL.setVisibility(View.GONE);
                mapRL.setVisibility(View.VISIBLE);
                locLL.setVisibility(View.GONE);
                invLL.setVisibility(View.GONE);
                enc.setVisibility(View.VISIBLE);
                mode=false;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position)
                        .zoom(getZoom)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
                mode=true;
            }
        });
        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mapReady) return;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position)
                        .zoom(getZoom)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);

            }
        });
        Activity activity=this;

        t_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoRL.setVisibility(View.GONE);
                chatLL.setVisibility(View.VISIBLE);
                mapRL.setVisibility(View.GONE);
                locLL.setVisibility(View.GONE);
                invLL.setVisibility(View.GONE);
                enc.setVisibility(View.GONE);
            }
        });
        showloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveLoc=!giveLoc;
                if (giveLoc)  showloc.setColorFilter(Color.argb(255,0,255,0));
                if (!giveLoc) showloc.setColorFilter(Color.argb(255,255,0,0));


//                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                }
//
//                //permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                }
            }
        });

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView script=view.findViewById(R.id.description);

          //      //Log.d("Menu",script.getText().toString()+".php");
                getperc="http://a0568345.xsph.ru/winter/"+script.getText().toString()+".php";
                getJSON(getperc);
            }
        });

        t_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.parseDouble(lat)<1)
                {
                    Toast.makeText(getApplicationContext(),"Координаты не определены", Toast.LENGTH_LONG).show();
                    return;}

                if (mode)
                {   infoRL.setVisibility(View.GONE);
                    chatLL.setVisibility(View.GONE);
                    mapRL.setVisibility(View.GONE);
                    locLL.setVisibility(View.VISIBLE);
                    invLL.setVisibility(View.GONE);
                    enc.setVisibility(View.GONE);
                bottompanel.setVisibility(View.GONE);
                //stalkerslist.setVisibility(View.VISIBLE);
                    GroupLocAdapter stalkersAdapter;
                    stalkersAdapter = new GroupLocAdapter(context, grouplocs, activity);
                    stalkerslist.setAdapter(stalkersAdapter);
                    stalkerslist.setVisibility(View.VISIBLE);


                }
                else setToast("Переключитесь в режим карты");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mapRL=findViewById(R.id.maplayout);
                mapRL.setVisibility(View.GONE);
                bottompanel.setVisibility(View.GONE);
                //stalkerslist.setVisibility(View.VISIBLE);
                getJSON(getStalkers);
            }
        });

        stalkerslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView stalkername=view.findViewById(R.id.infoname);
                TextView lat=view.findViewById(R.id.p_lat);
                TextView lon=view.findViewById(R.id.p_lon);
                String pass=stalkername.getText().toString();
                double stalkerLat, stalkerLon;
                stalkerLat=Double.parseDouble(lat.getText().toString());
                stalkerLon=Double.parseDouble(lon.getText().toString());
            //    //Log.d("position", ""+position);

if (stalkerLat<1) {
    Toast.makeText(getApplicationContext(),"Местонахождение неизвестно",Toast.LENGTH_LONG).show();
//    RelativeLayout mapRL=findViewById(R.id.maplayout);
//    mapRL.setVisibility(View.VISIBLE);
//    stalkerslist.setVisibility(View.GONE);
    bottompanel.setVisibility(View.VISIBLE);

    return;


}
                if (mapReady){
                    int dist=(int)grouplocs.get(position).distance;
                    if (dist>settings.getInt(DISTANCE,500))
                    {    if (stalker!=null) stalker.remove();
                    stalker=(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(stalkerLat,stalkerLon))
                            .title(pass)
                            .icon(getBitmapHighDescriptor(R.drawable.stalker))));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(stalkerLat,stalkerLon))
                            .zoom(getZoom)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate);
                    RelativeLayout mapRL=findViewById(R.id.MapRL);
                    mapRL.setVisibility(View.VISIBLE);
                    locLL.setVisibility(View.GONE);}
                    if (dist<=settings.getInt(DISTANCE,500))
                    {
                        setToast("Магазин");

                        getinventory = "https://volcanorp.ru/winter/getinventory.php/?nom="+email;
                        currentposition=position;
                        getParametricJSON(getinventory, position,"","");

                      //  showshopdialog(position);
                    }
                   // bottompanel.setVisibility(View.VISIBLE);
                }
            }
        });

t_showme.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (mode)
        {
            if(!mapReady) return;
            if(Double.parseDouble(lat)<1)
            {
                Toast.makeText(getApplicationContext(),"Координаты не определены", Toast.LENGTH_LONG).show();
                return;}
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(lat),Double.parseDouble(lon)))
                    .zoom(getZoom)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(cameraUpdate);

        }
        else setToast("Переключитесь в режим карты");
    }
});
        showme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mapReady) return;
                if(Double.parseDouble(lat)<1)
                {
                    Toast.makeText(getApplicationContext(),"Координаты не определены", Toast.LENGTH_LONG).show();
                    return;}
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(lat),Double.parseDouble(lon)))
                        .zoom(getZoom)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
            }
        });



//        message.setOnKeyListener((v, keyCode, event) -> {
//            boolean consumed = false;
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            if (keyCode == KEYCODE_NAVIGATE_NEXT) {
//
//                try {
//                    sendMessage(message.getText().toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                consumed = true; //это если не хотим, чтобы нажатая кнопка обрабатывалась дальше видом, иначе нужно оставить false
//            }
//
//
//            if (keyCode == KEYCODE_ENTER) {
//
//                try {
//                    sendMessage(message.getText().toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                consumed = true; //это если не хотим, чтобы нажатая кнопка обрабатывалась дальше видом, иначе нужно оставить false
//            }
//
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_DPAD_CENTER:
//                    case KeyEvent.KEYCODE_ENTER:
//                        // hide keyboard
//                        return true;
//                    default:
//                        break;
//                }
//            }
//
//            return consumed;
//        });

        chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView del=view.findViewById(R.id.p_passcode);
                String url=del.getText().toString();
                if (url.contains("http"))
                {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

                return false;
            }
        });

        chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView del=view.findViewById(R.id.p_id);
                String deleting=del.getText().toString();
                //     //Log.d("isDel",deleting);
                //int forDel=10000000;
                showDialog(deleting);
            }
        });


    }

    private void contactlistselector() {
        ListView contactlist = findViewById(R.id.contacts);
        contactlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                TextView recname=findViewById(R.id.receivername);
                TextView name = view.findViewById(R.id.contact);
                for (int i=0;i<contacts.size();i++)
                {
                    contacts.get(i).color=0;
                }

                contacts.get(pos).color=1;
                ContactAdapter adapter = new ContactAdapter(context,contacts,activity);
                contactlist.setAdapter(adapter);
                recname.setText("Получатель: "+ contacts.get(pos).name);
                currentreceiver=contacts.get(pos).id;
                //Log.d("id", ""+currentreceiver);

            }
        });
    }

    private void setShopUnits() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    AlertDialog.Builder shopalert;
    AlertDialog shopdialog;

    AlertDialog.Builder tradealert;
    AlertDialog tradedialog;
    private void showshopdialog(int position, ArrayList<String> inventory, String _money) {
        playerunits.clear();
        shopunits.clear();


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            shopalert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            shopalert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.shop,null);
        Button dismiss = view.findViewById(R.id.closeshop);
        TextView money=view.findViewById(R.id.plmoney);
        money.setText(_money+" RU");
        currentloc=position;
        shopunits.add(new Shopunits("Аптечка","medikits",R.id.shopmedikit));
        shopunits.add(new Shopunits("Бинт","bints",R.id.shopbint));
        shopunits.add(new Shopunits("Ремкомплект","remont",R.id.shoprepair));
        shopunits.add(new Shopunits("Антирад","antirad",R.id.shopantirad));
        shopunits.add(new Shopunits("Патроны для пистолета","pistolshells",R.id.shoppistolammo));
        shopunits.add(new Shopunits("Картечь","obrezshells",R.id.shopobrezammo));
        shopunits.add(new Shopunits("Патроны для АК74","kalashshells",R.id.shopkalashammo));
        shopunits.add(new Shopunits("Патроны для СВД","svdshells",R.id.shopsvdammo));
        shopunits.add(new Shopunits("ПМ","pistol",R.id.shoppistol));
        shopunits.add(new Shopunits("Обрез","obrez",R.id.shopobrez));
        shopunits.add(new Shopunits("АК-74","kalash",R.id.shopkalash));
        shopunits.add(new Shopunits("СВД","svd",R.id.shopsvd));
        shopunits.add(new Shopunits("Комбинезон СЕВА","seva",R.id.shopseva));
        shopunits.add(new Shopunits("Комбинезон ЗАРЯ","zaria",R.id.shopzaria));
        shopunits.add(new Shopunits("Костюм ЭКОЛОГ","ecolog",R.id.shopecolog));
        shopunits.add(new Shopunits("Колбаса","kolbasa",R.id.shopkolbasa));
        shopunits.add(new Shopunits("Тушенка","tushenka",R.id.shoptushenka));
        shopunits.add(new Shopunits("Батон","baton",R.id.shopbaton));
        shopunits.add(new Shopunits("Огненный шар","shar",R.id.shopshar));
        shopunits.add(new Shopunits("Душа","dusha",R.id.shopdusha));
        shopunits.add(new Shopunits("Лунный свет","moonlight",R.id.shopmoonlight));
        shopunits.add(new Shopunits("Каменный цветок","flower",R.id.shopflower));
        shopunits.add(new Shopunits("Пустышка","pust",R.id.shoppust));
        shopitems = new ImageView[shopunits.size()];
        for (int i = 0;i<shopunits.size();i++)
        {
            shopitems[i] = view.findViewById(shopunits.get(i).view);
            shopitems[i].setTag(i);
            int finalI = i;
            shopitems[i].setOnClickListener(view1 -> {
            //    //Log.d("Item is", ""+ shopitems[finalI].getTag()+", "+"field is "+shopunits.get(finalI).field);
                itemfield = shopunits.get(finalI).field;
                getSellPrices = "http://a0568345.xsph.ru/winter/getsellprice.php/get.php?nom="+(finalI+1)+"&dp="+currentloc;
            //    //Log.d("link", getSellPrices);
                getJSON(getSellPrices);
                shopdialog.dismiss();
            });

        }

        playerunits.add(new PlayerUnits("Аптечка","medikits",R.id.playermedikit, R.id.qplayermedikit));
        playerunits.add(new PlayerUnits("Бинт","bints",R.id.playerbint, R.id.qplayerbint));
        playerunits.add(new PlayerUnits("Ремкомплект","remont",R.id.playerrepair, R.id.qplayerrepair));
        playerunits.add(new PlayerUnits("Антирад","antirad",R.id.playerantirad, R.id.qplayerantiradq));
        playerunits.add(new PlayerUnits("Патроны для пистолета","pistolshells",R.id.playerpistolammo, R.id.qplayerpistolammo));
        playerunits.add(new PlayerUnits("Картечь","obrezshells",R.id.playerobrezammo, R.id.qplayerobrezammo));
        playerunits.add(new PlayerUnits("Патроны для АК74","kalashshells",R.id.playerkalashammo, R.id.qplayerkalashammo));
        playerunits.add(new PlayerUnits("Патроны для СВД","svdshells",R.id.playersvdammo, R.id.qplayersvdammo));
        playerunits.add(new PlayerUnits("ПМ","pistol",R.id.playerpistol, R.id.qplayerpistol));
        playerunits.add(new PlayerUnits("Обрез","obrez",R.id.playerobrez, R.id.qplayerobrez));
        playerunits.add(new PlayerUnits("АК-74","kalash",R.id.playerkalash, R.id.qplayerkalash));
        playerunits.add(new PlayerUnits("СВД","svd",R.id.playersvd, R.id.qplayersvd));
        playerunits.add(new PlayerUnits("Комбинезон СЕВА","seva",R.id.playerseva, R.id.qplayerseva));
        playerunits.add(new PlayerUnits("Комбинезон ЗАРЯ","zaria",R.id.playerzaria, R.id.qplayerzaria));
        playerunits.add(new PlayerUnits("Костюм ЭКОЛОГ","ecolog",R.id.playerecolog, R.id.qplayerecolog));
        playerunits.add(new PlayerUnits("Колбаса","kolbasa",R.id.playerkolbasa, R.id.qplayerkolbasa));
        playerunits.add(new PlayerUnits("Тушенка","tushenka",R.id.playertushenka, R.id.qplayertushenka));
        playerunits.add(new PlayerUnits("Батон","baton",R.id.playerbaton, R.id.qplayerbaton));
        playerunits.add(new PlayerUnits("Огненный шар","shar",R.id.playershar, R.id.qplayershar));
        playerunits.add(new PlayerUnits("Душа","dusha",R.id.playerdusha, R.id.qplayerdusha));
        playerunits.add(new PlayerUnits("Лунный свет","moonlight",R.id.playermoonlight, R.id.qplayermoonlite));
        playerunits.add(new PlayerUnits("Каменный цветок","flower",R.id.playerflower, R.id.qplayerflower));
        playerunits.add(new PlayerUnits("Пустышка","pust",R.id.playerpust, R.id.qplayerpust));

        playeritems = new RelativeLayout[playerunits.size()];
        unitsquantity = new TextView[playerunits.size()];
 //       //Log.e("units", ""+playerunits.size()+", "+inventory.size());
        for (int i = 0;i<playerunits.size();i++)
        {
            playeritems[i] = view.findViewById(playerunits.get(i).imageview);
            unitsquantity[i] = view.findViewById(playerunits.get(i).textview);
            if (Integer.parseInt(inventory.get(i))==0)
            {
                playeritems[i].setVisibility(View.GONE);
            }
            else
            {
                unitsquantity[i].setText("x"+inventory.get(i));
            }
            int finalI = i;
            playeritems[i].setOnClickListener(view1 -> {

                itemfield = playerunits.get(finalI).field;
                itemquantity = Integer.parseInt(unitsquantity[finalI].getText().toString().replace("x",""));
                getBuyPrices = "http://a0568345.xsph.ru/winter/getbuyprice.php/get.php?nom="+(finalI+1)+"&dp="+currentloc;
         //       //Log.d("link", getSellPrices);
                getJSON(getBuyPrices);
                shopdialog.dismiss();
            });

        }


        shopalert.setView(view);
        shopalert.setCancelable(false);

        shopdialog = shopalert.create();
        shopdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

dismiss.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        playerunits.clear();
        shopdialog.dismiss();
    }
});
        shopdialog.show();

    }

    private void tradeDialog(String _itemname, String _itemdesc, int _price, int _loc, String _itemfield,int _requestcode)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tradealert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            tradealert = new AlertDialog.Builder(this);
        }


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.tradedialog, null);
        TextView itemname = view.findViewById(R.id.itemname);
        TextView itemdesc = view.findViewById(R.id.itemdesc);
        TextView price = view.findViewById(R.id.price);
        Button approve = view.findViewById(R.id.tradeapprove);
        Button dismiss = view.findViewById(R.id.tradeclose);
        EditText quantity  =view.findViewById(R.id.tobuy);
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int text=quantity.length();
                if (text==0) tradequantity = 0; else
                tradequantity=Integer.parseInt(quantity.getText().toString());
                if (_requestcode==3 && tradequantity>itemquantity) {quantity.setText("0");tradequantity=0;}

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        itemname.setText(_itemname);
        itemdesc.setText(_itemdesc);
        price.setText("Цена продажи: "+_price);
        tradealert.setView(view);
        tradealert.setCancelable(false);
        tradedialog= tradealert.create();
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToast("Обработка операции");
                getMoney = "http://a0568345.xsph.ru/winter/getmoney.php/get.php?nom="+settings.getString(EMAIL,"");
                getinfquan = "http://a0568345.xsph.ru/winter/getinvquantity.php?nom="+email+"&dp="+_itemfield;
                if (_requestcode==1) getParametricJSON(getMoney,2,"","") ;
                if (_requestcode==2) getParametricJSON(getinfquan,0,quantity.getText().toString(),"") ;
                tradedialog.dismiss();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tradedialog.dismiss();
                getinventory = "https://volcanorp.ru/winter/getinventory.php/?nom="+email;
                getParametricJSON(getinventory, currentposition,"","");
            }
        });

       tradedialog.show();

    }

    private void contactDialog(String _email) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            conalert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            conalert = new AlertDialog.Builder(this);
        }


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.addcontact, null);
        EditText nickname = view.findViewById(R.id.perscode);
        persname = view.findViewById(R.id.persname);
        Button check = view.findViewById(R.id.checkcontact);
        Button dismiss = view.findViewById(R.id.dismisscontact);
        contactapprove = view.findViewById(R.id.approvecontact);
        conalert.setView(view);
        conalert.setCancelable(false);
        // mess.setText("Удалить сообщение?");
        condialog= conalert.create();

        contactapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new uploadVolcAsyncTask().execute("addcontact",KeyHelper.fpr,settings.getString(ID,""),""+contactID,"","","","","");
                new uploadVolcAsyncTask().execute("getcontact",KeyHelper.fpr,settings.getString(ID,""),"","","","","","");
                condialog.dismiss();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new uploadVolcAsyncTask().execute("getpass",KeyHelper.fpr,nickname.getText().toString(),"","","","","","");
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condialog.dismiss();
            }
        });

        condialog.show();
    }

    private void showAuthDialog(String _email) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            authalert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            authalert = new AlertDialog.Builder(this);
        }


            LayoutInflater inflater = getLayoutInflater();
             View view = inflater.inflate(R.layout.authinfodialog, null);
            EditText nickname = view.findViewById(R.id.nickname);
            EditText password = view.findViewById(R.id.pass);
            EditText email = view.findViewById(R.id.email);
            Button approve = view.findViewById(R.id.authapprove);
            Button dismiss = view.findViewById(R.id.buttonauthdismiss);
            email.setText(_email);
            email.setEnabled(false);
            authalert.setView(view);
            authalert.setCancelable(false);
            // mess.setText("Удалить сообщение?");
           authdialog= authalert.create();
           approve.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String _nick=nickname.getText().toString();
                   String _pass=password.getText().toString();
                   if (_nick.length()<4 || _pass.length()<4)
                   {
                       setToast("Недостаточная длина позывного или личного кода");
                       return;
                   }

                   String post="'"+_email+"','"+_nick+"','"+_pass+"',"+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+0+","+100+","+0+","+100+", "+2500;
                   new  uploadAsyncTask().execute("adduser", post,"","","","");
                   RelativeLayout mainRL = findViewById(R.id.mainRL);
                   mainRL.setVisibility(View.VISIBLE);

               }
           });

           dismiss.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   authdialog.dismiss();
               }
           });

        authdialog.show();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder updatealert;
        AlertDialog updatedialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           updatealert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            updatealert = new AlertDialog.Builder(this);
        }


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.updatedialog, null);

        updatealert.setView(view);
       updatealert.setCancelable(false);

        updatedialog= updatealert.create();


       updatedialog.show();
    }

    private void setCityLocArray() {
        String cities = settings.getString(CITIES,"");
        //if (!cities.contains("#")) return;
        cityLocs.clear();
        String []  citiesLevelOne = cities.split("#");
            for (int i=0;i<citiesLevelOne.length;i++)
            {
                String []  citiesLevelTwo = citiesLevelOne[i].split(":");

                     String city=citiesLevelTwo[0];
                     double lat = Double.parseDouble(citiesLevelTwo[1].replace(",","."));
                     double lon = Double.parseDouble(citiesLevelTwo[2].replace(",","."));
                     int id = Integer.parseInt(citiesLevelTwo[3]);
                     cityLocs.add(new CityLoc(city,lat,lon,id));

            }

//            for (int t=0;t<cityLocs.size();t++)
//            {
//                //Log.d("city", ""+t+": "+cityLocs.get(t).city+", id is "+cityLocs.get(t).id);
//            }

    }

    MediaPlayer mp;

    private void startPlay() {
            Random random=new Random();
            int rnd=random.nextInt(audiodata)+1;

            mp= null;
            mp = new MediaPlayer();
            try {
                String url = "http://volcanorp.ru/tracks/"+rnd+".ogg";
                mp.setDataSource(url);
                mp.prepare();
                mp.start();
                t_radio.setTextColor(Color.parseColor("#ff9600"));
                t_radio.setText("Радио ON");
           //     //Log.d("playing","Пытаюсь запустить "+url);
            } catch (IOException e) {

            }

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMP();
                    startPlay();
                }
            });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer!=null)
        {
            mTimer.cancel();
            mTimer=null;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //mMap.getUiSettings().setRotateGesturesEnabled(false);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(getZoom)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                String place = "";
                LatLng center=mMap.getCameraPosition().target;
                String centcoord = "Шир.: "+center.latitude+", Долг.: "+center.longitude;
                if (city.length()>0) place = ", "+city;
                else place=", Координаты не определены";
                player.setText(settings.getString(NICK,"")+place+"\n"+centcoord);
                getZoom=mMap.getCameraPosition().zoom;
            }
        });
        mapReady=true;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //ToDo Обработчик маркеров локаций
               Log.d ("tag", ""+marker.getTag());
               String [] splitting = (""+marker.getTag()).split(":");
               String info = mutants.get(Integer.parseInt(splitting[0])).info;
               Mutants isMutant = mutants.get(Integer.parseInt(splitting[0]));
               showMutantInfoDialog(isMutant, marker);
                return false;
            }
        });




    }

    public int sendMessage(String msg) throws IOException {
        URL url = new URL("https://api.telegram.org/bot2100186692:AAGn_uCeld3MuAa5_cYJ9gQtkjrfAu1HVr8/sendMessage");

        HttpURLConnection client = (HttpURLConnection) url.openConnection();
        client.setRequestMethod("POST");
        client.setRequestProperty("Content-Type", "application/json");
        client.setDoOutput(true);
        String body = "{\"chat_id\": \"-1001641714783\", \"text\": \"" + msg + "\"}";
        String true_body = new String(body.getBytes("UTF-8"), "CP1251");
        OutputStream stream = client.getOutputStream();
        stream.write(true_body.getBytes("CP1251"), 0, true_body.length());
        stream.flush();
        stream.close();
        client.connect();
        return client.getResponseCode();
    }

    String domain = "http://a0568345.xsph.ru/winter/";
    String volcDomain = "https://volcanorp.ru/winter/";



    private void releaseMP() {
        if (mp != null) {
            try {
                mp.release();
                mp = null;
            //    startPlay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
     //   //Log.d("stopPlay", "Остановка проигрывания");
        releaseMP();
        startPlay();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class uploadAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3], params[4]);
            return null;
        }

        protected void onPostExecute(Double result) {
           // //Log.d("result", ""+result);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String isUri, String post1, String post2, String post3, String post4) {
            HttpClient httpclient = new DefaultHttpClient();
            String getUri = String.format(domain + "%s.php", isUri);
            ResponseHandler<String> res = new BasicResponseHandler();
      //      //Log.e("Orientation", ""+getResources().getConfiguration().orientation);
            System.out.println(getUri + ", " + post1 + ", " + post2 + ", " + post3+ ", " + post4);
            HttpPost httppost = new HttpPost(getUri);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("post1", post1));
                nameValuePairs.add(new BasicNameValuePair("post2", post2));
                nameValuePairs.add(new BasicNameValuePair("post3", post3));
                nameValuePairs.add(new BasicNameValuePair("post4", post4));
//                nameValuePairs.add(new BasicNameValuePair("event",event ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//                HttpResponse response = httpclient.execute(httppost);
//                System.out.println(response);
                String resp = httpclient.execute(httppost, res);
                System.out.println("resp is:" + resp);
                if (isUri.equals("adduser"))
                {
                  authdialog.dismiss();
                }


            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }


    private class uploadVolcAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
            return null;
        }

        protected void onPostExecute(Double result) {
         //   //Log.d("result", ""+result);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String isUri, String post1, String post2, String post3, String post4, String post5, String post6, String post7, String post8) {
            HttpClient httpclient = new DefaultHttpClient();
            String getUri = String.format(volcDomain + "%s.php", isUri);
        //    //Log.e("VolcAsync", getUri + ", " + post1 + ", " + post2 + ", " + post3+ ", " + post4);
            ResponseHandler<String> res = new BasicResponseHandler();
            HttpPost httppost = new HttpPost(getUri);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("post1", post1));
                nameValuePairs.add(new BasicNameValuePair("post2", post2));
                nameValuePairs.add(new BasicNameValuePair("post3", post3));
                nameValuePairs.add(new BasicNameValuePair("post4", post4));
                nameValuePairs.add(new BasicNameValuePair("post5", post5));
                nameValuePairs.add(new BasicNameValuePair("post6", post6));
                nameValuePairs.add(new BasicNameValuePair("post7", post7));
                nameValuePairs.add(new BasicNameValuePair("post8", post8));
//                nameValuePairs.add(new BasicNameValuePair("event",event ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
     //           HttpResponse response = httpclient.execute(httppost);
                String resp = httpclient.execute(httppost, res);
                System.out.println(resp);
                if (isUri.equals("adduser"))
                {
                    authdialog.dismiss();
                }

                if (isUri.equals("getcontact") && resp.length()>5)
                {
                    loadcontact(resp.replace("json",""));
                }

                if (isUri.equals("getchat") && resp.length()>5)
                {
                    loadChat(resp.replace("json",""));
                }

                if (isUri.equals("getpass") && resp.length()>5)
                {
                    loadUserInfo(resp.replace("json",""));
                }

                if (isUri.equals("addcontact"))
                {


                }


            } catch (IOException | JSONException e) {
            }

        }
    }


    public void getJSON(final String urlWebService) {
        //Log.e("script", urlWebService);
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                if (s==null)
                {return;}

                if (s != null) {
                 //   //Log.d("taken S", s);

                    try {

                        if (urlWebService == getaudio) {loadaudio(s);return;}

                        if (urlWebService == getCitiesLoc) {loadCities(s);return;}

                        if (urlWebService == getLocs) {loadLocations(s);return;}

                        if (urlWebService == getUser) {loadUser(s);return;}

                     //   if (urlWebService == getchat) {loadChat(s);return;}
                        if (urlWebService == getCount)
                            {loadCount(s);return;}
                        if (urlWebService == getStalkers)
                        {loadStalkers(s);return;}
                        if (urlWebService == getperc)
                        {loadperc(s);return;}
                        if (urlWebService == getSellPrices)
                        {loadSellPrices(s);return;}

                        if (urlWebService == getBuyPrices)
                        {loadBuyPrices(s);return;}

                        if (urlWebService == getVersion)
                        {loadVersion(s);return;}
                        if (urlWebService == getMonsters)
                        {loadMonsters(s);return;}



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            protected String doInBackground(Void... voids) {


                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    Log.d("setjson", "a time "+urlWebService);
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

//
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    public void getParametricJSON (final String urlWebService, int requestCode, String par1, String par2) {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s==null)
                {return;}

                if (s != null) {
                   // //Log.d("taken parametric S", s);

                    try {

                       if (urlWebService == getMoney)
                            loadMoney(s,requestCode);
                        if (urlWebService == getinventory)
                            loadInventory(s,requestCode);
                        if (urlWebService == getinfquan)
                            loadInvQuan(s, par1);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            protected String doInBackground(Void... voids) {


                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }


        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            botcounter++;
            chatcounter++;
           // if (city.length()>0 && mutant==null)
               // setMutants();

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (chatcounter>10)
                    {new uploadVolcAsyncTask().execute("getchat", KeyHelper.fpr, settings.getString(ID,""), ""+ lastchatid,"","","","","" );
                    getJSON(getCount);
                    sendmessage=true;
                    chatcounter=0;
                    }

                    if (botcounter>1)
                    {
                        if (mapReady && mutant!=null) {
                            for (int i=0;i<mutantMarker.length;i++)
                            {
                            if (mutantMarker[i] != null)
                                mutantMarker[i].remove();
                            if (mutant[i]!=null)
                            {mutantMarker[i] = (mMap.addMarker(new MarkerOptions()
                                        .position(mutant[i].getPosition())
                                        .title(mutantbase.get(i).mutantName)
                                        .icon(getBitmapMutantDescriptor(mutantbase.get(i).drawmarker))));
                                mutantMarker[i].setTag(""+mutantbase.get(i).id+":"+i);
                                Log.d(mutantbase.get(i).mutantName+" "+i, ""+mutant[i].getDistance());}
                            }

                        }
                        botcounter=0;
                    }

                }
            });
        }

    }


    class BotTimerTask extends TimerTask {

        @Override
        public void run() {


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                   // krovosos.getPosition();
//                    if (mapReady) {
//                        if (mutantMarker[0] != null)
//                            mutantMarker[0].remove();
//                        if (krovosos != null) {
//                            mutantMarker[0] = (mMap.addMarker(new MarkerOptions()
//                                    .position(krovosos.getPosition())
//                                    .title("Кровосос")
//                                    .icon(getBitmapMutantDescriptor(R.drawable.krovosos))));
//                        }
//                    }


                }
            });
        }

    }

    private void loadChat(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        final ChatAdapter[] chatAdapter = new ChatAdapter[1];
        //posts.clear();
     //   //Log.d("MPdata", ""+mp);

        if (jsonArray.length() == 0) { Log.d ("data", "No data");
//            chatAdapter[0] = new ChatAdapter(this, posts, this);
//            chat.setAdapter(chatAdapter[0]);
            return;
        }
        String take = "";
        String[] id_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];
        String[] player_s = new String[jsonArray.length()];
        String[] text_s = new String[jsonArray.length()];



        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                String id = obj.getString("id");
                String text = obj.getString("text");
                String timest = obj.getString("timest");
                String scode = obj.getString("scode");
                String sender = obj.getString("sender");
                String receiver = obj.getString("receiver");
                int scoder= Integer.parseInt(scode);
                int isID=Integer.parseInt(settings.getString(ID,""));
                //Log.e("scoder",Integer.parseInt(scode)+", "+Integer.parseInt(settings.getString(ID,"")));
                if (receiver!=null) {
                 //   if (scoder ==isID )
                        posts.add(new Posts(id_s[i], sender + "->" + receiver, text, timest, 0));
//                    if (scoder!=isID)
//                        posts.add(new Posts(id_s[i], receiver + "->" + sender, text, timest, 0));
                }
                lastchatid=Integer.parseInt(id);

            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    chatAdapter[0] = new ChatAdapter(context, posts, activity);
                    chat.setAdapter(chatAdapter[0]);
                    Audio audio = new Audio(getApplicationContext());
                    audio.playClick();

                }
            });






        }


    }


    private void loadUserInfo(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) { Log.d ("data", "No data");
            return;
        }
        if (jsonArray.length() > 0) {

//
                JSONObject obj = jsonArray.getJSONObject(0);
                contactID=Integer.parseInt(obj.getString("id"));

                String name=obj.getString("name");

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    persname.setText("Контакт: "+name);
                    for (int i=0;i<contacts.size();i++)
                    {
                        if (contactID==contacts.get(i).id)
                        {
                            setToast ("Пользователь уже в списке контактов");
                            return;
                        }

                    }
                    contactapprove.setEnabled(true);
                    contactapprove.setAlpha(1);

                }
            });

        }


    }

    private void loadaudio(String json) throws JSONException {

        if (json.contains("co# "))
        {
            audiodata = Integer.parseInt(json.replace("co# ",""))-3;
        }

    }

    private void loadCities(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        cityLocs.clear();
        if (jsonArray.length() == 0) { Log.d ("Cities", "No cities");
            return;
        }
        String take = "";
        String[] ids = new String[jsonArray.length()];
        String[] cities = new String[jsonArray.length()];
        String[] lats = new String[jsonArray.length()];
        String[] lons = new String[jsonArray.length()];




        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                ids[i] = obj.getString("id");
                cities[i] = obj.getString("city");
                lats[i] = obj.getString("latitude");
                lons[i] = obj.getString("longitude");
                take=take+cities[i]+":"+lats[i]+":"+lons[i]+":"+ids[i]+"#";


            }
            SharedPreferences.Editor editor=settings.edit();
            editor.putString(CITIES,take);
            editor.apply();
            {setCityLocArray();}


        }


    }

    private void loadcontact(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        contacts.clear();
        if (jsonArray.length() == 0) {
           // setToast("Контакты не найдены");
            return;
        }
        if (jsonArray.length()>0)
        { for (int i=0;i<jsonArray.length();i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String name = obj.getString("name");
            String id = obj.getString("id");
            //Log.e("contacts", ""+id+", " +name);
            if (name!=null && name !="null")
            contacts.add(new Contacts(Integer.parseInt(obj.getString("id")),name,0));
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    ContactAdapter adapter = new ContactAdapter(context,contacts,activity);
                    ListView contactlist=findViewById(R.id.contacts);
                    contactlist.setAdapter(adapter);

                }
            });


        }
    }


    private void loadUser(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() == 0) {
            setToast("Пользователь не найден");
            RelativeLayout mainRL=findViewById(R.id.mainRL);
            mainRL.setVisibility(View.GONE);
            showAuthDialog(acct.getEmail());
        }
        if (jsonArray.length()>0)
        { setToast("Пользователь найден");
            JSONObject obj = jsonArray.getJSONObject(0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(EMAIL,acct.getEmail());
            editor.putString(NICK, obj.getString("nick"));
            editor.putString(PASSCODE,obj.getString("pass"));
            editor.putString(ID,obj.getString("id"));
            editor.apply();
            //Log.e("getID", settings.getString(ID,""));
            isMoney = Integer.parseInt(obj.getString("fmon"));
          //  //Log.d("Money", ""+isMoney);
            RelativeLayout mainRL = findViewById(R.id.mainRL);
            mainRL.setVisibility(View.VISIBLE);
            String place = "";
            LatLng center=mMap.getCameraPosition().target;
            String centcoord = "Шир.: "+center.latitude+", Долг.: "+center.longitude;
            if (city.length()>0) place = ", "+city;
            else place="Координаты не определены";
            player.setText(settings.getString(NICK,"")+place+"\n"+centcoord);
            new uploadVolcAsyncTask().execute("getcontact",KeyHelper.fpr,settings.getString(ID,""),"","","","","","");
            int start=settings.getInt(START,0);
            //Log.e("isStart",""+start);
            if (start==0)
            {
                infoRL.setVisibility(View.VISIBLE);
                Audio audio = new Audio(getApplicationContext());
                audio.playStart();
                editor.putInt(START,1);
                editor.apply();

            }

        }
    }

    ArrayList<GroupLocations> grouplocs = new ArrayList<>();
    private void loadLocations(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() == 0) { Log.d ("LocsData", "No data");

            return;
        }
        String take = "";

        String[] alllocs = new String[locnames.length];

        if (jsonArray.length() > 0) {
            if (mutant==null && mutants.size()>0)
                setMutants();

            grouplocs.clear();
                JSONObject obj = jsonArray.getJSONObject(0);
                alllocs[0] = obj.getString("cordon");
                alllocs[1] = obj.getString("dolg");
                alllocs[2] = obj.getString("svoboda");
                alllocs[3] = obj.getString("science");
                alllocs[4] = obj.getString("clearsky");
                    for (int i=0;i<locnames.length;i++)
                    {
                        alllocs[i] = alllocs[i].replace(",", ".");
                        String [] locposition = alllocs[i].split(":");
                        double locLatitude = Double.parseDouble(locposition[0]);
                        double locLongitude = Double.parseDouble(locposition[1]);
                        Location destLocation=new Location("");
                        destLocation.setLatitude(locLatitude);
                        destLocation.setLongitude(locLongitude);
                        double dist=comLocation.distanceTo(destLocation);
                       // Log.d ("DistancebyGM", locnames[i]+", "+dist);
//                        double distance=distanceInKmBetweenEarthCoordinates(comLocation.getLatitude(),comLocation.getLongitude(), locLatitude, locLongitude);
//                        Log.d ("DistancebyScript", locnames[i]+", "+distance*1000);
                        int d=(int)dist;
                        grouplocs.add(new GroupLocations(locnames[i]+" ("+d+" м.)", "", stalkerpics[i], locposition[0],locposition[1],dist));
                    }
        }
    }


    private void loadStalkers(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        StalkersAdapter stalkersAdapter;
        ArrayList<Stalkers> stalkers = new ArrayList<Stalkers>();
        if (jsonArray.length() == 0) { Log.d ("data", "No data");

            return;
        }
        String take = "";

        String[] player_s = new String[jsonArray.length()];
        String[] passcode_s = new String[jsonArray.length()];
        String[] lat_s = new String[jsonArray.length()];
        String[] lon_s = new String[jsonArray.length()];



        if (jsonArray.length() > 0 && jsonArray.length()==stalkerpics.length) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);

                player_s[i] = obj.getString("player");
                passcode_s[i] = obj.getString("passcode");
                lat_s[i] = obj.getString("lat");
                lon_s[i] = obj.getString("lon");

                stalkers.add(new Stalkers( player_s[i], passcode_s[i], stalkerpics[i], lat_s[i],lon_s[i]));


            }
            stalkersAdapter = new StalkersAdapter(this, stalkers, this);
            stalkerslist.setAdapter(stalkersAdapter);
            stalkerslist.setVisibility(View.VISIBLE);


        }


    }

    private void loadperc(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        InfoAdapter infoAdapter;
        ArrayList<Info> isInfo = new ArrayList<Info>();
        if (jsonArray.length() == 0) { Log.d ("data", "No data");

            return;
        }
        String take = "";

        String[] name_s = new String[jsonArray.length()];
        String[] description_s = new String[jsonArray.length()];
        String[] image_s = new String[jsonArray.length()];



        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);

                name_s[i] = obj.getString("name");
                description_s[i] = obj.getString("description");
                image_s[i] = obj.getString("image");

                isInfo.add(new Info( name_s[i], description_s[i], image_s[i]));
           //     //Log.d("Info", name_s[i]);


            }
            infoAdapter = new InfoAdapter(this, isInfo, this);
            info.setAdapter(infoAdapter);


        }


    }

    private void loadVersion(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
               if (jsonArray.length() == 0) { Log.d ("data", "No data");
            return;
        }



        if (jsonArray.length() > 0) {

            JSONObject obj = jsonArray.getJSONObject(0);
            int version=Integer.parseInt(obj.getString("v"));
            int curver=Integer.parseInt(getResources().getString(R.string.version));
            SharedPreferences.Editor editor=settings.edit();
            editor.putInt(DISTANCE,Integer.parseInt(obj.getString("dist")));
            editor.apply();
            if (version!=curver)
            {
                RelativeLayout mainRL=findViewById(R.id.mainRL);
                mainRL.setVisibility(View.GONE);
                showUpdateDialog();
                return;
            }

        }


    }

    private void loadMonsters(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) { Log.d ("data", "No data");
            return;
        }



        if (jsonArray.length() > 0) {
            mutantIcons.clear();
            mutantPics.clear();
            mutants.clear();
            mutantIcons.add(R.drawable.krovosos);
            mutantIcons.add(R.drawable.snork);
            mutantIcons.add(R.drawable.controler);
            mutantPics.add(R.drawable.krovososbig);
            mutantPics.add(R.drawable.snorkbig);
            mutantPics.add(R.drawable.kontrolerbig);
            mutantSounds.add(R.raw.krovososattack);
            mutantSounds.add(R.raw.snorkattack);
            mutantSounds.add(R.raw.controllerattack);


            for (int i=0;i<jsonArray.length();i++) {
    JSONObject obj = jsonArray.getJSONObject(i);
    String name = obj.getString("name");
    int distance = obj.getInt("distance");
    int hits = obj.getInt("hits");
    int cost = obj.getInt("cost");
    int resourse = obj.getInt("icon");
    int power=obj.getInt("power");
    String info = obj.getString("info");
    Log.d("NameFromBase", name);
    mutants.add(new Mutants(name, hits, distance,cost,
            power,mutantIcons.get(i),i, info, mutantSounds.get(i), mutantPics.get(i)));

}


        }


    }

    private void loadMoney(String json, int requestCode) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) { Log.d ("data", "No data");
            return;
        }

        if (jsonArray.length() > 0) {
            JSONObject obj = jsonArray.getJSONObject(0);
            isMoney = Integer.parseInt(obj.getString("fmon"));
         //   //Log.d("requestCode",""+requestCode);
            if (requestCode==1) tradeDialog(sellitem, selldesc, sellprice, currentloc, itemfield, 1);
            if (requestCode==3) tradeDialog(sellitem, selldesc, buyprice, currentloc, itemfield, 2);

            if (requestCode==2)
            {
                int price = sellprice*tradequantity;
                if (price>isMoney)
                {
                    setToast("Недостаточно средств");
                    return;
                }
                else
                {
                    KeyHelper.get(this, "SHA1");
                    new uploadVolcAsyncTask().execute("updateinv", KeyHelper.fpr, itemfield, ""+tradequantity,email,"","","","" );
                    new uploadVolcAsyncTask().execute("removemoney", KeyHelper.fpr, ""+price,email,"","","","","");

                }
                getinventory = "https://volcanorp.ru/winter/getinventory.php/?nom="+email;
                getParametricJSON(getinventory, currentposition,"","");

            }

//            if (requestCode==4)
//            {
//
////                    new uploadVolcAsyncTask().execute("updateinv", KeyHelper.fpr, itemfield, ""+(0-tradequantity),email,"","","","" );
////                    new uploadVolcAsyncTask().execute("removemoney", KeyHelper.fpr, ""+(0-price),email,"","","","","");
//
//
//            }
        }


    }


    private void loadSellPrices(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) { Log.d ("data", "No data");

            return;
        }

        if (jsonArray.length() > 0) {
              JSONObject obj = jsonArray.getJSONObject(0);

            sellitem = obj.getString("name");
            selldesc = obj.getString("descript");
            sellprice = Integer.parseInt(obj.getString("fmon"));
            getMoney = "http://a0568345.xsph.ru/winter/getmoney.php/get.php?nom="+settings.getString(EMAIL,"");
            getParametricJSON(getMoney,1,"","");
        }


    }

    private void loadBuyPrices(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) { Log.d ("data", "No data");

            return;
        }

        if (jsonArray.length() > 0) {
            JSONObject obj = jsonArray.getJSONObject(0);

            sellitem = obj.getString("name");
            selldesc = obj.getString("descript");
            buyprice = Integer.parseInt(obj.getString("fmon"));
            getMoney = "http://a0568345.xsph.ru/winter/getmoney.php/get.php?nom="+settings.getString(EMAIL,"");
            getParametricJSON(getMoney,3,"","");
        }


    }

    private void loadInvQuan(String json, String quantity) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

            return;
        }

        if (jsonArray.length() > 0) {
            JSONObject obj = jsonArray.getJSONObject(0);
            int realquan=Integer.parseInt(quantity);
            int getquan=Integer.parseInt(obj.getString("fmon"));
            if (getquan<realquan)
            {
                setToast("Требуемое количество отсутствует");
                return;
            }

            else
            {   int price = buyprice*realquan;
                KeyHelper.get(this, "SHA1");
                new uploadVolcAsyncTask().execute("updateinv", KeyHelper.fpr, itemfield, ""+(0-realquan),email,"","","","" );
                new uploadVolcAsyncTask().execute("removemoney", KeyHelper.fpr, ""+(0-price),email,"","","","","");
            }

            getinventory = "https://volcanorp.ru/winter/getinventory.php/?nom="+email;
            getParametricJSON(getinventory, currentposition,"","");

        }


    }


    private void loadInventory(String json, int requestcode) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) { Log.d ("inv", "No JSON");

            return;
        }

        if (jsonArray.length() > 0) {
            ArrayList<String> inventory = new ArrayList<>();
            JSONObject obj = jsonArray.getJSONObject(0);
            inventory.add(obj.getString("medikits"));
            inventory.add(obj.getString("bints"));
            inventory.add(obj.getString("remont"));
            inventory.add(obj.getString("antirad"));
            inventory.add(obj.getString("pistolshells"));
            inventory.add(obj.getString("obrezshells"));
            inventory.add(obj.getString("kalashshells"));
            inventory.add(obj.getString("svdshells"));
            inventory.add(obj.getString("pistol"));
            inventory.add(obj.getString("obrez"));
            inventory.add(obj.getString("kalash"));
            inventory.add(obj.getString("svd"));
            inventory.add(obj.getString("seva"));
            inventory.add(obj.getString("zaria"));
            inventory.add(obj.getString("ecolog"));
            inventory.add(obj.getString("kolbasa"));
            inventory.add(obj.getString("tushenka"));
            inventory.add(obj.getString("baton"));
            inventory.add(obj.getString("shar"));
            inventory.add(obj.getString("dusha"));
            inventory.add(obj.getString("moonlight"));
            inventory.add(obj.getString("flower"));
            inventory.add(obj.getString("pust"));
            String money=obj.getString("money");
            int weapon = Integer.parseInt(obj.getString("weapon"));
            int suit = Integer.parseInt(obj.getString("suit"));
            SharedPreferences.Editor editor=settings.edit();
            editor.putInt(WEAPON,weapon);
            editor.putInt(SUIT,suit);
            editor.apply();
            if (requestcode<5000) {
                showshopdialog(requestcode, inventory, money);
                if (currentloc == 0) {
                    Audio audio = new Audio(getApplicationContext());
                    audio.playSidor();
                }
                 else
                {
            Audio audio = new Audio(getApplicationContext());
            audio.playGreeting();}

            }
            if (requestcode==10123)
            {
                    showInventory(inventory,money, weapon, suit);
            }
        }


    }

    private void showInventory(ArrayList<String> inventory, String _money, int _weapon, int _suit) {
        playerunits.clear();

      //  //Log.d("inventory", inventory.get(0));

        playerunits.add(new PlayerUnits("Аптечка","medikits",R.id.invmedikit, R.id.qinvmedikit));
        playerunits.add(new PlayerUnits("Бинт","bints",R.id.invbint, R.id.qinvbint));
        playerunits.add(new PlayerUnits("Ремкомплект","remont",R.id.invrepair, R.id.qinvrepair));
        playerunits.add(new PlayerUnits("Антирад","antirad",R.id.invantirad, R.id.qinvantiradq));
        playerunits.add(new PlayerUnits("Патроны для пистолета","pistolshells",R.id.invpistolammo, R.id.qinvpistolammo));
        playerunits.add(new PlayerUnits("Картечь","obrezshells",R.id.invobrezammo, R.id.qinvobrezammo));
        playerunits.add(new PlayerUnits("Патроны для АК74","kalashshells",R.id.invkalashammo, R.id.qinvkalashammo));
        playerunits.add(new PlayerUnits("Патроны для СВД","svdshells",R.id.invsvdammo, R.id.qinvsvdammo));
        playerunits.add(new PlayerUnits("ПМ","pistol",R.id.invpistol, R.id.qinvpistol));
        playerunits.add(new PlayerUnits("Обрез","obrez",R.id.invobrez, R.id.qinvobrez));
        playerunits.add(new PlayerUnits("АК-74","kalash",R.id.invkalash, R.id.qinvkalash));
        playerunits.add(new PlayerUnits("СВД","svd",R.id.invsvd, R.id.qinvsvd));
        playerunits.add(new PlayerUnits("Комбинезон СЕВА","seva",R.id.invseva, R.id.qinvseva));
        playerunits.add(new PlayerUnits("Комбинезон ЗАРЯ","zaria",R.id.invzaria, R.id.qinvzaria));
        playerunits.add(new PlayerUnits("Костюм ЭКОЛОГ","ecolog",R.id.invecolog, R.id.qinvecolog));
        playerunits.add(new PlayerUnits("Колбаса","kolbasa",R.id.invkolbasa, R.id.qinvkolbasa));
        playerunits.add(new PlayerUnits("Тушенка","tushenka",R.id.invtushenka, R.id.qinvtushenka));
        playerunits.add(new PlayerUnits("Батон","baton",R.id.invbaton, R.id.qinvbaton));
        playerunits.add(new PlayerUnits("Огненный шар","shar",R.id.invshar, R.id.qinvshar));
        playerunits.add(new PlayerUnits("Душа","dusha",R.id.invdusha, R.id.qinvdusha));
        playerunits.add(new PlayerUnits("Лунный свет","moonlight",R.id.invmoonlight, R.id.qinvmoonlite));
        playerunits.add(new PlayerUnits("Каменный цветок","flower",R.id.invflower, R.id.qinvflower));
        playerunits.add(new PlayerUnits("Пустышка","pust",R.id.invpust, R.id.qinvpust));
        ImageView invweapon=findViewById(R.id.invweapon);
        ImageView invsuit=findViewById(R.id.invsuit);
        ImageView detector = findViewById(R.id.detector);
        //TextView money=findViewById()
        TextView money=findViewById(R.id.invmoney);
        money.setText(_money+" RU");

        invweapon.setImageResource(weapons.get(_weapon));
        invsuit.setImageResource(suits.get(_suit));

        invsuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSuit(suits.get(0),"0");
            }
        });

        detector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("detector","isDetector");
                Audio audio= new Audio(getApplicationContext());
                audio.playDetector();

            }
        });






        invitems = new RelativeLayout[playerunits.size()];
        invunitsquantity = new TextView[playerunits.size()];
        for (int i = 0;i<playerunits.size();i++)
        {
            invitems[i] = findViewById(playerunits.get(i).imageview);
            invunitsquantity[i] = findViewById(playerunits.get(i).textview);
            if (Integer.parseInt(inventory.get(i))==0)
            {
                invitems[i].setVisibility(View.GONE);
            }
            else
            {
                invunitsquantity[i].setText("x"+inventory.get(i));
            }
            int finalI = i;
            invitems[i].setOnClickListener(view1 -> {
                String dataweapon;
                invitemfield = playerunits.get(finalI).field;
                if (finalI==8) setWeapon(weapons.get(0),"0"); //PM
                if (finalI==9) setWeapon(weapons.get(1),"1");//Obrez
                if (finalI==10) setWeapon(weapons.get(2),"2");//kalak
                if (finalI==11) setWeapon(weapons.get(3),"3");//svd
                if (finalI==12) setSuit(suits.get(1),"1"); //zaria
                if (finalI==13) setSuit(suits.get(2),"2");//seva
                if (finalI==14) setSuit(suits.get(3),"3");//

            });

        }


        infoRL.setVisibility(View.GONE);
        chatLL.setVisibility(View.GONE);
        mapRL.setVisibility(View.GONE);
        locLL.setVisibility(View.GONE);
        invLL.setVisibility(View.VISIBLE);
        enc.setVisibility(View.GONE);
    }

    private void setSuit(Integer i, String s) {

        ImageView suit = findViewById(R.id.invsuit);
        suit.setImageResource(i);
        SharedPreferences.Editor editor=settings.edit();
        editor.putInt(SUIT,i);
        new uploadVolcAsyncTask().execute("setsuit", KeyHelper.fpr, s, email,"","","","","" );

    }

    private void setWeapon(Integer i, String s) {
        ImageView weapon = findViewById(R.id.invweapon);
        weapon.setImageResource(i);
        SharedPreferences.Editor editor=settings.edit();
        editor.putInt(WEAPON,i);
        new uploadVolcAsyncTask().execute("setweapon", KeyHelper.fpr, s, email,"","","","","" );


    }

    private void loadCount(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
         if (jsonArray.length() == 0) { Log.d ("data", "No data");
             return;
        }
        String take = "";
        String[] count_s = new String[jsonArray.length()];
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                count_s[i] = obj.getString("count");
                int count=Integer.parseInt(count_s[i]);
                if (count<posts.size())
                {
                    posts.clear();
                    new uploadVolcAsyncTask().execute("getchat", KeyHelper.fpr, settings.getString(ID,""), ""+ lastchatid,"","","","","" );
//                    getchat="http://a0568345.xsph.ru/winter/getchat.php/get.php?nom="+0;
//                    getJSON(getchat);
                }
            }

        }


    }



    private void showDialog(String message)
    {

        AlertDialog.Builder alert;
        AlertDialog dialog;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue,null);
        Button approve, dismiss;
        TextView mess;
        dismiss=view.findViewById(R.id.dismiss);
        approve=view.findViewById(R.id.approve);
        mess=view.findViewById(R.id.mess);
        alert.setView(view);
        alert.setCancelable(false);
        mess.setText("Удалить сообщение?");
        dialog = alert.create();

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dialog.dismiss();}
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new uploadAsyncTask().execute("delpost",message,passcode.getText().toString(),"","");
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void showMutantInfoDialog(Mutants _mutant, Marker marker)
    {

        AlertDialog.Builder alert;
        AlertDialog dialog;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.mutantinfodialog,null);
        ImageView mutant = view.findViewById(R.id.mutant);
        TextView info = view.findViewById(R.id.mutantinfo);
        TextView mutname = view.findViewById(R.id.mutname);
        mutname.setText(_mutant.name);
        TextView dist = view.findViewById(R.id.distance);
        dist.setText("Дистанция атаки: "+_mutant.distance);
        TextView hits = view.findViewById(R.id.hits);
        hits.setText("Здоровье: "+_mutant.hits);
        TextView power = view.findViewById(R.id.power);
        power.setText("Сила атаки: "+_mutant.power);
        TextView cost = view.findViewById(R.id.cost);
        cost.setText ("Премия за уничтожение: "+_mutant.cost);
        mutant.setImageResource(mutantPics.get(_mutant.id));
        Button fight = view.findViewById(R.id.fight);
        info.setText(_mutant.info);
        TextView geodistance=view.findViewById(R.id.geodistance);
        double lat=marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        double playerlat=imhere.getPosition().latitude;
        double playerlon = imhere.getPosition().longitude;

        int distanceToGo = (int) (distanceInKmBetweenEarthCoordinates(lat,lon,playerlat,playerlon)*1000);
        geodistance.setText ("Текущая дистанция, м.: "+distanceToGo);
        int isDistance = settings.getInt(DISTANCE,500);
        if (distanceToGo<isDistance)
        {
            geodistance.setTextColor(Color.parseColor("#ff0000"));
            fight.setVisibility(View.VISIBLE);
        }

        else

        {geodistance.setTextColor(Color.parseColor("#ffffff"));
            fight.setVisibility(View.GONE);
        }

        alert.setView(view);
        alert.setCancelable(false);

        dialog = alert.create();
        mutant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToast("Боевая система в разработке");
                showFightDialog(_mutant);
                dialog.dismiss();
            }
        });



        dialog.show();
    }

    AlertDialog.Builder fightalert;
    AlertDialog fightdialog;
    private void showFightDialog(Mutants _mutant)
    {
        Audio audio = new Audio(getApplicationContext());
        audio.playMutant(_mutant.sound);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            fightalert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            fightalert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fight,null);
        ImageView mutant = view.findViewById(R.id.mutant);

        //Button fight = view.findViewById(R.id.fight);


        fightalert.setView(view);
        fightalert.setCancelable(false);

        fightdialog = fightalert.create();
        mutant.setImageResource(_mutant.picture);


        mutant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fightdialog.dismiss();
            }
        });

        fightdialog.show();
    }


    private void showPopupMenu(View v, String message)
    {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup);

        popupMenu.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.menu1:

                    return true;
                   default:
                    return false;
            }
        });

        popupMenu.setOnDismissListener(menu ->
        {
            //Toast.makeText(getApplicationContext(), "Все ушло", Toast.LENGTH_SHORT).show();
        });
        popupMenu.show();
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            try {
                showLocation(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {
                showLocation(locationManager.getLastKnownLocation(provider));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {

            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {

            }
        }
    };
String lat="0", lon="0";
    private void showLocation(Location location) throws IOException {
        double distance;
        double mindist=400000;

        int id = 0;
        ArrayList<Double> dist = new ArrayList<>();
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            lat=""+location.getLatitude();
            lon=""+location.getLongitude();

            comLocation=location;
        //    //Log.d("Coord", lat+", "+lon);
            for (int i=0;i<cityLocs.size();i++)
            {
                distance = distanceInKmBetweenEarthCoordinates(location.getLatitude(),location.getLongitude(),cityLocs.get(i).latitude, cityLocs.get(i).longitude);
              //  distance = distanceInKmBetweenEarthCoordinates(53,158,cityLocs.get(i).latitude, cityLocs.get(i).longitude);

                if (distance<mindist)
                {mindist=distance;
                city = cityLocs.get(i).city;
                id=cityLocs.get(i).id;
                position = new LatLng(cityLocs.get(i).latitude, cityLocs.get(i).longitude);
                player.setText(settings.getString(NICK,"")+", "+city);

                }

            }

       //     //Log.d("Distance",city+", "+id);
            getLocs = "http://a0568345.xsph.ru/winter/getlocations.php/get.php?nom="+id;
            getJSON(getLocs);
            for (int i=0;i<grouplocs.size();i++)
            {
                double _dist=grouplocs.get(i).distance;
               // if (_dist<200) setToast (grouplocs.get(i).playername);

            }


//            String name=player.getText().toString();
//            String pass= passcode.getText().toString();
//            if (name.length()>0 && pass.length()>0 && giveLoc)
//            {
//                new uploadAsyncTask().execute("updatecoord",name,pass,""+lat,""+lon);
//            }

            //passcode.setText(lat+", "+lon);
            LatLng position=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
            if (imhere!=null && mapReady) imhere.remove();
            if (mapReady){

                imhere=(mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("Я здесь")
                    //.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromURL(resDomain+"krovosos.png")))));}
                    .icon(getBitmapHighDescriptor(R.drawable.marker))));}
        }
    }

    private void setMutants() {

        mutant = new BotMutant[mutantMarker.length];

        for (int i=0;i<mutantMarker.length;i++)
        {
            Random rnd = new Random();
            int selectMut = rnd.nextInt(mutants.size());

            Log.d ("mutantbase",""+ (position.latitude+getGen())+":"
                    +(position.longitude+getGen())+" Mutant # "+i+" Mutant is "+selectMut+", "+
                    mutants.get(selectMut).name);
            LatLng mutPosition = new LatLng(position.latitude+getGen(),position.longitude+getGen());
            mutantbase.add(new BotMutant(mutPosition,5, mutantMarker[i],
                    mutants.get(selectMut).name,mutants.get(selectMut).icon, mutants.get(selectMut).id));
        }

        for (int j=0;j<mutantbase.size();j++)
        {
            mutant[j] =mutantbase.get(j);

            Log.d("mutanticon", ""+mutantbase.get(j).drawmarker);
        }
        Log.d("mutants2", ""+mutantbase.size());
    }


    private BitmapDescriptor getBitmapHighDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getApplicationContext(), id);
        vectorDrawable.setBounds(0, 0, 40, 70);
        Bitmap bm = Bitmap.createBitmap(40, 70, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private BitmapDescriptor getBitmapMutantDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getApplicationContext(), id);
        vectorDrawable.setBounds(0, 0, 100, 130);
        Bitmap bm = Bitmap.createBitmap(100, 130, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    public Bitmap getBitmapFromURL (String link) throws IOException {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        String imageURLBase = link;
        URL imageURL = new URL(imageURLBase);
        Bitmap.Config config = null;
        URLConnection connection = imageURL.openConnection();
        InputStream iconStream = connection.getInputStream();
       Bitmap bmp = BitmapFactory.decodeStream(iconStream);
       return bmp;
    }

    public Bitmap getBitmapFromLink(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.connect();
            } catch (Exception e) {

            }
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.v("asfwqeds", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private void checkEnabled() {
//        tvEnabledGPS.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER));
//        tvEnabledNet.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };

    public void setToast(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


    private double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        int earthRadiusKm = 6371;
        double dLat = degreesToRadians(lat2 - lat1);
        double dLon = degreesToRadians(lon2 - lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }
    GoogleSignInAccount acct;
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {


            // Signed in successfully, show authenticated UI.

            acct = result.getSignInAccount();
         //   //Log.d("isEmail", acct.getEmail());
            getUser="http://a0568345.xsph.ru/winter/getusers.php/get.php?nom="+acct.getEmail();

           Log.e("getUser", getUser);
            getJSON(getUser);
           // showAuthDialog(acct.getEmail());
        }

    }

    private void getKeyHash(String hashStretagy) {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance(hashStretagy);
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //Log.e("KeyHash  -->>>>>>>>>>>>" , something);

                // Notification.registerGCM(this);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            //Log.e("name not found" , e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //Log.e("no such an algorithm" , e.toString());
        } catch (Exception e) {
            //Log.e("exception" , e.toString());
        }
    }


    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 300000;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
                else
                {
                    if (hideMarker)
                    {
                        marker.setVisible(false);
                    }
                    else
                    {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private double getGen()
    {
        double maxGen = 0.06;
        double minGen= -0.06;
        Random rnd = new Random();
        double randomLatValue = minGen + (maxGen - minGen) * rnd.nextDouble();
        String formattedLatDouble = new DecimalFormat("#0.0000000").format(randomLatValue);
        double gen = Double.parseDouble(formattedLatDouble.replace(",","."));
        return gen;
    }

}