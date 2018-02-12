# Author : Bc. Juraj Vraniak
# Name: codeAnalysator.py
# Date : 24.9.2017
# Description : Script to analyse source codes.
# version 1.0

import os
import subprocess
# from subprocess import Popen, PIPE
# from shlex import split

# extensions = list()
extensions=[".js", ".py", ".coffee", ".bat", ".sh"]
all_files = list()
report_path = '/home/jv/Documents/Skola/Diplomovka/Diplomova-Praca/Sources/scripts/scriptOut/'
sourcePath='/home/jv/Documents/Skola/Diplomovka/Diplomova-Praca/Sources/sources_for_analysis/'
analyzedProjects = ['angular', 'atom', 'free-python-games', 'MINGW-packages', 'og-aws', 'oh-my-zsh', 'rails', 'react', 'scikit-learn', 'stacker']
merged_statements = ['import ', 'static import ','&&', '||', '&', '|', '!', '==', '<', '>', '>=', '<=', '!=', '^', '~', '<<', '>>', '+', '-', '\*', '/', '%', '+=', '\-=', '*=', '/=', '%=', '**=', '//=', '++', '\-\-', 'if', 'else if', 'else', 'switch', 'case', 'for', 'while', 'do', 'done']

def get_all_files(searchPath):
    for root, dirs, files in os.walk(searchPath):
        for file in files:
            all_files.append(os.path.join(root, file))

def load_extensions():
    get_all_files(sourcePath)
    for path in all_files:  
        ext = os.path.splitext(path)[1]
        if(ext not in extensions):
            extensions.append(ext)

def main():
    # load_extensions()  
    for project in analyzedProjects:
        print('project : ', project)
        for ext in extensions:
            print('ext : ', ext)
            for stat in merged_statements:
                newPath = sourcePath+project
                print(stat, ' ',subprocess.call("find "+newPath+" -name "+ext+" -type f | xargs grep -c "+stat+" | tr \":\" \" \" | awk '{s+=$2} END {print s}'", shell=True))
                # print('path ->',newPath,' ext -> ', ext, ' stat -> ', stat)
                # subprocess.call(['find',newPath,'-name',ext,'-type','f','|','xargs','grep','-c',stat])
                # subprocess.call(['find',newPath,'-name',ext,'-type','f','|','xargs','grep','-c',stat,'|','tr','":"','" "','|','awk','{s+=$2} END {print s}'])
                # subprocess.call("find "+newPath+" -name "+ext+" -type f | xargs grep -c "+stat+" | tr \":\" \" \" | awk '{s+=$2} END {print s}'", shell=True)

if __name__ == '__main__':
    main()