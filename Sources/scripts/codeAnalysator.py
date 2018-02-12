# Author : Bc. Juraj Vraniak
# Name: codeAnalysator.py
# Date : 24.9.2017
# Description : Script to analyse source codes.
# version 1.0

import os
import subprocess

extensions = list()
# extensions=[".js", ".py", ".coffee", ".bat", ".sh"]
report_path = '/home/jv/Documents/Skola/Diplomovka/Diplomova-Praca/Sources/scripts/scriptOut/'

class HelpData(object):
    searchPath = ""
    project = ""
    extension_map = dict()

    def __init__(self, searchPath, project, extensions):
        self.searchPath = searchPath
        self.project = project
        # self.init_extension_map(extensions)

    def init_extension_map(self, extensions):
        for ext in extensions:
            self.extension_map[ext]=set()

    def get_search_path(self):
        return self.searchPath+self.project
    
    def insert_path(self, path):
        extension = os.path.splitext(path)[1]
        # print('path -> ', path, ' ext -> ', extension)
        if(extension not in extensions):
            self.extension_map[extension]=set()
            extensions.append(extension)
        self.extension_map[extension].add(path)

    def get_extension_map(self):
        return self.extension_map

    def get_extension_set(self, ext):
        return self.extension_map[ext]

    def get_project(self):
        return self.project       

sourcePath='/home/jv/Documents/Skola/Diplomovka/Diplomova-Praca/Sources/sources_for_analysis/'
# analyzedProjects = ['nmap','pix2pix', 'angular', 'atom', 'free-python-games', 'MINGW-packages', 'og-aws', 'oh-my-zsh', 'rails', 'react', 'scikit-learn', 'stacker','kong']
analyzedProjects = ['moonshine','GroupButler','luakit','lua-resty-websocket']
merged_statements = ['import', 'static import','&&', '||', '&', '|', '!', '==', '<', '>', '>=', '<=', '!=', '^', '~', '<<', '>>', '+', '-', '\*', '/', '%', '+=', '\-=', '*=', '/=', '%=', '**=', '//=', '++', '\-\-', 'if', 'else if', 'else', 'switch', 'case', 'for', 'while', 'do', 'done']

def get_all_files(helpData):
    # print(helpData.get_search_path(), '\n')
    for root, dirs, files in os.walk(helpData.get_search_path()):
        for file in files:
            helpData.insert_path(os.path.join(root, file))
    return helpData

def count_occurence(searchArray, filesList, fh):
    # go throu every search word
    for keyWord in searchArray:
        # in every file
        count = 0
        for file in filesList:
            count += subprocess.call(['grep','-c',keyWord, file])
        fh.write(keyWord+ ' '+ str(count)+'\n')
            # time.sleep(5.5)
            # print(keyWord, ': ', subprocess.call(['grep','-c',keyWord, file]),'\n')

def load_project_data():
    helpDatas = list()
    # for loop to gather all paths in project - devided by extension
    for project in analyzedProjects:
        tempData = HelpData(sourcePath, project, extensions)
        helpDatas.append(get_all_files(tempData))
    return helpDatas

def print_results(projectDatas):
    for data in projectDatas:
        project = data.get_project()
        reportOut = report_path + project
        fh = open(reportOut ,'w')
        fh.write('Report for : '+ project + '\n')

        for ext in data.get_extension_map():
            fh.write('Extension : '+ ext + '\n')
            count_occurence(merged_statements, data.get_extension_set(ext), fh)
        fh.close()

def main():
    projectDatas = load_project_data()  
    print_results(projectDatas)

if __name__ == '__main__':
    main()