#include "mbed.h"
Serial orenoPc1(USBTX, USBRX);
Serial orenoAndroid1(p28, p27);
DigitalOut orenoLed1(LED1);
DigitalOut orenoLed2(LED2);
int i;
int main(){
    int moji1[11] = {'A', 5,1, 2, 3, 4, 5,5,'B'};
    int moji2[11] = {'A', 5,5, 4, 3, 2, 1,5,'B'};
    orenoAndroid1.baud(9600);
    while (1){
        orenoLed2 = 1;
        for (i = 0; i < 11; i++){
            orenoAndroid1.putc(moji1[i]);
        }
        wait(0.1);
        orenoLed2 = 0;
        wait(1);
        orenoLed2 = 1;
        for (i = 0; i < 11; i++){
            orenoAndroid1.putc(moji2[i]);
        }
        wait(0.1);
        orenoLed2 = 0;
        wait(1);
        
        
    }
}
