// file created on 19/05/2023

class Foo {
    bar() {
        print this.message;
    }
}

var a = Foo();
a.message = "Hello this!";
a.bar();
