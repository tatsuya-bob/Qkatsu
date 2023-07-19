package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz extends AppCompatActivity {
    int Answer = 0;//回答選択肢
    int Q_num = 0;//問題番号0～9(1~10の意)
    int point = 0;//取得点数
    //引き継ぎ
    int user_id = 0;
    String user_name = "";
    String user_password = "";
    String SELECT = "";

    //データベースから取得した値を格納する変数の用意。
    int ex_id = 0;//問題のid
    String question = "";//問題
    int correct = 0;//正解
    String explain = "";//解説

    //データーベースヘルパーオブジェクト
    private DatabaseHelper _helper;
    //Listオブジェクトを用意
    List<Map<String,Object>> itemList = new ArrayList<>();
    Map<String, Object> item = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Button button1 = (Button) findViewById(R.id.bt_A);//回答
        Button button2 = (Button) findViewById(R.id.bt_home);//HOME
        button1.setOnClickListener(button1Click);//回答
        button2.setOnClickListener(button2Click);//HOME

        //画面遷移エキストラデータを取得する
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            user_id = extras.getInt("user_id");
            user_name = extras.getString("user_name");
            user_password = extras.getString("user_password");
            SELECT = extras.getString("select");
            //Toast.makeText(Quiz.this,(SELECT),Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("Range")
    @Override

    protected void onStart() {
        super.onStart();
        //DBヘルパーオブジェクトを生成。
        _helper = new DatabaseHelper(Quiz.this);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        //SQL文字列
        String SQL1 = "SELECT ex_id,question,answer,explain from exercises";
        SQL1 = SQL1 + " WHERE kinds = '" + SELECT;
        SQL1 = SQL1 + "' ORDER BY RANDOM() LIMIT 10";
        //SQLの実行
        Cursor cursor = db.rawQuery(SQL1,null);
        //Toast.makeText(Quiz.this,(SQL1),Toast.LENGTH_LONG).show();
        //itemList内ハッシュマップitemに取得した値を格納
        while (cursor.moveToNext()) {
            // Map型変数を作成
            Map<String, Object> item = new HashMap<>();

            // データーベースより値を取得
            ex_id = cursor.getInt(cursor.getColumnIndex("ex_id"));
            question = cursor.getString(cursor.getColumnIndex("question"));
            correct = cursor.getInt(cursor.getColumnIndex("answer"));
            explain = cursor.getString(cursor.getColumnIndex("explain"));

            // ハッシュマップに値を追加
            item.put("ex_id", ex_id);
            item.put("question", question);
            item.put("correct", correct);
            item.put("explain", explain);

            // HashMapに値を追加
            itemList.add(item);
        }


        // itemListより 問題番号の要素を取り出して値を代入
        Map<String, Object> item = itemList.get(Q_num); // Q_numは０～9に変化ここでは0
        ex_id = (int) item.get("ex_id");
        question = (String) item.get("question");
        correct = (int) item.get("correct");
        explain = (String) item.get("explain");
        // tv_qオブジェクトを取得
        TextView textView1 = findViewById(R.id.tv_q);
        //textViewにquestionの値をセットSQLiteは\nにエスケープ付加されている
        question = question.replace("\\n", "\n");
        textView1.setText(question);
        // クローズ処理
        cursor.close();
        _helper.close();
        //問題番号を更新
        TextView textView2 = findViewById(R.id.tv_num);
        textView2.setText(SELECT + String.valueOf(Q_num + 1)+ "/10");
        //問題idを更新
        TextView textView3 = findViewById(R.id.tv_qid);
        textView3.setText("問題id=" + String.valueOf(ex_id));
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbA1:
                Answer=1;
                break;
            case R.id.rbA2:
                Answer=2;
                break;
            case R.id.rbA3:
                Answer=3;
                break;
            case R.id.rbA4:
                Answer=4;

                break;
        }
    }


    private View.OnClickListener button1Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //正解の場合点数加算
            if(Answer == correct){
                point = point + 1;
                Toast.makeText(Quiz.this,("正解"),Toast.LENGTH_SHORT).show();
            }else{
                //不正解の場合
                Toast.makeText(Quiz.this,("不正解 " + String.valueOf(correct)+ " が正解です。" ),Toast.LENGTH_SHORT).show();
            }
            //共通処理
            // 解説ダイヤログ表示
            //ダイヤログにexplain値をセットSQLiteは\nにエスケープ付加されている
            explain = explain.replace("\\n", "\n");
            showPopupDialog(explain); // メッセージを指定してポップアップダイアログを表示

        }
    };


    //HOMEに戻る
    private View.OnClickListener button2Click = new View.OnClickListener() {
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
    //ダイヤログ
    private void showPopupDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // ダイアログを閉じる
                        showNextQuestion();
                    }
                });
        builder.create().show();
    }
    //ダイヤログのokボタンがタップされたら
    private void showNextQuestion() {
        //ラジオボタンを非選択に戻す
        RadioButton rbA0 = findViewById(R.id.rbA0);
        rbA0.setChecked(true);
        /*
        RadioButton rbA1 = findViewById(R.id.rbA1);
        RadioButton rbA2 = findViewById(R.id.rbA2);
        RadioButton rbA3 = findViewById(R.id.rbA3);
        RadioButton rbA4 = findViewById(R.id.rbA4);
        rbA1.setChecked(false);
        rbA2.setChecked(false);
        rbA3.setChecked(false);
        rbA4.setChecked(false);
         */
        Answer = 0;

        //問題回数0~9が9未満なら、問題回数を+1
        if(Q_num < 9){
            Q_num = Q_num + 1;
            // itemListより 問題番号の要素を取り出して値を代入
            Map<String, Object> item = itemList.get(Q_num); // Q_numは０～9に変化ここでは0
            ex_id = (int) item.get("ex_id");
            question = (String) item.get("question");
            correct = (int) item.get("correct");
            explain = (String) item.get("explain");
            // tv_qオブジェクトを取得
            TextView textView1 = findViewById(R.id.tv_q);
            //textViewにquestionの値をセットSQLiteは\nにエスケープ付加されている
            question = question.replace("\\n", "\n");
            textView1.setText(question);


            //問題番号を更新
            TextView textView2 = findViewById(R.id.tv_num);
            textView2.setText(SELECT + String.valueOf(Q_num + 1)+ "/10");
            //問題idを更新
            TextView textView3 = findViewById(R.id.tv_qid);
            textView3.setText("問題id=" + String.valueOf(ex_id));
        }else{
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), Quiz_Result.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("user_password",user_password);
            intent.putExtra("select",SELECT);
            intent.putExtra("point",point);

            // 次のActivityを開く
            startActivity(intent);
            // 自Activityはfinish() で終了
            finish();
        }
    }

    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放。
        _helper.close();
        super.onDestroy();
    }
}