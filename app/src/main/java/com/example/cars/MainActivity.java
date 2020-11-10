package com.example.cars;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final int maxValue = 1000;
    final String LOG_TAG = "myLogs";
    MyTask mt1, mt2, mt3;
    int sec1, sec2, sec3, place1, place2, place3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawCarInfo(1, 0, 0);
        DrawCarInfo(2, 0, 0);
        DrawCarInfo(3, 0, 0);

    }

    private void DrawCarInfo(int number, int sec, int path) {
        ImageView imageView;
        TextView pathView, secView;
        if (number == 1) {
            imageView = findViewById(R.id.imageViewCar1);
            pathView = findViewById(R.id.textViewPath1);
            secView = findViewById(R.id.textViewSec1);
        } else if (number == 2) {
            imageView = findViewById(R.id.imageViewCar2);
            pathView = findViewById(R.id.textViewPath2);
            secView = findViewById(R.id.textViewSec2);
        } else if (number == 3) {
            imageView = findViewById(R.id.imageViewCar3);
            pathView = findViewById(R.id.textViewPath3);
            secView = findViewById(R.id.textViewSec3);
        } else {
            return;
        }
        if (path > maxValue) {
            path = maxValue;
        }
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(-70, (maxValue - path) - 10, 0, 0); //left, top, right, bottom
        imageView.setLayoutParams(param);
        pathView.setText(Integer.toString(path));
        secView.setText(Integer.toString(sec) + " сек.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
                sec1 = 0;
                sec2 = 0;
                sec3 = 0;
                place1 = 0;
                place2 = 0;
                place3 = 0;
                mt1 = new MyTask();
                mt1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
                mt2 = new MyTask();
                mt2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2);
                mt3 = new MyTask();
                mt3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Кто какое место занял,
    // в зависимости от затраченного времени в секундах
    public void CalcPlace(int number, int sec) {
        if (number == 1) {
            sec1 = sec;
        } else if (number == 2) {
            sec2 = sec;
        } else if (number == 3) {
            sec3 = sec;
        }
        if (sec1 > 0 && sec2 > 0 && sec3 > 0) {
            place1 = 0;
            place2 = 0;
            place3 = 0;
            // Все машины пришли за одинаковое время
            if ((sec1 == sec2) && (sec2 == sec3)) {
                // У всех 1-ое место
                place1 = 1;
                place2 = 1;
                place3 = 1;
            } else if ((sec1 == sec2) && (sec1 < sec3)) {
                place1 = 1;
                place2 = 1;
                place3 = 2;
            } else if ((sec1 == sec2) && (sec3 < sec1)) {
                place1 = 2;
                place2 = 2;
                place3 = 1;
            } else if ((sec2 == sec3) && (sec1 < sec2)) {
                place1 = 1;
                place2 = 2;
                place3 = 2;
            } else if ((sec2 == sec3) && (sec2 < sec1)) {
                place1 = 2;
                place2 = 1;
                place3 = 1;
            } else if ((sec1 == sec3) && (sec1 < sec2)) {
                place1 = 1;
                place2 = 2;
                place3 = 1;
            } else if ((sec1 == sec3) && (sec2 < sec1)) {
                place1 = 2;
                place2 = 1;
                place3 = 2;
            } else if ((sec1 < sec2) && (sec2 < sec3)) {
                place1 = 1;
                place2 = 2;
                place3 = 3;
            } else if ((sec2 < sec3) && (sec3 < sec1)) {
                place2 = 1;
                place3 = 2;
                place1 = 3;
            } else if ((sec2 < sec1) && (sec1 < sec3)) {
                place2 = 1;
                place1 = 2;
                place3 = 3;
            } else if ((sec1 < sec3) && (sec3 < sec2)) {
                place1 = 1;
                place3 = 2;
                place2 = 3;
            } else if ((sec3 < sec1) && (sec1 < sec2)) {
                place3 = 1;
                place1 = 2;
                place2 = 3;
            } else if ((sec3 < sec2) && (sec2 < sec1)) {
                place3 = 1;
                place2 = 2;
                place1 = 3;
            }
            // Результаты на экран
            ViewPlaces();
        }
    }

    private void ViewPlaces() {
        View view = LayoutInflater
                .from(MainActivity.this)
                .inflate(R.layout.layout_custom, null);
        TextView textPlace1 = view.findViewById(R.id.textViewPlace1);
        TextView textPlace2 = view.findViewById(R.id.textViewPlace2);
        TextView textPlace3 = view.findViewById(R.id.textViewPlace3);
        textPlace1.setText(Integer.toString(place1) + "-ое место");
        textPlace2.setText(Integer.toString(place2) + "-ое место");
        textPlace3.setText(Integer.toString(place3) + "-ое место");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        builder.setTitle("Результаты гонки:");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cancelTask() {
        if (mt1 == null) return;
        Log.d(LOG_TAG, "cancel result: " + mt1.cancel(false));
    }

    class MyTask extends AsyncTask<Integer, Integer, Integer> {
        int number, sec;

        protected Integer doInBackground(Integer... params) {
            int path, rnd, result;
            number = params[0];
            try {
                path = 0;
                sec = 0;
                do {
                    rnd = (int) (10 + Math.random() * 100);
                    path = path + rnd;
                    TimeUnit.SECONDS.sleep(1);
                    sec = sec + 1;
                    publishProgress(number, sec, path);
                } while (path < maxValue);
                return sec;
            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "Interrupted");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            //tvInfo.setText("End");
            CalcPlace(number, sec);
            Log.d(LOG_TAG, "End");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            DrawCarInfo(values[0], values[1], values[2]);
        }
    }

}