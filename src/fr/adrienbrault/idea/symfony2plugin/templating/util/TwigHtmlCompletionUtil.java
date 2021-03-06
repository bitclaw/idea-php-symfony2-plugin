package fr.adrienbrault.idea.symfony2plugin.templating.util;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;

public class TwigHtmlCompletionUtil {

    // html inside twig: href=""
    public static PsiElementPattern.Capture<PsiElement> getHrefAttributePattern() {
        return PlatformPatterns.psiElement()
            .withParent(
                XmlPatterns.xmlAttributeValue()
                    .withParent(XmlPatterns.xmlAttribute("href")
                    )
            ).inFile(XmlPatterns.psiFile()
                .withName(XmlPatterns
                    .string().endsWith(".twig")
                )
            );
    }

    // html inside twig: <link href="#" rel="stylesheet" />
    public static PsiElementPattern.Capture<PsiElement> getAssetCssAttributePattern() {

        return PlatformPatterns.psiElement()
            .withParent(
                XmlPatterns.xmlAttributeValue()
                    .withParent(XmlPatterns
                        .xmlAttribute("href")
                        .withParent(XmlPatterns
                            .xmlTag()
                            .withChild(XmlPatterns
                                .xmlAttribute("rel")
                                .withValue(
                                    StandardPatterns.string().equalTo("stylesheet")
                                )
                            )
                        )
                    )
            ).inFile(XmlPatterns.psiFile()
                .withName(XmlPatterns
                    .string().endsWith(".twig")
                )
            );

    }

    // html inside twig: <script src="#"></script>
    public static PsiElementPattern.Capture<PsiElement> getAssetJsAttributePattern() {

        return PlatformPatterns.psiElement()
            .withParent(
                XmlPatterns.xmlAttributeValue()
                    .withParent(XmlPatterns
                        .xmlAttribute("src")
                        .withParent(XmlPatterns
                            .xmlTag().withName("script")
                        )
                    )
            ).inFile(XmlPatterns.psiFile()
                .withName(XmlPatterns
                    .string().endsWith(".twig")
                )
            );

    }

}
