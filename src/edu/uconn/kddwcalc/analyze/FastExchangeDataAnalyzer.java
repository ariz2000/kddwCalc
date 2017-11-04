package edu.uconn.kddwcalc.analyze;

import edu.uconn.kddwcalc.fitting.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.data.Titration;
import edu.uconn.kddwcalc.fitting.ResultsTwoParamKdMaxObs;
import edu.uconn.kddwcalc.gui.RawData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author Rizzo
 */
public class FastExchangeDataAnalyzer {
    
    /**
     * Takes the user data in a {@link RawData} object, analyzes it and outputs the results to disk
     * 
     * 
     * 
     * @param rawDataInstance contains data from the user input
     * @throws IOException if unable to read/write files
     * @throws ArraysInvalidException if data is duplicated or arrays have different lengths (they cant)
     */
    public static void analyze(RawData rawDataInstance) 
        throws IOException, ArraysInvalidException {
        
        // figures out which Factory subclass to instantiate and then sorts
        //  
        TitrationSeries series = FactoryMaker.createFactory(rawDataInstance.getType()) // returns AbsFactory subclass
                                             .sortDataFiles(rawDataInstance); // returns TitrationSeries

        // prints sorted data to disk
        series.printTitrationSeries(rawDataInstance.getDataOutputFile());
        
        // gets an object back containing Kd, max observable, and the presentation fit
        //    using the cumulative data
        ResultsTwoParamKdMaxObs aggTwoParamResults = LeastSquaresFitter.fitTwoParamKdAndMaxObs(series);
        
        // uses the Kd from the aggregate two parameter fit and 
        //     finds the max observable in a one param fit (Kd fixed)
        double kd = aggTwoParamResults.getKd();
        double[] boundCSPArrayByResidueWithFixedKd = getArrayOfCSPs(kd, series);
        
        // does a two parameter fitting for each residue
        List<ResultsTwoParamKdMaxObs> twoParamResultsByResidue =
            getTwoParamResultsByResidue(series);
        
        writeResultsToDisk(aggTwoParamResults,
                           boundCSPArrayByResidueWithFixedKd, 
                           twoParamResultsByResidue,
                           rawDataInstance.getResultsFile());
        
    } // end method Analyze
    
    // 
    private static void writeResultsToDisk(ResultsTwoParamKdMaxObs aggTwoParamResults,
                                           double[] boundCSPArrayByResidue,
                                           List<ResultsTwoParamKdMaxObs> twoParamResultsList,
                                           File resultsFile) throws FileNotFoundException, IOException {
        
        try (Formatter output = new Formatter(resultsFile)) {
            
            writeCumulativeResults(output,
                                   aggTwoParamResults,
                                   boundCSPArrayByResidue);
            
            writeTwoParamResultsByResidue(resultsFile,
                                          twoParamResultsList);
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening the file to write results");
        }
        
    }

    private static double[] getArrayOfCSPs(double kd, TitrationSeries series) {

        return series.getListOfTitrations()
                     .stream() // now have Stream<Titration>
                     .mapToDouble((Titration titr) ->  {
                         return LeastSquaresFitter.fitOneParamMaxObs(titr, kd);
                     }) // now have DoubleStream
                     .toArray();
    }

    private static List<ResultsTwoParamKdMaxObs> getTwoParamResultsByResidue(TitrationSeries series) {
        return series.getListOfTitrations()
                     .stream() // have Stream<Titration>
                     .map(LeastSquaresFitter::fitTwoParamKdAndMaxObs)
                     .collect(Collectors.toList());
    }
    
    
    
    
    /**
     */
    private static void writeCumulativeResults(Formatter output,
                                               ResultsTwoParamKdMaxObs aggTwoParamResults,
                                               double[] boundCSPArrayByResidue) {
        
        output.format("Results from cumulative fit:%n%n%s%n%n", 
            aggTwoParamResults.toString());
        
        output.format("dw for fully bound:%n");    
        Arrays.stream(boundCSPArrayByResidue)
              .forEach(csp -> output.format("%.6f%n", csp));
        
    } // end writeResults
    /**
     * 
     * 
     * @param resultsFile
     * @param twoParamResultsList 
     */
    
    private static void writeTwoParamResultsByResidue(File resultsFile, 
                                                      List<ResultsTwoParamKdMaxObs> twoParamResultsList) 
                                                throws IOException {
        
        // get the absolute path to where final results are written
        Path path = resultsFile.getAbsoluteFile().toPath();
        
        // create a new path below where results file goes
        Path newPath = Paths.get(path.getParent().toString(), "individualResidueData");
        
        // if this program was already run once, then the output data might
        // have already been written. this begins the process of deleting that data
        // by getting an array of the files inside
        Path[] oldFiles = Files.list(newPath).toArray(Path[]::new);
        
        // take array of old files and delete
        for (Path pth : oldFiles) {
            Files.delete(pth);
        }
        
        // delete the directory ending in "individualResidueData" if program
        // was already executed
        Files.deleteIfExists(newPath);
        
        // create directory where individual residue results will go
        Files.createDirectory(newPath);
        
        // iterate through the individual residue data and write the disk
        for (int ctr = 0; ctr < twoParamResultsList.size(); ctr++) {
            
            try (Formatter output = new Formatter(Paths.get(newPath.toFile().getAbsolutePath(), 
                                                  String.format("%s.txt", ctr)).toFile())) {
                
                output.format("%s", String.format(twoParamResultsList.get(ctr).toString())); 
            }    
        }
    } // end method writeTwoParamResultsByResidue 
} // end class FastExchangeDataAnalyzer
