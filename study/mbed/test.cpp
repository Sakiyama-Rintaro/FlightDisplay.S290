#include "mbed.h" 

Serial droid(p28,p27);
Serial com(USBTX,USBRX);

int main(){
    char send_DATA[6] ={'0','1','0','2','0','3'};

    while(1){
        for(int i = 0;i<6;i++){
            droid.putc(send_DATA[i]);
            com.putc(send_DATA[i]);
        }
        wait(1);
    }

}