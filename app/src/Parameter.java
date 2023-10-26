import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

public class Parameter {
    private String Name;
    private String Value;

    public Parameter() {
        this.Name = "";
        this.Value = "";
    }

    public Parameter(String Name, String value) {
        this.Name = Name;
        this.Value = value;

    }

    public String getName() {
        return Name;
    }

    public String getValue() {
        return Value;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setValue(String value) {
        this.Value = value;
    }
}
