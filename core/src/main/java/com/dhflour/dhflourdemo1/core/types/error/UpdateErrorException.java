package com.dhflour.dhflourdemo1.core.types.error;

public class UpdateErrorException extends CRUDException {

    private static final long serialVersionUID = -7840105682252209337L;

    public UpdateErrorException(Long id, String entityName) {
        super(id, entityName, "UPDATE ERROR");
    }

    public UpdateErrorException(String id, String entityName) {
        super(id, entityName, "UPDATE ERROR");
    }
}