74
https://raw.githubusercontent.com/rayfowler/rotp-public/master/src/rotp/model/tech/TechCombatTransporter.java
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
package rotp.model.tech;

import rotp.model.empires.Empire;

public final class TechCombatTransporter extends Tech {
    public float pct;

    public TechCombatTransporter(String typeId, int lv, int seq, boolean b, TechCategory c) {
        id(typeId, seq);
        typeSeq = seq;
        level = lv;
        cat = c;
        free = b;
        init();
    }
    @Override
    public void init() {
        super.init();
        techType = Tech.COMBAT_TRANSPORTER;

        switch(typeSeq) {
            case 0: pct = .50f; break;
            case 1: pct = .75f; break;
        }
    }
    @Override
    public float baseValue(Empire c) { return c.ai().scientist().baseValue(this); }
    @Override
    public float warModeFactor()        { return 3; }
    @Override
    public boolean isObsolete(Empire c)  { return pct < c.combatTransportPct(); }
    @Override
    public void provideBenefits(Empire c) {
        super.provideBenefits(c);
        c.combatTransportPct(max(c.combatTransportPct(), pct));
    }
}
