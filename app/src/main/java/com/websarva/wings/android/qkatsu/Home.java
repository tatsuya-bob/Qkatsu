package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Home extends AppCompatActivity {
    //引き継ぎ
    int user_id = 0;
    String user_name = "";
    String user_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button button1 = (Button) findViewById(R.id.Qbt);//クイズ
        Button button2 = (Button) findViewById(R.id.Rbt);//履歴
        Button button3 = (Button) findViewById(R.id.Mbt);//マッチング
        Button button4 = (Button) findViewById(R.id.Hbt);//登録変更
        Button button5 = (Button) findViewById(R.id.bt_logout);//ログアウト


        button1.setOnClickListener(buttonClick);//クイズ
        button2.setOnClickListener(buttonClick2);//履歴
        button3.setOnClickListener(buttonClick3);//マッチング
        button4.setOnClickListener(buttonClick4);//登録変更
        button5.setOnClickListener(buttonClick5);//ログアウトボタン

        //画面遷移エキストラデータを取得する
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            user_id = extras.getInt("user_id");
            user_name = extras.getString("user_name");
            user_password = extras.getString("user_password");

        }
    }
    //クイズ画面に遷移
    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), Quiz_Select.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("user_password",user_password);

            // 次のActivityを開く
            startActivity(intent);


        }
    };
    //履歴画面に遷移
    private View.OnClickListener buttonClick2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), History.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("user_password",user_password);

            // 次のActivityを開く
            startActivity(intent);


        }
    };
    //マッチング画面に遷移
    private View.OnClickListener buttonClick3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), Matching.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("user_password",user_password);

            // 次のActivityを開く
            startActivity(intent);


        }
    };
    //登録変更画面に遷移
    private View.OnClickListener buttonClick4 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), Change_Account.class);
            intent.putExtra("user_id", user_id);

            // 次のActivityを開く
            startActivity(intent);


        }
    };
    //ログアウト
    private View.OnClickListener buttonClick5 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            // 次のActivityを開く
            startActivity(intent);

            finish();
        }
    };
}