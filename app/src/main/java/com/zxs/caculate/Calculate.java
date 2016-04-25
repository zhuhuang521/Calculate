package com.zxs.caculate;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by zxs on 16/4/25.
 */
public class Calculate {

    private int[][] data;
    private Handler hadler;
    int score;
    private Context context;
    int calculateNum = 0;
    int lastPointX =-1;
    int lastPointY = -1;
    int doubleTime = 0;
    public Calculate(int [][] data,Handler handler,int score,Context context){
        this.context = context;
        this.data = data;
        this.hadler = handler;
        this.score = score;
    }
    public void start(){
        Message timeMessage = new Message();
        timeMessage.what = 3;
        timeMessage.arg1 = calculateNum;
        calculateNum = calculateNum+1;
        hadler.sendMessage(timeMessage);
        int maxScore = 0;
        int x = -1,y = -1;
        for(int i=0;i<16;i++){
            for(int j=0;j<25;j++){
                if(data[i][j] == 0){
                    int num = getScore(i,j);
                    if(num > maxScore){
                        maxScore = num;
                        y = j;
                        x = i;
                        if(lastPointX == -1){
                            lastPointX = i;
                            lastPointY = j;
                            doubleTime = 1;
                        }else if(lastPointX != i || lastPointY != j){
                            lastPointX = -1;
                            lastPointY = -1;
                            doubleTime = 0;
                        }
                    }
                }
            }
        }
        if(maxScore != 0){
            clearPoint(x,y);
            score = score + maxScore;
            start();
        }
        Bundle bundle = new Bundle();
        bundle.putString("score",score+"");
        Message message = new Message();
        message.setData(bundle);
        message.what = 2;
        hadler.sendMessage(message);
    }

    /**
     * 获取点击某个点的数据
     * */
    private int getScore(int y,int x){
        int num = 0;
        int point[] = new int[4];
        //计算四个方向的数据
        //left
        for(int i = x-1;i>=0;i--){
            if(data[y][i] != 0){
                point[0] = data[y][i];
                break;
            }
        }
        //top
        for(int i = y-1;i>=0;i--){
            if(data[i][x] != 0){
                point[1] = data[i][x];
                break;
            }
        }
        //right
        for(int i = x+1;i<25;i++){
            if(data[y][i] != 0){
                point[2] = data[y][i];
                break;
            }
        }
        //bottom
        for(int i = y+1;i<16;i++){
            if(data[i][x] != 0){
                point[3] = data[i][x];
                break;
            }
        }
        //计算四个数据的数值
        for(int i=0;i<4;i++){
            for(int j=i+1;j<4;j++){
                if(point[i] == point[j] && point[i] != 0){
                    num = (num==0?2:2*num);
                    point[j] = 0;
                }
            }
        }
        if(num>0 && lastPointX == y && lastPointY == x && doubleTime > 0){
            num = num +doubleTime;
            doubleTime = doubleTime +1;
        }
        return num;
    }

    /***
     * 清除点击某个点后的数据
     */
    private void clearPoint(int y,int x){
        //left
        for(int i = x-1;i>=0;i--){
            if(data[y][i] != 0){
                data[y][i]=0;
                break;
            }
        }
        //top
        for(int i = y-1;i>=0;i--){
            if(data[i][x] != 0){
                data[i][x]=0;
                break;
            }
        }
        //right
        for(int i = x+1;i<25;i++){
            if(data[y][i] != 0){
                data[y][i]=0;
                break;
            }
        }
        //bottom
        for(int i = y+1;i<16;i++){
            if(data[i][x] != 0){
                data[i][x]=0;
                break;
            }
        }
    }


}
