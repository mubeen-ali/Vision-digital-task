package au.net.horizondigital.assessment.enums;

public enum OrderStatus {
    PENDING('P'), SERVED('S'), CANCELED('C');

    private char status;

    OrderStatus(char status) {
        this.status = status;
    }

    public char getStatus() {
        return status;
    }
}
