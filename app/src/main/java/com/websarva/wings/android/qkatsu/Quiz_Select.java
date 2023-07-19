package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Quiz_Select extends AppCompatActivity {
    String SELECT="";
    //引き継ぎ
    int user_id = 0;
    String user_name = "";
    String user_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select);
        Button button1 = (Button) findViewById(R.id.button5);
        Button button2 = (Button) findViewById(R.id.bt_home);
        //画面遷移エキストラデータを取得する
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            user_id = extras.getInt("user_id");
            user_name = extras.getString("user_name");
            user_password = extras.getString("user_password");
            //Toast.makeText(Quiz_Select.this,(user_name),Toast.LENGTH_LONG).show();
        }
        button1.setOnClickListener(buttonClick);//スタートボタン
        button2.setOnClickListener(btHomeClick);//ホームボタン
        //Toast.makeText(Quiz_Select.this,(User_name),Toast.LENGTH_LONG).show();
    }

    public void onRadioButtonClicked(View view) {
        // ボタンが押されているか？
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbJ:

                if (checked)
                    // Javaの場合の処理
                    SELECT="J";
                break;

            case R.id.rbP:
                if (checked)
                    //  Pythonの場合の処理
                    SELECT="P";
                break;

            case R.id.rbA:
                if (checked)
                    // AndroidStudioの場合の処理
                    SELECT="A";
                break;

            case R.id.rbM:
                if (checked)
                    // 機械学習の場合の処理
                    SELECT="M";
                break;

            case R.id.rbV:
                if (checked)
                    // VBAの場合の処理
                    SELECT="V";
                break;

            case R.id.rbW:
                if (checked)
                    // ウェブアプリの場合の処理
                    SELECT="W";
                break;

            case R.id.rbS:
                if (checked)
                    // SQLの場合の処理
                    SELECT="S";
                break;

        }
    }

    //スタートボタン
    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(SELECT != "") {
                // 次の画面を開く為のインテント。
                Intent intent = new Intent(getApplicationContext(), Quiz.class);
                intent.putExtra("select", SELECT);
                intent.putExtra("user_id",user_id);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_password",user_password);

                // 次のActivityを開く
                startActivity(intent);

                // 自Activityはfinish() で終了
                finish();
            }else{
                // radioButtonが選択されていなかった場合の処理
                String strMessage = "問題種別を選択してください。";
                showPopupDialog(strMessage); // メッセージを指定してポップアップダイアログを表示
            }
        }
    };
    //ダイヤログ
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

    //ホーム画面に遷移
    private View.OnClickListener btHomeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。
            Intent intent = new Intent(getApplicationContext(), Home.class);

            // 次のActivityを開く
            startActivity(intent);

            // 自Activityはfinish() で終了
            finish();
        }
    };
}