12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/cache/UserNewsCache.java
package ru.bgcrm.cache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import ru.bgcrm.dao.NewsDAO;
import ru.bgcrm.dao.message.MessageType;
import ru.bgcrm.dao.message.config.MessageTypeConfig;
import ru.bgcrm.dao.user.UserDAO;
import ru.bgcrm.event.client.NewsInfoEvent;
import ru.bgcrm.model.News;
import ru.bgcrm.model.SearchResult;
import ru.bgcrm.model.user.User;
import ru.bgcrm.struts.action.MessageAction;
import ru.bgcrm.struts.action.NewsAction;
import ru.bgcrm.util.Preferences;
import ru.bgcrm.util.Setup;
import ru.bgcrm.util.Utils;
import ru.bgcrm.util.sql.SQLUtils;

public class UserNewsCache extends Cache<UserNewsCache> {
    private static final Logger log = Logger.getLogger(UserNewsCache.class);

    private static CacheHolder<UserNewsCache> holder = new CacheHolder<>(new UserNewsCache());

    private static volatile long lastFullFlush = System.currentTimeMillis();

    public static NewsInfoEvent getUserEvent(Connection con, int userId) throws Exception {
        // периодический полный сброс кэша новостей, нужно:
        // - в режиме нескольких серверов на одной базе, чтобы новости приходили
        // - обновления информации о новых EMail сообщениях
        long flushInterVal = Setup.getSetup().getLong("flush.news.everySeconds", 0) * 1000;

        if (flushInterVal > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime > lastFullFlush + flushInterVal)
                flush(con);
        }

        NewsInfoEvent result = holder.getInstance().userInfoMap.get(userId);

        if (result == null) {
            final NewsDAO newsDao = new NewsDAO(con);

            List<Integer> notPopupNews = new ArrayList<>();

            // непрочитанные новости пользователя
            SearchResult<News> searchResult = new SearchResult<>();
            newsDao.searchNewsList(searchResult, userId, false, null);

            List<Integer> popupNews = new ArrayList<>();
            for (News news : searchResult.getList()) {
                if (news.isPopup())
                    popupNews.add(news.getId());
                else
                    notPopupNews.add(news.getId());
            }

            final User user = UserCache.getUser(userId);

            String currentNonpopupUnread = Utils.toString(notPopupNews);
            String lastNonpopupUnread = user.getPersonalizationMap()
                    .get(NewsAction.UNREAD_NEWS_PERSONAL_KEY, "");

            final boolean blinkNews = !currentNonpopupUnread.equals(lastNonpopupUnread);

            // необработанные сообщения
            int currentUnprocessedMessages = 0;

            if (UserCache.getPerm(userId, "ru.bgcrm.struts.action.MessageAction:messageList") != null) {
                MessageTypeConfig mtConfig = Setup.getSetup().getConfig(MessageTypeConfig.class);
                for (MessageType type : mtConfig.getTypeMap().values()) {
                    if (type.getUnprocessedMessagesCount() != null)
                        currentUnprocessedMessages += type.getUnprocessedMessagesCount();
                }
            }

            int lastUnprocessedMessages = user.getPersonalizationMap()
                    .getInt(MessageAction.UNPROCESSED_MESSAGES_PERSONAL_KEY, 0);

            final boolean blinkMessages = currentUnprocessedMessages > 0 && currentUnprocessedMessages != lastUnprocessedMessages;

            // при возврате количества необработанных в 0 мигание прекращается, но сохраняется этот ноль в известном статусе,
            // чтобы начало мигать при любом изменении
            if (currentUnprocessedMessages == 0) {
                Preferences prefs = new Preferences();
                prefs.put(MessageAction.UNPROCESSED_MESSAGES_PERSONAL_KEY, "0");
                new UserDAO(con).updatePersonalization(user, prefs);
            }

            result = new NewsInfoEvent(searchResult.getList().size(), currentUnprocessedMessages, popupNews,
                    blinkNews, blinkMessages);

            holder.getInstance().userInfoMap.put(userId, result);
        }

        return result;
    }

    public static void flushCache(Connection con, Set<Integer> userIds) {
        SQLUtils.commitConnection(con);
        for (Integer userId : userIds) {
            holder.getInstance().userInfoMap.remove(userId);
        }
    }

    public static void flush(Connection con) {
        log.info("Full flushing cache..");

        holder.flush(con);

        lastFullFlush = System.currentTimeMillis();
    }

    @Override
    protected UserNewsCache newInstance() {
        return new UserNewsCache();
    }

    private Map<Integer, NewsInfoEvent> userInfoMap = new ConcurrentHashMap<>();

    private UserNewsCache() {}
}