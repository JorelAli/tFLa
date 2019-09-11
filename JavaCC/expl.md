```
include {
	hello = { hello = "hello"; }.((hello -> hello ++ "hello") |> | |> | hello' ->> hello' | ->> hello' ->> "" | ->> "hello");
	hello' = true;
	world = {world = "hello";} /+ {world = "world!";};
} in {hello = hello ++ ", " ++ match "world!" | '\w[o|r]+ld!' ->> |> | world.world == "world!" ->> "world!" | ->> "hello" | ->> "hello";}.hello
```

There are 3 declared functions: `hello`, `hello'` and `world`.

`hello'` is true. It's basically an alias for true. That's it.

`hello` is split into multiple sections, as follows:
`{ hello = "hello"; }` - Creates a set (basically like a HashMap) with a key called `hello` and a value of the String `"hello"`
`.` means "access the set with the following key name"
`(hello -> hello ++ "hello")` is a lambda function, taking in a parameter denoted `hello` and returns the parameter concatenated with the String `"hello"`
`|> | |> | hello' ->> hello' | ->> hello' ->> "" | ->> "hello"` is a guard expression (basically like an if ... else) which converts into the following:
```
|> 
| (|> 
    | hello' ->> hello' 
    | ->> hello') ->> ""
| ->> "hello"
```
which converts into the pseudocode:
```
boolean inner() {
    if(hello') {
        return hello'
    } else {
        return hello'
    }
}

if(inner()) {
    return ""
} else {
    return "hello"
}
```
The guard expression inner section evalutes to `true` (because of `hello'`), meaning it returns empty string.
In the function call, it concatenates whatever with the String hello, so it concatenates "" with "hello", giving us "hello"
That is then the key, which is used to access the element `hello` in the set, which returns the String `"hello"`

And that's just for the bit saying hello.

-----

World is split into two parts, the declared function `world` and the main expression at the bottom.

The function `world` is equal to the set `{ world = "hello"; }` unioned with the set `{ world = "world!"; }`. Union operators perform a union on the keys, and then replace matching values with the expression on the right, which results in the set `{ world = "world!"; }`

----

The final part is `{hello = hello ++ ", " ++ match "world!" | '\w[o|r]+ld!' ->> |> | world.world == "world!" ->> "world!" | ->> "hello" | ->> "hello";}.hello`

This evaluates to a set `{hello = ...}` (key is "hello") and then we access it with `.hello`
The set's value is equal to `hello ++ ", " ++ ...` which is `hello`, the function that is declared in the first part, which evaluates to the String `hello`. We concatenate that with a command and the next expression.

The next expression performs a pattern match on the String `"world!"`. If the String `"world!"` matches with the regular expression `\w[o|r]+ld!` (any character + one or more of (`o` or `r`) followed by "ld!"), it the guard expression `world.world == "world!"`, which takes the function `world`, which is a set containing the key `world`, accesses it (which as stated above, returns the String "world!"), and compares it with the String "world!", which is true. It then returns "world!". (If none of the previous conditions were met, i.e. the regex or the guard expression, it would return hello).

Therefore, we have the String "world!" and that's concatenated at the end, which gives us the final result of
`"hello, world!"`