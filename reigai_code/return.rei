// file created on 18/05/2023

fun fib(n){
    if(n <= 1) return n;
    return fib(n-2) + fib(n - 1);
}

for(var i = 1; i <= 20; i = i + 1){
    print fib(i);
}
