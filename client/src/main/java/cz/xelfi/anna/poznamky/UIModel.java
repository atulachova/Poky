package cz.xelfi.anna.poznamky;

import cz.xelfi.anna.poznamky.js.Dialogs;
import cz.xelfi.anna.poznamky.shared.Contact;
import cz.xelfi.anna.poznamky.shared.Phone;
import cz.xelfi.anna.poznamky.shared.PhoneType;
import java.util.List;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.ModelOperation;
import net.java.html.json.OnPropertyChange;
import net.java.html.json.OnReceive;
import net.java.html.json.Property;

/** Generates UI class that provides the application logic model for
 * the HTML page.
 */
@Model(className = "UI", targetId="", properties = {
    @Property(name = "url", type = String.class),
    @Property(name = "message", type = String.class),
    @Property(name = "alert", type = boolean.class),
    @Property(name = "contacts", type = Contact.class, array = true),
    @Property(name = "selected", type = Contact.class),
    @Property(name = "edited", type = Contact.class)
})
final class UIModel {

    //
    // REST API callbacks
    //

    

   
   

   

   
    //
    // UI callback bindings
    //

   
    @ModelOperation
    @Function
    static void addNote(UI model, Contact data) {
        if (model.getSelected() == null) {
            model.getContacts().add(model.getEdited());
        } else {
            model.getSelected().setFirstName(data.getFirstName());
            model.getSelected().setAddress(data.getAddress());
        }

        model.setEdited(null);

    }

    @Function static void addNew(UI ui) {
        ui.setSelected(null);
        final Contact c = new Contact();
        ui.setEdited(c);
    }

    @Function static void edit(UI ui, Contact data) {
        ui.setSelected(data);
        ui.setEdited(data.clone());
    }

    @Function static void delete(UI ui, Contact data) {
        ui.getContacts().remove(data);
    }

    @Function static void cancel(UI ui) {
        ui.setEdited(null);
        ui.setSelected(null);
    }

   

    @Function static void addPhoneEdited(UI ui) {
        final List<Phone> phones = ui.getEdited().getPhones();
        PhoneType t = PhoneType.values()[phones.size() % PhoneType.values().length];
        phones.add(new Phone("", t));
    }

    @Function static void removePhoneEdited(UI ui, Phone data) {
        ui.getEdited().getPhones().remove(data);
    }
    
    @Function static void hideAlert(UI ui) {
        ui.setAlert(false);
    }
    
    @OnPropertyChange(value = "message") static void messageChanged(UI ui) {
        ui.setAlert(true);
    }    

    /**
     * Called when the page is ready.
     */
    static void onPageLoad() throws Exception {
        UI uiModel = new UI();
        final String baseUrl = "http://localhost:8080/contacts/";
        uiModel.setUrl(baseUrl);
        uiModel.setEdited(null);
        uiModel.setSelected(null);
        uiModel.applyBindings();
        
    }

}
