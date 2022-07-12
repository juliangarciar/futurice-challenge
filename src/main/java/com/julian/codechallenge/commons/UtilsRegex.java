package com.julian.codechallenge.commons;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.julian.codechallenge.commons.exception.InvalidQueryException;

public class UtilsRegex {

	private static final Pattern PARENTHESES_EXPRESSION_P = Pattern.compile("\\(([^()]+)\\)");
	private static final Pattern VALIDATE_EXPRESSION_P = Pattern.compile("-?\\d+(?:\\.\\d+)?( ?[-+*/] ?-?\\d+(?:\\.\\d+)?){0,100}");
	private static final Pattern MULT_DIV_OPERATION_P = Pattern.compile("(-?\\d+(?:\\.\\d+)? ?[*/] ?-?\\d+(?:\\.\\d+)?)");
	private static final Pattern SUM_SUBS_OPERATION_P = Pattern.compile("(-?\\d+(?:\\.\\d+)? ?[-+] ?-?\\d+(?:\\.\\d+)?)");
	private static final Pattern GROUPING_EXPRESION_P = Pattern.compile("((-?\\d+(?:\\.\\d+)?)|([-+*/]))");
	private static final Pattern DIGIT_EXPRESION_P = Pattern.compile("(-?\\d+(?:\\.\\d+)?)");

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

	private static String resolveExpression(String query) throws InvalidQueryException {
		StringBuilder strBuilder = new StringBuilder(query);

		strBuilder = simplifyExpression(PARENTHESES_EXPRESSION_P, strBuilder.toString());		
		if (!VALIDATE_EXPRESSION_P.matcher(strBuilder).matches()) {
			throw new InvalidQueryException(INVALID_QUERY_EXCEPTION_INFO);
		}

		strBuilder = simplifyExpression(MULT_DIV_OPERATION_P, strBuilder.toString());
		strBuilder = simplifyExpression(SUM_SUBS_OPERATION_P, strBuilder.toString());
		
		return strBuilder.toString();
	}

	private static StringBuilder simplifyExpression(Pattern pattern, String originalQuery) throws InvalidQueryException {
		StringBuilder strBuilder = new StringBuilder(originalQuery);
		
		Matcher matcher = pattern.matcher(originalQuery);
		while (matcher.find()) {
			String match = matcher.group();
            if (pattern == PARENTHESES_EXPRESSION_P) {
                match = match.substring(1, match.length() - 1);
            }

			String curatedMatch = match.replaceAll("\\s+", "");

			String result = pattern == PARENTHESES_EXPRESSION_P 
				? resolveExpression(curatedMatch)
				: resolveOperator(curatedMatch);

			strBuilder = new StringBuilder();
			matcher.appendReplacement(strBuilder, result);
			matcher.appendTail(strBuilder);

			matcher = pattern.matcher(strBuilder);
		}

		return strBuilder;
	}

	private static String resolveOperator(String factors) {
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
					addSign = checkNegativeValues(stack, parsedMatch);
					stack *= parsedMatch;
				} else if (operation == '/') {
					Double parsedMatch = Double.parseDouble(match);
					addSign = checkNegativeValues(stack, parsedMatch);
					stack /= parsedMatch;
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

	private static boolean checkNegativeValues(Double stack, Double parsedMatch) {
		return stack.compareTo(0d) < 0 && parsedMatch.compareTo(0d) < 0;
	}

}
