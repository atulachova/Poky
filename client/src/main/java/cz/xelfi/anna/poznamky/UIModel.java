package cz.xelfi.anna.poznamky;

import cz.xelfi.anna.poznamky.js.PlatformServices;
import cz.xelfi.anna.poznamky.shared.Contact;
import cz.xelfi.anna.poznamky.shared.Phone;
import cz.xelfi.anna.poznamky.shared.PhoneType;
import java.util.List;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.ModelOperation;
import net.java.html.json.OnPropertyChange;
import net.java.html.json.Property;

/** Generates UI class that provides the application logic model for
 * the HTML page.
 */
@Model(className = "UI", targetId="", instance = true, properties = {
    @Property(name = "url", type = String.class),
    @Property(name = "message", type = String.class),
    @Property(name = "alert", type = boolean.class),
    @Property(name = "contacts", type = Contact.class, array = true),
    @Property(name = "selected", type = Contact.class),
    @Property(name = "edited", type = Contact.class)
})
public final class UIModel {
    private PlatformServices services;
   

    @ModelOperation
    void assignServices(UI model, PlatformServices s) {
        this.services = s;
        this.services.readList("poznamky", Contact.class, model.getContacts());
    }
   
    //
    // UI callback bindings
    //

   
    @ModelOperation
    @Function
    void addNote(UI model, Contact data) {
        if (model.getSelected() == null) {
            model.getContacts().add(model.getEdited());
        } else {
            model.getSelected().setFirstName(data.getFirstName());
            model.getSelected().setAddress(data.getAddress());
        }

        model.setEdited(null);
        services.storeList("poznamky", model.getContacts());
    }

    @Function void addNew(UI ui) {
        ui.setSelected(null);
        final Contact c = new Contact();
        ui.setEdited(c);
        services.storeList("poznamky", ui.getContacts());
    }

    @Function void edit(UI ui, Contact data) {
        ui.setSelected(data);
        ui.setEdited(data.clone());
        services.storeList("poznamky", ui.getContacts());
    }

    @Function void delete(UI ui, Contact data) {
        ui.getContacts().remove(data);
        services.storeList("poznamky", ui.getContacts());
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
    static void onPageLoad(PlatformServices services) throws Exception {
        UI uiModel = new UI();
        uiModel.assignServices(services);
        final String baseUrl = "http://localhost:8080/contacts/";
        uiModel.setUrl(baseUrl);
        uiModel.setEdited(null);
        uiModel.setSelected(null);
        uiModel.applyBindings();
        
    }

}
