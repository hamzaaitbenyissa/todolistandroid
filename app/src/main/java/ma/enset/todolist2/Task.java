package ma.enset.todolist2;

public class Task {

    private Long id;
    private String description;

    public Task(String id,String description) {
        this.id=Long.parseLong(id);
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
