package com.lvlockard.components.AddContact;

import com.lvlockard.forms.EntryForm;
import org.apache.wicket.markup.html.panel.Panel;

public class AddContactPanel extends Panel {

    public AddContactPanel(String id, String userEmail) {
        super(id);

        add(new EntryForm("entryForm", userEmail));
    }
}
