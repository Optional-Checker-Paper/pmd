/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd;

import static net.sourceforge.pmd.util.CollectionUtil.listOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.sourceforge.pmd.lang.rule.MockRule;
import net.sourceforge.pmd.util.internal.xml.SchemaConstants;
import net.sourceforge.pmd.util.internal.xml.XmlErrorMessages;

import com.github.stefanbirkner.systemlambda.SystemLambda;

class RuleSetFactoryMessagesTest extends RulesetFactoryTestBase {

    @Test
    void testFullMessage() throws Exception {
        String log = SystemLambda.tapSystemErr(() -> assertCannotParse(
            rulesetXml(
                dummyRule(
                    priority("not a priority")
                )
            )
        ));

        assertThat(log, containsString(
            "Error at dummyRuleset.xml:9:1\n"
                + " 7| \n"
                + " 8| <rule name=\"MockRuleName\" language=\"dummy\" class=\"net.sourceforge.pmd.lang.rule.MockRuleWithNoProperties\" message=\"avoid the mock rule\">\n"
                + " 9| <priority>not a priority</priority></rule></ruleset>\n"
                + "    ^^^^^^^^^ Not a valid priority: 'not a priority', expected a number in [1,5]"
        ));
    }


    @Test
    void testPropertyConstraintFailure() throws Exception {
        String log = SystemLambda.tapSystemErr(() -> assertCannotParse(
            rulesetXml(
                dummyRule(
                    attrs -> attrs.put(SchemaConstants.CLASS, MockRule.class.getName()),
                    properties(
                        propertyWithValueAttr(MockRule.PROP.name(), "-4")
                    )
                )
            )
        ));

        assertThat(log, containsString(
            " 10| <property name='testIntProperty' value='-4'/>\n"
                + "                                      ^^^^^ Value should be between 1 and 100"
        ));
    }

    @Test
    void testPropertyValueAsAttributeAndTag() throws Exception {
        String log = SystemLambda.tapSystemErr(() -> assertEquals(1, loadFirstRule(
                rulesetXml(
                        dummyRule(
                                attrs -> attrs.put(SchemaConstants.CLASS, MockRule.class.getName()),
                                properties(
                                        "<property name='" + MockRule.PROP.name() + "' value='4'>\n"
                                                + "  <value>1</value>\n"
                                                + "</property>\n"
                                )
                        )
                )
        ).getProperty(MockRule.PROP)));

        assertThat(log, containsString(
                " 10| <property name='testIntProperty' value='4'>\n"
                      + "                                      ^^^^^ Both a 'value' attribute and a child element are present, the attribute will be ignored\n"
        ));
    }


    @Test
    void testStringMultiPropertyDelimiterDeprecated() throws Exception {
        String log = SystemLambda.tapSystemErr(() -> {
            Rule r = loadFirstRule(
                    rulesetXml(
                            dummyRule(
                                    priority("3"),
                                    properties(
                                            "<property name=\"packageRegEx\" value=\"com.aptsssss|com.abc\" delimiter=\"|\" type=\"List[String]\" description=\"valid packages\"/>"
                                    )
                            )
                    ));
            Object propValue = r.getProperty(r.getPropertyDescriptor("packageRegEx"));

            // note: the delimiter is ignored
            assertEquals(listOf("com.aptsssss|com.abc"), propValue);

            verifyFoundAWarningWithMessage(
                    containing(XmlErrorMessages.WARN__DELIMITER_DEPRECATED)
            );
        });

        assertThat(log, containsString(
                " 11| <property name=\"packageRegEx\" value=\"com.aptsssss|com.abc\" delimiter=\"|\" type=\"List[String]\" description=\"valid packages\"/></properties></rule></ruleset>\n"
                      + "                                                                ^^^^^^^^^ Delimiter attribute is not supported anymore, values are always comma-separated.\n"
        ));
    }
}
