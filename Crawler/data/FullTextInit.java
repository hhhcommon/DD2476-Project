12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/plugin/fulltext/FullTextInit.java
package ru.bgcrm.plugin.fulltext;

import static ru.bgcrm.dao.Tables.TABLE_CUSTOMER;
import static ru.bgcrm.dao.message.Tables.TABLE_MESSAGE;
import static ru.bgcrm.dao.process.Tables.TABLE_PROCESS;

import java.sql.Connection;

import ru.bgcrm.model.Customer;
import ru.bgcrm.model.message.Message;
import ru.bgcrm.model.process.Process;
import ru.bgcrm.plugin.fulltext.dao.SearchDAO;
import ru.bgcrm.plugin.fulltext.model.Config;
import ru.bgcrm.util.Setup;
import ru.bgerp.util.Log;

/**
 * Fulltext init for doing indexing of tables.
 */
public class FullTextInit implements Runnable {
    private static final Log log = Log.getLog();

    @Override
    public void run() {
        try (Connection con = Setup.getSetup().getDBConnectionFromPool()) {
            con.setAutoCommit(true);

            Config config = Setup.getSetup().getConfig(Config.class);
            SearchDAO dao = new SearchDAO(con);

            initIfConfigured(config, dao, Customer.OBJECT_TYPE, TABLE_CUSTOMER);
            initIfConfigured(config, dao, Process.OBJECT_TYPE, TABLE_PROCESS);
            initIfConfigured(config, dao, Message.OBJECT_TYPE, TABLE_MESSAGE);
        }
        catch (Exception e) {
            log.error(e);
        }
    }

    private void initIfConfigured(Config config, SearchDAO dao, String objectType, String objectTable) throws Exception {
        if (config.getObjectTypeMap().containsKey(objectType))
            dao.init(objectType, objectTable);
    }
}