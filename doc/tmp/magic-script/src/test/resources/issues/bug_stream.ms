import java.util.stream.Collectors
import java.util.Collections
var list = [
    {name: 'a'},
    {name: 'b'}
]
return list.stream().map(it -> Collections.singletonList(it.name))
            .flatMap(x -> x.stream())
            .collect(Collectors.toList())
            + ''
