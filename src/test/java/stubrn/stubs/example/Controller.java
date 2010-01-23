package stubrn.stubs.example;

import java.util.List;

/**
 *
 */
public class Controller {

    private EntityDAO entityDAO;

    public void setEntityDAO(EntityDAO entityDAO) {
        this.entityDAO = entityDAO;
    }

    public List<Entity> listEntities(String name){
        return entityDAO.findByName(name);
    }
}
