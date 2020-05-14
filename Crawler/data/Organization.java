9
https://raw.githubusercontent.com/everest-engineering/lhotse/master/organizations-api/src/main/java/engineering/everest/lhotse/organizations/Organization.java
package engineering.everest.lhotse.organizations;

import engineering.everest.lhotse.axon.common.domain.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization implements Identifiable {

    private UUID id;
    private String organizationName;
    private OrganizationAddress organizationAddress;
    private String websiteUrl;
    private String contactName;
    private String phoneNumber;
    private String emailAddress;
    private boolean deregistered;

}
