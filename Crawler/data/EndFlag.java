25
https://raw.githubusercontent.com/Sauilitired/Hyperverse/master/Core/src/main/java/se/hyperver/hyperverse/flags/implementation/EndFlag.java
//
// Hyperverse - A Minecraft world management plugin
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.
//

package se.hyperver.hyperverse.flags.implementation;

import se.hyperver.hyperverse.Hyperverse;
import se.hyperver.hyperverse.configuration.Messages;
import se.hyperver.hyperverse.flags.FlagParseException;
import se.hyperver.hyperverse.flags.WorldFlag;
import se.hyperver.hyperverse.util.WorldUtil;
import org.jetbrains.annotations.NotNull;
import se.hyperver.hyperverse.world.HyperWorld;
import se.hyperver.hyperverse.world.WorldConfiguration;

import java.util.Collection;
import java.util.stream.Collectors;

public class EndFlag extends WorldFlag<String, EndFlag> {

    public static final EndFlag END_FLAG_DEFAULT = new EndFlag("");

    private EndFlag(@NotNull final String value) {
        super(value, Messages.flagDescriptionEnd);
    }

    @Override public EndFlag parse(@NotNull final String input) throws FlagParseException {
        if (WorldUtil.validateName(input)) {
            return flagOf(input);
        }
        throw new FlagParseException(this, input, "A world name may only contain (up to) 16 alphanumerical characters, - and _");
    }

    @Override public EndFlag merge(@NotNull String newValue) {
        return flagOf(newValue);
    }

    @Override public String toString() {
        return this.getValue();
    }

    @Override public String getExample() {
        return "end_world";
    }

    @Override protected EndFlag flagOf(@NotNull final String value) {
        return new EndFlag(value);
    }

    @Override public Collection<String> getTabCompletions() {
        return Hyperverse.getApi().getWorldManager().getWorlds().stream()
            .map(HyperWorld::getConfiguration).map(WorldConfiguration::getName).collect(Collectors.toList());
    }

}
