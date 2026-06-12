import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
 
public class App {
 
    private final Stage stage;
    private final DatabaseAUTH auth = new DatabaseAUTH();
    private final StudentDataBase db = new StudentDataBase();
 
    public App(Stage stage) {
        this.stage = stage;
        stage.setTitle("Student Enrollment System");
    }
 
   
    // LOGIN SCREEN

    public void showLogin() {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
 
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
 
        Label message = new Label();
 
        Button loginBtn  = new Button("Log In");
        Button signupBtn = new Button("Sign Up");
 
        loginBtn.setOnAction(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) { message.setText("Fill in both fields."); return; }
            if (auth.login(u, p)) showDashboard();
            else message.setText("Wrong username or password.");
        });
 
        signupBtn.setOnAction(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) { message.setText("Fill in both fields."); return; }
            if (auth.signup(u, p)) message.setText("Account created. You can log in now.");
            else message.setText("Signup failed. Username may already exist.");
        });
 
        HBox buttons = new HBox(8, loginBtn, signupBtn);
 
        VBox layout = new VBox(10,
            new Label("Username:"), usernameField,
            new Label("Password:"), passwordField,
            buttons, message
        );
        layout.setPadding(new Insets(30));
 
        stage.setScene(new Scene(layout, 320, 260));
        stage.show();
    }
 //MAIN SCREEN
    private void showDashboard() {
        TableView<Undergraduate> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
 
        table.getColumns().addAll(
    col("Name",    "name"),
    col("Age",     "age"),
    col("ID",      "id"),
    col("Contact", "contactnum"),
    col("Email",   "emailaddress"),
    col("Course 1","c1"), col("Chrs ","ch1"),
    col("Course 2","c2"), col("Chrs ","ch2"),
    col("Course 3","c3"), col("Chrs ","ch3"),
    col("Course 4","c4"), col("Chrs ","ch4"),
    col("Course 5","c5"), col("Chrs ","ch5"),
    col("Course 6","c6"), col("Chrs ","ch6")
);
 
        refreshTable(table);
 
        Button addBtn     = new Button("Add Student");
        Button deleteBtn  = new Button("Delete Student (by ID)");
        Button updateBtn  = new Button("Update Student (by ID)");
        Button refreshBtn = new Button("Refresh");
 
        addBtn.setOnAction(e -> showForm(null, table));
        deleteBtn.setOnAction(e -> showDeleteScreen(table));
        updateBtn.setOnAction(e -> showUpdateSearchScreen(table));
        refreshBtn.setOnAction(e -> refreshTable(table));
 
        HBox toolbar = new HBox(8, addBtn, deleteBtn, updateBtn, refreshBtn);
        toolbar.setPadding(new Insets(8));
 
        VBox layout = new VBox(0, toolbar, new Separator(), table);
        VBox.setVgrow(table, Priority.ALWAYS);
 
        stage.setScene(new Scene(layout, 900, 480));
    }
 

    private void showDeleteScreen(TableView<Undergraduate> table) {
        Label title = new Label("Delete Student");
        Label idLabel = new Label("Enter Student ID:");
        TextField idField = new TextField();
        Label resultLabel = new Label();
 
        Button deleteBtn = new Button("Delete");
        Button backBtn = new Button("Back");
 
        deleteBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
 
                Student s = new Student();
                s.setid(id);
 
                boolean ok = db.deletestudentbyid(s);
                if (ok) {
                    resultLabel.setText("Student with ID " + id + " deleted successfully.");
                    refreshTable(table);
                } else {
                    resultLabel.setText("No student found with ID " + id + ".");
                }
            } catch (NumberFormatException ex) {
                resultLabel.setText("ID must be a number.");
            }
        });
 
        backBtn.setOnAction(e -> showDashboard());
 
        VBox layout = new VBox(12, title, idLabel, idField, deleteBtn, resultLabel, backBtn);
        layout.setPadding(new Insets(30));
 
        stage.setScene(new Scene(layout, 360, 280));
    }
 
    
    private void showUpdateSearchScreen(TableView<Undergraduate> table) {
        Label title = new Label("Update Student");
        Label idLabel = new Label("Enter Student ID:");
        TextField idField = new TextField();
        Label resultLabel = new Label();
 
        Button searchBtn = new Button("Search");
        Button backBtn = new Button("Back");
 
        searchBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
 
                Undergraduate found = null;
                for (Undergraduate u : db.getallstudents()) {
                    if (u.getid() == id) {
                        found = u;
                        break;
                    }
                }
 
                if (found == null) {
                    resultLabel.setText("No student found with ID " + id + ".");
                } else {
                    showForm(found, table);
                }
            } catch (NumberFormatException ex) {
                resultLabel.setText("ID must be a number.");
            }
        });
 
        backBtn.setOnAction(e -> showDashboard());
 
        VBox layout = new VBox(12, title, idLabel, idField, searchBtn, resultLabel, backBtn);
        layout.setPadding(new Insets(30));
 
        stage.setScene(new Scene(layout, 360, 280));
    }
 
   
    
    private void showForm(Undergraduate existing, TableView<Undergraduate> table) {
        boolean isEdit = existing != null;
 
        // Personal info
        TextField nameField    = new TextField();
        TextField ageField     = new TextField();
        TextField idField      = new TextField();
        TextField contactField = new TextField();
        TextField cnicField    = new TextField();
        TextField fcnicField   = new TextField();
        TextField emailField   = new TextField();
 
        // Courses
        TextField[] courseFields = new TextField[6];
        TextField[] creditFields = new TextField[6];
        for (int i = 0; i < 6; i++) {
            courseFields[i] = new TextField();
            creditFields[i] = new TextField();
        }
 
        if (isEdit) {
            nameField.setText(existing.getname());
            ageField.setText(String.valueOf(existing.getage()));
            idField.setText(String.valueOf(existing.getid()));
            idField.setEditable(false); 
            contactField.setText(existing.getcontactnum());
            cnicField.setText(existing.getCNIC());
            fcnicField.setText(existing.getFCNIC());
            emailField.setText(existing.getemailaddress());
 
            courseFields[0].setText(existing.getC1()); creditFields[0].setText(String.valueOf(existing.getCh1()));
            courseFields[1].setText(existing.getC2()); creditFields[1].setText(String.valueOf(existing.getCh2()));
            courseFields[2].setText(existing.getC3()); creditFields[2].setText(String.valueOf(existing.getCh3()));
            courseFields[3].setText(existing.getC4()); creditFields[3].setText(String.valueOf(existing.getCh4()));
            courseFields[4].setText(existing.getC5()); creditFields[4].setText(String.valueOf(existing.getCh5()));
            courseFields[5].setText(existing.getC6()); creditFields[5].setText(String.valueOf(existing.getCh6()));
        }
 
        Label errorLabel = new Label();
 
        Button saveBtn   = new Button(isEdit ? "Save" : "Add");
        Button cancelBtn = new Button("Cancel");
 
        saveBtn.setOnAction(e -> {
            try {
                Undergraduate u = isEdit ? existing : new Undergraduate();
                u.setname(nameField.getText().trim());
                u.setage(Integer.parseInt(ageField.getText().trim()));
                u.setid(Integer.parseInt(idField.getText().trim()));
                u.setcontactnum(contactField.getText().trim());
                u.setCNIC(cnicField.getText().trim());
                u.setFCNIC(fcnicField.getText().trim());
                u.setemailaddress(emailField.getText().trim());
                u.setStudentid(u.getid());
 
                u.setC1(courseFields[0].getText()); u.setCh1(intVal(creditFields[0]));
                u.setC2(courseFields[1].getText()); u.setCh2(intVal(creditFields[1]));
                u.setC3(courseFields[2].getText()); u.setCh3(intVal(creditFields[2]));
                u.setC4(courseFields[3].getText()); u.setCh4(intVal(creditFields[3]));
                u.setC5(courseFields[4].getText()); u.setCh5(intVal(creditFields[4]));
                u.setC6(courseFields[5].getText()); u.setCh6(intVal(creditFields[5]));
 
                boolean ok = isEdit
                    ? db.updatestudentbyid(u, u)
                    : db.addstudent(u) && db.addCourses(u);
 
                if (ok) { refreshTable(table); showDashboard(); }
                else errorLabel.setText("Database error. Try again.");
 
            } catch (NumberFormatException ex) {
                errorLabel.setText("Age, ID, and credit hours must be numbers.");
            }
        });
 
        cancelBtn.setOnAction(e -> showDashboard());
 
        // Build form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(16));
 
        // Personal section
        grid.add(new Label("-- Personal Info --"), 0, 0, 4, 1);
        grid.addRow(1, new Label("Name:"),    nameField,    new Label("Age:"),     ageField);
        grid.addRow(2, new Label("ID:"),      idField,      new Label("Contact:"), contactField);
        grid.addRow(3, new Label("CNIC:"),    cnicField,    new Label("F.CNIC:"),  fcnicField);
        grid.addRow(4, new Label("Email:"),   emailField);
 
        // Courses section
        grid.add(new Label("-- Courses --"), 0, 5, 4, 1);
        for (int i = 0; i < 6; i++) {
            courseFields[i].setPromptText("Course " + (i + 1));
            creditFields[i].setPromptText("Credits");
            creditFields[i].setPrefWidth(55);
            grid.addRow(6 + i, new Label("C" + (i+1) + ":"), courseFields[i],
                                new Label("Hrs:"),            creditFields[i]);
        }
 
        HBox buttons = new HBox(8, saveBtn, cancelBtn);
        grid.add(errorLabel, 0, 12, 4, 1);
        grid.add(buttons,    0, 13, 4, 1);
 
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
 
        stage.setScene(new Scene(scroll, 520, 540));
    }
 

    private void refreshTable(TableView<Undergraduate> table) {
        table.setItems(FXCollections.observableArrayList(db.getallstudents()));
    }
 
    private <T> TableColumn<Undergraduate, T> col(String header, String property) {
        TableColumn<Undergraduate, T> col = new TableColumn<>(header);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }
 
    private int intVal(TextField tf) {
        try { return Integer.parseInt(tf.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }
 
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}