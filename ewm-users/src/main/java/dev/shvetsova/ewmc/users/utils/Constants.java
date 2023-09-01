package dev.shvetsova.ewmc.users.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);

    public static final String USER_WITH_ID_D_WAS_NOT_FOUND = "User with id=%d was not found.";
    public static final String THE_REQUIRED_OBJECT_WAS_NOT_FOUND = "The required object was not found.";

    public static final String INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED = "Integrity constraint has been violated.";
    public static final String FOR_THE_REQUESTED_OPERATION_THE_CONDITIONS_ARE_NOT_MET = "For the requested operation the conditions are not met.";

    public static final String FROM = "0";
    public static final String PAGE_SIZE = "10";

}