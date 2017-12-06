package com.org.huy.woakbart.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class WestOakDest {

	private final String etaTime;

	private final String etaDate;

	private final String currentTime;

	public WestOakDest(String etaTime, String etaDate, String currentTime) {
		super();
		this.etaTime = etaTime;
		this.etaDate = etaDate;
		this.currentTime = currentTime;
	}
	
	public String getEtaTime() {
		return etaTime;
	}

	public String getEtaDate() {
		return etaDate;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return Objects.hash(this.etaTime, this.etaDate, this.currentTime);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WestOakDest)) {
			return false;
		}

		final WestOakDest other = (WestOakDest) obj;
		return Objects.equals(this.etaTime, other.etaTime) && Objects.equals(this.etaDate, other.etaDate)
				&& Objects.equals(this.currentTime, other.currentTime);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("etaTime", etaTime).add("etaDate", etaDate)
				.add("currentTime", currentTime).toString();
	}

}
