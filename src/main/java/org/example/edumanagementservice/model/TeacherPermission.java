package org.example.edumanagementservice.model;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher_permission")
public class TeacherPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer teacherId;

    private Boolean canPublishCourse;
}
