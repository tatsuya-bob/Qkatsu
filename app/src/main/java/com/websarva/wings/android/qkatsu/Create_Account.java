package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Create_Account extends AppCompatActivity {
    private DatabaseHelper _helper;

    private String user_name="";
    private String user_password="";
    private String TABLE_NAME="user_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        copyDatabaseFromAssets();
        _helper = new DatabaseHelper(Create_Account.this);
        SQLiteDatabase db = _helper.getWritableDatabase();
        String strMessage2 = "７つ選択肢から分野を選んで、クイズにチャレンジ\n" +
                "正答率によってあなたのランクを査定し、スキルに合ったジョブマッチングするアプリだよ♡";
        showPopupDialog2(strMessage2); // メッセージを指定してポップアップダイアログを表示


        Button saveButton = findViewById(R.id.Qbt);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etname = findViewById(R.id.UserNameText);
                EditText etpass = findViewById(R.id.editTextTextPassword);
                String name=etname.getText().toString();
                String pass=etpass.getText().toString();
                if(name.isEmpty() || pass.isEmpty()){

                    String strMessage = "文字を入力してください";
                    showPopupDialog(strMessage); // メッセージを指定してポップアップダイアログを表示
                }else{
                    String selectQuery = "SELECT user_name,user_password FROM  user_data  WHERE user_name=" +"\""+ name + "\"" +"  and user_password=" + "\"" + pass +"\"" ;
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor.getCount() > 0) {

                        String strMessage = "既に登録されています";
                        showPopupDialog(strMessage); // メッセージを指定してポップアップダイアログを表示




                    }else {
                        String sqlInsert = "INSERT INTO user_data(user_name,user_password)VALUES(?,?)";
                        SQLiteStatement stmt = db.compileStatement(sqlInsert);
                        stmt.bindString(1, name);
                        stmt.bindString(2, pass);
                        stmt.executeInsert();
                        String selectQuery2 = "SELECT user_id FROM  user_data  WHERE user_name=" + "\"" + name + "\"" + "  and user_password=" + "\"" + pass + "\"";
                        Cursor cursor2 = db.rawQuery(selectQuery2, null);
                        String id = "";
                        while (cursor2.moveToNext()) {
                            int cursor_id = cursor2.getColumnIndex("user_id");
                            id = cursor2.getString(cursor_id);
                        }
                        int user_id=Integer.parseInt(id);
                        Toast.makeText(Create_Account.this, "登録完了", Toast.LENGTH_LONG).show();
                        Toast.makeText(Create_Account.this, "ようこそQ活へ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("user_name", name);
                        intent.putExtra("user_password", pass);
                        startActivity(intent);
                        finish();


                    }
                }
            }
        });
    }
    //assetsフォルダに保存したsales.dbデーターベースをdata/data/アプリ名/databasesに登録する
    private void copyDatabaseFromAssets() {
        String databaseName = "qkatsu.db";
        String destinationPath = getApplicationContext().getDatabasePath(databaseName).getPath();

        try {
            // コピー先のデータベースファイルが存在するかチェック
            File file = new File(destinationPath);
            if (file.exists()) {
                return; // 既にデータベースファイルが存在する場合は何もしない
            }

            // assetsフォルダ内のデータベースファイルを開く
            InputStream inputStream = getApplicationContext().getAssets().open(databaseName);

            // コピー先のデータベースファイルを作成
            OutputStream outputStream = new FileOutputStream(destinationPath);

            // データのコピー
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // ストリームを閉じる
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
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
    private void showPopupDialog2(String message2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message2)
                .setPositiveButton("登録開始", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // ダイアログを閉じる
                    }
                });
        builder.create().show();
    }
}