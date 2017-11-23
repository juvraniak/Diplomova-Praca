#!/bin/bash
#
# Author : Bc. Juraj Vraniak
# Name: codeAnalysator.sh
# Date : 24.9.2017
# Description : Script to analyse source codes.
# version 1.0
#



SOURCES_PATH=/home/jv/Documents/Skola/Diplomovka/Diplomova-Praca/Sources/sources_for_analysis

get_number_of_total_rows_per_extension(){  
    count=$(find $SOURCES_PATH -name $1 -type f | xargs -d '\n' wc -l | awk '{s+=$1} END {print s/=2}')
    echo $count
}

get_number_of_occurences(){
    count=$(find $SOURCES_PATH -name $1 -type f | xargs grep -c $2 | tr ":" " " | awk '{s+=$2} END {print s}')
    echo $count
}

loop_array(){
    declare -a arr_to_loop=("${!1}")
       
    for operator in "${arr_to_loop[@]}";
        do
            count=`get_number_of_occurences $ext $operator`
            echo $operator ":" $count
        done
}

main(){
    local extensions=("*.js" "*.py" "*.coffee" "*.bat" "*.sh")
    # local extensions=("*.sh")

    local imports_search=("import" "static import")
    local logic_operators=("&&" "||" "&" "|" "!" "==" "<" ">" ">=" "<=" "!=" "^" "~" "<<" ">>")
    local math_operators=("+" "-" "\*" "/" "%" "+=" "\-=" "*=" "/=" "%=" "**=" "//=" "++" "\-\-")
    local decision_statements=("if" "else if" "else" "switch" "case")
    local loop_statements=("for" "while" "do" "done")

    for ext in ${extensions[@]};
    do
        echo "*************** "$ext" ***************"

        loop_array imports_search[@] $ext
        loop_array logic_operators[@] $ext
        loop_array math_operators[@] $ext
        loop_array decision_statements[@] $ext
        loop_array loop_statements[@] $ext
        


        count=`get_number_of_total_rows_per_extension $ext`
        echo "Total rows examined : " $count
        echo "*************** "$ext" **************"
        echo
    done
}

main