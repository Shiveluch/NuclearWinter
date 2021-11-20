package com.shiveluch.nuclearwinter;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_NAVIGATE_NEXT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView send, showmap, showme, search, centralbutton, showloc;
    EditText message, player, passcode;
    TextView t_map, t_showme, t_find, t_loc, t_enc;
    GoogleMap mMap;
    boolean mapReady=false;
    String getchat = "";
    String getNewChat="";
    String getStalkers="http://a0568345.xsph.ru/winter/getstalkers.php";
    String getCount="http://a0568345.xsph.ru/winter/getcount.php";
    String getperc="";
    SupportMapFragment mapFragment;
    ListView chat, stalkerslist, menu, info;
    Context context;
    Marker imhere;
    Marker stalker;
    boolean giveLoc=true;
    LinearLayout bottompanel, mapchat, enc;
    int[] stalkerpics={R.drawable.st1,R.drawable.st2,R.drawable.st3,R.drawable.st4,R.drawable.st5,R.drawable.st6,R.drawable.st7};
    String [] menuArray={"Персонажи", "Группировки", "Аномалии","Артефакты","Оружие" };
    String [] menyScriptsArray={"getpers", "getgroup", "getanomaly","getarts", "getweapon"};



    final static String LOGTAG = "FIRE";
    ArrayList<Posts> posts = new ArrayList<Posts>();
    LocationManager locationManager;
    boolean GpsStatus = false;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    LatLng position = new LatLng(53.135664, 38.105510);
    String lastID="0";
    boolean mode=true;
    Activity activity;

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        activity=this;
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pda);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = getApplicationContext();
        activity=this;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showloc=findViewById(R.id.showloc);
        send = findViewById(R.id.send);
        chat = findViewById(R.id.chat);
        message = findViewById(R.id.message);
        player = findViewById(R.id.player);
        passcode = findViewById(R.id.passcode);
        bottompanel=findViewById(R.id.bottompanel);
        showmap=findViewById(R.id.showmap);
        showme=findViewById(R.id.showme);
        search=findViewById(R.id.search);
        centralbutton=findViewById(R.id.centralbutton);
        stalkerslist=findViewById(R.id.stalkerslist);
        t_map=findViewById(R.id.t_map);
        t_showme=findViewById(R.id.t_showme);
        t_find=findViewById(R.id.t_find);
        t_loc=findViewById(R.id.t_loc);
        t_enc=findViewById(R.id.t_enc);
        mapchat=findViewById(R.id.mapchat);
        enc=findViewById(R.id.encyclopedia);
        menu=findViewById(R.id.menu);
        info=findViewById(R.id.info);
        bottompanel.setVisibility(View.GONE);

        t_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapchat.setVisibility(View.GONE);
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

        centralbutton.setOnClickListener(v -> {
//            int vis=bottompanel.getVisibility();
//            if (vis==0) bottompanel.setVisibility(View.GONE);
//            else bottompanel.setVisibility(View.VISIBLE);
        });

        t_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mapReady) return;
                mapchat.setVisibility(View.VISIBLE);
                enc.setVisibility(View.GONE);
                mode=false;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position)
                        .zoom(15)
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
                        .zoom(15)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);

            }
        });
        Activity activity=this;

        t_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode) {
                    giveLoc = !giveLoc;
                    if (giveLoc) setToast("трансляция координат включена");
                    if (!giveLoc) setToast("Трансляция координат отключена");
                }
                else setToast("Переключитесь в режим карты");
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

                Log.d("Menu",script.getText().toString()+".php");
                getperc="http://a0568345.xsph.ru/winter/"+script.getText().toString()+".php";
                getJSON(getperc);
            }
        });

        t_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode)
                {RelativeLayout mapRL=findViewById(R.id.maplayout);
                mapRL.setVisibility(View.GONE);
                bottompanel.setVisibility(View.GONE);
                //stalkerslist.setVisibility(View.VISIBLE);
                getJSON(getStalkers);}
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
                Log.d("coord", ""+stalkerLat+", "+stalkerLon);
