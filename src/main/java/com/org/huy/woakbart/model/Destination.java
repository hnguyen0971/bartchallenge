package com.org.huy.woakbart.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * Bean representing Destination.
 * 
 * @author hnguyen
 */
@JsonDeserialize(builder = Destination.Builder.class)
public final class Destination {
    private final String destination;

    private final String abbreviation;
    
    private final int platform;

    private final String color;

    private final String hexColor;

    private final List<String> minutes;

    /**
     * Destination builder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String destination;

        private String abbreviation;
        
        private int platform;

        private String color;

        private String hexColor;

        private List<String> minutes;

        public Builder destination(@JsonProperty("destination") String destination) {
            this.destination = destination;
            return this;
        }
        
        public Builder abbreviation(@JsonProperty("abbreviation") String abbreviation) {
            this.abbreviation = abbreviation;
            return this;
        }
        
        public Builder platform(@JsonProperty("platform") int platform) {
            this.platform = platform;
            return this;
        }
        
        public Builder color(@JsonProperty("color") String color) {
            this.color = color;
            return this;
        }

        public Builder hexColor(@JsonProperty("hexcolor") String hexColor) {
            this.hexColor = hexColor;
            return this;
        }


        public Builder minutes(@JsonProperty("minutes") List<String> minutes) {
            this.minutes = CollectionUtils.isEmpty(minutes) ? Collections.emptyList() : ImmutableList.copyOf(minutes);
            return this;
        }

        /**
         * Build method, create an instance of Destination.
         * 
         * @return an instance of Destination
         */
        @JsonCreator
        public Destination build() {
            return new Destination(this);
        }
    }

    /**
     * Constructor to initialize the Destination.
     * 
     * @param builder Destination builder
     */
    private Destination(Builder builder) {
        this.destination = builder.destination;
        this.abbreviation = builder.abbreviation;
        this.color = builder.color;
        this.hexColor = builder.hexColor;
        this.minutes = builder.minutes;
        this.platform = builder.platform;
    }

	public String getDestination() {
		return destination;
	}

	public String getAbbreviation() {
		return abbreviation;
	}
	
	public int getPlatform() {
		return platform;
	}

	public String getColor() {
		return color;
	}

	public String getHexColor() {
		return hexColor;
	}

	public List<String> getMinutes() {
		return minutes;
	}

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(this.destination, this.abbreviation, this.platform, this.color, this.hexColor, this.minutes);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Destination)) {
            return false;
        }

        final Destination other = (Destination) obj;
        return Objects.equals(this.destination, other.destination) && Objects.equals(this.abbreviation, other.abbreviation)
        		    && Objects.equals(this.platform, other.platform) && Objects.equals(this.color, other.color)
                && Objects.equals(this.hexColor, other.hexColor) && Objects.equals(this.minutes, other.minutes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("destination", destination).add("abbreviation", abbreviation).add("platform", platform)
        		.add("color", color).add("hexColor", hexColor).add("minutes", minutes).toString();
    }

}
