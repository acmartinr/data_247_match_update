package models;

public class Response {
    private ResponseStatus status;
    private String message;
    private Object data;

    public Response() {}

    private Response(ResponseStatus status,
                     String message,
                     Object data ) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseStatus getStatus() { return status; }
    public void setStatus( ResponseStatus status ) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage( String message ) { this.message = message; }

    public Object getData() { return data; }
    public void setData( Object data ) { this.data = data; }

    public enum ResponseStatus {
        OK,
        WARNING,
        ERROR
    }


    public static Response OK( String message, Object data ) {
        return new Response( ResponseStatus.OK, message, data );
    }

    public static Response OK( Object data ) {
        return OK( null, data );
    }

    public static Response OK( String message ) {
        return OK( message, null );
    }

    public static Response OK() {
        return OK( null, null );
    }


    public static Response WARNING( String message, Object data ) {
        return new Response( ResponseStatus.WARNING, message, data );
    }

    public static Response WARNING( Object data ) {
        return WARNING( null, data );
    }

    public static Response WARNING( String message ) {
        return WARNING( message, null );
    }

    public static Response WARNING() {
        return WARNING( null, null );
    }


    public static Response ERROR( String message, Object data ) {
        return new Response( ResponseStatus.ERROR, message, data );
    }

    public static Response ERROR( Object data ) {
        return ERROR( null, data );
    }

    public static Response ERROR( String message ) {
        return ERROR( message, null );
    }

    public static Response ERROR() {
        return ERROR( null, null );
    }
}