if (stalkerLat<1) {
    Toast.makeText(getApplicationContext(),"Местонахождение неизвестно",Toast.LENGTH_LONG).show();
    RelativeLayout mapRL=findViewById(R.id.maplayout);
    mapRL.setVisibility(View.VISIBLE);
    stalkerslist.setVisibility(View.GONE);
    bottompanel.setVisibility(View.VISIBLE);

    return;


}
                if (mapReady){
                    if (stalker!=null) stalker.remove();
                    stalker=(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(stalkerLat,stalkerLon))
                            .title(pass)
                            .icon(getBitmapHighDescriptor(R.drawable.stalker))));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(stalkerLat,stalkerLon))
                            .zoom(15)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate);
                    RelativeLayout mapRL=findViewById(R.id.maplayout);
                    mapRL.setVisibility(View.VISIBLE);
                    stalkerslist.setVisibility(View.GONE);
                    bottompanel.setVisibility(View.VISIBLE);
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
                    .zoom(15)
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
                        .zoom(15)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
            }
        });

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 10000, 10000);

        message.setOnKeyListener((v, keyCode, event) -> {
            boolean consumed = false;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (keyCode == KEYCODE_NAVIGATE_NEXT) {

                try {
                    sendMessage(message.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                consumed = true; //это если не хотим, чтобы нажатая кнопка обрабатывалась дальше видом, иначе нужно оставить false
            }


            if (keyCode == KEYCODE_ENTER) {

                try {
                    sendMessage(message.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                consumed = true; //это если не хотим, чтобы нажатая кнопка обрабатывалась дальше видом, иначе нужно оставить false
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        // hide keyboard
                        return true;
                    default:
                        break;
                }
            }

            return consumed;
        });

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
                Log.d("isDel",deleting);
                //int forDel=10000000;
                showDialog(deleting);


            }
        });
        send.setOnClickListener(v -> {

            String sendpass="34343";
            if (!passcode.getText().toString().equals(sendpass))
            {
                Toast.makeText(getApplicationContext(),"Неверный пароль на отправку сообщений",Toast.LENGTH_LONG).show();
                return;
            }

            Date currentDate = new Date();
// Форматирование времени как "день.месяц.год"
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
// Форматирование времени как "часы:минуты:секунды"
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String timeText = timeFormat.format(currentDate);
            String playername = player.getText().toString();
            String text = message.getText().toString();
            if (playername.length() < 3 ) {
                Toast.makeText(getApplicationContext(), "Недостаточная длина прозвища", Toast.LENGTH_LONG).show();
                return;
            }

            if (message.getText().toString().length()<2)
            {
                setToast("Недостаточная длина сообщения");
                return;
            }

            if (timeText.length()<4) return;

            String isMessage = timeText + ". " + playername + ": " + text;


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                sendMessage(isMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new uploadAsyncTask().execute("addpost", timeText, playername, text, "");
            message.setText("");
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //mMap.getUiSettings().setRotateGesturesEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        mapReady=true;

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

    private class uploadAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3], params[4]);
            return null;
        }

        protected void onPostExecute(Double result) {
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String isUri, String post1, String post2, String post3, String post4) {
            HttpClient httpclient = new DefaultHttpClient();
            String getUri = String.format(domain + "%s.php", isUri);
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
                HttpResponse response = httpclient.execute(httppost);


            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }

    public void getJSON(final String urlWebService) {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {
                    try {

                        if (urlWebService == getchat)
                            loadChat(s);
                        if (urlWebService == getCount)
                            loadCount(s);
                        if (urlWebService == getStalkers)
                            loadStalkers(s);
                        if (urlWebService == getperc)
                            loadperc(s);


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


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    getchat="http://a0568345.xsph.ru/winter/getposts.php/get.php?nom="+lastID;
                    getJSON(getchat);
                    getJSON(getCount);
                }
            });
        }

    }

    private void loadChat(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        ChatAdapter chatAdapter;
        if (jsonArray.length() == 0) { Log.d ("data", "No data");
            chatAdapter = new ChatAdapter(this, posts, this);
            chat.setAdapter(chatAdapter);
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
                id_s[i] = obj.getString("id");
                time_s[i] = obj.getString("time");
                player_s[i] = obj.getString("player");
                text_s[i] = obj.getString("text");
                take = id_s[i] + ", " + time_s[i] + ", " + player_s[i] + ", " + text_s[i];
                Log.d("chat", take);


                posts.add(new Posts(id_s[i], player_s[i], text_s[i], time_s[i], 0));
                lastID=id_s[i];

            }
            chatAdapter = new ChatAdapter(this, posts, this);
            chat.setAdapter(chatAdapter);
            Audio audio = new Audio(getApplicationContext());
            audio.playClick();



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
                Log.d("Info", name_s[i]);


            }
            infoAdapter = new InfoAdapter(this, isInfo, this);
            info.setAdapter(infoAdapter);


        }


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
                    getchat="http://a0568345.xsph.ru/winter/getposts.php/get.php?nom="+0;
                    getJSON(getchat);
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
                new uploadAsyncTask().execute("delpost",message,"","","");
                dialog.dismiss();
            }
        });

        dialog.show();
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
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

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
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {

            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {

            }
        }
    };
String lat="0", lon="0";
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            lat=""+location.getLatitude();
            lon=""+location.getLongitude();
            String name=player.getText().toString();
            String pass= passcode.getText().toString();
            if (name.length()>0 && pass.length()>0 && giveLoc)
            {
                new uploadAsyncTask().execute("updatecoord",name,pass,""+lat,""+lon);
            }

            //passcode.setText(lat+", "+lon);
            LatLng position=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
            if (imhere!=null && mapReady) imhere.remove();
            if (mapReady){
            imhere=(mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("Я здесь")
                    .icon(getBitmapHighDescriptor(R.drawable.marker))));}
        }
    }


    private BitmapDescriptor getBitmapHighDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getApplicationContext(), id);
        vectorDrawable.setBounds(0, 0, 40, 50);
        Bitmap bm = Bitmap.createBitmap(40, 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
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


}