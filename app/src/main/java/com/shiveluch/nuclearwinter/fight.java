package com.shiveluch.nuclearwinter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class fight extends AppCompatActivity {
    Button doMove;
    ArrayList<MonsterActions> monsteractions=new ArrayList<>();
    ArrayList<String> snorkactions=new ArrayList<>();
    ArrayList<String> controleractions=new ArrayList<>();
    String monsterlog="";
    int monsterhealth = 100;
    int monsterdistance = 0;
    int moves=0;
    int monactionsnumber=0;
    int movesToExit=3;
    int monsterchance=0;


    ProgressBar healthPB, monsterhealthpb;
    int pchance=50, ochance=30, kchance=30,schance=80;
    ArrayList<InvProperty> inventory = new ArrayList<>();
    ArrayList<BattleItem> battleItems = new ArrayList<>();
    // String getWeapon = ;
    LinearLayout fightscreen;
    Mutants isMutant;
    WeaponsInfo weapon;
    Context context;
    Activity activity;
    int playerhealth=100;
    int action=0;
    int chance=0;
    int price=0;
    int medikits=0,bints=0,mainammo=0,addammo=0;
    String email;
    TextView qmedikits, qbints, qmainammo, qaddammo;
    ListView actionsList;
    ImageView fmedikits, fbints, fmainammo, faddammo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            context=getApplicationContext();
            activity=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle arguments = getIntent().getExtras();
        int invsize=arguments.getInt("invsize");
        for (int i=0;i<invsize;i++)
        {
            inventory.add((InvProperty) getIntent().getParcelableExtra("invProp"+i));
            Log.d("inventory",inventory.get(i).invname+", "+getScreenOrientation());
        }

        monsterdistance = (arguments.getInt("distanceToGo"))/10;
        medikits = arguments.getInt("medikits");
        bints = arguments.getInt("bints");
        mainammo = arguments.getInt("mainammo");
        addammo = arguments.getInt("addammo");
        email = arguments.getString("email");
        int orientation = getScreenOrientation();
        isMutant = (Mutants) getIntent().getParcelableExtra("ismutant");
        weapon = (WeaponsInfo) getIntent().getParcelableExtra("isweapon");

        price = (isMutant.cost/100)*monsterhealth;
        if (orientation==2)
        {
            Log.d("orient", "Orientation: "+getScreenOrientation()+", "+"Monster: "+isMutant.name );
            fightscreen = findViewById(R.id.fightscreen);
            fightscreen.setVisibility(View.VISIBLE);
            initVisualElements();
            loadPlayerActionsList();
            loadMonsterChar();
            loadMonsterActions();

        }



    }

    private void initVisualElements() {

        Random rnd=new Random();
        doMove=findViewById(R.id.domove);
        qmedikits=findViewById(R.id.qmedikits);
        qbints=findViewById(R.id.qbints);
        qmainammo=findViewById(R.id.qmainammo);
        qaddammo=findViewById(R.id.qaddammo);
        fmedikits=findViewById(R.id.fmedikits);
        fbints=findViewById(R.id.fbints);
        fmainammo=findViewById(R.id.fmainammo);
        faddammo=findViewById(R.id.faddammo);
        healthPB = findViewById(R.id.healthpb);
        monsterhealthpb=findViewById(R.id.monsterhealthpb);

        qmedikits.setText("x"+medikits);
        qbints.setText("x"+bints);
        qmainammo.setText("x"+mainammo);
        qaddammo.setText("x"+addammo);

        healthPB.setProgress(playerhealth);

        ImageView mainweapon=findViewById(R.id.mainweapon);
        ImageView addweapon=findViewById(R.id.addweapon);
        ArrayList<Integer> mainweaponpic =new  WeaponsPics().weaponspics();
        ArrayList<Integer> addammo =new  WeaponAmmo().weaponspics();
        mainweapon.setImageResource(mainweaponpic.get(weapon.id));
        fmainammo.setImageResource(addammo.get(weapon.id));


        mainweapon.setOnClickListener(view -> {MainWeaponSelected();});
        addweapon.setOnClickListener(view -> {AddWeaponSelected();});
        fmedikits.setOnClickListener(view -> {medikitsSelected();});
        fbints.setOnClickListener(view -> {bintsSelected();});

        doMove.setOnClickListener(view -> { move(action);  });
        ImageView monstertimage = findViewById(R.id.monsterfightimage);
        monstertimage.setImageResource(isMutant.picture);
        TextView monstername = findViewById(R.id.fightmutantname);
        monstername.setText(isMutant.name);
        TextView distanceleft=findViewById(R.id.distanceleft);
        span("Дистанция до цели: "+monsterdistance+" м.", distanceleft, 18);
    }

    private void bintsSelected() {
        if (bints<=0)
        {
            bints=0;
            sendToast("Бинты закончились");
            return;
        }
        for (int i=0;i<actions.size();i++)
        {
            actions.get(i).description="0";
        }
        actions.get(5).description="1";
        ActionsAdapter adapter = new ActionsAdapter(context,actions,activity);
        actionsList.setAdapter(adapter);
        action=6;
        actionsList.setSelection(5);

    }

    private void medikitsSelected() {
        if (medikits<=0)
        {
            medikits=0;
            sendToast("Аптечки закончились");
            return;
        }
        for (int i=0;i<actions.size();i++)
        {
            actions.get(i).description="0";
        }
        actions.get(4).description="1";
        ActionsAdapter adapter = new ActionsAdapter(context,actions,activity);
        actionsList.setAdapter(adapter);
        action=5;

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void AddWeaponSelected() {
        for (int i=0;i<actions.size();i++)
        {
            actions.get(i).description="0";
        }
        actions.get(3).description="1";
        ActionsAdapter adapter = new ActionsAdapter(context,actions,activity);
        actionsList.setAdapter(adapter);
        action=4;

    }

    private void MainWeaponSelected() {
        for (int i=0;i<actions.size();i++)
        {
            actions.get(i).description="0";
        }
        actions.get(2).description="1";
        ActionsAdapter adapter = new ActionsAdapter(context,actions,activity);
        actionsList.setAdapter(adapter);
        action=3;

    }

    private void move(int _playeraction) {


        ArrayList<Integer> sounds = new ArrayList<>();
        sounds.add(R.raw.pmshot);
        sounds.add(R.raw.obrezshot);
        sounds.add(R.raw.kalashshot);
        sounds.add(R.raw.svdshot);
        int maxDist = weapon.maxdistance;
        int minDist= weapon.mindistance;
        int attackdistance = isMutant.distance;
        int monsterchance=0;
        Log.d("distance","Attack on " +attackdistance+", "+"distance is "+monsterdistance);

        //Блок небоевых действий игрока
        String playerlog="";
        int customdistance=0;
        if (_playeraction==0)
        {
            sendToast("Не выбрано действие");
            return;
        }

        if (movesToExit<3)  //ходы до выхода из боя
        {

            playerlog="Игрок пытается покинуть бой. Ходов до выхода: "+(movesToExit-1)+". ";
            movesToExit--;
            if (movesToExit<1)
            {
                this.finish();
            }
        }

        if (_playeraction==1)//смена позиции
        {
            customdistance=new Random().nextInt(10);
            playerlog=playerlog+"Игрок выжидает и меняет позицию на "+customdistance + " м ";
            monsterchance=4;


        }

        if (_playeraction==2)
        {
            playerlog="Игрок пытается покинуть бой.Ходов до выхода: "+(movesToExit-1);
            if (movesToExit==3) movesToExit--;
        }

        if (_playeraction==5)
        {
            playerhealth = playerhealth+inventory.get(0).property;
            if (playerhealth>100) playerhealth=100;
            healthPB.setProgress(playerhealth);
            playerlog="Игрок применяет аптечку.";
            medikits--;
            qmedikits.setText("x"+medikits);
        }

        if (_playeraction==6)
        {
            playerhealth = playerhealth+inventory.get(1).property;
            if (playerhealth>100) playerhealth=100;
            healthPB.setProgress(playerhealth);
            playerlog="Игрок использует бинт.";
            bints--;
            qbints.setText("x"+bints);
        }

        //небоевые действия монстра
        monactionsnumber=monsteractions.size();

        if (attackdistance<monsterdistance)
        {
            int faraction=new Random().nextInt(10);
            if (faraction<7)
            {
                int randomMove = new Random().nextInt(monsteractions.get(0).changedistance-15)+15;
                monsterdistance=monsterdistance-randomMove+customdistance;
                if (monsterdistance<(attackdistance/2)) monsterdistance=attackdistance/2;
                monsterlog = monsteractions.get(0).action+" на "+randomMove+" м";
                monsterchance=10;


            }
            else
            {
                int variant=new Random().nextInt(monsteractions.size());
                int randomMove = new Random().nextInt(10);
                monsterdistance=monsterdistance-randomMove+customdistance;
                monsterlog = monsteractions.get(variant).action+" на "+randomMove +" м";
                monsterchance = monsteractions.get(variant).chancepistol;
            }
        }
            else {
            int closeaction = new Random().nextInt(10);
            if (closeaction < 8-monsterchance) {
                int damage = new Random().nextInt(isMutant.power);
                playerhealth = playerhealth - damage;
                monsterlog = "Монстр наносит удар и отнимает " + damage + " HP";
                healthPB.setProgress(playerhealth);
            } else
                {
                    int randomMove = new Random().nextInt(monsteractions.get(0).changedistance-15)+15;
                    monsterdistance=monsterdistance+randomMove+20+customdistance;
                    monsterlog = "Монстр промахивается и отбегает на " + (randomMove+20+customdistance) + " м";
                    healthPB.setProgress(playerhealth);
            }
        }



int maxDistance=0, minDistance=0;
        if (_playeraction==3)
        {
             maxDistance = maxDist;
             minDistance = minDist;
            new Audio(getApplicationContext()).shot(sounds.get(weapon.id));
        }

        if (_playeraction==4)
        {
            maxDistance = 25;
            minDistance = 0;
            new Audio(getApplicationContext()).shot(sounds.get(0));
        }

        if (_playeraction==3 || _playeraction == 4) {
            chance = mapOneRangeToAnother(monsterdistance, maxDistance, minDistance, 1, 100, 0);
            chance=chance-monsterchance;

            if (chance < 0) chance = 1;
            if (chance > 100) chance = 1;
            if (_playeraction==3) {//стрельба основным
                int damage=0;
                int shots=0;
                for (int i=0;i<weapon.shots;i++) {
                    mainammo--;
                    if (mainammo<=0)
                    {
                        sendToast("Нет патронов");
                        qmainammo.setText(("x"+0));
                        return;
                    }
                    qmainammo.setText(("x"+mainammo));
                    if (weapon.id==0) {
                        addammo--;
                        qaddammo.setText("x"+addammo);
                    }

                    int damagechance = new Random().nextInt(100);
                    if (damagechance <= chance + 1) {
                        monsterhealth=monsterhealth-weapon.uron;
                        if (monsterhealth <0) monsterhealth=0;
                        damage+=weapon.uron;
                        shots++;
                        monsterchar.get(2).name="Здоровье: "+monsterhealth+" %";
                        MonsterDesc adapter = new MonsterDesc(this,monsterchar,this);
                        ListView monsterchars=findViewById(R.id.monsterchar);
                        monsterchars.setAdapter(adapter);
                        monsterhealthpb.setProgress(monsterhealth);

                        Log.d("chance", "Ход " + moves + ", попадание с шансом " + chance);
                    }

                    if (damagechance > chance + 1) {
                        Log.d("chance", "Ход " + moves + ", промах с шансом " + chance);
                    }
                }
                if (shots>0) playerlog=playerlog+"Игрок попал и нанес "+damage + " урона.";
                else
                {
                    playerlog=playerlog+"Игрок промахнулся.";
                }


            }

            if (_playeraction==4) {//стрельба вторичкой
                int damage=0;
                int shots=0;
                for (int i=0;i<1;i++) {
                    addammo--;
                    if (addammo<=0)
                    {
                        sendToast("Нет патронов");
                        qaddammo.setText("x"+0);
                        return;
                    }
                    qaddammo.setText("x"+addammo);

                    if (weapon.id==0) {
                        mainammo--;
                        qmainammo.setText("x"+mainammo);

                    }

                    int damagechance = new Random().nextInt(100);
                    if (damagechance <= chance + 1) {
                        monsterhealth=monsterhealth-5;
                        if (monsterhealth <0) monsterhealth=0;
                        damage+=5;
                        shots++;

                        monsterchar.get(2).name="Здоровье: "+monsterhealth+" %";
                        MonsterDesc adapter = new MonsterDesc(this,monsterchar,this);
                        ListView monsterchars=findViewById(R.id.monsterchar);
                        monsterchars.setAdapter(adapter);
                        monsterhealthpb.setProgress(monsterhealth);

                        Log.d("chance", "Ход " + moves + ", попадание с шансом " + chance);
                    }

                    if (damagechance > chance + 1) {
                        Log.d("chance", "Ход " + moves + ", промах с шансом " + chance);
                    }
                }
                if (shots>0) playerlog=playerlog+"Игрок попал и нанес "+damage + " урона.";
                else
                {
                    playerlog=playerlog+"Игрок промахнулся.";
                }


            }
        }
        if (monsterdistance<0) monsterdistance=0;
        moves++;
        TextView distanceleft=findViewById(R.id.distanceleft);
        span("Дистанция до цели: "+monsterdistance+" м.", distanceleft, 18);
        Log.d ("monsmove", ""+ moves+":"+monsterlog+". "+playerlog);
        battleItems.add(new BattleItem("Ход "+moves+":", monsterlog+". "+playerlog));

        ArrayList<BattleItem> invert=new ArrayList<>();
        for (int i=battleItems.size();i>0;i--)
        {
            //Log.d("invert",""+i);
            invert.add(battleItems.get(i-1));
        }
        BattleLogAdapter adapter=new BattleLogAdapter(this,invert);
        ListView battlelog=findViewById(R.id.battlelog);
        battlelog.setAdapter(adapter);
                for (int i=0;i<actions.size();i++)
        {
            actions.get(i).description="0";
        }
        ActionsAdapter thisadapter = new ActionsAdapter(context,actions,activity);
        actionsList.setAdapter(thisadapter);
        action=0;
        new fightAsyncTask().execute("aftermove","", email, ""+medikits, ""+bints,""+weapon.id,""+mainammo,""+addammo,"");
        if (playerhealth<=0)
        {
            showInfoDialog("Игрок погиб");
        }

        if (monsterhealth<=0)
        {
            new fightAsyncTask().execute("removemoney", KeyHelper.fpr, ""+(-price),email,"","","","","");
            Log.d("keyhelper", ""+KeyHelper.fpr+", "+price+", "+email);
            showInfoDialog("Монстр убит");


        }



    }

    private void sendToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void loadMonsterActions() {
        {

            if (monsteractions.size()==0)
            {
                monsteractions.add(new MonsterActions(isMutant.name +" приблизился",isMutant.speed,0,10,ochance,kchance,schance));
                monsteractions.add(new MonsterActions(isMutant.name +" отпрыгнул влево",(isMutant.speed / 2),0,20,(ochance/2),(kchance/2),(schance/2)));
                monsteractions.add(new MonsterActions(isMutant.name +" отбежал влево",(isMutant.speed / 5),0,10,(ochance),(kchance),(schance)));
                monsteractions.add(new MonsterActions(isMutant.name +" отпрыгнул вправо",(isMutant.speed / 2),0,20,(ochance/2),(kchance/2),(schance/2)));
                monsteractions.add(new MonsterActions(isMutant.name +" отбежал вправо",(isMutant.speed / 5),0,10,(ochance),(kchance),(schance)));
                monsteractions.add(new MonsterActions(isMutant.name +" внезапно ускорился",(isMutant.speed*2),0,40,(ochance/10),(kchance/10),(schance/10)));
                monsteractions.add(new MonsterActions(isMutant.name +" нанес удар",0,1,(pchance*2),0,(kchance*2),(schance*2)));
            }
        }
    }

    private void span(String _text, TextView _tv, int _start)
    {
        String text2 = _text;
        Spannable spannable = new SpannableString(text2);
        spannable.setSpan(new ForegroundColorSpan(Color.GREEN), _start, _text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        _tv.setText(spannable, TextView.BufferType.SPANNABLE);

    }
    ArrayList<Info> actions = new ArrayList<>();
    private void loadPlayerActionsList() {
        actions.clear();
        actions.add(new Info("Сменить позицию","",""));
        actions.add(new Info("Выйти из боя","",""));
        actions.add(new Info("Выстрел из основного оружия","",""));
        actions.add(new Info("Выстрел из вспомогательного оружия","",""));
        actions.add(new Info("Использовать аптечку","",""));
        actions.add(new Info("Использовать бинт","",""));
        ActionsAdapter adapter = new ActionsAdapter(this,actions,this);

        actionsList = findViewById(R.id.actionslist);
        actionsList.setAdapter(adapter);
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos==1 && movesToExit<3)
                {
                    sendToast("Уже выходите из боя");
                    return;
                }
                for (int i=0;i<actions.size();i++)
                {
                    actions.get(i).description="0";
                }
                actions.get(pos).description="1";
                ActionsAdapter adapter = new ActionsAdapter(context,actions,activity);
                actionsList.setAdapter(adapter);

               action=pos+1;
            }
        });
    }

    ArrayList<Info> monsterchar = new ArrayList<>();

    private void loadMonsterChar() {
        monsterhealth = isMutant.hits+new Random().nextInt(100-isMutant.hits);
        Log.d("hits", ""+isMutant.hits);
        monsterchar.add(new Info("Дистанция атаки: "+isMutant.distance+" метров","0",""));
        monsterchar.add(new Info("Урон за 1 атаку: до "+isMutant.power+" %","1",""));
        monsterchar.add(new Info("Здоровье: "+monsterhealth+" %","","2"));
        monsterchar.add(new Info("Скорость: до "+isMutant.speed+"м./ход","3",""));
        monsterchar.add(new Info("Награда: "+((isMutant.cost/100)*monsterhealth)+" RU","4",""));


        MonsterDesc adapter = new MonsterDesc(this,monsterchar,this);
        ListView monsterchars=findViewById(R.id.monsterchar);
        monsterchars.setAdapter(adapter);
        monsterhealthpb.setProgress(monsterhealth);
        Log.d("monst",""+monsterhealthpb.getProgress());
        monsterchars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Action", monsterchar.get(i).name );
            }
        });
    }

    public void getJSON(final String urlWebService) {
         class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                if (s==null)
                {return;}

                if (s != null) {

                    try {

                        if (urlWebService == "") {
                            loadinfo(s);
                            return;}

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

    private void loadinfo(String s) throws JSONException {
    }

    private void loadmonster(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() == 0) {
            //Log.d ("LocsData", "No data");

            return;
        }
//        String take = "";
//
//        String[] alllocs = new String[locnames.length];

        if (jsonArray.length() > 0) {
//            if (mutant==null && mutants.size()>0)
//                setMutants();
//
//            grouplocs.clear();
//            JSONObject obj = jsonArray.getJSONObject(0);
//            alllocs[0] = obj.getString("cordon");
//            alllocs[1] = obj.getString("dolg");
//            alllocs[2] = obj.getString("svoboda");
//            alllocs[3] = obj.getString("science");
//            alllocs[4] = obj.getString("clearsky");
//            for (int i=0;i<locnames.length;i++)
//            {
//                alllocs[i] = alllocs[i].replace(",", ".");
//                String [] locposition = alllocs[i].split(":");
//                double locLatitude = Double.parseDouble(locposition[0]);
//                double locLongitude = Double.parseDouble(locposition[1]);
//                Location destLocation=new Location("");
//                destLocation.setLatitude(locLatitude);
//                destLocation.setLongitude(locLongitude);
//                double dist=comLocation.distanceTo(destLocation);
//                // Log.d ("DistancebyGM", locnames[i]+", "+dist);
////                        double distance=distanceInKmBetweenEarthCoordinates(comLocation.getLatitude(),comLocation.getLongitude(), locLatitude, locLongitude);
////                        Log.d ("DistancebyScript", locnames[i]+", "+distance*1000);
//                int d=(int)dist;
//                grouplocs.add(new GroupLocations(locnames[i]+" ("+d+" м.)", "", stalkerpics[i], locposition[0],locposition[1],dist));
         //   }
        }
    }
    public int getScreenOrientation()
    {
        Display getOrient = this.getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    public static int mapOneRangeToAnother(int sourceNumber, int fromA, int fromB, int toA, int toB, int decimalPrecision ) {
        double deltaA = fromB - fromA;
        double deltaB = toB - toA;
        double scale  = deltaB / deltaA;
        double negA   = -1 * fromA;
        double offset = (negA * scale) + toA;
        double finalNumber = (sourceNumber * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (int) Math.round(finalNumber * calcScale) / calcScale;
    }
    private void showInfoDialog(String message) {
        AlertDialog.Builder infoalert;
        AlertDialog infodialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            infoalert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            infoalert = new AlertDialog.Builder(this);
        }


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.infodialog, null);

        TextView info=view.findViewById(R.id.information);
        Button dismiss=view.findViewById(R.id.approve);

        info.setText(message);


        infoalert.setView(view);
        infoalert.setCancelable(false);

      infodialog= infoalert.create();

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                infodialog.dismiss();
            }
        });
        infodialog.show();
    }

    private class fightAsyncTask extends AsyncTask<String, Integer, Double> {
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
        String volcDomain = "https://volcanorp.ru/winter/";
        public void postData(String isUri, String post1, String post2, String post3, String post4, String post5, String post6, String post7, String post8) {
            HttpClient httpclient = new DefaultHttpClient();
            String getUri = String.format(volcDomain + "%s.php", isUri);
            System.out.println(getUri);
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


            } catch (IOException e) {
            }

        }
    }


}