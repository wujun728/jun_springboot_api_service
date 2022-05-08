var s1 = "123aaa";
var array = [1,2,3];
var map = {key : 'value'}
return s1::int(0) === 0
    && "123"::int === 123
    && "123"::double === 123d
    && 123d::int === 123
    && ("2000-01-01"::date('yyyy-MM-dd')).time === 946656000000L
    && 654::double === 654d
    && array[0]::double === 1d
    && map[key]::int(2) === 2
    && map.key::int === null
    && ("123456" + 4)::int == 1234564
    && null::long::double?.asDate()::int(6) === 6

