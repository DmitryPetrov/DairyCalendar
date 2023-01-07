package net.personal.dairycalendar.exception;

public class RecordIsNotExistException extends RuntimeException {

    public RecordIsNotExistException() {
    }

    public RecordIsNotExistException(Class className, long id) {
        super("There is no entity [" + className.getName() + "] with id [" + id + "] in database");
    }

    public RecordIsNotExistException(Class className, String fieldName, String fieldValue) {
        super("There is no entity [" + className.getName() + "] " +
                "with field [" + fieldName + "] has value [" + fieldValue + "] in database");
    }
}
