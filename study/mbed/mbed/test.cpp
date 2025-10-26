#include "mbed.h"
Serial orenoPc1(USBTX, USBRX); //シリアル通信を定義
Serial android(p28, p27);
DigitalOut orenoLed1(LED1);
DigitalOut orenoLed2(LED2);

int main(){
    char recept_DATA[5];
    while (1){
        orenoLed1 = 1;
        recept_DATA[0] = android.getc();
        recept_DATA[1] = android.getc();
        recept_DATA[2] = android.getc();
        recept_DATA[3] = android.getc();
        recept_DATA[4] = android.getc();
        orenoLed1 = 0;
        orenoLed2 = 1;
        for (int i = 0; i >= 4; i++){
            orenoPc1.putc(recept_DATA[i]);
        }
        orenoLed2 = 0;
    }
}