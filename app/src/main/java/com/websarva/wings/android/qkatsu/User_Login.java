package com.websarva.wings.android.qkatsu;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class User_Login extends AppCompatActivity {
    private DatabaseHelper _helper;
    public int user_id=0;
    private String user_name="";
    private String user_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Button button1 = (Button) findViewById(R.id.Qbt);
        button1.setOnClickListener(buttonClick);
    }

    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @SuppressLint({"Range", "StringFormatInvalid"})
        @Override
        public void onClick(View view) {

            EditText etname = findViewById(R.id.tvUser_name);
            EditText etpass = findViewById(R.id.tvPassword);
            String name=etname.getText().toString();
            String pass=etpass.getText().toString();

            //tvUser_name と tvPasswordの両方に文字が入力されていれば
            if (name.length()>0 & pass.length()>0) {
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
                _helper = new DatabaseHelper(User_Login.this);
                SQLiteDatabase db = _helper.getWritableDatabase();
                //SQL文字列

                String sql1 = "SELECT user_id,user_name,user_password FROM user_data WHERE user_name = '";
                sql1 = sql1 + name + "' AND user_password = '";
                sql1 = sql1 + pass + "';";

                //SQLの実行
                Cursor cursor = db.rawQuery(sql1,null);

                if (cursor.moveToFirst()) {

                    // データベースから取得した値を格納する変数の用意。
                    int User_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                    String User_name = cursor.getString(cursor.getColumnIndex("user_name"));
                    String User_password = cursor.getString(cursor.getColumnIndex("user_password"));

                    // 値の使用や処理を行う
                    // 次の画面を開く為のインテント。次の画面への値としてcountの現在値 + 1を入れておく。



                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.putExtra("user_id",User_id);
                    intent.putExtra("user_name",User_name);
                    intent.putExtra("user_password",User_password);
                    // 次のActivityを開く
                    startActivity(intent);
                    //自Activityはfinish() で終了
                    //finish();
                } else {
                    // データが見つからなかった場合の処理
                    String strMessage = "ユーザー名とパスワードが一致しません";
                    showPopupDialog(strMessage); // メッセージを指定してポップアップダイアログを表示
                }

            }else{
                // textViewに値が入力されていなかった場合の処理
                String strMessage = "ユーザー名またはパスワードが入力されていません";
                showPopupDialog(strMessage); // メッセージを指定してポップアップダイアログを表示
            }

            /*
            // 次の画面を開く為のインテント。次の画面への値としてcountの現在値 + 1を入れておく。
            Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
            intent.putExtra("IntValue", 0);

            // 次のActivityを開く
            startActivity(intent);

             //自Activityはfinish() で終了
            finish();
            */
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
}