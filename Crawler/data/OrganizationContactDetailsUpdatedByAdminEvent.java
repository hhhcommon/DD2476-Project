9
https://raw.githubusercontent.com/everest-engineering/lhotse/master/organizations/src/main/java/engineering/everest/lhotse/organizations/domain/events/OrganizationContactDetailsUpdatedByAdminEvent.java
package engineering.everest.lhotse.organizations.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Revision("0")
public class OrganizationContactDetailsUpdatedByAdminEvent {
    private UUID organizationId;
    private String contactName;
    private String phoneNumber;
    private String emailAddress;
    private String websiteUrl;
    private UUID adminId;

}
