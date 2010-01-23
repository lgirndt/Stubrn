package stubrn.stubs.example;

/**
 *
 */
public class Entity {
    
    private int id;
    private String name;
    private int age;

    public Entity(int id) {
        this.id = id;
    }

    public Entity(String name) {
        this.name = name;
    }

    public Entity(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
