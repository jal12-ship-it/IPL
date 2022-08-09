import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;



public class ipl {

    public static Map<String, Float> topEconomical = new HashMap<>();
    public static Map<String, Integer> runsConceded = new HashMap<>();
    public static Map<String, Integer> totalDeliveries = new HashMap<>();
    public static Map<String, Integer> extraRuns = new HashMap<>();
    public static Map<String, Integer> win = new HashMap<>();
    public static List<Integer> year2016 = new ArrayList<>();
    public static List<Integer> year2015 = new ArrayList<>();
    public static Map<Integer, Integer> noOfMatches = new HashMap<>();

    public static void main(String... args) {
        readDataFromCSV("matches.csv");
        readDataFromCSV("deliveries.csv");

        for (Map.Entry<String, Integer> entry : runsConceded.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            topEconomical.put(key, value / (float) (totalDeliveries.get(key) / 6));
        }
        System.out.println("1. No. of matches played per year of all the years in IPL" + '\n' + noOfMatches);
        System.out.println('\n'+"2. No. of matches won of all teams over all the years of IPL"+'\n'+ win);
        System.out.println('\n'+"3. For the year 2016 get the extra runs conceded per team"+'\n'+ extraRuns);
        System.out.println('\n'+"4. For the year 2015 get the top economical bowlers"+'\n'+ topEconomical);
        System.out.println();
    }

    private static void readDataFromCSV(String fileName) {
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            line = br.readLine();
            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                if(fileName.equals("matches.csv")) {
                    createMatch(attributes);

                } else if (fileName.equals("deliveries.csv")) {
                    createDelivery(attributes);

                }

                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private static void createDelivery(String[] metadata) {
        int match_id = Integer.parseInt(metadata[0]);
        String bowler = metadata[8];
        int extra_runs = Integer.parseInt(metadata[16]);
        String teamName = metadata[2];
        int total_runs = Integer.parseInt(metadata[17]);
        String batsman = metadata[6];
        String non_striker = metadata[7];


        new IPL(match_id, bowler, extra_runs, teamName, total_runs, batsman, non_striker);
    }

    private static void createMatch(String[] metadata) {
        int match_id = Integer.parseInt(metadata[0]);
        int year = Integer.parseInt(metadata[1]);
        String winner = metadata[10];
        String player_of_match = metadata[13];

        new IPL(match_id, year, winner, player_of_match);
    }
}

class IPL extends ipl{
    Integer match_id1, match_id2, extra_runs, year, total_runs;
    String bowler, teamName, winner, match_man, batsman, non_striker;


    public IPL(Integer match_id2, String bowler, Integer extra_runs, String team_name, Integer total_runs, String batsman, String non_striker) {
        this.match_id2 = match_id2;
        this.bowler = bowler;
        this.extra_runs = extra_runs;
        teamName = team_name;
        this.total_runs = total_runs;
        this.batsman = batsman;
        this.non_striker = non_striker;

        checkExtraRuns();
        topEconomical();

    }
    void yearCheck() {
        if(year == 2016) {
            year2016.add(match_id1);
        } else if (year == 2015) {
            year2015.add(match_id1);
        }
    }

    public IPL(Integer match_id1, Integer year, String winner, String match_man) {
        this.match_id1 = match_id1;
        this.year = year;
        this.winner = winner;
        this.match_man = match_man;

        noOfMatchesPlayed();
        yearCheck();
        noOfWins();
    }

    void checkExtraRuns() {

        if(year2016.contains(match_id2)) {
            int count = extraRuns.getOrDefault(teamName, 0);
            extraRuns.put(teamName, count + extra_runs);
        }
    }

    void noOfMatchesPlayed() {
        noOfMatches.merge(year, 1, Integer::sum);
    }

    void noOfWins() {
        if(!winner.isBlank() ) {
        /*  int count = win.getOrDefault(winner, 0);
            win.put(winner, count + 1);*/
            win.merge(winner, 1, Integer::sum);
        }
    }

    void topEconomical() {
        if(year2015.contains(match_id2)) {
            int count = runsConceded.getOrDefault(bowler, 0);
            runsConceded.put(bowler, count + total_runs);
            totalDeliveries.merge(bowler, 1, Integer::sum);

        }
    }



}
/*
//3.
if year == 2016:
	Year2016.add(matchid)

if (matchid in Year2016) //from deliveries
	extraRuns[teamName] += extra_runs;

//1.
noOfMatches[year]++;

//2.
win[winner]++;

//4.
if year == 2015:
	Year2015.add(matchid)
overs_bowled = total_deliveries/6;
if matchid in Year2015
    Runs_conceded[bowler] += total_runs;
    total_deliveries[bowler] ++;
	economical[bowler] = runs_conceded/overs_bowled; //lower the value higher economical


*/
