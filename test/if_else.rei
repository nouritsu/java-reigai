// file created on 14/05/2023

var a = true;
if(a){
    print "A is true"; // Should print
} else {
    print "A is false"; // Should not print
}

var b = 5;
if(b < 10){
    print "B is less than 10"; // Should print
} else {
    print "B is greater than or equal to 10"; // Should not print
}

a = false;
if(a){
    print "A is true"; // Should not print
} else {
    print "A is false"; // Should print
}

b = 10;
if(b < 10){
    print "B is less than 10"; // Should not print
} else {
    print "B is greater than or equal to 10"; // Should print
}
