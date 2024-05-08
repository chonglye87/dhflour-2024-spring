package com.dhflour.dhflourdemo1.core.types.error;

public class CRUDException extends RuntimeException {
    private static final long serialVersionUID = -4309216903071981515L;

    public CRUDException(Long id, String entityName, String message) {
        super(String.format("( ID : %s ) %s - %s", id, entityName, message));
    }

    public CRUDException(String id, String entityName, String message) {
        super(String.format("( ID : %s ) %s - %s", id, entityName, message));
    }
}
