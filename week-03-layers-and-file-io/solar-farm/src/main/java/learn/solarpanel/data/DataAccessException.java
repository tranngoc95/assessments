package learn.solarpanel.data;

public class DataAccessException extends Exception {
    public DataAccessException(String msg){
        super(msg);
    }

    public DataAccessException(String msg, Throwable cause){
        super(msg, cause);
    }

}
