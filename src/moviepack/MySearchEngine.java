package moviepack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Gotta add some javadoc and remove some comments
 * @author Nicolas Borelli
 */
public class MySearchEngine {

    private final ArrayList<Movie> movies;
    private final TreeMap<Movie, TreeMap<String, Double>> tf;
    private final TreeMap<String, Double> idf;

    /**
     * Constructor for MySearchEngine
     * @param movies - the list of movies to parse
     */
    public MySearchEngine(ArrayList<Movie> movies) {
        this.movies = movies;
        tf = new TreeMap();
        idf = new TreeMap();
        calculateTF();
        calculateIDF();
    }

    private void calculateTF() {

        // Iterate on each movie
        for (Movie m : movies) {
            // get the movie overview
            String[] overview = m.getOverview().split(" ");
            double totalWords = overview.length;
            TreeMap<String, Double> innerMap = new TreeMap<>();
            // Write the code to fill this TreeMap
            for (String word : overview) {
                double count = 0.0;
                for (String wordCheck : overview) {
                    if (word.equals(wordCheck)) {
                        count++;
                    }
                }
                innerMap.put(word, count / totalWords);
            }
            // add this TreeMap to tf
            tf.put(m, innerMap);
        }
    }

    private void calculateIDF() {
        double N = movies.size();
        // iterate on all songs
        for (Movie m : movies) {
            // get all words
            String[] terms = m.getOverview().split(" ");
            // get all unique words
            TreeSet<String> uniqueWords = new TreeSet<>(Arrays.asList(terms));
            for (String word : uniqueWords) {
                idf.put(word, idf.getOrDefault(word, 0.0) + 1);
            }
        }
        // idf only has nx values so far; by this code we will update the values
        for (String m : idf.keySet()) {
            double nx = idf.get(m);
            double idfValue = (N - nx + 0.5) / (nx + 0.5);
            idfValue = Math.log(idfValue + 1);
            idf.put(m, idfValue);
        }
    }

    private double relevance(String query, Movie m) {
        String[] queryTerms = query.split(" ");
        double score = 0.0;
        for (String queryTerm : queryTerms) {
            if (idf.containsKey(queryTerm) && tf.get(m).containsKey(queryTerm)) {
                score += idf.get(queryTerm) * tf.get(m).get(queryTerm);
            }
        }
        return score;
    }

    /**
     * Searches for movies that are most relevant to the given query string.
     * Uses TF-IDF scores to rank movies by relevance and prints the top 5.
     *
     * @param query the search string (e.g., "world war" or "harry potter")
     */
    public void search(String query) {
        // Store movie / relevance score pairs
        TreeMap<Double, ArrayList<Movie>> rankedResults = new TreeMap<>();

        for (Movie m : movies) {
            double score = relevance(query, m);
            rankedResults.putIfAbsent(score, new ArrayList<>());
            rankedResults.get(score).add(m);
        }

        System.out.println("Results for query \"" + query + "\"");
        int rank = 1;
        // Iterate over scores from highest to lowest
        for (Double score : rankedResults.descendingKeySet()) {
            for (Movie m : rankedResults.get(score)) {
                System.out.printf("%d: %s \t%s\n", rank, m.getTitle(), m.getOverview());
                rank++;
                if (rank > 5) {
                    //System.out.println();
                    return; // stop after top 5
                }
            }
        }
        System.out.println();
    }
}
