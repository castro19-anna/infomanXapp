package com.castro.controller;

import com.castro.DatabaseConnection;
import model.newStudent; // Keeping the original class name
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {
    @FXML
    private TextField firstName, middleName, lastName, phoneNumber, email;
    @FXML
    private RadioButton male, female;

    @FXML
    private TableView<newStudent> table;
    @FXML
    private TableColumn<newStudent, String> fname, mname, lname, pnumber, pemail, pgender;

    private boolean isEditing = false;
    private int studentId = 0;

    private ToggleGroup genderGroup;
    private DatabaseConnection connection;
    private ObservableList<newStudent> studentsList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        connection = new DatabaseConnection();

        fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        mname.setCellValueFactory(new PropertyValueFactory<>("mname"));
        lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        pnumber.setCellValueFactory(new PropertyValueFactory<>("pnumber"));
        pemail.setCellValueFactory(new PropertyValueFactory<>("pemail"));
        pgender.setCellValueFactory(new PropertyValueFactory<>("pgender"));


        genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);

    }


    public void loadStudents() {
        String sql = "SELECT * FROM new_student";
        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            studentsList.clear(); // Clear the list before loading new data
            while (resultSet.next()) {
                newStudent student = new newStudent();
                student.setId(resultSet.getInt("id")); // Make sure this column exists in your database
                student.setFname(resultSet.getString("First_Name"));
                student.setMname(resultSet.getString("Middle_Name"));
                student.setLname(resultSet.getString("Last_Name"));
                student.setPnumber(resultSet.getString("Phone_Number"));
                student.setPemail(resultSet.getString("Email"));
                student.setPgender(resultSet.getString("Gender"));
                studentsList.add(student); //
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL errors
        }

        table.setItems(studentsList); // Refresh the TableView with the updated list
    }



    private String getSelectedGender() {
        RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();
        return selectedRadioButton != null ? selectedRadioButton.getText() : "Unspecified";
    }

    @FXML
    private void save() {
        String sql = isEditing
                ? "UPDATE new_student SET First_Name = ?, Middle_Name = ?, Last_Name = ?, Phone_Number = ?, Email = ?, Gender = ? WHERE id = ?"
                : "INSERT INTO new_student (First_Name, Middle_Name, Last_Name, Phone_Number, Email, Gender) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, firstName.getText());
            stmt.setString(2, middleName.getText());
            stmt.setString(3, lastName.getText());
            stmt.setString(4, phoneNumber.getText());
            stmt.setString(5, email.getText());
            stmt.setString(6, getSelectedGender());
            if (isEditing) {
                stmt.setInt(7, studentId);
            }
            stmt.executeUpdate();
            loadStudents(); // Refresh the table
            showAlert("Success", "Student record saved successfully!");
            clearFields();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while saving the record.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        firstName.clear();
        middleName.clear();
        lastName.clear();
        phoneNumber.clear();
        email.clear();
        genderGroup.selectToggle(null);
        isEditing = false; // Reset editing state
    }

    @FXML
    private void delete() {
        newStudent selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            String sql = "DELETE FROM new_student WHERE id = ?";
            try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
                stmt.setInt(1, selectedStudent.getId());
                stmt.executeUpdate();
                studentsList.remove(selectedStudent);
                showAlert("Success", "Student record deleted successfully!");
                clearFields();
            } catch (SQLException e) {
                showAlert("Error", "An error occurred while deleting the record.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void edit() {
        newStudent selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            firstName.setText(selectedStudent.getFname());
            middleName.setText(selectedStudent.getMname());
            lastName.setText(selectedStudent.getLname());
            phoneNumber.setText(selectedStudent.getPnumber());
            email.setText(selectedStudent.getPemail());
            // Set gender selection
            if ("Male".equals(selectedStudent.getPgender())) {
                male.setSelected(true);
            } else {
                female.setSelected(true);
            }
            isEditing = true;
            studentId = selectedStudent.getId();
        }
    }
}