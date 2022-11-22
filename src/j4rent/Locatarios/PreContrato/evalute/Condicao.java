package j4rent.Locatarios.PreContrato.evalute;

import java.util.ArrayList;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionHelper;
import net.sourceforge.jeval.function.FunctionResult;

/**
 *
 * @author Samic
 */
public class Condicao implements Function {
    public String getName() { return "Condicao"; }
    
    /**
     * Executes the function for the specified argument. This method is called
     * internally by Evaluator.
     *
     * @param evaluator
     *            An instance of Evaluator.
     * @param arguments
     *            A string argument that will be converted into a string that is
     *            in reverse order. The string argument(s) HAS to be enclosed in
     *            quotes. White space that is not enclosed within quotes will be
     *            trimmed. Quote characters in the first and last positions of
     *            any string argument (after being trimmed) will be removed
     *            also. The quote characters used must be the same as the quote
     *            characters used by the current instance of Evaluator. If there
     *            are multiple arguments, they must be separated by a comma
     *            (",").
     *
     *            oneParameter
     *            String contendo o condicionador
     *
     *            twoParameter
     *            String contendo condição verdadeira
     *
     *            treeParameter
     *            String contendo condição falsa
     *
     * @return The source string verdadeira ou falsa de acordo com a sentença.
     *
     * @exception FunctionException
     *                Thrown if the argument(s) are not valid for this function.
     */    
    public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException {
        String result = "";
        String exceptionMessage = "Três argumentos do tipo <String> são requeridos.";
        ArrayList values = FunctionHelper.getStrings(arguments, ',');
        if(values.size() != 3) {
                throw new FunctionException(exceptionMessage);
        } else {
                try {
                        String e = (String)values.get(0);
                        String argumentTwo = FunctionHelper.trimAndRemoveQuoteChars((String)values.get(1), evaluator.getQuoteCharacter());
                        String argumentTree = FunctionHelper.trimAndRemoveQuoteChars((String)values.get(2), evaluator.getQuoteCharacter());

                        if (evaluator.evaluate(e).equals("1.0")) {
                                result = argumentTwo;
                        } else {
                                result = argumentTree;
                        }
                } catch (Exception var10) {
                        throw new FunctionException(exceptionMessage, var10);
                }
        }
        return new FunctionResult(result, FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
    }
}
