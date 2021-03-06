137
https://raw.githubusercontent.com/201206030/novel-plus/master/novel-crawl/src/main/java/com/java2nb/novel/core/crawl/CrawlParser.java
package com.java2nb.novel.core.crawl;

import com.java2nb.novel.core.utils.HttpUtil;
import com.java2nb.novel.core.utils.IdWorker;
import com.java2nb.novel.core.utils.RandomBookInfoUtil;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.BookContent;
import com.java2nb.novel.entity.BookIndex;
import com.java2nb.novel.utils.Constants;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 爬虫解析器
 *
 * @author Administrator
 */
public class CrawlParser {

    public static final Integer BOOK_INDEX_LIST_KEY = 1;

    public static final Integer BOOK_CONTENT_LIST_KEY = 2;

    @SneakyThrows
    public static Book parseBook(RuleBean ruleBean, String bookId) {
        Book book = new Book();
        String bookDetailUrl = ruleBean.getBookDetailUrl().replace("{bookId}", bookId);
        String bookDetailHtml = HttpUtil.getByHttpClient(bookDetailUrl);
        if (bookDetailHtml != null) {
            Pattern bookNamePatten = compile(ruleBean.getBookNamePatten());
            Matcher bookNameMatch = bookNamePatten.matcher(bookDetailHtml);
            boolean isFindBookName = bookNameMatch.find();
            if (isFindBookName) {
                String bookName = bookNameMatch.group(1);
                //设置小说名
                book.setBookName(bookName);
                Pattern authorNamePatten = compile(ruleBean.getAuthorNamePatten());
                Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
                boolean isFindAuthorName = authorNameMatch.find();
                if (isFindAuthorName) {
                    String authorName = authorNameMatch.group(1);
                    //设置作者名
                    book.setAuthorName(authorName);
                    if (StringUtils.isNotBlank(ruleBean.getPicUrlPatten())) {
                        Pattern picUrlPatten = compile(ruleBean.getPicUrlPatten());
                        Matcher picUrlMatch = picUrlPatten.matcher(bookDetailHtml);
                        boolean isFindPicUrl = picUrlMatch.find();
                        if (isFindPicUrl) {
                            String picUrl = picUrlMatch.group(1);
                            //设置封面图片路径
                            book.setPicUrl(picUrl);
                        }
                    }
                    if (StringUtils.isNotBlank(ruleBean.getScorePatten())) {
                        Pattern scorePatten = compile(ruleBean.getScorePatten());
                        Matcher scoreMatch = scorePatten.matcher(bookDetailHtml);
                        boolean isFindScore = scoreMatch.find();
                        if (isFindScore) {
                            String score = scoreMatch.group(1);
                            //设置评分
                            book.setScore(Float.parseFloat(score));
                        }
                    }
                    if (StringUtils.isNotBlank(ruleBean.getVisitCountPatten())) {
                        Pattern visitCountPatten = compile(ruleBean.getVisitCountPatten());
                        Matcher visitCountMatch = visitCountPatten.matcher(bookDetailHtml);
                        boolean isFindVisitCount = visitCountMatch.find();
                        if (isFindVisitCount) {
                            String visitCount = visitCountMatch.group(1);
                            //设置访问次数
                            book.setVisitCount(Long.parseLong(visitCount));
                        }
                    }

                    String desc = bookDetailHtml.substring(bookDetailHtml.indexOf(ruleBean.getDescStart()) + ruleBean.getDescStart().length());
                    desc = desc.substring(0, desc.indexOf(ruleBean.getDescEnd()));
                    //设置书籍简介
                    book.setBookDesc(desc);
                    if (StringUtils.isNotBlank(ruleBean.getStatusPatten())) {
                        Pattern bookStatusPatten = compile(ruleBean.getStatusPatten());
                        Matcher bookStatusMatch = bookStatusPatten.matcher(bookDetailHtml);
                        boolean isFindBookStatus = bookStatusMatch.find();
                        if (isFindBookStatus) {
                            String bookStatus = bookStatusMatch.group(1);
                            if (ruleBean.getBookStatusRule().get(bookStatus) != null) {
                                //设置更新状态
                                book.setBookStatus(ruleBean.getBookStatusRule().get(bookStatus));
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(ruleBean.getUpadateTimePatten()) && StringUtils.isNotBlank(ruleBean.getUpadateTimeFormatPatten())) {
                        Pattern updateTimePatten = compile(ruleBean.getUpadateTimePatten());
                        Matcher updateTimeMatch = updateTimePatten.matcher(bookDetailHtml);
                        boolean isFindUpdateTime = updateTimeMatch.find();
                        if (isFindUpdateTime) {
                            String updateTime = updateTimeMatch.group(1);
                            //设置更新时间
                            book.setLastIndexUpdateTime(new SimpleDateFormat(ruleBean.getUpadateTimeFormatPatten()).parse(updateTime));

                        }
                    }

                }
                if (book.getVisitCount() == null && book.getScore() != null) {
                    //随机根据评分生成访问次数
                    book.setVisitCount(RandomBookInfoUtil.getVisitCountByScore(book.getScore()));
                } else if (book.getVisitCount() != null && book.getScore() == null) {
                    //随机根据访问次数生成评分
                    book.setScore(RandomBookInfoUtil.getScoreByVisitCount(book.getVisitCount()));
                } else if (book.getVisitCount() == null && book.getScore() == null) {
                    //都没有，设置成固定值
                    book.setVisitCount(Constants.VISIT_COUNT_DEFAULT);
                    book.setScore(6.5f);
                }
            }
        }
        return book;
    }

    public static Map<Integer, List> parseBookIndexAndContent(String sourceBookId, Book book, RuleBean ruleBean, Map<Integer, BookIndex> hasIndexs) {
        Map<Integer,List> result = new HashMap<>(2);
        result.put(BOOK_INDEX_LIST_KEY,new ArrayList(0));
        result.put(BOOK_CONTENT_LIST_KEY,new ArrayList(0));

        Date currentDate = new Date();

        List<BookIndex> indexList = new ArrayList<>();
        List<BookContent> contentList = new ArrayList<>();
        //读取目录
        String indexListUrl = ruleBean.getBookIndexUrl().replace("{bookId}", sourceBookId);
        String indexListHtml = HttpUtil.getByHttpClient(indexListUrl);
        if (indexListHtml != null) {
            Pattern indexIdPatten = compile(ruleBean.getIndexIdPatten());
            Matcher indexIdMatch = indexIdPatten.matcher(indexListHtml);

            Pattern indexNamePatten = compile(ruleBean.getIndexNamePatten());
            Matcher indexNameMatch = indexNamePatten.matcher(indexListHtml);

            boolean isFindIndex = indexIdMatch.find() & indexNameMatch.find();

            int indexNum = 0;

            //总字数
            Integer totalWordCount = 0;
            //最新目录
            Long lastIndexId = null;
            String lastIndexName = null;

            while (isFindIndex) {
                BookIndex hasIndex = hasIndexs.get(indexNum);
                String indexName = indexNameMatch.group(1);

                if (hasIndex == null || !StringUtils.deleteWhitespace(hasIndex.getIndexName()).equals(StringUtils.deleteWhitespace(indexName))) {
                    String contentUrl = ruleBean.getBookContentUrl().replace("{bookId}", sourceBookId).replace("{indexId}", indexIdMatch.group(1));

                    //查询章节内容
                    String contentHtml = HttpUtil.getByHttpClient(contentUrl);
                    if (contentHtml != null) {
                        String content = contentHtml.substring(contentHtml.indexOf(ruleBean.getContentStart()) + ruleBean.getContentStart().length());
                        content = content.substring(0, content.indexOf(ruleBean.getContentEnd()));
                        //TODO插入章节目录和章节内容
                        BookIndex bookIndex = new BookIndex();

                        bookIndex.setIndexName(indexName);
                        bookIndex.setIndexNum(indexNum);
                        indexList.add(bookIndex);
                        BookContent bookContent = new BookContent();

                        bookContent.setContent(content);
                        contentList.add(bookContent);

                        //判断是新增还是更新
                        if(hasIndexs.size() == 0){
                            //新书入库
                            //设置目录和章节内容
                            Long indexId = new IdWorker().nextId();
                            lastIndexId = indexId;
                            lastIndexName = indexName;
                            bookIndex.setId(indexId);
                            bookIndex.setBookId(book.getId());
                            Integer wordCount = bookContent.getContent().length();
                            totalWordCount += wordCount;
                            bookIndex.setWordCount(wordCount);
                            bookIndex.setCreateTime(currentDate);
                            bookIndex.setUpdateTime(currentDate);

                            bookContent.setIndexId(indexId);

                            //设置小说基础信息
                            book.setWordCount(totalWordCount);
                            book.setLastIndexId(lastIndexId);
                            book.setLastIndexName(lastIndexName);
                            book.setLastIndexUpdateTime(currentDate);
                            book.setCreateTime(currentDate);
                            book.setUpdateTime(currentDate);

                        }else{
                            //老书更新
                        }



                        if(hasIndex != null){
                            bookIndex.setId(hasIndex.getId());
                            bookContent.setIndexId(hasIndex.getId());
                        }


                    } else {
                        break;
                    }


                }
                indexNum++;
                isFindIndex = indexIdMatch.find() & indexNameMatch.find();
            }

            if (indexList.size() == contentList.size() && indexList.size() > 0) {

                result.put(BOOK_INDEX_LIST_KEY,indexList);
                result.put(BOOK_CONTENT_LIST_KEY,contentList);

            }

        }

        return result;
    }


}
