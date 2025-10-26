/////////////////////////////////////////////
//mbed->Androidシリアル通信評価プログラムver.2
//対応Androidアプリパッケージ名-> cx.bird.tbt.serialconanddraw
//対応AndroidアプリversionCode-> 1
//対応AndroidアプリversionName-> 1.0
//2020/12/29
/////////////////////////////////////////////

#include "mbed.h"
Serial com(USBTX, USBRX);
Serial android(p28, p27);
DigitalOut led1(LED1);
DigitalOut led2(LED2);
AnalogIn var(p20);

int main(){
    int moji[11];
    int tmp=0;
    int checkSum;
    android.baud(9600);
    while(1){
        led1=1;
        tmp=var*100;//可変抵抗の値に100かけて代入[0~100]
        //A 0 0 0 0 0 X X
        //[0]識別子,[1~5]ペイロード,[6~7]チェックサム
        moji[0] = 'A';
        moji[5] = (tmp % 10); tmp /= 10;
        moji[4] = (tmp % 10); tmp /= 10;
        moji[3] = (tmp % 10); tmp /= 10;
        moji[2] = (tmp % 10); tmp /= 10;
        moji[1] = (tmp % 10); tmp /= 10;
        checkSum = moji[1] + moji[2] + moji[3] + moji[4] + moji[5];//チェックサム計算（単純に全ての値を足し合わせるだけ）
        moji[6] = (checkSum / 10);
        moji[7] = (checkSum % 10);
        led2=1;
        for(int i = 0;i<11;i++){
            android.putc(moji[i]);
        }
        wait(0.1);
        led2=0;
        led1=0;
        wait(0.9);
    }
}
