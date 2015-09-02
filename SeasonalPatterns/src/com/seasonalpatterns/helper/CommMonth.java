// Represents the Month of a Commodity according to its official declaration.

package com.seasonalpatterns.helper;

public enum CommMonth {
	F, G, H, J, K, M, N, Q, U, V, X, Z;

	@Override
	public String toString() {
		return this.name();
	}
	
	public String toWordString() {
		switch (this) {
		case F:
			return "January";
		case G:
			return "February";
		case H:
			return "March";
		case J:
			return "April";
		case K:
			return "May";
		case M:
			return "June";
		case N:
			return "July";
		case Q:
			return "August";
		case U:
			return "September";
		case V:
			return "October";
		case X:
			return "November";
		default:
			return "December";
		}
	}
	
	public int toInt() {
		switch (this) {
		case F:
			return 1;
		case G:
			return 2;
		case H:
			return 3;
		case J:
			return 4;
		case K:
			return 5;
		case M:
			return 6;
		case N:
			return 7;
		case Q:
			return 8;
		case U:
			return 9;
		case V:
			return 10;
		case X:
			return 11;
		default:
			return 12;
		}
	}

}
