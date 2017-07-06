/* created by Alex Ri on 170704
*
* After the user presses "Analyze" or "Execute" in the GUI, the user input needs to be 
* gathered and sent to the factory to parse. This class is sent to the 
* Template method in the abstractFactory class 
* 
* The object is created using a simple static factory method (createRawData) so I could
* add a couple of small validations. 1) each ArrayList must have same number of elements
* and 2) each text file associated with each path must have the same number of lines. 
*
* multipler is how to scale the two nuclei (carbon/proton different than carbon/nitrogen)
*
* resonanceReverseal is related to how the points are listed in the text files (the order),
* i.e. is it proton then carbon, or carbon then proton.
*/

package edu.uconn.kdCalcdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public final class RawData 
{
    private final ArrayList<Path> dataFiles;
    private final ArrayList<Double> ligandConcs;
    private final ArrayList<Double> receptorConcs;
    private final double multiplier;
    private final boolean resonanceReversal;
    
    private RawData(ArrayList<Path> dataFiles, ArrayList<Double> ligandConcs,
        ArrayList<Double> receptorConcs, double multiplier, boolean resonanceReversal)
    {
        this.dataFiles = dataFiles;
        this.ligandConcs = ligandConcs;
        this.receptorConcs = receptorConcs;
        this.multiplier = multiplier;
        this.resonanceReversal = resonanceReversal;
    }
    
    public final static RawData createRawData(ArrayList<Path> dataFiles, ArrayList<Double> ligandConcs,
        ArrayList<Double> receptorConcs, double multiplier, boolean resonanceReversal) throws IOException
    {
        if (dataFiles.size() != ligandConcs.size()
            || ligandConcs.size() != receptorConcs.size()
            || dataFiles.size() != receptorConcs.size())
        {
            throw new IllegalArgumentException("The size of ArrayLists for dataFiles, ligandConcs"
                + " and receptorConcs do not have the same length. They must.");
        }
        
        // TODO finish validation code to ensure equal number of lines in each data file
        
/*        long[] numLines = new long[dataFiles.size()];
        
        try
        {
            for (int ctr = 0; ctr < dataFiles.size(); ctr++)
            {
                numLines[ctr] = Files.lines(dataFiles.get(ctr)).count();
            }
        }
        catch(IOException e)
        {
            System.err.println("Error when opening file in class RawData");
        }
*/
        
  /*      
        long count = 0;
        
        

            count = dataFiles.stream()   // have a Stream<Path>
                .mapToLong(datafile -> {
                    try {Files.lines(datafile).count();}
                    catch (IOException e) {}
                    })
                .distinct()
                .count();

        
        
       
         if(count != 1)
             throw new IllegalArgumentException("Data files do not all have the same number of lines");
      */
        
        // if this statement is reached, all the validation was passed 
        return new RawData(dataFiles, ligandConcs,receptorConcs, multiplier, resonanceReversal);
          
        
    }

    
    // GETTERS
    public final ArrayList<Path> getDataFiles()
    {
        return dataFiles;
    }
    
    public final ArrayList<Double> getLigandConcs()
    {
        return ligandConcs;
    }
    
    public final ArrayList<Double> getReceptorConcs()
    {
        return receptorConcs;
    }
    
    public final double getMultiplier()
    {
        return multiplier;
    }
    
    public final boolean getResonanceReversal()
    {
        return resonanceReversal;
    }
    
}
