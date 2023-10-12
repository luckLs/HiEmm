package com.lhc.newV.framework.common.result;

import lombok.Data;


/**
 * @author liaohaicheng
 */
@Data
public class ResultData<T> {
    private int code;
    private String mes;
    private T data;

    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ResultCode.RC100.getCode());
        resultData.setMes(ResultCode.RC100.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> fail(int code, String mes) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMes(mes);
        return resultData;
    }

}