/*
    Author  -> Dennis Turco
    Year    -> 2022

    WebSite -> https://dennisturco.github.io/
    GitHub  -> https://github.com/DennisTurco?tab=repositories
*/

#include <iostream>
#include <fstream>
#include <string>
#include <chrono>
#include <ctime> 
#include <time.h>

// Get current date/time, format is YYYY-MM-DD
const std::string currentDateTime() {
    time_t     now = time(0);
    struct tm  tstruct;
    char       buf[80];
    tstruct = *localtime(&now);
    strftime(buf, sizeof(buf), "%Y-%m-%d", &tstruct);

    return buf;
}

int main() {

    // ottengo la prossima data per il backup dal file
    std::string new_date;
    std::ifstream file("./res/next_backup.json");
    if (file.is_open()){
        getline(file, new_date); //ottengo la stringa dal file
        file.close();
    }

    //ottengo il tempo attuale
    std::string current_date = currentDateTime();


    // confronto le date per vedere se eseguire un backup automatico
    if (new_date <= current_date) {
        // a questo punto avvio il programma automaticamente
        system("java -jar AutoBackupProgram.jar");
    }

    return EXIT_SUCCESS;
}