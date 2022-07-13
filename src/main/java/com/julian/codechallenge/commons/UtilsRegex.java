package com.julian.codechallenge.commons;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.julian.codechallenge.commons.exception.InvalidQueryException;

public class UtilsRegex {

	private static final Pattern PARENTHESES_EXPRESSION_P = Pattern.compile("\\(([^()]+)\\)");
	private static final Pattern VALIDATE_EXPRESSION_P = Pattern.compile("( *[-+]? *\\d+(?:\\.\\d+)? *)([-+*/] *[-+]? *\\d+(?:\\.\\d+)? *){0,100}");
	private static final Pattern MULT_DIV_OPERATION_P = Pattern.compile("([-+]?\\d+(?:\\.\\d+)? ?[*/] ?[-+]?\\d+(?:\\.\\d+)? ?)");
	private static final Pattern SUM_SUBS_OPERATION_P = Pattern.compile("([-+]?\\d+(?:\\.\\d+)? ?[-+] ?[-+]?\\d+(?:\\.\\d+)? ?)");
	private static final Pattern GROUPING_EXPRESION_P = Pattern.compile("([-+]?\\d+(?:\\.\\d+)?)|([-+*/])");
	private static final Pattern DIGIT_EXPRESION_P = Pattern.compile("([-+]?\\d+(?:\\.\\d+)?)");

    private static final String INVALID_QUERY_EXCEPTION_INFO = "The query is not valid for resolving.";

    private UtilsRegex() {}

    public static Optional<String> resolveQueryExpression(String query) {
        try {
            String result = resolveExpression(query);
            return Optional.of(result);
        } catch (InvalidQueryException ex) {
            return Optional.empty();
        }
    }

	private static String resolveExpression(String expression) throws InvalidQueryException {
		expression = simplifyExpression(expression);
 		if (!VALIDATE_EXPRESSION_P.matcher(expression).matches()) {
			throw new InvalidQueryException(INVALID_QUERY_EXCEPTION_INFO);
		}

 		expression = expression.replaceAll("\\s+", "");

		expression = resolveOperators(MULT_DIV_OPERATION_P, expression);
		expression = resolveOperators(SUM_SUBS_OPERATION_P, expression);
		
		return expression;
	}
 
	private static String simplifyExpression(String expression) throws InvalidQueryException {
		StringBuilder simplifiedExpression = new StringBuilder(expression);
		
		Matcher matcher = PARENTHESES_EXPRESSION_P.matcher(expression);
		while (matcher.find()) {
			String matchedExpression = matcher.group();
			matchedExpression = matchedExpression.substring(1, matchedExpression.length() - 1);
			
			String result = resolveExpression(matchedExpression);

			simplifiedExpression = new StringBuilder();
			matcher.appendReplacement(simplifiedExpression, result);
			matcher.appendTail(simplifiedExpression);

			matcher = PARENTHESES_EXPRESSION_P.matcher(simplifiedExpression);
		}
		
		return simplifiedExpression.toString();
	}

	private static String resolveOperators(Pattern pattern, String originalQuery) {
		StringBuilder expression = new StringBuilder(originalQuery);
		
		Matcher matcher = pattern.matcher(originalQuery);
		while (matcher.find()) {
			String result = resolve(matcher.group());

			expression = new StringBuilder();
			matcher.appendReplacement(expression, result);
			matcher.appendTail(expression);

			matcher = pattern.matcher(expression);
		}

		return expression.toString();
	}

	private static String resolve(String factors) {
		Double stack = 0d;
		char operation = 'n';
		boolean addSign = false;

		Matcher groupMatcher = GROUPING_EXPRESION_P.matcher(factors);
		while (groupMatcher.find()) {
			String match = groupMatcher.group();
			
			if (DIGIT_EXPRESION_P.matcher(match).matches()) {
				if (operation == 'n' && !stack.equals(0d)) {
					stack += Double.parseDouble(match);
				} else if (operation == 'n') {
					stack = Double.parseDouble(match); 
				} else if (operation == '*') {
					Double parsedMatch = Double.parseDouble(match);
					stack *= parsedMatch;
					addSign = stack.compareTo(0d) >= 0;
				} else if (operation == '/') {
					Double parsedMatch = Double.parseDouble(match);
					stack /= parsedMatch;
					addSign = stack.compareTo(0d) >= 0;
				} else if (operation == '+') {
					stack += Double.parseDouble(match);
				} else if (operation == '-') {
					stack -= Double.parseDouble(match);
				}

				operation = 'n';
			} else {
				operation = match.charAt(0);
			}
		}
		
		return addSign ? "+" + stack.toString() : stack.toString();
	}

}
