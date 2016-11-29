import java.util.Comparator;

/**
 * Created by Thomas Straetmans on 29/11/2016.
 * <p>
 * MathSolver for USG Professionals
 */
public class CustomCompare implements Comparator<String> {


    @Override
    public int compare(String o1, String o2) {
        return (new Integer(o1.length())).compareTo(o2.length());
    }
}
