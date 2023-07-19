package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Change_Account extends AppCompatActivity {
    private DatabaseHelper _helper;

    private String user_name="";
    private String user_password="";
    private String TABLE_NAME="user_data";

    int id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        Button button1 = (Button) findViewById(R.id.button);

        button1.setOnClickListener(buttonClick);

        _helper = new DatabaseHelper(Change_Account.this);

        Intent intent = getIntent();
        id = intent.getIntExtra("user_id",0);

    }

    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 変更内容を取得
            EditText etName = findViewById(R.id.tvUser_name);
            String name = etName.getText().toString();
            EditText etPass = findViewById(R.id.tvPassword);
            String pass = etPass.getText().toString();


            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            SQLiteDatabase db = _helper.getWritableDatabase();

            String selectQuery = "SELECT user_name,user_password FROM  user_data  WHERE user_name=" +"\""+ name + "\"" +"  and user_password=" + "\"" + pass +"\"" ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {

                String strMessage = "変更出来ません";
                showPopupDialog(strMessage); // メッセージを指定してポップアップダイアログを表示
            }else {
                // インサート用SQL文字列の用意
                String sql1 = "UPDATE user_data SET user_name = '" + name + "' , user_password = '" + pass + "' WHERE user_id = ";
                sql1 = sql1 + id + ";";

                System.out.println(sql1);
                //SQLの実行
                //Cursor cursor = db.rawQuery(sql1,null);

                Log.i("Change_Account", sql1);
                // SQL文字列を元にプリペアドステートメントを取得
                SQLiteStatement stmt = db.compileStatement(sql1);
                // インサートSQLの実行
                stmt.executeUpdateDelete();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("user_id", id);

                // 次のActivityを開く
                startActivity(intent);

                // 自Activityはfinish() で終了
                finish();
            }

        }
    };
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