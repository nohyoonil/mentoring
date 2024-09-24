package kookmin.kookmin.enums;

public enum ReservationStatus {
    noMoney(1),
    confirmStay(2),
    history(3),
    refund(4);

    private final int value;

    ReservationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}