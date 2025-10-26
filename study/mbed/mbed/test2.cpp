#include "mbed.h"
Serial orenoPc1(USBTX, USBRX);
Serial orenoAndroid1(p28, p27);
DigitalOut orenoLed1(LED1);
DigitalOut orenoLed2(LED2);
int i;
int main(){
    int moji[11] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    orenoAndroid1.baud(9600);
    while (1){
        orenoLed2 = 1;
        for (i = 0; i < 11; i++){
            orenoAndroid1.putc(moji[i]);
        }
        orenoLed2 = 0;
        wait(2);
        
    }
}
