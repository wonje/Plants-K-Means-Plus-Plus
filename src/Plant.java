import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static jdk.nashorn.internal.objects.Global.Infinity;

public class Plant {
    // MEMBERS
    private String name;
    private boolean[] placeVector;
    private int nearestCentroid;
    private double distanceToNearestCentroid;

    /**
     * Location Abbreviations table.
     */
    public static final String[] PLACE_TABLE = {
            "ab", "ak", "ar", "az", "ca", "co", "ct", "de", "dc", "fl", "ga", "hi", "id", "il", "in", "ia", "ks", "ky",
            "la", "me", "md", "ma", "mi", "mn", "ms", "mo", "mt", "ne", "nv", "nh", "nj", "nm", "ny", "nc", "nd", "oh",
            "ok", "or", "pa", "pr", "ri", "sc", "sd", "tn", "tx", "ut", "vt", "va", "vi", "wa", "wv", "wi", "wy", // U.S. States
            "al", "bc", "mb", "nb", "lb", "pe", "nt", "ns", "nu", "on", "qc", "sk", "yt", // Canada
            "dengl", //Greenland (Denmark)
            "fraspm", //St. Pierre and Miquelon (France)
            "nf", "gl" // Unknown
    };

    /**
     * Index lookup converting location abbreviation to vector index
     */
    private static final Map<String, Integer> INDEX_LOOKUP = generateLookup();
    private static Map<String, Integer> generateLookup() {
        // Generate a hash map from PLACE_TABLE
        Map<String, Integer> temp = new HashMap<>();
        for(int i = 0; i < PLACE_TABLE.length; i++) {
            temp.put(PLACE_TABLE[i], i);
        }
        return temp;
    }

    /**
     * Get Eucliean distance from a mean, this method uses parallelism.
     * @param mean
     * @return Eucliean distance from a mean
     */
    public double getDistance(double[] mean) {
        return Math.sqrt(IntStream.range(0, placeVector.length).
                mapToDouble(i -> Math.pow(placeVector[i]?1:0 - mean[i], 2)).
                sum());
    }

    /**
     * Print status of mean table
     * @param centroids
     * @return
     */
    static public void printCentroids(double[][] centroids) {
        int count = 1;
        for (double[] a: centroids) {
            System.out.print("Centroid " + (count++) + " : \t");
            for (double f:a) {
                System.out.print(f + " ");
            }
            System.out.println("");
        }
    }

    /**
     * Getter Setter
     */
    public void setNearestCentroid(int centroid){
        this.nearestCentroid = centroid;
    }

    public int getNearestCentroid(){
        return nearestCentroid;
    }

    public void setDistanceToNearestCentroid(double distance){
        this.distanceToNearestCentroid = distance;
    }

    public double getDistanceToNearestCentroid(){
        return distanceToNearestCentroid;
    }

    /**
     * Default Constructor
     */
    public Plant(){
        this.name = new String();
        this.placeVector = new boolean[PLACE_TABLE.length]; // the array is initialized with false.
        this.nearestCentroid = -1; // Set default. No one selected yet.
        this.distanceToNearestCentroid = Infinity; // Set default. No one selected yet.
    }

    /**
     * Constructor parsing a row of the dataset
     * @param rowToParse a string of a row in the dataset. This string is being parsed and then assign name and
     *                   placeVector accordingly.
     */
    public Plant(String rowToParse) throws IllegalArgumentException {
        this();
        List<String> columns = Arrays.asList(rowToParse.split(","));
        if (columns.size() < 2) {
            throw new IllegalArgumentException("Parsing error");
        }
        // Step 1. Put name
        this.name = columns.get(0);
        // Step 2. Make a placeVector according to the rest of columns
        for (String col:columns.subList(1, columns.size())) {
            try {
                this.placeVector[INDEX_LOOKUP.get(col)] = true;
            } catch (Exception e) {
                throw new IllegalArgumentException("Unknown location: " + col);
            }
        }
    }

