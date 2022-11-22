package j4rent.Locatarios.PreContrato.evalute;

import Funcoes.Capitular;
import Funcoes.Extenco;
import java.math.BigDecimal;
import net.sourceforge.jeval.EvaluationException;
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
public class Extenso implements Function {
    public String getName() { return "Extenso"; }
    
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
     * @return The source string in reverse order.
     *
     * @exception FunctionException
     *                Thrown if the argument(s) are not valid for this function.
     */
    public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException {
        String result = "";

        try {
            String stringValue = new Evaluator().evaluate(arguments, true, false);
            String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
                                 stringValue, evaluator.getQuoteCharacter());
            result = new Capitular(EXTENCO(argumentOne)).getValue();
        } catch (FunctionException fe) {
                throw new FunctionException(fe.getMessage(), fe);
        } catch (EvaluationException ee) {
                throw new FunctionException("Expressão invalida nos argumentos.", ee);
        } catch (Exception e) {
                throw new FunctionException("Um argumento do tipo <String> é requerido..", e);
        }

        return new FunctionResult(result, FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
    }

    private String EXTENCO(String variavel) {
        String valor = variavel; 
        return new Extenco(new BigDecimal(valor)).toString();
    }
}
