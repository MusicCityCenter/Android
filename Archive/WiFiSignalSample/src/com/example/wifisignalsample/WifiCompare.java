package com.example.wifisignalsample;

import java.util.Comparator;

public class WifiCompare implements Comparator<WifiConnectionBean> {
	@Override
	public int compare(WifiConnectionBean o1, WifiConnectionBean o2) {
		return o2.getSignalStrength() - o1.getSignalStrength();
	}
}
