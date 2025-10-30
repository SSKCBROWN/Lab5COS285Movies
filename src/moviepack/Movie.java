package moviepack;
import java.io.*;
import java.util.*;

/** Create a Movie object that stores information about a movie.
 * @author Abby Pitcairn
 * @version October 27, 2025
 */
public class Movie implements Comparable<Movie> {

    private final String title;
    private final double votes;
    private final String releaseDate;
    private final int runtime;
    private final int budget;
    private final String genre;
    private final String overview;

    /** Constructor for a Movie:
     * @param title - movie title
     * @param votes - movie ranking out of ten
     * @param releaseDate - movie release in format month-day-year
     * @param runTime - movie run time
     * @param budget - approximate movie budget
     * @param genre - most prevalent genre
     * @param overview - short summary of movie plot
     */
    public Movie(String title, double votes, String releaseDate,
                 int runTime, int budget, String genre, String overview) {
        this.title = title;
        this.votes = votes;
        this.releaseDate = releaseDate;
        this.runtime = runTime;
        this.budget = budget;
        this.genre = genre;
        this.overview = overview;
    }

    /** Get the title for a Movie @return title - the title of the Movie */
    public String getTitle() { 
        return title; 
    }
    
    /** Get the overview for a Movie @return overview - the overview of the Movie */
    public String getOverview() { 
        return overview; 
    }

    /** Compare two Movie objects based on title
     * @param other - the Movie to compare the current Movie to
     * @return the value 0 if this == other; 
     *          a value less than 0 if this < other; 
     *          and a value greater than 0 if this > other
     */
    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }

    /** Print the title and overview of a Movie object */
    @Override
    public String toString(){
        return title + ":" + overview;
    }
    
    /** Reads a tab-separated file with columns.
     * @param tsvPath - the path to the tsv file to be read
     * @return movies - an ArrayList of Movies from the file
    */
   public static ArrayList<Movie> readMoviesFromFile(String path) throws IOException {
    ArrayList<Movie> movies = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        String line = br.readLine(); // header
        while ((line = br.readLine()) != null) {
            String[] p = line.split("\t", -1);
            if (p.length < 7) continue;
            try {
                String title = p[0].trim();
                double votes = Double.parseDouble(p[1].trim());
                String release = p[2].trim();
                int runtime = p[3].isBlank() ? 0 : Integer.parseInt(p[3].trim());
                int budget  = p[4].isBlank() ? 0 : Integer.parseInt(p[4].trim());
                String genre = p[5].trim();
                String overview = p[6].trim();
                movies.add(new Movie(title, votes, release, runtime, budget, genre, overview));
            } catch (NumberFormatException e) {
                // skip malformed row
            }
        }
    }
    return movies;
}

}
