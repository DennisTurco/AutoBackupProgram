#include<iostream>
#include<fstream>
#include<string.h>
#include<chrono>
#include<Windows.h>

using namespace std;

int main(){

    ifstream f1;
    ifstream f2;
    string text;

    //controllo se e' attivo la modalita' automatica del backup
    f2.open("res/auto_generation");
    if(f2.fail()) return 1;
    getline(f2, text, '\n');
    if(text != "true"){
        f2.close();
        return 0;
    } 
    

    f1.open("res/info"); 
    if(f1.fail()) return 1;

    for(int i=0; i<3; i++){
        getline(f1, text, '\n');
    }

    long last_date_in_seconds;
    f1>>last_date_in_seconds;
    long current_date_in_seconds = std::chrono::duration_cast<std::chrono::seconds>(std::chrono::system_clock::now().time_since_epoch()).count();

    current_date_in_seconds = current_date_in_seconds - 43202;

    /*cout<< current_date_in_seconds<<endl;
    cout<< last_date_in_seconds<<endl;*/
    cout<<"Time Passed From last Backup: "<< current_date_in_seconds - last_date_in_seconds<<endl;

    if(current_date_in_seconds - last_date_in_seconds >= 86400){ //120 sarebbero 2 minuti e' un valore momentaneo da test
        system("java -jar AutoBackupProgramv1.jar");
    }

    f1.close();

    return 0;
}