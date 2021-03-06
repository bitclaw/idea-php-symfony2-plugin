package fr.adrienbrault.idea.symfony2plugin.tests.templating.assets;

import com.jetbrains.twig.TwigFileType;
import fr.adrienbrault.idea.symfony2plugin.tests.SymfonyLightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see com.jetbrains.twig.completion.TwigCompletionContributor
 */
public class TwigAssetsCompletionContributorTest extends SymfonyLightCodeInsightFixtureTestCase {
    
    public void setUp() throws Exception {
        super.setUp();

        createDummyFiles(
            "web/assets/foo.css",
            "web/assets/foo.less",
            "web/assets/foo.sass",
            "web/assets/foo.scss",
            "web/assets/foo.js",
            "web/assets/foo.dart",
            "web/assets/foo.coffee",
            "web/assets/foo.jpg",
            "web/assets/foo.png",
            "web/assets/foo.gif"
        );

    }

    private void createDummyFiles(String... files) throws Exception {
        for (String file : files) {
            String path = myFixture.getProject().getBaseDir().getPath() + "/" + file;
            File f = new File(path);
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
    }

    protected String getTestDataPath() {
        return new File(this.getClass().getResource("fixtures").getFile()).getAbsolutePath();
    }

    public void testTwigAssetFunctionCompletion() {
        assertCompletionContains(TwigFileType.INSTANCE, "{{ asset('<caret>') }}", "assets/foo.css", "assets/foo.js", "assets/foo.less", "assets/foo.coffee");
        assertCompletionResultEquals(TwigFileType.INSTANCE, "<script src=\"assets/foo.coffee<caret>\"></script>", "<script src=\"{{ asset('assets/foo.coffee') }}\"></script>");
    }

    public void testTwigAssetsTagCompletion() {
        assertCompletionContains(TwigFileType.INSTANCE, "{% stylesheets '<caret>' %}{% endstylesheets %}", "assets/foo.css", "assets/foo.less", "assets/foo.sass", "assets/foo.scss");
        assertCompletionNotContains(TwigFileType.INSTANCE, "{% stylesheets '<caret>' %}{% endstylesheets %}", "assets/foo.js", "assets/foo.dart", "assets/foo.coffee");

        assertCompletionContains(TwigFileType.INSTANCE, "{% javascripts '<caret>' %}{% endjavascripts %}", "assets/foo.js", "assets/foo.dart", "assets/foo.coffee");
        assertCompletionNotContains(TwigFileType.INSTANCE, "{% javascripts '<caret>' %}{% endjavascripts %}", "assets/foo.css", "assets/foo.less", "assets/foo.sass", "assets/foo.scss");
    }

}
