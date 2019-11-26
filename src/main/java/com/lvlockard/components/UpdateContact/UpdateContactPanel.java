package com.lvlockard.components.UpdateContact;

import com.lvlockard.forms.UpdateContactForm;
import com.lvlockard.utils.Entry;
import org.apache.wicket.markup.html.panel.Panel;

public class UpdateContactPanel extends Panel {

    public UpdateContactPanel(String id, String userEmail, Entry entry) {
        super(id);

        add(new UpdateContactForm("updateEntry", userEmail,  entry));
    }
}
