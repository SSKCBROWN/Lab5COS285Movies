package moviepack;
import java.io.IOException;
import java.util.ArrayList;

/** Program to run Lab 5
 * @author Abby Pitcairn
 * @version October 27, 2025
 */
public class Lab5 {
    
    /** Run program 5
     * @param args - the movies filepath
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        //String tsv = args[0];
        ///Users/abigailpitcairn/eclipse-workspace/COS285TA_Grading
        String tsv = "C:\\Users\\missy\\Documents\\New folder\\Lab5COS285Movies\\src\\moviepack\\imdb_movies.tsv";
        ArrayList<Movie> movies = Movie.readMoviesFromFile(tsv);
        
        //Time the creation of the search engine
        long t0 = System.currentTimeMillis();
        MySearchEngine engine = new MySearchEngine(movies);
        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0) + " milliseconds to create search engine");

            // Queries from the lab
            String[] queries = new String[] {
                "harry potter",
                "small town in maine",
                "world war",
                "john mcclanes",
                "bruce wayne"
            };

            // Time each search
            for (String q : queries) {
                t0 = System.currentTimeMillis();
                engine.search(q);
                t1 = System.currentTimeMillis();
                System.out.println("Time for search: " + (t1-t0) + " ms\n");
            }
    }
}
