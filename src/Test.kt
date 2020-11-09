fun fib(n: Int): Int {
    var a = 0
    var b = 1
    var c = 0
    if (n < 2) return n
    var i = 1
    while (i < n) {
        c = a + b
        a = b
        b = c
        i++
    }
    return c
}

fun main() {
    print(fib(10))
}