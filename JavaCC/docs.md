### `all`
```
all :: [bool] -> bool
```
Checks whether each element in the list evaluates to `true`

### `genList`
```
genList :: (num -> b) -> num -> [b]
```
Generates a list of size `num`, starting at 0
For example, `genList (x -> x * x) 5` produces `[0, 1, 4, 9, 16]`

### `map`
```
map :: (a -> b) -> [a] -> [b]
```
Maps each element from the input list using the function provided
For example, `map (x -> x * x) [1, 2, 3]` produces `[1, 4, 9]`

