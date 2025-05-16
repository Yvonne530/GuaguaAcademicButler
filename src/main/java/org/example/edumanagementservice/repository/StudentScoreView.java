package org.example.edumanagementservice.repository;

public interface StudentScoreView {
    Integer getStudentId();

    String getStudentName();

    Integer getCourseId();

    String getCourseName();

    Double getScore();

    String getGrade();

    String getTeacherName();
}
