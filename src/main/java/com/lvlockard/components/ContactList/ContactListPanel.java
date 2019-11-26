package com.lvlockard.components.ContactList;

import com.lvlockard.utils.Entry;
import com.lvlockard.utils.Redis;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.Arrays;
import java.util.List;

public class ContactListPanel extends Panel {

    public ContactListPanel(String id, Entry[] entries, String userEmail) {
        super(id);

        List<Entry> entryList = Arrays.asList(entries);

        add(new ListView<Entry>("entryList", entryList) {

            @Override
            protected void populateItem(ListItem<Entry> item) {
                final Entry entry = item.getModelObject();
                Label firstName = new Label("firstName", entry.getFirstName());
                Label lastName = new Label("lastName", entry.getLastName());
                Label email = new Label("email", entry.getEmail());
                Label address = new Label("address", entry.getAddress());
                Label phoneNumber = new Label("phoneNumber", entry.getPhoneNumber());
                Button update = new Button("update") {
                    public void onSubmit() {
                        System.out.println("Update");
                    }
                };
                Button delete = new Button("delete") {
                    public void onSubmit() {
                        Redis.deleteEntry(userEmail, entry.getEmail());
                    }
                };

                item.add(firstName);
                item.add(lastName);
                item.add(email);
                item.add(address);
                item.add(phoneNumber);
                item.add(update);
                item.add(delete);
            }
        });
    }
}
