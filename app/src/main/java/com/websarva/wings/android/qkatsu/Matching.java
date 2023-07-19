package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.HardwarePropertiesManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matching extends AppCompatActivity {
    private ListView _lvItem;
    private List<Map<String, Object>> _itemList;
    private SimpleAdapter _adapter;
    private DatabaseHelper _helper;
    List<Map<String,Object>> itemList = new ArrayList<>();
    int id =0;
    private static final String[] FROM = {"company_name","company_address","URL"};

    private static final int[] TO = {R.id.tvcn,R.id.tvca};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(buttonClick);

        Intent intent = getIntent();
        id = intent.getIntExtra("user_id",0);
        //Toast.makeText(Matching.this, "\"" + id + "\"", Toast.LENGTH_LONG).show();

        _lvItem = findViewById(R.id.lv1);
        _itemList = new ArrayList<>();
        _helper = new DatabaseHelper(Matching.this);

        ListView lv1 = findViewById(R.id.lv1);
        //ListViewにリスナを設定
        lv1.setOnItemClickListener(new ListItemClickListener());
    }
    @SuppressLint("Range")
    @Override
    protected void onStart() {
        super.onStart();

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        //SQL文字列
        String sql = "SELECT rank_J,rank_P,rank_A,rank_M,rank_S,rank_V,rank_W FROM skill WHERE user_id=" +"\""+ id +"\"";
        //SQLの実行
        Cursor cursor = db.rawQuery(sql, null);
        //データベースから取得した値を格納する変数の用意。
        int rankJ=0;
        int rankP=0;
        int rankA=0;
        int rankM=0;
        int rankW=0;
        int rankV=0;
        int rankS=0;

        while (cursor.moveToNext()) {

            rankJ = cursor.getInt(cursor.getColumnIndex("rank_J"));
            rankP = cursor.getInt(cursor.getColumnIndex("rank_P"));
            rankA = cursor.getInt(cursor.getColumnIndex("rank_A"));
            rankM = cursor.getInt(cursor.getColumnIndex("rank_M"));
            rankW = cursor.getInt(cursor.getColumnIndex("rank_W"));
            rankV = cursor.getInt(cursor.getColumnIndex("rank_V"));
            rankS = cursor.getInt(cursor.getColumnIndex("rank_S"));

        }

        //SQL文字列
        String sql2 = "SELECT company_name,company_address,URL FROM job_offer WHERE " + "skill_J <= " + rankJ + " OR " + " skill_P <= " + rankP + " OR "+ " skill_A <= " + rankA + " OR "+ " skill_M <= " + rankM + " OR " + " skill_W <= " + rankW + " OR "+ " skill_V <= " + rankV + " OR "+ " skill_S <= " + rankS ;
        //SQLの実行
        Cursor cursor2 = db.rawQuery(sql2, null);

        String cn="";
        String ca="";
        String ur="";



        while (cursor2.moveToNext()) {

            cn = cursor2.getString(cursor2.getColumnIndex("company_name"));
            ca = cursor2.getString(cursor2.getColumnIndex("company_address"));
            ur = cursor2.getString(cursor2.getColumnIndex("URL"));


            Map<String, Object> item = new HashMap<>();
            item.put( "company_name" , cn );
            item.put("company_address" ,ca );
            item.put("URL" ,ur );

            itemList.add(item);

        }

        //画面部品LvItemを取得し、フィールドに格納
        _lvItem = findViewById(R.id.lv1);
        // itemListを_itemList変数に割り当て
        _itemList = new ArrayList<>(itemList);
        SimpleAdapter adapter = new SimpleAdapter(Matching.this, itemList, R.layout.matching_row, FROM, TO);
        // アダプタの登録
        _lvItem.setAdapter(adapter);


    }
    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。次の画面への値としてcountの現在値 + 1を入れておく。
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("user_id", id);

            // 次のActivityを開く
            startActivity(intent);

            // 自Activityはfinish() で終了
            finish();
        }
    };
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), HP.class);
            // クリックされた項目の位置（インデックス）を取得
            int clickedIndex = position;
            //itemListより値を取得
            // アダプタからクリックされた項目のデータを取得
            Map<String, Object> clickedItem = _itemList.get(clickedIndex);
            String ur = (String) clickedItem.get("URL");
            intent.putExtra("URL", ur);
            startActivity(intent);
        }
    }
    private void showPopupDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // ダイアログを閉じる
                    }
                });
        builder.create().show();
    }
}