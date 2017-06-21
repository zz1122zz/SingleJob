package org.creation.singlejob;

public enum SingleJobPolicy {

    WAIT_IN_QUENE_AND_USE_SAME_RETURN(0),

    WAIT_IN_QUENE_TO_PROCEED(2),

    REJECT(1);

    private final int value;

    SingleJobPolicy(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
