#include<iostream>
#include<fstream>
#include<string.h>
#include<chrono>

using namespace std;

int main(){

    system("start ../AutoBackupProgramv1.jar /I /d <../AutoBackupProgramv1.jar> /WAIT");
    return 0;

    ifstream f1;
    string text;

    f1.open("text1"); 
    if(f1.fail()) return 1;

    for(int i=0; i<3; i++){
        getline(f1, text, '\n');
        cout<<text;
    }

    int cont = 0;
    char last_backup[20];
    for(int i=13; i<=31; i++){
        last_backup[cont] = text[i];
        cont++;
    }

    long current_date_in_seconds = std::chrono::duration_cast<std::chrono::seconds>(std::chrono::system_clock::now().time_since_epoch()).count();
    long last_date_in_seconds;

    cout<<endl<<last_backup;
    cout<<endl<<current_date_in_seconds;


    /*if(current_date_in_seconds - last_backup >= 120){ //120 sarebbero 2 minuti e' un valore momentaneo da test
        system("start ./AutoBackupProgramv1.jar");  //il nome del file è provvisorio
    } */

    f1.close();
    return 0;
}