package test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {

	private static final int MAX_DEPTH = 1;

	public WebScraper(){

	}

	public List<String> getPageLinks(String URL, int depthStart){

		List<String> links = new ArrayList<>();

		if ((!links.contains(URL) && (depthStart < MAX_DEPTH))) {
			
			links.add(URL);

			Elements linksOnPage = getDocument(URL).select("a[href]");
			depthStart++;
			
			for (Element page : linksOnPage) {
				getPageLinks(page.attr("abs:href"), depthStart);
			}
		}
		return links;
	}

	public String getTitle(String URL){
		return getDocument(URL).title();
	}

	public List<String> getImagesURL(String URL){
		List<String> imageURLs = new ArrayList<>();
		Elements imageElements = getDocument(URL).select("img");
		for(Element element : imageElements){
			imageURLs.add(element.select("img").attr("src"));
		}
		return imageURLs;
	}

	public List<String> getH2Texts(String URL){
		List<String> list = new ArrayList<>();
		Elements imageElements = getDocument(URL).select("H2");
		for(Element element : imageElements){
			if(!element.text().equals(""))
				list.add(element.text());
			System.out.println(element.text());
		}
		return list;
	}

	public Document getDocument(String URL){
		try {
			return Jsoup.connect(URL)
					.timeout(9000)
					.userAgent("Mozilla")
					.get();
		}catch (IOException e) {
			System.err.println("For '" + URL + "': " + e.getMessage());
			return null;
		}
	}

	public void writeToFile(String filename, String URL) {

		String title = getTitle(URL);
		List<String> links = getPageLinks(URL, 0);
		List<String> imageURL = getImagesURL(URL);
		List<String> H2Texts = getH2Texts(URL);
		
		StringBuilder temp = new StringBuilder();
		temp.append("\n");
		temp.append("- Title: " + title);
		

		FileWriter writer;
		try {
			writer = new FileWriter(filename);
			temp.append("\n");
			temp.append("----------------Links----------------");
			for(String link : links){
				temp.append("\n");
				temp.append("Link: " + link);
			}
			temp.append("\n");
			temp.append("----------------Images URL----------------");
			for(String url : imageURL){
				temp.append("\n");
				temp.append("Image src: " + url);
			}
			temp.append("\n");
			temp.append("----------------H2 Texts----------------");
			for(String H2text : H2Texts){
				temp.append("\n");
				temp.append("H2 text: " + H2text);
			}
			System.out.println(temp);

			writer.write(temp.toString());
			writer.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
