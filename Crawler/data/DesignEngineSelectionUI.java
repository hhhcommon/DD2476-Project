74
https://raw.githubusercontent.com/rayfowler/rotp-public/master/src/rotp/ui/design/DesignEngineSelectionUI.java
/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.design;

import java.util.List;
import javax.swing.JLabel;
import rotp.model.ships.ShipComponent;
import rotp.model.ships.ShipEngine;

public class DesignEngineSelectionUI extends DesignSelectionUI {
    private static final long serialVersionUID = 1L;
    @Override
    String title()                    { return text("SHIP_DESIGN_ENGINE_TITLE"); }
    @Override
    int numColumns()                  { return 5; }
    @Override
    String header(int i) {
        switch(i) {
            case 0: return text("SHIP_DESIGN_ENGINE_TYPE");
            case 1: return text("SHIP_DESIGN_ENGINE_COST");
            case 2: return text("SHIP_DESIGN_ENGINE_SIZE");
            case 3: return text("SHIP_DESIGN_ENGINE_NUM");
            case 4: return text("SHIP_DESIGN_ENGINE_SPACE");
        }
        return "";
    }
    @Override
    int alignment(int i) {
        switch(i) {
            case 1:
            case 2:
            case 3:
            case 4:
                return JLabel.RIGHT;
        }
        return super.alignment(i);
    }
    @Override
    int minimumWidth(int i) {
        switch(i) {
            case 0: return 175;
        }
        return super.minimumWidth(i);
    }
    @Override
    List<? extends ShipComponent> baseComponents() { return player().shipLab().engines(); }
    @Override
    ShipComponent selectedComponent()              { return selectedDesign.engine(); }
    @Override
    void select(int compNum)   { selectedDesign.engine((ShipEngine)components().get(compNum)); }
}
