/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.problems.internal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import org.gradle.api.problems.DocLink;
import org.gradle.api.problems.ProblemDefinition;
import org.gradle.api.problems.ProblemId;
import org.gradle.api.problems.Severity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;

import static com.google.common.base.Objects.equal;

@NullMarked
public class DefaultProblemDefinition implements Serializable, ProblemDefinition {

    private final ProblemId id;
    private final Severity severity;
    private final DocLink documentationLink;

    @VisibleForTesting
    DefaultProblemDefinition(
        ProblemId id,
        Severity severity,
        @Nullable DocLink documentationUrl
    ) {
        this.id = id;
        this.severity = severity;
        this.documentationLink = documentationUrl;
    }

    @Override
    public ProblemId getId() {
        return id;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Nullable
    @Override
    public DocLink getDocumentationLink() {
        return documentationLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultProblemDefinition that = (DefaultProblemDefinition) o;
        return severity == that.severity &&
            equal(id, that.id) &&
            equal(documentationLink, that.documentationLink);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, severity, documentationLink);
    }

    @Override
    public String toString() {
        return "DefaultProblemDefinition{" +
            "id=" + id +
            ", severity=" + severity +
            ", documentationLink=" + documentationLink +
            '}';
    }
}
