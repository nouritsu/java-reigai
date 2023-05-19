// file created on 17/05/2023

var a = 0;
var temp;

for(var b = 1; a < 10000; b = temp + b){
    print a;
    temp = a;
    a = b;
}
