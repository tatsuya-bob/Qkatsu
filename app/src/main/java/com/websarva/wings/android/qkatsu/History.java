package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {
    private ListView _lvItem;
    private List<Map<String, Object>> _itemList;
    private SimpleAdapter _adapter;
    private DatabaseHelper _helper;
    List<Map<String,Object>> itemList = new ArrayList<>();
    int id =0;
    String rank1="";
    private static final String[] FROM = {"datetime","kinds","point","rank"};
    //SipleAdapter第5引数to用データの用意
    private static final int[] TO = {R.id.tvdate,R.id.tvkinds,R.id.tvpoint,R.id.tvrank};

    String formattedDateTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(buttonClick);

        Intent intent = getIntent();
         id = intent.getIntExtra("user_id",0);
        //Toast.makeText(History.this, "\"" + id + "\"", Toast.LENGTH_LONG).show();

        _lvItem = findViewById(R.id.lv1);
        _itemList = new ArrayList<>();
        _helper = new DatabaseHelper(History.this);



    }
    @SuppressLint("Range")
    @Override
    protected void onStart() {
        super.onStart();

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        //SQL文字列
        String sql = "SELECT datetime,kinds,point,rank FROM logging WHERE user_id=" +"\""+ id +"\"";
        //SQLの実行
        Cursor cursor = db.rawQuery(sql, null);
        //データベースから取得した値を格納する変数の用意。

        while (cursor.moveToNext()) {



            long datetime = cursor.getLong(cursor.getColumnIndex("datetime"));
            String kinds = cursor.getString(cursor.getColumnIndex("kinds"));
            int point = cursor.getInt(cursor.getColumnIndex("point"));
            int rank = cursor.getInt(cursor.getColumnIndex("rank"));

            if(rank==4){
                rank1="S";
                Toast.makeText(History.this, rank1, Toast.LENGTH_LONG).show();
            }else if(rank==3){
                rank1="A";
            }else if(rank==2){
                rank1="B";
            }else if(rank==1){
                rank1="C";
            }else if(rank==0){
                rank1="D";
            }else{
                rank1="";
            }
            time(datetime);
            Map<String, Object> item = new HashMap<>();
            String kd="";

            switch (kinds){
                case "J":
                    kd="Java";
                    break;
                case "P":
                    kd="Python";
                    break;
                case "A":
                    kd="AndroidStudio";
                    break;
                case "V":
                    kd="VBA";
                    break;
                case "S":
                    kd="SQL";
                    break;
                case "W":
                    kd="Webアプリ";
                    break;
                case "M":
                    kd="機械学習";
                    break;
            }
          /*  TextView tvRank = findViewById(R.id.tvrank);
           switch (rank1) {
                case "S":
                    tvRank.setTextColor(Color.parseColor("#FFD700"));
                    break;
                case "A":
                    tvRank.setTextColor(Color.parseColor("#FFD700"));
                    break;
                case "B":
                    tvRank.setTextColor(Color.parseColor("#FFD700"));
                    break;
                case "C":
                    tvRank.setTextColor(Color.parseColor("#FFD700"));
                    break;
                case "D":
                    tvRank.setTextColor(Color.parseColor("#FFD700"));
                    break;
            }*/




            item.put("datetime",formattedDateTime);
            item.put("kinds" ,kd);
            item.put("point",point );
            item.put("rank" ,rank1); // 表示したいデータのフォーマットに変更する
            itemList.add(item);

        }

        //画面部品LvItemを取得し、フィールドに格納
        //_lvItem = findViewById(R.id.lv1);
        // itemListを_itemList変数に割り当て
        //_itemList = new ArrayList<>(itemList);
        SimpleAdapter adapter = new SimpleAdapter(History.this, itemList,
                R.layout.history_row, FROM, TO);
        // アダプタの登録
        _lvItem.setAdapter(adapter);
        System.out.println(_lvItem);
        System.out.println(adapter);
        System.out.println(_itemList);

    }

    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("user_id", id);

            // 次のActivityを開く
            startActivity(intent);

            // 自Activityはfinish() で終了
            finish();
        }
    };

    public  void time(long datetime) {
        long unixTimeMilliseconds = datetime + 32400000;// 取得したUNIX時間（ミリ秒単位）

        // UNIX時間をDateオブジェクトに変換
        Date date = new Date(unixTimeMilliseconds);

        // SimpleDateFormatを使用して、必要な形式の日付文字列を取得
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        formattedDateTime = sdf.format(date);


    }

}