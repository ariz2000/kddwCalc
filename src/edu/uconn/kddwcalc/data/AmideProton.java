package edu.uconn.kddwcalc.data;

/**
 * A subclass of {@link Resonance} representing an an amide proton nucleus. This class also performs validation 
 * of the chemical shift to ensure it is within an expected range using a simple static factory.
 * 
 * @author Alex R
 * 
 * @see edu.uconn.kddwcalc.analyze.AbsFactory
 * @see edu.uconn.kddwcalc.analyze.AmideNitrogenProtonFactory
 * @see AmideNitrogenProtonTitrationPoint
 * @see AmideNitrogen
 * 
 * @since 1.8
 */
public class AmideProton extends Resonance {
    private static final int AMIDE_PROTON_MAX_SHIFT = 17;
    private static final int AMIDE_PROTON_MIN_SHIFT = 4;

    /**
     * Initializes an instance of the class with an NMR chemical shift value
     * 
     * @param chemShift an NMR chemical shift for an amide proton nucleus
     */
    private AmideProton(double chemShift) {
        super(chemShift);
    }
    
    /**
     * A static simple factory to validate and create a instance of class {@link AmideProton}.
     * 
     * @param chemShift an NMR chemical shift for an amide proton nucleus
     * 
     * @return an instance of {@link AmideProton} that has been initialized with a chemical shift
     */
    public static AmideProton validateAndCreate(double chemShift) {
        if (isWithinLegitRange(chemShift, AMIDE_PROTON_MAX_SHIFT, AMIDE_PROTON_MIN_SHIFT))
            return new AmideProton(chemShift);
        
        else throw new IllegalArgumentException("Chemical shifts out of range in "
            + "AmideProton.validateAndCreate. Consider switching titration type or order of nuclei.");
    }  
}
