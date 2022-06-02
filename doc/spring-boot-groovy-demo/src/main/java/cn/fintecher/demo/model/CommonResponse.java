package cn.fintecher.demo.model;

import lombok.Data;

/**
 * Created by ChenChang on 2018/6/13.
 */
@Data
public class CommonResponse {
    private String responseCode;
    private String responseMsg;
    private String bizResponseMsg;
    private Object data;
    private Object originalData;
    private String flag;
}
