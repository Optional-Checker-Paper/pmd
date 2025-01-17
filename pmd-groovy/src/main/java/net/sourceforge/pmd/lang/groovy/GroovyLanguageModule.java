/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.groovy;

import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.lang.LanguagePropertyBundle;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.groovy.cpd.GroovyTokenizer;
import net.sourceforge.pmd.lang.impl.CpdOnlyLanguageModuleBase;

/**
 * Language implementation for Groovy
 */
public class GroovyLanguageModule extends CpdOnlyLanguageModuleBase {
    private static final String ID = "groovy";

    /**
     * Creates a new Groovy Language instance.
     */
    public GroovyLanguageModule() {
        super(LanguageMetadata.withId(ID).name("Groovy").extensions("groovy"));
    }

    public static GroovyLanguageModule getInstance() {
        return (GroovyLanguageModule) LanguageRegistry.CPD.getLanguageById(ID);
    }

    @Override
    public Tokenizer createCpdTokenizer(LanguagePropertyBundle bundle) {
        return new GroovyTokenizer();
    }
}
