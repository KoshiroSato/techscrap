package com.koshirosato.techscrap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OGPUtils {
    public static String fetchOGImage(String url) {
        
        try {
            Document doc = Jsoup.connect(url)
                .userAgent("Mozilla")
                .timeout(5000)
                .get();
            Element ogImage = doc.select("meta[property=og:image]").first();
            if (ogImage != null) {
                return ogImage.attr("content");
            }
        } catch (Exception e) {
            System.out.println("OGP fetch error: " + e.getMessage());
        }
        return null;
    }
}
