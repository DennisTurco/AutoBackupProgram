package table;

public interface TableActionEvent {
    public void onEdit(int row);
    public void onDelete(int row);
}
