package com.zxs.caculate;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String data = "";
    TextView textView ;
    TextView stateText;
    TextView scoreText;
    long time;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    textView.setText(data);
                    Toast.makeText(MainActivity.this,"总耗时"+time+"ms",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    stateText.setText("开始计算");
                    break;
                case 2:
                    scoreText.setText("最后得分"+msg.getData().getString("score"));
                    break;
                case 3:
                    stateText.setText("第"+msg.getData().getLong("time")+"次数运算");
                    break;
                case 4:
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.start);
        textView = (TextView)findViewById(R.id.data_text);
        stateText = (TextView)findViewById(R.id.state_text);
        scoreText = (TextView)findViewById(R.id.score_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                data = "";
                new MyThread().start();
            }
        });
        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }
    }

    private class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            String path = Environment.getExternalStorageDirectory().getPath();
            File file = new File(path+"/locs.txt");
            ArrayList<String> dataList = new ArrayList<String>();
            try{
                String line = "";
                FileInputStream fis = new FileInputStream(file.getPath());
                InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                int num = 0;

                while((line=br.readLine())!=null){
                    if(num != 0){
                        data = data+"\n"+line;
                        dataList.add(line.replace(" ",""));
                    }
                    num ++;
                }
                br.close();
                isr.close();
                fis.close();
            }catch (Exception e){
                Log.v("zxs","error"+e.toString());
            }

            int data[][] = new int[dataList.size()][dataList.get(0).length()];
            for(int i =0;i<dataList.size();i++){
                char[] datas = dataList.get(i).replace(" ","").toCharArray();
                for(int j =0;j<dataList.get(0).length();j++){
                    data[i][j] = Integer.parseInt(datas[j]+"");
                }
            }
            Calculate calculate = new Calculate(data,handler,0,MainActivity.this);
            Calendar calendar = Calendar.getInstance();
            calculate.start();
            Calendar endCalendar = Calendar.getInstance();
            time = endCalendar.getTimeInMillis() - calendar.getTimeInMillis();
            handler.sendEmptyMessage(0);
        }
    }
}
