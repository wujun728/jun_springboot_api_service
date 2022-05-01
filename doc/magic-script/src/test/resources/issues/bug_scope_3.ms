var p_ = [
        {
            "relation": "AND",
            "parameters": [
                {
                    "relation": "OR",
                    "parameters": [
                        {
                            "relation": "OR",
                            "key": "Q^bill_code_^SL",
                            "value": "213",
                            "param": "04357fd5-40c4-402d-89aa-9884f9dd7281"
                        },
                        {
                            "relation": "OR",
                            "key": "Q^emp_code_^SL",
                            "value": "213",
                            "param": "1c3aeaaf-38a8-499e-b52f-a17062b49112"
                        }
                    ]
                }
            ]
        },
        {
            "key": "Q^emp_code_^SL",
            "value": "1"
        }]
var call_ = (p_)=>{
    var parameters_ = [];
    p_.map(p__=>{
        var apiP__ = {key:p__.key}
        if(p__.parameters){
            apiP__.parameters = call_(p__.parameters);
        }
        parameters_.push(apiP__);
    })
    return parameters_;
}
var f = (data) => {return select t.key,t.parameters && f(t.parameters) parameters from data t}
var x = f(p_)::string;
var y = call_(p_)::string;
if(x == '[{key=null, parameters=[{key=null, parameters=[{key=Q^bill_code_^SL, parameters=null}, {key=Q^emp_code_^SL, parameters=null}]}]}, {key=Q^emp_code_^SL, parameters=null}]'){
    return y;
}