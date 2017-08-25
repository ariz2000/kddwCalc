package edu.uconn.kddwcalc.data;

/**
 * A class containing a single simple static factory to instantiate a subclass of {link AbsFactory}.
 * The switch statement using a {link String} that came from
 * {link InputGUIController#typeOfTitration}. These string must match. I probably should have used enums
 * but its done now.
 * 
 * @author Alex R.
 * 
 * @see AbsFactory
 * @see TitrationSeries
 * @see edu.uconn.kddwcalc.gui.TypeOrderMultiplierGUIController
 * 
 * 
 * @since 1.8
 */
public class FactoryMaker {
    
    /**
     * Construction of {@link FactoryMaker} object is unnecessary; call the static method {@link #createFactory}. 
     */
    private FactoryMaker() {
        }

    /**
     * Creates a concrete implementation of {@link AbsFactory}.
     * 
     * @param type the type of titration (chosen by user in GUI)
     * 
     * @return a concrete subclass of {@link AbsFactory}
     * 
     * @see TypesOfTitations
     * @see AbsFactory
     * 
     * @throws NullPointerException if the variable {@Link AbsFactory} is not initialized in the
     * switch statement.
     */
    public static AbsFactory createFactory(TypesOfTitrations type) {
        
        AbsFactory absFactory = null;
        
        if (type == TypesOfTitrations.AMIDEHSQC)
            absFactory = new AmideNitrogenProtonFactory();
        
        else if (type == TypesOfTitrations.METHYLHMQC)
            absFactory = new MethylCarbonProtonFactory();
        
        
        if (absFactory == null)
            throw new NullPointerException("In class FactoryMaker, the variable absFactory"
                + " was still null before return statement. Why didn't the if statement "
                + "make a reference to a concrete factory class when switching enums?");
        
        return absFactory;
    }
}  // end class FactoryMaker
