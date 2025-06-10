package com.koshirosato.techscrap;

import java.util.Map;
import java.util.Set;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;

@Component
public class TextVectorizer {
    
    private final JapaneseAnalyzer analyzer = new JapaneseAnalyzer();

    public Map<String, Double> tfidf(String text, List<String> corpus) {
        List<String> terms = tokenize(text);
        Map<String, Double> tf = termFrequency(terms);
        Map<String, Double> idf = inverseDocumentFrequency(corpus);
        Map<String, Double> tfidf = new HashMap<>();
        for (String term : tf.keySet()) {
            tfidf.put(term, tf.get(term) * idf.getOrDefault(term, 0.0));
        }
        return tfidf;
    }

    private List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        try (TokenStream stream = analyzer.tokenStream("", new StringReader(text))) {
            CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {
                tokens.add(attr.toString());
            }
            stream.end();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tokens;
    }

    private Map<String, Double> termFrequency(List<String> tokens) {
        Map<String, Double> tf = new HashMap<>();
        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0.0) + 1);
        }
        double size = tokens.size();
        tf.replaceAll((k, v) -> v / size);
        return tf;
    }

    private Map<String, Double> inverseDocumentFrequency(List<String> corpus) {
        Map<String, Double> idf = new HashMap<>();
        int docCount = corpus.size();
        for (String doc : corpus) {
            Set<String> terms = new HashSet<>(tokenize(doc));
            for (String term : terms) {
                idf.put(term, idf.getOrDefault(term, 0.0) + 1);
            }
        }
        idf.replaceAll((term, count) -> Math.log((double) docCount / count));
        return idf;
    }  
    
    public double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> allKeys = new HashSet<>(vec1.keySet());
        allKeys.addAll(vec2.keySet());
        double dot = 0, norm1 = 0, norm2 = 0;
        for (String key : allKeys) {
            double v1 = vec1.getOrDefault(key, 0.0);
            double v2 = vec2.getOrDefault(key, 0.0);
            dot += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }
        return (norm1 == 0 || norm2 == 0) ? 0.0 : dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}