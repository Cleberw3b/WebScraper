package test;

public class mainClass {


	public static void main(String[] args) {
		
		WebScraper webScraper = new WebScraper();
		webScraper.writeToFile("scraper.txt", "http://www.simvis.com.br/");
	}
}