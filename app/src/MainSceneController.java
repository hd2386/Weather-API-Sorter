import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import javax.print.DocFlavor.STRING;
import javax.xml.transform.ErrorListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class MainSceneController {

    public static final String API_KEY = "92c8655787msh7ea35a1c5b792ebp1e18dbjsne33e6f59238a";

    @FXML
    private Button Sendbutton;

    @FXML
    private TextField UrlField;

    @FXML
    private Label UrlLabel;

    @FXML
    private TableView<Parameter> Table;

    @FXML
    private TableColumn<Parameter, String> nameCol;

    @FXML
    private TableColumn<Parameter, String> valueCol;

    @FXML
    private Label Rspnslabel;

    @FXML
    private ComboBox<String> SelectBox;

    @FXML
    private TextArea Responsefield;

    @FXML
    private Label errLabel;

    @FXML
    public void btnsendClicked(ActionEvent event) throws JSONException {
        Responsefield.setEditable(false);

        try {
            errLabel.setVisible(false);
            errLabel.setText("");

            URL url = new URL(UrlField.getText());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("content-type", "application/octet-stream");
            con.setRequestProperty("X-RapidAPI-Key", API_KEY);
            con.setRequestProperty("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com");
            con.connect();

            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(con.getInputStream())); // konventiert von Bytecode zu JSON
                                                                                  // String
            String responseBody = br.lines().collect(Collectors.joining());

            String formattedbody = formatJsonString(responseBody);

            Responsefield.setText(formattedbody);

            int responsecode = con.getResponseCode();
            // 200 ist erfolgreich

            if (responsecode >= 200 && responsecode < 300) { // check every success code
                errLabel.setVisible(true);
                errLabel.setText("OK");
                errLabel.setTextFill(javafx.scene.paint.Color.GREEN);

                // System.out.println(getParamsFromURL(UrlField.getText()));
                Table = new TableView<>();
                nameCol = new TableColumn<>();
                nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                valueCol = new TableColumn<>();
                valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
                // Table.getColumns().addAll(nameCol, valueCol);
                Table.getColumns().add(nameCol);
                Table.getColumns().add(valueCol);
                // ObservableList<Parameter> params = getParamsFromURL(UrlField.getText());
                Table.getItems().add(new Parameter());
                ObservableList<Parameter> data = FXCollections.observableArrayList();
                data.addAll(getParamsFromURL(UrlField.getText()));
                Table.setItems(data);
                UrlField.setText(" ");

                File logFile;
                LocalDateTime timestamp = LocalDateTime.now();
                String logEntry = timestamp + " INFO - Request sent: \nRequest sent: \"" + url
                        + "\", \nResponse received: \""
                        + formattedbody + "\"\n";
                try {
                    logFile = new File(
                            "C:/Users/HD/Desktop/Uni/Coding/.vscode/semester_zwei/app/src/log.txt");
                    FileWriter fileWriter = new FileWriter(logFile, true);
                    fileWriter.write(logEntry);
                    fileWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            String text = errLabel.getText();
            if (errLabel.isVisible() && text.contains("OK")) {
                String response = Responsefield.getText();
                updateComboBoxOptions(response, SelectBox);

            }

        } catch (IOException e) {
            Responsefield.setText("Error: " + e.getMessage());

            errLabel.setVisible(true);
            errLabel.setText("NOK");
            errLabel.setTextFill(javafx.scene.paint.Color.RED);

        }

    }
    // https://api.openweathermap.org/data/3.0/onecall?lat=48.763016&lon=11.425039&exclude=minutely,hourly,daily,alerts&appid=889b48722656894e72fac7d0ad41ff3d

    public static List<Parameter> getParamsFromURL(String url) {
        List<Parameter> params = new ArrayList<>();
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] parts = pair.split("=");
                    if (parts.length == 2) {
                        String key = parts[0];
                        String value = parts[1];
                        params.add(new Parameter(key, value));
                    }

                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return params;
    }

    private static String formatJsonString(String jsonString) throws JSONException { // formating responsebody to many
                                                                                     // lines
        Object obj = new JSONTokener(jsonString).nextValue();
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject.toString(5); // Einzug mit 4 Leerzeichen
        } else if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            return jsonArray.toString(5); // Einzug mit 4 Leerzeichen
        } else {
            return null;
        }

    }

    private void updateComboBoxOptions(String jsonString, ComboBox<String> comboBox) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        SelectBox.setItems(findKeys(jsonObject));
        SelectBox.setOnAction(event -> {

            String selectedItem = SelectBox.getSelectionModel().getSelectedItem();
            try {
                if (selectedItem != null && !selectedItem.isEmpty()) {
                    Object selectedValue = getValueFromPath(jsonObject, selectedItem);
                    Responsefield.setText(selectedValue.toString());

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });

    }

    public static ObservableList<String> findKeys(JSONObject obj) throws JSONException {
        ObservableList<String> keys = FXCollections.observableArrayList();
        Iterator iterator = obj.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = obj.get(key);
            if (value instanceof JSONObject) {
                // Falls der Wert ein JSONObject ist, rufe die Methode rekursiv auf und füge die
                // Ergebnisse hinzu
                ObservableList<String> childKeys = findKeys((JSONObject) value);
                for (String childKey : childKeys) {
                    keys.add(key + "." + childKey);
                }
            } else if (value instanceof JSONArray) {
                // Falls der Wert ein JSONArray ist, rufe die Methode rekursiv auf und füge die
                // Ergebnisse hinzu
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object arrayValue = array.get(i);
                    if (arrayValue instanceof JSONObject) {
                        ObservableList<String> childKeys = findKeys((JSONObject) arrayValue);
                        for (String childKey : childKeys) {
                            keys.add(key + "[" + i + "]." + childKey);
                        }
                    }
                }
            } else {
                // Wenn der Wert ein primitiver Datentyp oder ein String ist, füge den Schlüssel
                // direkt hinzu
                keys.add(key);
            }
        }

        return keys;
    }

    public static Object getValueFromPath(JSONObject json, String path) throws JSONException {
        String[] parts = path.split("\\.");
        Object value = json;
        for (String part : parts) {
            if (value instanceof JSONObject) {
                value = ((JSONObject) value).get(part);
            } else if (value instanceof JSONArray) {
                int index = Integer.parseInt(part);
                value = ((JSONArray) value).get(index);
            }
        }
        return value;
    }

    @FXML
    private void sendButtonStatus() {

        UrlField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.startsWith("https://")) {
                Sendbutton.setDisable(false);
            } else {
                Sendbutton.setDisable(true);
            }
        });
    }

}