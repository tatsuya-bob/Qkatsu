package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.buton);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        // ボタンのテキストに現在のcount値をセット。

        button1.setOnClickListener(buttonClick);
        button2.setOnClickListener(buttonClick2);
        button3.setOnClickListener(buttonClick3);

        //データーベースを実行用フォルダに転記するメソッドを呼び出し
        copyDatabaseFromAssets();
    }

    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。次の画面への値としてcountの現在値 + 1を入れておく。
            Intent intent = new Intent(getApplicationContext(), User_Login.class);


            // 次のActivityを開く
           startActivity(intent);

            // 自Activityはfinish() で終了
            //finish();
        }
    };
    private View.OnClickListener buttonClick2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。次の画面への値としてcountの現在値 + 1を入れておく。
            Intent intent = new Intent(getApplicationContext(),Create_Account .class);


            // 次のActivityを開く
            startActivity(intent);

            // 自Activityはfinish() で終了
           // finish();
        }
    };
    private View.OnClickListener buttonClick3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 次の画面を開く為のインテント。次の画面への値としてcountの現在値 + 1を入れておく。
            Intent intent = new Intent(getApplicationContext(), copyright.class);


            // 次のActivityを開く
            startActivity(intent);
        }
    };
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


}