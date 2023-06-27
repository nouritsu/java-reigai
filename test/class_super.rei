// file created on 27/06/2023

class Foo {
    bar() {
        print "Method of Foo";
    }
}

class SubFoo < Foo {
    sub_bar(){
        print "Method of SubFoo";
    }
}

var a = SubFoo();
a.bar();
a.sub_bar();
