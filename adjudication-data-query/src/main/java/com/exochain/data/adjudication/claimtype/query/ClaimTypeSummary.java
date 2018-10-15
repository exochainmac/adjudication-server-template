package com.exochain.data.adjudication.claimtype.query;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;
import com.exochain.data.adjudication.claimtype.api.ClaimTypeInfo;
import com.exochain.jpa.base.ManualVersionTraceableEntityBase;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "claim_type_summary")
public class ClaimTypeSummary extends ManualVersionTraceableEntityBase implements ClaimTypeInfo {
    @Id
    private String id;
    private String title;
    private String description;

    public ClaimTypeSummary() {
    }

    public ClaimTypeSummary(ClaimTypeInfo other) {
        this(other.getTypeId().getValue(), other.getTitle(), other.getDescription());
    }

    public ClaimTypeSummary(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public ClaimTypeId getTypeId() {
        return new ClaimTypeId(getId());
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClaimTypeSummary that = (ClaimTypeSummary) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(title, that.title) &&
                Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), id, title, description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("description", description)
                .toString();
    }
}
