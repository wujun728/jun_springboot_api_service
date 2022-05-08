var groups = [
    { id: 100, group_name: '组1' },
    { id: 101, group_name: '组2' }
]
var persons = [
    { id: 1, name: 'A', group_id: 100 },
    { id: 2, name: 'B', group_id: 100 },
    { id: 3, name: 'C', group_id: 101 }
]
return (select g.*,group_concat(p.name) names from groups g left join persons p on p.group_id = g.id group by g.id).toString()
