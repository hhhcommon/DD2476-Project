12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/event/link/LinkAddingEvent.java
package ru.bgcrm.event.link;

import ru.bgcrm.event.UserEvent;
import ru.bgcrm.model.CommonObjectLink;
import ru.bgcrm.struts.form.DynActionForm;

public class LinkAddingEvent extends UserEvent {
    private final CommonObjectLink link;

    public LinkAddingEvent(DynActionForm form, CommonObjectLink link) {
        super(form);
        this.link = link;
    }

    public CommonObjectLink getLink() {
        return link;
    }
}
