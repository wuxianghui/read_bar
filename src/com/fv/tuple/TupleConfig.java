package com.fv.tuple;

/**
 * 
 * @author zhanwei
 * @since 1.0
 */
public class TupleConfig {
	
	// 发布版本时候要置为false
	public static boolean DEBUG = false;
	
	// 使用测试服务器，发布版本时候要置为false
	public static boolean USE_TEST_SERVER = false;

	// 打log的开关，给内部人员的时候设为true，其余为false
	public static boolean DEBUG_LOG	= true;
	
	// 设置是否可升级，默认为true，将来一些预装机器要设置为false
	public static final boolean DBG_CHECK_VERSION = true;

}
