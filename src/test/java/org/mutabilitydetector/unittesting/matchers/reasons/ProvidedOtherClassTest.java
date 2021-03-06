/*
 *    Copyright (c) 2008-2011 Graham Allan
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.mutabilitydetector.unittesting.matchers.reasons;

import static java.lang.String.format;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mutabilitydetector.MutabilityReason.ABSTRACT_TYPE_TO_FIELD;
import static org.mutabilitydetector.MutabilityReason.COLLECTION_FIELD_WITH_MUTABLE_ELEMENT_TYPE;
import static org.mutabilitydetector.MutabilityReason.MUTABLE_TYPE_TO_FIELD;
import static org.mutabilitydetector.MutableReasonDetail.newMutableReasonDetail;
import static org.mutabilitydetector.locations.Dotted.dotted;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mutabilitydetector.MutableReasonDetail;
import org.mutabilitydetector.TestUtil;
import org.mutabilitydetector.checkers.CollectionField;
import org.mutabilitydetector.locations.CodeLocation;

public class ProvidedOtherClassTest {
    private static CodeLocation<?> unusedClassLocation = TestUtil.unusedCodeLocation();
    private Matcher<MutableReasonDetail> matcher;

    @Test
    public void matchesWhenReasonIsAssigningAbstractTypeWithGivenClassNameToField() throws Exception {
        MutableReasonDetail reason = newMutableReasonDetail("Field can have an abstract type (some.mutable.clazz) assigned to it.",
                                                             unusedClassLocation,
                                                             ABSTRACT_TYPE_TO_FIELD);

        matcher = ProvidedOtherClass.provided(dotted("some.mutable.clazz")).isAlsoImmutable();

        assertTrue(matcher.matches(reason));
    }
    
    @Test
    public void matchesWhenReasonIsAssigningMutableTypeWithGivenClassNameToField() throws Exception {
        MutableReasonDetail reason = newMutableReasonDetail("Field can have an abstract type (some.mutable.clazz) assigned to it.",
                                                             unusedClassLocation,
                                                             MUTABLE_TYPE_TO_FIELD);

        matcher = ProvidedOtherClass.provided(dotted("some.mutable.clazz")).isAlsoImmutable();

        assertTrue(matcher.matches(reason));
    }

    @Test
    public void doesNotMatchWhenThereDifferentAbstractTypeAssignedToField() {
        MutableReasonDetail notAllowed = newMutableReasonDetail("Field can have an abstract type (some.othermutable.Clazz) assigned to it.",
                                                                 unusedClassLocation,
                                                                 ABSTRACT_TYPE_TO_FIELD);
        matcher = ProvidedOtherClass.provided(dotted("some.mutable.Clazz")).isAlsoImmutable();

        assertFalse(matcher.matches(notAllowed));
    }
    
    @Test
    public void doesNotMatchesWhenNameOfOtherTypeAssignedIsNotExactlyEqual() {
        MutableReasonDetail notAllowed = newMutableReasonDetail("Field can have an abstract type (some.mutable.clazz.with.similar.but.different.name) assigned to it.",
                                                                 unusedClassLocation,
                                                                 ABSTRACT_TYPE_TO_FIELD);
        matcher = ProvidedOtherClass.provided(dotted("some.mutable.clazz")).isAlsoImmutable();

        assertFalse(matcher.matches(notAllowed));
    }

    @Test
    public void matchesWhenReasonIsCollectionWithMutableElementType() {
        CollectionField collectionField = CollectionField.from("Ljava/util/List;", "Ljava/util/List<Lsome/mutable/Clazz;>;");
        
        MutableReasonDetail reason = newMutableReasonDetail(
                format("Field can have collection with mutable element type (%s) assigned to it.", collectionField.asString()),
                unusedClassLocation,
                COLLECTION_FIELD_WITH_MUTABLE_ELEMENT_TYPE);
        
        matcher = ProvidedOtherClass.provided(dotted("some.mutable.Clazz")).isAlsoImmutable();
        
        assertTrue(matcher.matches(reason));
    }

    @Test
    public void matchesWhenReasonIsMapWithMutableElementTypes() {
        CollectionField collectionField = CollectionField.from("Ljava/util/Map;", "Ljava/util/Map<Lsome/mutable/Clazz;Lsome/mutable/OtherClazz;>;");
        
        MutableReasonDetail reason = newMutableReasonDetail(
                format("Field can have collection with mutable element type (%s) assigned to it.", collectionField.asString()),
                unusedClassLocation,
                COLLECTION_FIELD_WITH_MUTABLE_ELEMENT_TYPE);
        
        matcher = ProvidedOtherClass.provided(dotted("some.mutable.Clazz"), dotted("some.mutable.OtherClazz")).isAlsoImmutable();
        
        assertTrue(matcher.matches(reason));
    }


}
