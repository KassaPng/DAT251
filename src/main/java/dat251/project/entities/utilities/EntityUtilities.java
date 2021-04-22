package dat251.project.entities.utilities;

import dat251.project.entities.Course;
import dat251.project.entities.Group;
import dat251.project.entities.User;

import java.util.List;

public class EntityUtilities {

    public static String printListContents(List<?> array) {
        if (array.isEmpty()) {
            return "[]";
        }
        StringBuilder out = new StringBuilder("[ ");
        for (Object obj : array) {
            if (obj instanceof User) {
                out.append(((User) obj).getUserName());
            } else if (obj instanceof Group) {
                out.append(((Group) obj).getGroupName());
            } else if (obj instanceof Course) {
                out.append(((Course) obj).getName());
            } else  {
                throw new IllegalArgumentException("Array content not recognized");
            }
            out.append(", ");
        }
        out.deleteCharAt(out.length() - 2);
        out.append("]");
        return out.toString();
    }
}
