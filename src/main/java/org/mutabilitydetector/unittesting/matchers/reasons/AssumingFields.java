package org.mutabilitydetector.unittesting.matchers.reasons;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.concat;
import static java.util.Arrays.asList;
import static org.mutabilitydetector.MutabilityReason.COLLECTION_FIELD_WITH_MUTABLE_ELEMENT_TYPE;
import static org.mutabilitydetector.MutabilityReason.MUTABLE_TYPE_TO_FIELD;

import java.util.Set;

import org.hamcrest.Matcher;
import org.mutabilitydetector.MutableReasonDetail;
import org.mutabilitydetector.locations.CodeLocation;
import org.mutabilitydetector.locations.FieldLocation;

public final class AssumingFields  {
    
    private final Set<String> fieldNames;

    private AssumingFields(Set<String> fieldNames) {
        this.fieldNames = fieldNames;
    }
    
    public static AssumingFields named(String first, String... rest) {
        return new AssumingFields(copyOf(concat(asList(first), asList(rest))));
    }

    public static AssumingFields assumingFieldsNamed(String first, String... rest) {
        return new AssumingFields(copyOf(concat(asList(first), asList(rest))));
    }
    
    public Matcher<MutableReasonDetail> areNotModifiedAndDoNotEscape() {
        return isMutableFieldWithName();
    }
    
    public Matcher<MutableReasonDetail> areModifiedAsPartAsAnUnobservableCachingStrategy() {
        return isMutableFieldWithName();
    }

    private Matcher<MutableReasonDetail> isMutableFieldWithName() {
        return new BaseMutableReasonDetailMatcher() {
            @Override
            protected boolean matchesSafely(MutableReasonDetail item) {
                CodeLocation<?> locationOfMutability = item.codeLocation();
                if (locationOfMutability instanceof FieldLocation) {
                    return item.reason().isOneOf(MUTABLE_TYPE_TO_FIELD, COLLECTION_FIELD_WITH_MUTABLE_ELEMENT_TYPE)
                            && fieldNames.contains(((FieldLocation)locationOfMutability).fieldName());
                } else {
                    return false;
                }
            }

        };
    }
}