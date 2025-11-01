package moviepack;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class implements a simple search engine for Movie objects.
 * @author Christopher Brown
 * @version October 28, 2025
 */
public class MySearchEngine {
    private final ArrayList<Movie> movies;
    private final TreeMap<Movie, TreeMap<String, Double>> tf;
    private final TreeMap<String, Double> idf;
    private boolean built = false;

    /**
     * Constructor method for MySearchEngine, creating the search engine.
     * @param movies - the list of movies to parse
     */
    public MySearchEngine(ArrayList<Movie> movies) {
        this.movies = movies;
        this.tf = new TreeMap<>();
        this.idf = new TreeMap<>();
        build();
    }

    /** */
    private void build() {
        if (built) return;
        calculateTF();
        calculateIDF();
        built = true;
    }

    /** */
    private void calculateTF() {
        for (Movie m : movies) {
            String overview = m.getOverview();
            String title = m.getTitle();
            TreeMap<String, Double> innerMap = new TreeMap<>();
            if (overview == null || overview.isEmpty()) {
                tf.put(m, innerMap);
                continue;
            }

            String[] words = (title + " " + overview).toLowerCase().split("\\W+");
            TreeMap<String, Integer> counts = new TreeMap<>();
            int totalWords = 0;
            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }
                Integer existing = counts.get(word);
                if (existing == null) {
                    counts.put(word, 1);
                } else {
                    counts.put(word, existing + 1);
                }
                totalWords++;
            }

            if (totalWords == 0) {
                tf.put(m, innerMap);
                continue;
            }

            double total = (double) totalWords;
            for (String term : counts.keySet()) {
                innerMap.put(term, counts.get(term) / total);
            }

            tf.put(m, innerMap);
        }
    }
    /*
     * private void calculateIDF() {
     * int total_movies = this.movies.size();
     * TreeMap<String, Integer> term_counts = new TreeMap<>();
     * for (Movie m : movies) {
     * String overview = m.getOverview();
     * String title = m.getTitle();
     * String allWords = overview + title;
     * String[] terms = allWords.split("\\s+");
     * TreeSet<String> unique_terms = new TreeSet<>();
     * for (String term : terms) {
     * term = term.trim();
     * if (!term.isEmpty()) continue;
     * unique_terms.add(term);
     * }
     * for (String unique_term : unique_terms) {
     * term_counts.put(unique_term, term_counts.getOrDefault(unique_term, 0) + 1);
     * }
     * }
     * for (Map.Entry<String, Integer> e : term_counts.entrySet()) {
     * double term_count = e.getValue();
     * double value = term_count / total_movies;
     * idf.put(e.getKey(), value);
     * }
     * }
     */
    /** */
    private void calculateIDF() {
        double totalMovies = this.movies.size();
        if (totalMovies == 0) {
            return;
        }

        TreeMap<String, Integer> documentCounts = new TreeMap<>();
        for (Movie m : movies) {
            TreeMap<String, Double> terms = tf.get(m);
            if (terms == null) {
                continue;
            }
            for (String term : terms.keySet()) {
                Integer existing = documentCounts.get(term);
                if (existing == null) {
                    documentCounts.put(term, 1);
                } else {
                    documentCounts.put(term, existing + 1);
                }
            }
        }
//Math.log((totalMovies + 1.0) / (documentCounts.get(term) + 1.0));
        for (String term : documentCounts.keySet()) {
            double idfValue = Math.log((totalMovies + 1.0) / (documentCounts.get(term) + 1.0));
            idf.put(term, idfValue);
        }
    }

    private double relevanceScore(String query, Movie m) {
        String[] queryTerms = query.toLowerCase().split("\\W+");
        double score = 0.0;
        for (String term : queryTerms) {
            Double tfValue = tf.get(m).get(term);
            Double idfValue = idf.get(term);
            if (tfValue != null && idfValue != null) {
                score += tfValue * idfValue;
            }
        }
        return score;
    }

    public void search(String query) {
        TreeMap<Double, ArrayList<Movie>> scoredMovies = new TreeMap<>();
        for (Movie m : movies) {
            double score = relevanceScore(query, m);
            scoredMovies.putIfAbsent(score, new ArrayList<>());
            scoredMovies.get(score).add(m);
        }
        System.out.println("Search results for query: \"" + query + "\"");
        int rank = 1;
        outer:
        for (Double score : scoredMovies.descendingKeySet()) {
            ArrayList<Movie> movieList = scoredMovies.get(score);
            for (Movie m : movieList) {
                System.out.println(rank + ". " + m.getTitle() + " (score: " + score + ")");
                System.out.println("   Overview: " + m.getOverview());
                rank++;
                if (rank > 5) {
                    break outer;
                }
            }
        }
        if (rank == 1) {
            System.out.println("No results found.");
        }
    }

    
}
