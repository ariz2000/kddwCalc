package edu.uconn.kdCalc.data;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;

class Results 
{
    double kd;
    double percentBound;
    double[] boundCSPArray;
    
    
    private Results(double kd, 
                    double percentBound, 
                    double[] boundCSPArray)
    {
        this.kd = kd;
        this.percentBound = percentBound;
        this.boundCSPArray = boundCSPArray;
    }
    
    public static Results makeResultsObject(double kd, 
                                            double percentBound, 
                                            double[] boundCSPArray)
    {
        if (kd < 0.0 || percentBound < 0.0)
            throw new IllegalArgumentException("negative kd or %bound in Results");
        
        for(double csp : boundCSPArray)
        {
            if (csp < 0.0)
                throw new IllegalArgumentException("negative csp in Results");
        }
        
        return new Results(kd, percentBound, boundCSPArray);
    }
    
    public double getKd()
    {
        return kd;
    }
    
    public double getPercentBound()
    {
        return percentBound;
    }
    
    public double[] getBoundCSPArray()
    {
        return boundCSPArray;
    }
    
    public void writeResultsToDisk()
    {
        /* test code to see if values correct. seem to be
        System.out.println(getKd());
        System.out.println(getPercentBound());
        
        System.out.println();
        
        Arrays.stream(getBoundCSPArray())
                        .forEach(System.out::println);
        */
        
        Formatter output = openFile();
        
        writeResults(output);
        
        closeFile(output);
    }
    
    private Formatter openFile()
    {
        Formatter output = null;
        
        try
        {
            return new Formatter("finalResults.txt");
        }
        catch(SecurityException | FileNotFoundException e)
        {
            System.err.println(e);
        }
        
        if (output == null)
            throw new IllegalArgumentException("output Formatter is null before returning");
                
        return output; 
    }
    
    private void writeResults(Formatter output)
    {
        try
        {
            output.format("kd = %.2f%n", getKd());
            output.format("percent bound = %.2f%n", getPercentBound());
            
            output.format("%ndw for fully bound%n");
            
            Arrays.stream(getBoundCSPArray())
                  .forEach(csp -> output.format("%.6f%n", csp));
        }
        catch(FormatterClosedException | NoSuchElementException e)
        {
            System.err.println(e);
        }
    }
    
    private void closeFile(Formatter output)
    {
        if (output != null)
            output.close();
    }
}
