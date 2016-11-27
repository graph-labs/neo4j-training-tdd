package io.github.fbiville.trainings.neo4j.internal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HamcrestRegexMatcher extends TypeSafeMatcher<String> {

    private final String regex;

    private HamcrestRegexMatcher(String regex) {
        this.regex = regex;
    }

    public static Matcher<String> matchesRegex(String regex) {
        return new HamcrestRegexMatcher(regex);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Matches string against specified regex");
    }

    @Override
    protected boolean matchesSafely(final String item) {
        return item.matches(regex);
    }
}
