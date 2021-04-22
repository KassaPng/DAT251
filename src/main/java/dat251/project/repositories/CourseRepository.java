package dat251.project.repositories;

import dat251.project.entities.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, String> {

    Course findById(long id);

    Course findByName(String courseName);

}
