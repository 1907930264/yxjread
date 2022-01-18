package com.yxj.yosmfa.common;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.yxj.yosmfa.entity.Book;
import com.yxj.yosmfa.entity.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class GrabUtil {

    private  RestTemplate restTemplate = null;
    private  ExecutorService executorService = null;
    private AtomicInteger a = new AtomicInteger(0);

     {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
         List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
         for (HttpMessageConverter<?> httpMessageConverter : list) {
             if(httpMessageConverter instanceof StringHttpMessageConverter) {
                 ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf8"));
                 break;
             }
         }

        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        executorService = Executors.newFixedThreadPool(5);
    }

    public  List<Chapter> getChapter(GrabRule grabRule){
        List<Chapter> chapters = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();

        headers.set("user-agent", "Chrome/83.0.4103.116");

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
       // ResponseEntity<String> exchange = restTemplate.exchange(grabRule.getUrl(), HttpMethod.GET, httpEntity, String.class);
       // ResponseEntity<String> forEntity = restTemplate.getForEntity(grabRule.getUrl(), String.class);
        String result2= HttpUtil.get(grabRule.getUrl(), CharsetUtil.CHARSET_UTF_8);
        //章节数据
        Elements elements = parseData(result2, grabRule.getCssQuery());
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Element child = element.child(0);
            String text = child.text();
            int begin,end = -1;
            if ((begin = text.indexOf("第")) > -1 && (end = text.indexOf("章")) > -1){
                //截取中文数字章节转换为数字
                String substring = text.substring(begin+1, end);
                Chapter chapter = new Chapter();
                chapter.setChapterID(new Long(zhToNumber(substring)));
                chapter.setChapterName(child.text());
                chapter.setChapterUrl(child.attr("href"));
                chapters.add(chapter);
            }
        }
        return chapters;
    }

    public  Novel getNovel(String url,String cssQuery,Chapter chapter){
        System.out.println(chapter.getChapterName()+";;"+url);
        HttpHeaders headers = new HttpHeaders();

        headers.set("user-agent", "Chrome/83.0.4103.116");

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
//        ResponseEntity<String> exchange = null;
//        try {
//            exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//        }catch (Exception e){
//            exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//        }

        //ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String result2= HttpUtil.get(url, CharsetUtil.CHARSET_UTF_8);
        Elements elements = parseData(result2, cssQuery);
        //移除上面的div
        elements.select("div").remove();

        String html = elements.html();
        String text = elements.text();
        Novel novel = new Novel();
        novel.setNovelContent(html);
        novel.setChapter(chapter.getChapterName());
        novel.setChapterid(chapter.getChapterID());
        novel.setCreateTime(LocalDateTime.now());
        return novel;
    }

    public  List<Novel> getNovels(GrabRule grabRule){
        List<Novel> novels = new ArrayList<>();
        List<Chapter> chapter = getChapter(grabRule);
        //多线程抓取
//        chapter.stream().forEach((chapter1)->{
//            executorService.execute(() -> {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Novel novel = getNovel(grabRule.getUrlPrefix()+chapter1.getChapterUrl(), grabRule.getNovelCssQuery(),chapter1);
//                novels.add(novel);
//                System.out.println("完成第："+novels.size()+"章的抓取");
//
//            });
//        });
        //同步抓取
        chapter.stream().forEach((chapter1)->{
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Novel novel = getNovel(grabRule.getUrlPrefix()+chapter1.getChapterUrl(), grabRule.getNovelCssQuery(),chapter1);
                novels.add(novel);
                System.out.println("完成第："+novels.size()+"章的抓取");


        });
        //多线程抓取
//        while (true){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (novels.size() == chapter.size()){
//                return novels;
//            }
//        }
        return novels;
    }
    public  Elements parseData(String content,String cssQuery){
        Document parse = Jsoup.parse(content);
        Elements select = parse.select(cssQuery);
        return select;
    }

    public Book createBook(GrabRule grabRule){
//        List<Chapter> chapters = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();

        headers.set("user-agent", "Chrome/83.0.4103.116");

//        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
//        ResponseEntity<String> exchange = restTemplate.exchange(grabRule.getUrl(), HttpMethod.GET, httpEntity, String.class);
        //ResponseEntity<String> forEntity = restTemplate.getForEntity(grabRule.getUrl(), String.class);
        String result2= HttpUtil.get(grabRule.getUrl(), CharsetUtil.CHARSET_UTF_8);
        Book book = new Book();
        //书名
        Elements body = parseData(result2, grabRule.getBookInfoQuery());
        Elements select = body.select(".text h1");
        for (int i = 0; i < select.size(); i++) {
            Element element = select.get(i);
            Element child = element.child(0);
            book.setBookName(child.text());
        }
        //类型
        Elements select2 = body.select(".text .w3 font");
        String tag = select2.text();
        book.setTag(tag);
        //作者
        Elements select3 = body.select(".text .w2 a");
        String author = select3.text();
        book.setAuthor(author);
        //描述
        Elements elements2 = body.select(".desc");
        String desc = elements2.text();
        book.setBookDesc(desc);
        book.setType("1");
        book.setNameAndAuthor(book.getBookName()+","+book.getAuthor());
        book.setCreateTime(LocalDateTime.now());
        book.setStatus(0);
        return book;
    }

    public static int zhToNumber(String zhNumStr) {
        Stack<Integer> stack = new Stack<>();
        String numStr = "一二三四五六七八九";
        String unitStr = "十百千万亿";

        String[] ssArr = zhNumStr.split("");
        for (String e : ssArr ) {
            int numIndex = numStr.indexOf(e);
            int unitIndex = unitStr.indexOf(e);
            if (numIndex != -1 ) {
                stack.push(numIndex + 1);
            } else if (unitIndex != -1) {
                int unitNum = (int)Math.pow(10, unitIndex + 1);
                if (stack.isEmpty()) {
                    stack.push(unitNum);
                } else {
                    stack.push( stack.pop() * unitNum);
                }
            }
        };

        return stack.stream().mapToInt(s-> s).sum();
    }

    public static void main(String[] args) {
//        GrabRule grabRule = new GrabRule();
//        grabRule.setUrl("https://quanxiaoshuo.com/236758/");
//        grabRule.setCssQuery(".chapters .chapter");
//        grabRule.setUrlPrefix("https://quanxiaoshuo.com");
//        grabRule.setBookInfoQuery("body");
//        GrabUtil grabUtil = new GrabUtil();
//        List<Chapter> chapter = grabUtil.getChapter(grabRule);
//        Novel novel = grabUtil.getNovel(grabRule.getUrlPrefix()+chapter.get(2).getChapterUrl(), "#content", chapter.get(2));
//        System.out.printf(novel.toString());
//        System.out.printf("总数："+chapter.size());
//        String a = "第六百三十六章 天仙下凡";
//        int begin,end = -1;
//        if ((begin = a.indexOf("第")) > -1
//                && (end = a.indexOf("章")) >-1){
//            String substring = a.substring(begin+1, end);
//            System.out.printf(GrabUtil.zhToNumber(substring)+"");
//        }
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        a.add("4");
        System.out.println(a.subList(0,2));

    }
}
