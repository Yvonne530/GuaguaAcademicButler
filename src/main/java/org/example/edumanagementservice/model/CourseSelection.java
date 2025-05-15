package org.example.edumanagementservice.model;

@Entity
@Table(name = "course_selection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String courseCode;
    private String semester;
}
