package com.org.huy.woakbart.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Station {
	
	private final String name;
	
	private final String abbreviation;
	
	public Station(String name, String abbreviation) {
		super();
		this.name = name;
		this.abbreviation = abbreviation;

	}
	public String getName() {
		return name;
	}

    public String getAbbreviation() {
		return abbreviation;
	}

	/** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.abbreviation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Station)) {
            return false;
        }

        final Station other = (Station) obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.abbreviation, other.abbreviation);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).add("abbreviation", abbreviation).toString();
    }

}
