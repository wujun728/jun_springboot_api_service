var list = [
    {children:[]},
    {children: [{children:[]}]},
    {children:[]}
]

var compute = (items) => {
    for (item in items) {
        if (item.children?.size() > 0) {
            compute(item.children)
        }
        item.count = item.children?.size() || 0
    }
}
compute(list)
println(list)
return list.toString() == '[{children=[], count=0}, {children=[{children=[], count=0}], count=1}, {children=[], count=0}]';