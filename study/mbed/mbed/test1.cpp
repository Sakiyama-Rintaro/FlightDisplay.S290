#include "mbed.h"
Serial orenoPc1(USBTX, USBRX); //シリアル通信を定義
Serial android(p28,p27);
DigitalOut orenoLed1(LED1);

int main(){
    while(1){
        orenoLed1 = 1;
        orenoPc1.printf("%d",android.getc());
        orenoLed1 = 0;
        wait(1);
        
    }
    }