    /* Main program */
    public static void main(String[] args) {
        // Dataset
        int linesParsed = 0;
        List<Plant> dataset = new ArrayList<>();
        // Data for k means clustering algorithm
        int K;  // TODO : Change value later
        boolean isConverged;
        // Used for seeding before clustering (K Means ++)
        double[] probability;
        double totalProbability;

        /**
         *  0. Choose the number of cluster
         */
        Scanner input = new Scanner(System.in);

        while(true){
            System.out.print("Choose the number of the cluster : ");
            String str = input.nextLine();
            try{
                K = Integer.parseInt(str);
            }catch(NumberFormatException e){
                System.out.println("Please type as integer format.");
                continue;
            }
            break;
        }

        /**
         * 1. Parse and construct dataset
         */
        double[][] centroids = new double[K][PLACE_TABLE.length];   // Centroids

        try {
            // Get ready for dataset
            File f = new File("dataset/plants.data");
            BufferedReader b = new BufferedReader(new FileReader(f));

            // Start parsing data
            String line;
            while ((line = b.readLine()) != null) {
                linesParsed++;
                try {
                    Plant row = new Plant(line);
                    dataset.add(row);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + ", detected on " + linesParsed + "th row");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print parsing result
        System.out.println(linesParsed + " rows parsed.");
        System.out.println(dataset.size() + " rows successfully parsed.");

        /**
         * 2. Randomly choose centroids
         */
        Random rand = new Random();
        System.out.println("");
        System.out.println("Now setting " + K + " centroids...");
        // First, set centroid selection probability with uniform distribution.
        probability = new double[dataset.size()];
        Arrays.setAll(probability, i -> 1);
        for (int i = 0; i < K; i++) {
            totalProbability = 0;
            for (int j = 0; j < dataset.size(); j++) {
                totalProbability += probability[j];
            }
            // Choose one with the distribution.
            double r = rand.nextDouble() * totalProbability;
            int chosenIdx = 0;
            double sum = 0;
            for (; sum <= r; chosenIdx++) {
                sum += probability[chosenIdx];
            }
            chosenIdx -= 1;
            // assign mean
            for (int j = 0; j < PLACE_TABLE.length; j++) {
                centroids[i][j] = dataset.get(chosenIdx).placeVector[j]? 1: 0;
            }

            // Update the probability distribution proposal to D(x)^2.
            for (int j = 0; j < dataset.size(); j++) {
                double dx = dataset.get(j).getDistance(centroids[i]);
                probability[j] = Math.pow(dx, 2.0);
            }
        }
        System.out.println("Initial centroids:");
        printCentroids(centroids);

        /**
         * 3. Start clustering
         */
        System.out.println("");
        System.out.print("Start clustering...");

        long runningTime = System.currentTimeMillis();

        // Clustering loop
        while (true) {
            // Step 1. Find nearest centroid
            isConverged = true;
            for (int i = 0; i < dataset.size(); i++) {
                for (int j = 0; j < K; j++) {
                    double dis = dataset.get(i).getDistance(centroids[j]);
                    if (dis < dataset.get(i).getDistanceToNearestCentroid()) {
                        if (dataset.get(i).getNearestCentroid() != j) {
                            isConverged = false;
                        }
                        dataset.get(i).setNearestCentroid(j);
                        dataset.get(i).setDistanceToNearestCentroid(dis);
                    }
                }
            }
            if (isConverged) {
                // Repeat Step 1 and 2 until Nearest centroids are converged
                break;
            }
            // Step 2. update the mean from the centroid of clusters
            int[] numOfNearestData = new int[K];
            // Sum up & get average
            IntStream.range(0, K).forEach (i -> {
                centroids[i] = new double[PLACE_TABLE.length]; // Set to zero
                IntStream.range(0, dataset.size()).filter(j -> dataset.get(j).getNearestCentroid() == i).forEach(j -> {
                    numOfNearestData[i]++;
                    Arrays.setAll(centroids[i], n ->
                            (centroids[i][n] * (numOfNearestData[i] - 1) + (dataset.get(j).placeVector[n] ? 1 : 0)) / numOfNearestData[i]);
                });
            });
        }

        /**
         * Clusting Done. Print result
         */
        runningTime = System.currentTimeMillis() - runningTime;
        System.out.println("Done. Running time: " + runningTime + "ms taken.");
        System.out.println("Final centroids");
        // Print centroids
        printCentroids(centroids);
        // Print number of members in each cluster
        int[] numOfNearestData = new int[K];
        IntStream.range(0,K).forEach(i ->
                IntStream.range(0,dataset.size()).filter(j -> dataset.get(j).getNearestCentroid() == i).forEach(k ->
                        numOfNearestData[i]++)
        );
        IntStream.range(0,K).forEach(i->System.out.println((i + 1) + "th cluster: " + numOfNearestData[i] + " entries"));
    }
}
