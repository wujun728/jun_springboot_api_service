var str = "";
return str.call(()=>{
    import test;
    return test.format(test.parse("20210101"))
});