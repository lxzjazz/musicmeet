package cn.musicmeet.util;

public class DistanceUtils {

	// 美团的优化方案
	public static double distanceSimplifyMore(double lat1, double lng1, double lat2, double lng2, double[] a) {
		// 1)
		double dx = lng1 - lng2;
		// 经度差值
		double dy = lat1 - lat2;
		// 纬度差值
		double b = (lat1 + lat2) / 2.0;
		// 平均纬度
		// 2) 计算东西方向距离和南北方向距离(单位：米)，东西距离采用三阶多项式
		double Lx = (a[3] * b * b * b + a[2] * b * b + a[1] * b + a[0]) * toRadians(dx) * 6367000.0;
		// 东西距离
		double Ly = 6367000.0 * toRadians(dy);
		// 南北距离
		// 3)用平面的矩形对角距离公式计算总距离
		return Math.sqrt(Lx * Lx + Ly * Ly);
	}

	// TODO
	private static double toRadians(double value) {
		return value;
	}
}