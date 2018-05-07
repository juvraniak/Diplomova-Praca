use copy 1.0;
use move 1.0;

// zeby bol aj daky komentaris
    // zeby bol aj daky komentaris aj s odsadenim

function void main(String arg){
  int i =5;
  ${i}=9;
  copy /home/jv/examples.desktop /home/jv/Downloads/;
  move /home/jv/Downloads/examples.desktop \
  /home/jv/Documents/examples.desktop;
  rm /home/jv/Documents/examples.desktop;
  ls /home/jv/ | grep ecli;
  test();
}

function void test(){
  int i =4;
  ls ${i};
}