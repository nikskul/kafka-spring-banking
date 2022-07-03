package com.nikskul.kafkaspringbanking.exeption;

import java.util.Objects;

public record ErrorResponse(String error, String description) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ErrorResponse) obj;
        return Objects.equals(this.error, that.error) &&
               Objects.equals(this.description, that.description);
    }

    @Override
    public String toString() {
        return "ErrorResponse[" +
               "error=" + error + ", " +
               "description=" + description + ']';
    }


}
