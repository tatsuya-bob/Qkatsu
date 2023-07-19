package com.websarva.wings.android.qkatsu;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class copyright extends AppCompatActivity {
    private TextView multiLineTextView;
    private String[] lines; // 表示するテキストの各行を格納した配列
    private int currentLineIndex = 0; // 現在表示している行のインデックス
    private Handler handler = new Handler();


    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);

        mediaPlayer = MediaPlayer.create(this, R.raw.pirate);
        mediaPlayer.start();


        multiLineTextView = findViewById(R.id.multiLineTextView);

        // 表示するテキストの各行を配列に設定
        lines = new String[]{
                "７つの海のクイズに答え",
                "就活の旅へ出よ!",
                "",
                "グループC：Q活制作委員会",
                "リーダー　    \b\b ：　加納",
                "システム部長　：　山田",
                "デザイン部長　：　雑賀",
                "仲居かしら　    ：　岩井"
        };

        // 初回表示のために2秒後に次の行を表示する
        handler.postDelayed(updateTextRunnable, 1500);
    }

    private Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentLineIndex < lines.length) {
                multiLineTextView.append(lines[currentLineIndex] + "\n");
                currentLineIndex++;
            }

            // 次の行があれば2秒後に再度実行
            if (currentLineIndex < lines.length) {
                handler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activityが破棄された時にHandlerからRunnableを削除する
        handler.removeCallbacks(updateTextRunnable);
        mediaPlayer.release();
    }
}