package common;

import exceptions.UnknownResponseException;

public enum Response {
    SUCCESS_FILENAME_TRANSFER(201),
    SUCCESS_FILE_TRANSFER(202),
    FAILURE_FILENAME_TRANSFER(101),
    FAILURE_FILE_TRANSFER(102);

    private final int code;

    Response(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Response getResponseByCode(int code) throws UnknownResponseException {
        for (Response responseCode: Response.values()){
            if (responseCode.code == code){
                return responseCode;
            }
        }
        throw new UnknownResponseException("No response code = " + code);
    }
}
