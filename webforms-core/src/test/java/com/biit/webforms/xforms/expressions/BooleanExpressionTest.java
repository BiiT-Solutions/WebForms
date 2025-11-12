package com.biit.webforms.xforms.expressions;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Collections;

import org.testng.Assert;

import org.testng.annotations.Test;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

@Test(groups = { "booleanExpressions" })
public class BooleanExpressionTest {

	@Test
	public void basicTest() {
		// (((!C) | C) & A & B)
		Expression<String> expr = And.of(Variable.of("A"), Variable.of("B"),
				Or.of(Variable.of("C"), Not.of(Variable.of("C"))));

		Expression<String> simplified = RuleSet.simplify(expr);
		Assert.assertEquals("(A & B)", simplified.toString());

		Expression<String> halfAssigned = RuleSet.assign(simplified, Collections.singletonMap("A", true));
		Assert.assertEquals("B", halfAssigned.toString());

		Expression<String> resolved = RuleSet.assign(halfAssigned, Collections.singletonMap("B", true));
		Assert.assertEquals("true", resolved.toString());

		Expression<String> nonStandard = ExprParser.parse("((A|B) & (C|D))");
		Assert.assertEquals("((A | B) & (C | D))", nonStandard.toString());

		// Disjunctive Normal (Sum-of-Products)
		Expression<String> sopForm = RuleSet.toSop(nonStandard);
		Assert.assertEquals("((A & C) | (A & D) | (B & C) | (B & D))", sopForm.toString());

		Expression<String> nonStandard2 = ExprParser.parse("((A & B) | (C & D))");
		Assert.assertEquals("((A & B) | (C & D))", nonStandard2.toString());

		// Converting to Conjunctive Normal (Product-of-Sums) form
		Expression<String> posForm = RuleSet.toPos(nonStandard2);
		Assert.assertEquals("((A | C) & (A | D) & (B | C) & (B | D))", posForm.toString());
	}

	@Test
	public void simpleAndOrExpression() {
		Expression<String> parsedExpression = RuleSet.simplify(ExprParser.parse("(((!C) | C) & A & B)"));
		Assert.assertEquals("(A & B)", parsedExpression.toString());
	}

	@Test
	public void basicRelevantRule() {
		Expression<String> parsedExpression = ExprParser
				.parse("((((((((A)) | (! ( A ))) & ! ( B ))))) | ((((A)) | (! ( A ))) & B))");
		Expression<String> simplified = RuleSet.simplify(parsedExpression);
		Assert.assertEquals("true", simplified.toString());
	}

	@Test
	public void ruleWithLongNames() {
		Expression<String> parsedExpression = ExprParser
				.parse("(((((((($A1)) | (! ( $A1 ))) & ! ( B2 ))))) | (((($A1)) | (! ( $A1 ))) & B2))");
		Expression<String> simplified = RuleSet.simplify(parsedExpression);
		Assert.assertEquals("true", simplified.toString());
	}

	@Test
	public void ruleWithOrbeonNames() {
		Expression<String> parsedExpression = ExprParser
				.parse("(($Woonsituatie='Zelfstandig')) | (! ( $Woonsituatie='Zelfstandig' ))");
		Expression<String> simplified = RuleSet.simplify(parsedExpression);
		Assert.assertEquals("true", simplified.toString());
	}

	@Test
	public void testNotValues() {
		Expression<String> parsedExpression = ExprParser.parse("! ( ( A & B ) )");
		Expression<String> simplified = RuleSet.simplify(parsedExpression);
		Assert.assertEquals("!(A & B)", simplified.toString());
	}
	
	@Test
	public void testLongNotValues() {
		Expression<String> parsedExpression = ExprParser.parse("(( ( ( A & B ) ) ) | ( ! ( ( A & B ) ) & C ))");
		Expression<String> simplified = RuleSet.simplify(parsedExpression);
		Assert.assertEquals("((!(A & B) & C) | (A & B))", simplified.toString());
	}
	
	
	
	
}
