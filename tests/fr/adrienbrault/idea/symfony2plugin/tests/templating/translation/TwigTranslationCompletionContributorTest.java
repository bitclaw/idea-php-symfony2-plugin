package fr.adrienbrault.idea.symfony2plugin.tests.templating.translation;

import com.jetbrains.twig.TwigFileType;
import fr.adrienbrault.idea.symfony2plugin.tests.SymfonyLightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see fr.adrienbrault.idea.symfony2plugin.templating.TwigTemplateCompletionContributor
 */
public class TwigTranslationCompletionContributorTest extends SymfonyLightCodeInsightFixtureTestCase {

    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("messages.de.yml", "Resources/translations/messages.de.yml");
        myFixture.copyFileToProject("foo.de.yml", "Resources/translations/foo.de.yml");
        myFixture.copyFileToProject("interchange.en.xlf", "interchange.en.xlf");
    }

    protected String getTestDataPath() {
        return new File(this.getClass().getResource("fixtures").getFile()).getAbsolutePath();
    }

    public void testTwigTransCompletion() {
        assertCompletionContains(TwigFileType.INSTANCE, "{{ 'foo'|trans({}, '<caret>') }}", "messages");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|trans }}", "yaml_weak.symfony.great");

        assertCompletionContains(TwigFileType.INSTANCE, "{{ 'foo'|trans({}, '<caret>') }}", "foo");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ message|trans({'%name%': 'Haehnchen'}, \"<caret>\") }}", "foo");

        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|trans({}, 'foo') }}", "foo.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{{'<caret>'|trans({}, 'foo')}}", "foo.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>' | trans({}, 'foo') }}", "foo.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>' | trans( {}, 'foo') }}", "foo.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'  |   trans(  {}, 'foo'     ) }}", "foo.symfony.great");

        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|trans|desc('Profile') }}", "yaml_weak.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ 'foo'|trans({}, '<caret>')|desc('Profile') }}", "messages");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|trans({}, 'foo')|desc('Profile') }}", "foo.symfony.great");

        assertCompletionContains(TwigFileType.INSTANCE, "{% trans_default_domain \"foo\" %}\n{{ '<caret>'|trans }}", "foo.symfony.great");

        assertCompletionContains(TwigFileType.INSTANCE, "{{ sonata_block_render({\n" +
            "    'type': 'nice_block',\n" +
            "    'settings': {\n" +
            "        'title': '<caret>'|trans,\n" +
            "        'more_url': path('more_nice_stuff'),\n" +
            "        'sub_partner_id': app.request.query.get('sub_partner_id')\n" +
            "    }\n" +
            "    })\n" +
            "}}",
            "yaml_weak.symfony.great"
        );

        assertCompletionNotContains(TwigFileType.INSTANCE, "{{ 'foo'|trans|desc('<caret>') }}", "messages");

        assertCompletionNotContains(TwigFileType.INSTANCE, "{{ sonata_block_render({\n" +
                "    'type': 'nice_block',\n" +
                "    'settings': {\n" +
                "        'title': 'foo'|trans,\n" +
                "        'sub_partner_id': app.request.query.get('<caret>')\n" +
                "    }\n" +
                "    })\n" +
                "}}",
            "yaml_weak.symfony.great"
        );
    }

    public void testTwigTransChoiceCompletion() {
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|transchoice(5) }}", "yaml_weak.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{% trans_default_domain \"foo\" %}\n{{ '<caret>'|transchoice(5) }}", "foo.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|transchoice(5, {'%name%': 'Haehnchen'}) }}", "yaml_weak.symfony.great");
        assertCompletionContains(TwigFileType.INSTANCE, "{% trans_default_domain \"foo\" %}\n{{ '<caret>'|transchoice(5, {'%name%': 'Haehnchen'}) }}", "foo.symfony.great");

        assertCompletionContains(TwigFileType.INSTANCE, "{{ message|transchoice(5, {'%name%': 'Haehnchen'}, '<caret>') }}", "messages");
    }

    public void testTwigWeakIndexSubIndexesCompletion() {
        assertCompletionContains(TwigFileType.INSTANCE, "{{ 'foo'|trans({}, '<caret>') }}", "interchange");
        assertCompletionContains(TwigFileType.INSTANCE, "{{ '<caret>'|trans({}, 'interchange') }}", "xlf_weak.symfony.great");
    }

}
