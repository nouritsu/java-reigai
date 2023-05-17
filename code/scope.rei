// file created on 13/05/2023

print "IN SCOPE 0";

var a = "Scope 0";
var b = "Scope 0";

print a;
print b;

{
    print "IN SCOPE 1";

    var a = "Scope 1";
    print a;
    print b;

    {
        print "IN SCOPE 2";
        a = "Modified in Scope 2";
        var b = "Scope 2";
        print a;
        print b;
    }

    print "IN SCOPE 1";
    print a;
    print b;
}

print "IN SCOPE 0";
print a;
print b;