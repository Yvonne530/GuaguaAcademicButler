package org.example.edumanagementservice.util;

public class AccountGenerator {

    public static String generateTeacherAccount(int year, int deptId, int index) {
        return String.format("%02d%03d%04d", year % 100, deptId, index);
    }

    public static String generateStudentAccount(int year, int deptId, int majorId, int classId, int index) {
        return String.format("%02d%03d%02d%02d%02d", year % 100, deptId, majorId, classId, index);
    }
}
