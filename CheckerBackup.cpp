/*
    Author  -> Dennis Turco
    Year    -> 2022

    WebSite -> https://dennisturco.github.io/
    GitHub  -> https://github.com/DennisTurco?tab=repositories

    Notes:
        for compile use Linux terminal.
        it require libjsoncpp-dev library so you need to install it from terminal with: sudo apt-get install libjsoncpp-dev
        compile with:   g++ -xc++ CheckerBackup.cpp -ljsoncpp
        run with:       ./a.out
*/

#include <iostream>
#include <jsoncpp/json/value.h>
#include <jsoncpp/json/json.h>
#include <fstream>
#include <string>
#include <boost/json/src.hpp>

#include <filesystem>
#include <stdio.h>
#include <dirent.h>

// https://stackoverflow.com/questions/5283120/date-comparison-to-find-which-is-bigger-in-c
void comparison_dates() {

}

// https://stackoverflow.com/questions/1442116/how-to-get-the-date-and-time-values-in-a-c-program
void get_date () {

}

// https://stackoverflow.com/questions/72497520/how-to-know-count-of-json-objects-in-the-file
int get_number_object() {
    auto v = boost::json::parse(sample, {}, {.allow_trailing_commas=true});
    return v.at("media").as_array().size();
}

using std::cout; using std::cin;
using std::endl; using std::string;
using std::filesystem::directory_iterator;

int main(){

    std::ifstream file("./res/backup_list.json");
    Json::Value actualJson;
    Json::Reader reader;
    	
    reader.parse(file, actualJson);

    // print the whole file: std::cout << actualJson;

    

    return EXIT_SUCCESS;
}