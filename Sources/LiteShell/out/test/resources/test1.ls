use copy 1.0;
use move 1.0;

// zeby bol aj daky komentaris
    // zeby bol aj daky komentaris aj s odsadenim

int globalVar = \
8;

function void main(String arg){
  int i = 5;
  ${i} = 9;
  copy /home/jv/examples.desktop /home/jv/Downloads/;
  move /home/jv/Downloads/examples.desktop \
  /home/jv/Documents/examples.desktop;
  rm /home/jv/Documents/examples.desktop;
  rm /home/jv/Downloads/examples.desktop;
  ls /home/jv/ | grep ecli;
  test();
}

@Override
function void test(){
  String path = "/home/jv";
  ls ${path};
}

function void test(){
  int i = 4;
  ls;
}