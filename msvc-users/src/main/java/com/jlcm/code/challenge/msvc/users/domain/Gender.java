package com.jlcm.code.challenge.msvc.users.domain;

public enum Gender {
    MALE("male", 0), FEMALE("female", 1), OTHER("other", 2);

    private final String label;
    private final int index;

    Gender(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public static Gender fromIndex(int index) {
        for (Gender gender : Gender.values()) {
            if (gender.index == index) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid index for Gender: " + index);
    }

    public static Gender fromLabel(String label) {
        for (Gender gender : Gender.values()) {
            if (label != null && label.equalsIgnoreCase(gender.label)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid label for Gender: " + label);
    }
}
