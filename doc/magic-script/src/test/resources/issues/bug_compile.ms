var list =  [{a:1,b:2},{a:3,b:4}];
var keys = list.first().keySet();
var rows  = [
    keys.join(','),
    ...list.map(row => keys.map(key => row[key] || '').join(','))
];
return rows.join('\r\n')