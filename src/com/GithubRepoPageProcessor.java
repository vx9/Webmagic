package com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubRepoPageProcessor implements PageProcessor {
	// ����һ��ץȡ��վ��������ã��������롢ץȡ��������Դ�����
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	public void process(Page page) {
		// ���ֶ���������γ�ȡҳ����Ϣ������������
		page.putField("title", page.getHtml().xpath("//title/text()")
				.toString());
		page.putField("html", page.getHtml().toString());
		System.out.println(page.getResultItems());

		writeDown(page.getResultItems());
		// ����������ҳ�淢�ֺ�����url��ַ��ץȡhttp://news.sina.com.cn/china/xlxw/2016-11-14/doc-ifxxsmif2977856.shtml
		page.addTargetRequests(page.getHtml().links()
				.regex("(http://news\\.sina\\.com\\.cn/\\w+/\\w+/\\d{4}-\\d{2}-\\d{2}/\\w+-\\w+\\.shtml)").all());
	}

	private void writeDown(ResultItems resultItems) {
		String title = resultItems.get("title").toString().split("\\|")[0];
		title = title + ".html";
		String html = resultItems.get("html");
		File file = new File("d:/test/html/utf8/sina", title);
			
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter pw = null;
			pw = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
			//pw = new OutputStreamWriter(new FileOutputStream(file),"gb2312");
			pw.write(html);
			pw.close();
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new GithubRepoPageProcessor())
				.addUrl("http://news.sina.com.cn/china/xlxw/2016-11-14/doc-ifxxsmif2977856.shtml")
				// .addPipeline(new
				// JsonFilePipeline("D:\\webmagic\\")).thread(5)
				.run();
	}
}
