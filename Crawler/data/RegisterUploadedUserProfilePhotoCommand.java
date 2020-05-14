9
https://raw.githubusercontent.com/everest-engineering/lhotse/master/users-api/src/main/java/engineering/everest/lhotse/users/domain/commands/RegisterUploadedUserProfilePhotoCommand.java
package engineering.everest.lhotse.users.domain.commands;

import engineering.everest.lhotse.axon.command.validation.ValidatableCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUploadedUserProfilePhotoCommand implements ValidatableCommand {
    @TargetAggregateIdentifier
    private UUID userId;
    private UUID profilePhotoFileId;
}
