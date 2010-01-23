package stubrn.stubs.example;

import java.util.List;

/**
 *
 */
public interface EntityDAO {

    public List<Entity> findByName(String name);
    public Entity findById(int id);
    public void store(Entity entity);
    public List<Entity> findByAge(int age);
    public List<Entity> findByAge(int from,int to);
}
