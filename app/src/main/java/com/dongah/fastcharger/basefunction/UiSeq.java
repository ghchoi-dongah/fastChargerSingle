package com.dongah.fastcharger.basefunction;

public enum UiSeq {
    NONE(0),
    INIT(1),
    CABLE_SELECT(2),
    AUTH_SELECT(3),
    MEMBER_CARD(4),
    MEMBER_CARD_WAIT(5),
    CREDIT_CARD(6),
    CREDIT_CARD_WAIT(7),
    QR_CODE(8),
    PLUG_CHECK(9),
    CONNECT_CHECK(10),
    RUN_CHECK(11),
    CHARGING(12),
    FAULT(13),
    FINISH_WAIT(14),
    FINISH(15),
    PLUG_DISCONNECT(16),
    SMS(17),
    ADMIN_PASS(18),
    MESSAGE(19),
    REBOOTING(20),
    ENVIRONMENT(21),
    CONFIG_SETTING(22),
    CONTROL_BOARD_DEBUGGING(23),
    CHARGING_STOP_MESSAGE(24),
    WEB_SOCKET(25),
    LOAD_TEST(26),
    LOAD_TEST_TOTAL(27),
    LOAD_TEST_IO(28),
    CHANGE_MODE(29);

    private final int value;

    UiSeq(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
