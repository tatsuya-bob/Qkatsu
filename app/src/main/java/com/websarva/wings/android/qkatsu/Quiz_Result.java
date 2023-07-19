package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Date;

public class Quiz_Result extends AppCompatActivity {
    //引き継ぎ
    int user_id = 0;
    String user_name = "";
    String user_password = "";
    String SELECT = "";//直近問題種別
    int point = 0;//直近得点10点満点
    int point100 = 0;//直近得点100点満点
    int intRank = 0;//ランク0~4
    String strRank = "";//ランク文字
    String txstr="";
    //データーベースヘルパーオブジェクト
    private DatabaseHelper _helper;

    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        Button button1 = (Button) findViewById(R.id.bt_home);//HOME
        button1.setOnClickListener(buttonClick);//ホームボタン
        //画面遷移エキストラデータを取得する
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            user_id = extras.getInt("user_id");
            user_name = extras.getString("user_name");
            user_password = extras.getString("user_password");
            SELECT = extras.getString("select");
            point = extras.getInt("point");//問題画面で得たポイント10点満点
            point100 = point * 10;//100点満点にスケール変換
            //点数をランク付けする。表示はS,A,B,C,D,データーベースは,4,3,2,1,0
            if(point100 == 100){
                intRank = 4;
                strRank = "S";
                imageView= findViewById(R.id.iV);
                imageView.setImageResource(R.drawable.perfect);
            }else if (point100>= 80){
                intRank = 3;
                strRank = "A";
                imageView= findViewById(R.id.iV);
                imageView.setImageResource(R.drawable.newexcellent);
            }else if (point100 >= 60){
                intRank = 2;
                strRank = "B";
                imageView= findViewById(R.id.iV);
                imageView.setImageResource(R.drawable.newgreat);
            }else if (point100 >= 40){
                intRank = 1;
                strRank = "C";
                imageView= findViewById(R.id.iV);
                imageView.setImageResource(R.drawable.newgood);
            }else {
                intRank = 0;
                strRank = "D";
                imageView= findViewById(R.id.iV);
                imageView.setImageResource(R.drawable.newpoor);
            }
            //点数表示
            TextView textView1 = findViewById(R.id.tv_score);
            textView1.setText(String.valueOf(point100));
            //ランク表示
            TextView textView2 = findViewById(R.id.tv_rank);
            textView2.setText("あなたの得点　" +  strRank + " ランク");



        }
    }
    protected void onStart() {
        super.onStart();
        //現在日時を取得
        // 現在の日時を取得
        Date currentDate = new Date();
        long msecNow = currentDate.getTime();
        //DBヘルパーオブジェクトを生成。
        _helper = new DatabaseHelper(Quiz_Result.this);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        //logging記録用SQL文字列
        String SQL1 = "INSERT INTO logging (user_id,datetime,kinds,point,rank) VALUES (";
        SQL1 = SQL1 + user_id + "," + msecNow + ",'" + SELECT + "',"+ point100 +"," + intRank + ")";
        //SQLの実行logging結果
        db.execSQL(SQL1);

        //skillテーブル削除SQL文字列
        String SQL2 = "DROP TABLE IF EXISTS skill;";
        db.execSQL(SQL2);
        //skillテーブル作成用SQL文字列
        String SQL3 = "CREATE TABLE skill AS ";
        SQL3 = SQL3 + "SELECT l.user_id,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'J' THEN l.rank ELSE 0 END), 0) AS rank_J,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'P' THEN l.rank ELSE 0 END), 0) AS rank_P,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'A' THEN l.rank ELSE 0 END), 0) AS rank_A,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'M' THEN l.rank ELSE 0 END), 0) AS rank_M,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'W' THEN l.rank ELSE 0 END), 0) AS rank_W,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'V' THEN l.rank ELSE 0 END), 0) AS rank_V,";
        SQL3 = SQL3 + "COALESCE(MAX(CASE WHEN l.kinds = 'S' THEN l.rank ELSE 0 END), 0) AS rank_S,";
        SQL3 = SQL3 + "u.user_name ";
        SQL3 = SQL3 + "FROM logging l ";
        SQL3 = SQL3 + "LEFT JOIN user_data u ON l.user_id = u.user_id ";
        SQL3 = SQL3 + "GROUP BY l.user_id, u.user_name;";
        //SQLの実行logging結果
        db.execSQL(SQL3);
    }


    //ホームボタン
    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("user_password",user_password);
            // 次のActivityを開く
            startActivity(intent);
            //自Activityはfinish() で終了
            finish();
        }
    };
    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放。
        _helper.close();
        super.onDestroy();
    }

}