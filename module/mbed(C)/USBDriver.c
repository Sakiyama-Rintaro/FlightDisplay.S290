/////////////////////////////////////////////
//flightDisplayForS270Malus
//シリアル通信評価プログラムver.2
//2021/09/28
/////////////////////////////////////////////

#include "mbed.h"
Serial com(USBTX, USBRX);
Serial android(p28, p27);
DigitalOut led1(LED1);
DigitalOut led2(LED2);
void dataGenerate();
void dataShape(int,int);
void send(int[],int[]);

int main(){
    android.baud(115200);

    while(1){
        led1=1;
        //A 0 0 0 X X
        //[0]識別子,[1~3]ペイロード,[4~5]チェックサム
        dataGenerate();
        wait(0.2);
        led1=0;
        wait(0.05);
    }
}
int spdInt = 0;
int altInt = 0;
void dataGenerate(){
//    int spdInt = 40 + (int)( rand() * (60 - 40 + 1.0) / (1.0 + RAND_MAX) );
//    int altInt = 40 + (int)( rand() * (60 - 40 + 1.0) / (1.0 + RAND_MAX) );
if(altInt<100){
    altInt++;
}else{
    altInt = 0;
}
if(spdInt<100){
    spdInt+=2;
}else{
    spdInt = 0;    
}


    dataShape(spdInt,altInt);
}

void dataShape(int spd,int alt){
    int spdPacket[6];
    int altPacket[6];
    int spdCk;
    int altCk;
    spdPacket[0] = 'S';
    spdPacket[3] = (spd % 10); spd /= 10;//1
    spdPacket[2] = (spd % 10); spd /= 10;//10
    spdPacket[1] = (spd % 10);//100
    spdCk = spdPacket[1] + spdPacket[2] + spdPacket[3];
    spdPacket[4] = (spdCk / 10);
    spdPacket[5] = (spdCk % 10);

    altPacket[0] = 'A';
    altPacket[3] = (alt % 10); alt /= 10;//1
    altPacket[2] = (alt % 10); alt /= 10;//10
    altPacket[1] = (alt % 10);//100
    altCk = altPacket[1] + altPacket[2] + altPacket[3];
    altPacket[4] = (altCk / 10);
    altPacket[5] = (altCk % 10);
    send(spdPacket,altPacket);
}

void send(int spdPacket[],int altPacket[]){
    for(int i = 0;i<6;i++){
        android.putc(spdPacket[i]);
    }
    for(int i = 0;i<6;i++){
        android.putc(altPacket[i]);
    }
}