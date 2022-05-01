return
    new_array(1,2,3,4,5).class::string == 'class [I'
    && new_array("1","2","3").class::string == 'class [Ljava.lang.String;'
    && new_array(1d,2d,3d,4d,5d).class::string == 'class [D'
    && new_array(1f,2f,3f,4f,5f).class::string == 'class [F'
    && new_array(1b,2b,3b,4b,5b).class::string == 'class [B'
    && new_array(1l,2l,3l,4l,5l).class::string == 'class [J'