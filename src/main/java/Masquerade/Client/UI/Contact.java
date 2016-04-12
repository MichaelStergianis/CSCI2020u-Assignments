package Masquerade.Client.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Created by Irfaan on 30/03/2016.
 */

public class Contact {
    private String contactName;
    //insert contacts ip variable here

    public Contact(String cName) {
        this.contactName = cName;
    }
    public String getContactName() {return contactName;}
    public void setContactName(String fName) {
        contactName = fName;}

    //example contact database to use for testing UI functionality
    public static ObservableList<Contact> getContactList() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList(
                new Contact("John"),
                new Contact("Charles"),
                new Contact("Roberts"),
                new Contact("Tom")
        );
        return contacts;
    }
}